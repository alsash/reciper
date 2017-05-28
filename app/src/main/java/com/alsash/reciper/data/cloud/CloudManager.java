package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.data.cloud.request.GithubDbRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;

import java.util.Date;

/**
 * A cloud manager for single access to all cloud services
 */
public class CloudManager {

    private final Context context;
    private final GithubDbRequest githubDbRequest;
    private final UsdaRequest usdaRequest;
    private GithubDbConfigResponse jsonConfig;

    public CloudManager(Context context, GithubDbRequest githubDbRequest, UsdaRequest usdaRequest) {
        this.context = context;
        this.githubDbRequest = githubDbRequest;
        this.usdaRequest = usdaRequest;
    }

    @Nullable
    @WorkerThread
    public Date getDbUpdateDate() {
        return (getJsonConfig() == null) ? null : getJsonConfig().dbUpdateDate;
    }

    @Nullable
    @WorkerThread
    private GithubDbConfigResponse getJsonConfig() {
        if (jsonConfig != null) return jsonConfig;
        if (!isOnline()) return null;
        GithubDbConfigResponse response = githubDbRequest.getConfig().blockingGet();
        // String json = new String(Base64.decode(response.content, Base64.DEFAULT), Charset.forName("UTF-8"));
        return response;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected());
    }
}
