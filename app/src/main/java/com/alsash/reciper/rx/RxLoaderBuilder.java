package com.alsash.reciper.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * A Builder for the RxLoader instance
 */
public class RxLoaderBuilder<A, D> {

    protected final RxLoader<A, D> loader;

    public RxLoaderBuilder() {
        loader = new RxLoader<>();
    }

    public RxLoaderBuilder<A, D> when(PublishSubject<A> actionSubject) {
        loader.actionSubject = actionSubject;
        return this;
    }

    public RxLoaderBuilder<A, D> strategy(BackpressureStrategy actionStrategy) {
        loader.actionStrategy = actionStrategy;
        return this;
    }

    public RxLoaderBuilder<A, D> only(Predicate<A> actionFiler) {
        loader.actionFiler = actionFiler;
        return this;
    }

    public RxLoaderBuilder<A, D> before(Consumer<A> actionConsumer) {
        loader.actionConsumer = actionConsumer;
        return this;
    }

    public RxLoaderBuilder<A, D> actOn(Scheduler actionScheduler) {
        loader.actionScheduler = actionScheduler;
        return this;
    }

    public RxLoaderBuilder<A, D> start(A startAction) {
        loader.startAction = startAction;
        return this;
    }

    public RxLoaderBuilder<A, D> into(Subscriber<List<D>> dataSubscriber) {
        loader.dataSubscriber = dataSubscriber;
        return this;
    }

    public RxLoaderBuilder<A, D> into(DisposableSubscriber<List<D>> dataSubscriber,
                                      CompositeDisposable disposable) {
        loader.dataSubscriber = dataSubscriber;
        disposable.add(dataSubscriber);
        return this;
    }

    public RxLoaderBuilder<A, D> load(Publisher<List<D>> dataLoader) {
        loader.dataLoader = dataLoader;
        return this;
    }

    public RxLoaderBuilder<A, D> loadOn(Scheduler dataScheduler) {
        loader.dataScheduler = dataScheduler;
        return this;
    }

    public RxLoader<A, D> build() {
        if (loader.actionSubject == null) loader.actionSubject = PublishSubject.create();
        if (loader.actionStrategy == null) loader.actionStrategy = BackpressureStrategy.DROP;
        if (loader.actionScheduler == null) loader.actionScheduler = AndroidSchedulers.mainThread();
        if (loader.startAction == null) {
            throw new RxLoaderException("startAction == null. Set it at start()");
        }
        if (loader.dataLoader == null) {
            throw new RxLoaderException("dataLoader == null. Set it at load()");
        }
        if (loader.dataSubscriber == null) {
            throw new RxLoaderException("dataSubscriber == null. Set it at into()");
        }
        if (loader.dataScheduler == null) loader.dataScheduler = Schedulers.io();
        return loader;
    }
}
