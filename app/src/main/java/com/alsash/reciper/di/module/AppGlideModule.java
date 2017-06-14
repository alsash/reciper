package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.app.ReciperApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.alsash.reciper.app.AppContract.Glide.BITMAP_POOL_FACTOR;
import static com.alsash.reciper.app.AppContract.Glide.DISK_CACHE_SIZE_BYTES;
import static com.alsash.reciper.app.AppContract.Glide.MEMORY_CACHE_FACTOR;

/**
 * Glide integration with injection by Dagger 2
 */
@Module
public class AppGlideModule implements GlideModule {

    @Inject
    OkHttpUrlLoader.Factory factory;

    @Provides
    @Singleton
    static OkHttpUrlLoader.Factory provideOkHttpUrlLoaderFactory(OkHttpClient okHttpClient) {
        return new OkHttpUrlLoader.Factory(okHttpClient);
    }

    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        MemorySizeCalculator calc = new MemorySizeCalculator(context);
        int increasedMemoryCacheSize = (int) (calc.getMemoryCacheSize() * MEMORY_CACHE_FACTOR);
        int increasedBitmapPoolSize = (int) (calc.getBitmapPoolSize() * BITMAP_POOL_FACTOR);
        builder.setMemoryCache(new LruResourceCache(increasedMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(increasedBitmapPoolSize));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE_BYTES));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        ((ReciperApp) context.getApplicationContext())
                .getAppComponent()
                .inject(this);

        glide.register(
                GlideUrl.class,
                InputStream.class,
                factory
        );
    }
}
