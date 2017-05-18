package com.alsash.reciper.rx;

/**
 * A Reactive Pagination that helps load data asynchronously in response to scroll actions.
 */
public class RxPagination<D> extends RxLoader<Integer, D> {

    public void onScroll(int lastVisiblePosition) {
        nextAction(lastVisiblePosition);
    }

    public static class Builder<D> extends RxLoaderBuilder<Integer, D> {
        @Override
        public RxLoader<Integer, D> build() {
            if (loader.startAction == null) loader.startAction = -1;
            return super.build();
        }
    }
}
