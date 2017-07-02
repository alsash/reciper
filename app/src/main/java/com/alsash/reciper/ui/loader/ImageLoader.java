package com.alsash.reciper.ui.loader;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;

/**
 * A Simple Image Loader helper
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static final ImageLoader INSTANCE = new ImageLoader();

    private String source;
    private MutableBoolean completer;
    private WeakReference<ProgressBar> barRef;

    private ImageLoader() {
    }

    public static ImageLoader get() {
        return INSTANCE.clear();
    }

    public void load(ImageView imageView) {
        if (imageView != null) {
            Glide.with(imageView.getContext())
                    .load(source)
                    .listener(new GlideRequestListener(barRef, completer))
                    .diskCacheStrategy(AppContract.Glide.DEFAULT_CACHE_STRATEGY)
                    .fallback(R.color.gray_200)
                    .crossFade()
                    .into(imageView);
        }
        clear();
    }

    public ImageLoader source(Photo photo) {
        if (photo != null) this.source = photo.getUrl();
        return this;
    }

    public ImageLoader source(Author author) {
        if (author != null) return source(author.getPhoto());
        return this;
    }

    public ImageLoader bar(ProgressBar bar) {
        if (bar == null) return this;
        this.barRef = new WeakReference<>(bar);
        return this;
    }

    public ImageLoader completer(MutableBoolean completer) {
        this.completer = completer;
        return this;
    }

    private ImageLoader clear() {
        source = null;
        completer = null;
        barRef = null;
        return this;
    }

    private static class GlideRequestListener implements RequestListener<String, GlideDrawable> {

        private WeakReference<ProgressBar> barRef;
        private MutableBoolean completer;

        public GlideRequestListener(WeakReference<ProgressBar> barRef, MutableBoolean completer) {
            this.barRef = barRef;
            this.completer = completer;
            setComplete(false);
        }

        @Override
        public boolean onException(Exception e,
                                   String model,
                                   Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            Log.d(TAG, e.getMessage(), e);
            setComplete(true);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource,
                                       String model,
                                       Target<GlideDrawable> target,
                                       boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            setComplete(true);
            return false;
        }

        private void setComplete(boolean complete) {
            if (completer != null) {
                completer.set(complete);
                if (complete) completer = null;
            }
            if (barRef != null && barRef.get() != null) {
                barRef.get().setVisibility(complete ? View.GONE : View.VISIBLE);
                if (complete) barRef = null;
            }
        }
    }

}
