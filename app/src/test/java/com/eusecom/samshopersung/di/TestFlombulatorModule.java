package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.Flombulator;
import com.eusecom.samshopersung.FlombulatorI;

import org.mockito.Mockito;
import dagger.Module;
import dagger.Provides;
import static org.mockito.Mockito.when;

@Module
public class TestFlombulatorModule {

    @Provides
    FlombulatorI provideTestFlombulatorI() {
        System.out.println("I am the mocked flombulator");
        FlombulatorI flum = Mockito.mock(Flombulator.class);
        when(flum.flombulateMe()).thenReturn("flombulated test");
        return flum;
    }
}
