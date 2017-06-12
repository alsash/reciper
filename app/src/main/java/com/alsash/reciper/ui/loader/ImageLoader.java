package com.alsash.reciper.ui.loader;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alsash.reciper.mvp.model.entity.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * A Simple Image Loader helper
 */
public class ImageLoader {

    private static final ImageLoader INSTANCE = new ImageLoader();
    private static final String TAG = "ImageLoader";

    public static ImageLoader getInstance() {
        return INSTANCE;
    }

    public void load(Photo photo, ImageView imageView) {
        String url = (photo == null) ? null : photo.getUrl();
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new GlideRequestListener())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    public void load(Photo photo, ImageView imageView, ProgressBar progressBar) {
        String url = (photo == null) ? null : photo.getUrl();
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new GlideRequestListener(progressBar))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    private static class GlideRequestListener implements RequestListener<String, GlideDrawable> {

        private ProgressBar progressBar;

        public GlideRequestListener() {
        }

        public GlideRequestListener(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onException(Exception e,
                                   String model,
                                   Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            Log.d(TAG, e.getMessage(), e);
            resetProgressBar();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource,
                                       String model,
                                       Target<GlideDrawable> target,
                                       boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            resetProgressBar();
            return false;
        }

        private void resetProgressBar() {
            if (progressBar == null) return;
            progressBar.setVisibility(View.GONE);
            progressBar = null;
        }
    }

}
