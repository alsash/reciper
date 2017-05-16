package com.alsash.reciper.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by alsash on 5/17/17.
 */

class RxLoaderBuilder<A, D> {

    private final RxLoader<A, D> loader = new RxLoader<>();

    RxLoaderBuilder() {
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

    public RxLoaderBuilder<A, D> first(A startAction) {
        loader.startAction = startAction;
        return this;
    }

    public RxLoaderBuilder<A, D> into(Subscriber<List<D>> dataSubscriber) {
        loader.dataSubscriber = dataSubscriber;
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
}
