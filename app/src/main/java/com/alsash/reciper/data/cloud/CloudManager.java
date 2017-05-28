package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.data.cloud.request.GithubDbRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;
import com.alsash.reciper.data.db.table.AuthorTable;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A cloud manager for single access to all cloud services
 */
public class CloudManager {

    private final Context context;
    private final GithubDbRequest githubDbRequest;
    private final UsdaRequest usdaRequest;
    private GithubDbConfigResponse dbConfig;

    public CloudManager(Context context, GithubDbRequest githubDbRequest, UsdaRequest usdaRequest) {
        this.context = context;
        this.githubDbRequest = githubDbRequest;
        this.usdaRequest = usdaRequest;
    }

    @Nullable
    @WorkerThread
    public Date getDbUpdateDate() {
        return (getDbConfig() == null) ? null : getDbConfig().dbUpdateDate;
    }

    @Nullable
    @WorkerThread
    public String getDbPathLocale(Locale userLocale) {
        if (dbConfig == null) dbConfig = getDbConfig();
        if (dbConfig == null) return null;
        String userLanguage = userLocale.getLanguage().toLowerCase();
        for (String dbLanguage : dbConfig.dbLanguages) {
            if (userLanguage.equals(dbLanguage.toLowerCase())) {
                return dbConfig.dbPath + dbLanguage.toLowerCase();
            }
        }
        return null;
    }

    @Nullable
    @WorkerThread
    public List<AuthorTable> getAuthorTables(String dbPathLocale) {
        return githubDbRequest.getAuthorTable(dbPathLocale).blockingGet();
    }

    @Nullable
    @WorkerThread
    private GithubDbConfigResponse getDbConfig() {
        if (dbConfig != null) return dbConfig;
        if (!isOnline()) return null;
        dbConfig = githubDbRequest.getConfig().blockingGet();
        return dbConfig;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected());
    }
}
