package com.eusecom.samshopersung.mvvmschedulers;

import android.support.annotation.NonNull;
import rx.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface ISchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler ui();
}
