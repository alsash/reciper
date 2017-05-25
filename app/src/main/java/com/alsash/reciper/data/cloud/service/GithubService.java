package com.alsash.reciper.data.cloud.service;

import com.alsash.reciper.data.cloud.response.ConfigResponse;

import io.reactivex.Maybe;

/**
 * A Github REST service.
 * Documentation at https://developer.github.com/v3/
 */
public interface GithubService {

    Maybe<ConfigResponse> getConfig();
}
