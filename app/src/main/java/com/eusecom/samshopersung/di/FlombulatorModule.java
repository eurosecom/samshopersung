package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.Flombulator;
import com.eusecom.samshopersung.FlombulatorI;

import dagger.Module;
import dagger.Provides;

@Module
public class FlombulatorModule {

    @Provides
    FlombulatorI provideFlombulatorI() {
        System.out.println("Flombulated real implementation of FlombulatorModule");
        return new Flombulator();
    }
}
