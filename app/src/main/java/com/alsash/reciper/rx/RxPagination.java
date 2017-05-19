package com.alsash.reciper.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * A Reactive Pagination that helps load data asynchronously in response to scroll actions.
 */
public class RxPagination<D> extends RxLoader<Integer, D> {

    public void onScroll(int lastVisiblePosition) {
        nextAction(lastVisiblePosition);
    }

    public static class Builder<D> extends RxLoaderBuilder<Integer, D> {

        public Builder() {
            super();
        }

        @Override
        public Builder<D> when(PublishSubject<Integer> actionSubject) {
            return (Builder<D>) super.when(actionSubject);
        }

        @Override
        public Builder<D> strategy(BackpressureStrategy actionStrategy) {
            return (Builder<D>) super.strategy(actionStrategy);
        }

        @Override
        public Builder<D> only(Predicate<Integer> actionFiler) {
            return (Builder<D>) super.only(actionFiler);
        }

        @Override
        public Builder<D> before(Consumer<Integer> actionConsumer) {
            return (Builder<D>) super.before(actionConsumer);
        }

        @Override
        public Builder<D> actOn(Scheduler actionScheduler) {
            return (Builder<D>) super.actOn(actionScheduler);
        }

        @Override
        public Builder<D> start(Integer startAction) {
            return (Builder<D>) super.start(startAction);
        }

        @Override
        public Builder<D> into(Subscriber<List<D>> dataSubscriber) {
            return (Builder<D>) super.into(dataSubscriber);
        }

        @Override
        public Builder<D> into(DisposableSubscriber<List<D>> dataSubscriber,
                               CompositeDisposable disposable) {
            return (Builder<D>) super.into(dataSubscriber, disposable);
        }

        @Override
        public Builder<D> load(Publisher<List<D>> dataLoader) {
            return (Builder<D>) super.load(dataLoader);
        }

        @Override
        public Builder<D> loadOn(Scheduler dataScheduler) {
            return (Builder<D>) super.loadOn(dataScheduler);
        }

        @Override
        public RxPagination<D> build() {
            if (loader.startAction == null) loader.startAction = -1;
            return (RxPagination<D>) super.build();
        }
    }
}
