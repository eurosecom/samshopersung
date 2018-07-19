package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.Flombulator;
import dagger.Module;
import dagger.Provides;

@Module
public class FlombulatorModule {

    @Provides
    Flombulator provideFlombulator() {
        System.out.println("Flombulated real implementation of FlombulatorModule");
        return new Flombulator();
    }
}
