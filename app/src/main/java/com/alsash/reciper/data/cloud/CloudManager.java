package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.data.cloud.request.GithubRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.cloud.response.JsonConfigResponse;

import java.util.Date;

/**
 * A cloud manager for single access to all cloud services
 */
public class CloudManager {

    private final Context context;
    private final GithubRequest githubRequest;
    private final UsdaRequest usdaRequest;
    private JsonConfigResponse jsonConfig;

    public CloudManager(Context context, GithubRequest githubRequest, UsdaRequest usdaRequest) {
        this.context = context;
        this.githubRequest = githubRequest;
        this.usdaRequest = usdaRequest;
    }

    @Nullable
    @WorkerThread
    public Date getDbUpdateDate() {
        return (getJsonConfig() == null) ? null : getJsonConfig().dbUpdateDate;
    }

    @Nullable
    @WorkerThread
    private JsonConfigResponse getJsonConfig() {
        if (jsonConfig != null) return jsonConfig;
        if (!isOnline()) return null;
        jsonConfig = githubRequest.getConfig().blockingGet();
        return jsonConfig;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected());
    }
}
