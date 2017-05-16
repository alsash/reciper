package com.alsash.reciper.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Notification;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * A Reactive Loader that helps load data asynchronously in response to an external action.
 */
public class RxLoader<A, D> {

    PublishSubject<A> actionSubject = PublishSubject.create();
    BackpressureStrategy actionStrategy;
    Predicate<A> actionFiler;
    Consumer<A> actionConsumer;
    Scheduler actionScheduler;
    A startAction;

    Publisher<List<D>> dataLoader;
    Subscriber<List<D>> dataSubscriber;
    Scheduler dataScheduler;

    boolean fetched;
    boolean loading;

    RxLoader() {
    }

    public RxLoader(BackpressureStrategy actionStrategy,
                    Predicate<A> actionFiler,
                    Consumer<A> actionConsumer,
                    Scheduler actionScheduler,
                    A startAction,
                    Publisher<List<D>> dataLoader,
                    Subscriber<List<D>> dataSubscriber,
                    Scheduler dataScheduler) {
        this.actionStrategy = (actionStrategy != null) ? actionStrategy :
                BackpressureStrategy.DROP;
        this.actionFiler = (actionFiler != null) ? actionFiler :
                new Predicate<A>() {
                    @Override
                    public boolean test(@NonNull A a) throws Exception {
                        return true;
                    }
                };
        this.actionConsumer = (actionConsumer != null) ? actionConsumer :
                new Consumer<A>() {
                    @Override
                    public void accept(@NonNull A a) throws Exception {
                        // do nothing
                    }
                };
        this.actionScheduler = actionScheduler;
        this.startAction = startAction;
        this.dataLoader = dataLoader;
        this.dataSubscriber = dataSubscriber;
        this.dataScheduler = dataScheduler;
    }

    public static <A, D> RxLoaderBuilder<A, D> builder() {
        return new RxLoaderBuilder<>();
    }

    public synchronized boolean isFetched() {
        return fetched;
    }

    public synchronized boolean isLoading() {
        return loading;
    }

    public void action(A action) {
        this.actionSubject.onNext(action);
    }

    public final <E extends Subscriber<List<D>>> E subscribeWith(E subscriber) {
        dataSubscriber = subscriber;
        return subscriber;
    }

    private void start() {
        actionSubject
                .subscribeOn(actionScheduler) // Run on the main thread by default
                .startWith(startAction) // Start the first load without event
                .distinctUntilChanged()
                .filter(new Predicate<A>() {
                    @Override
                    public boolean test(@NonNull A action) throws Exception {
                        return !fetched && !loading;
                    }
                })
                .filter(actionFiler)
                .toFlowable(actionStrategy)
                .doOnNext(new Consumer<A>() {
                    @Override
                    public void accept(@NonNull A action) throws Exception {
                        loading = true;
                    }
                })
                .doOnNext(actionConsumer)
                .observeOn(dataScheduler) // Load on the background thread by default
                .concatMap(new Function<A, Publisher<List<D>>>() { // Convert Hot to Cold observable
                    @Override
                    public Publisher<List<D>> apply(@NonNull A action) throws Exception {
                        return dataLoader;
                    }
                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        return !fetched;
                    }
                })
                .observeOn(actionScheduler)
                .doOnEach(new Consumer<Notification<List<D>>>() {
                    @Override
                    public void accept(@NonNull Notification<List<D>> n) throws Exception {
                        loading = false;
                        if (n.isOnNext() && n.getValue().size() == 0) fetched = true;
                    }
                })
                .subscribe(dataSubscriber);
    }


    public class Builder {

    }

}
