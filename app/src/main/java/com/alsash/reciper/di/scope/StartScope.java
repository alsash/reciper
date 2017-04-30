package com.alsash.reciper.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import dagger.releasablereferences.CanReleaseReferences;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Documented
@Retention(RUNTIME)
@CanReleaseReferences
public @interface StartScope {
}
