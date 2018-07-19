package com.eusecom.samshopersung;

import org.mockito.Mockito;
import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.when;

@Module
public class FlombulatorTestModule {

    @Provides
    Flombulator provideTestFlombulator() {
        System.out.println("I the mocked flumbolator have been summoned, behold my mock power");
        Flombulator flum = Mockito.mock(Flombulator.class);
        when(flum.flombulateMe()).thenReturn("flombulated test");
        return flum;
    }
}
