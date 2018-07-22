package com.eusecom.samshopersung.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.Flombulator;
import com.eusecom.samshopersung.FlombulatorI;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.mvvmschedulers.ImmediateSchedulerProvider;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import rx.Observable;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

@Module
public class TestFlombulatorModule {

    @Provides
    FlombulatorI provideTestFlombulatorI() {
        System.out.println("I am the mocked flombulator");
        FlombulatorI flum = Mockito.mock(Flombulator.class);
        when(flum.flombulateMe()).thenReturn("flombulated test");
        return flum;
    }

    @Provides
    public ShopperIDataModel providesShopperIDataModel() {

        System.out.println("I am the mocked datamodel");
        ShopperIDataModel datamodel = Mockito.mock(ShopperDataModel.class);

        when(datamodel.getStringFromDataModel()).thenReturn("Mocked String from DataModel");

        List<String> mockedliststr = new ArrayList<>();
        mockedliststr.add("Mocked Rx String from DataModel");
        when(datamodel.getRxStringFromDataModel()).thenReturn(Observable.just(mockedliststr));

        List<CompanyKt> mockedlistcompany = new ArrayList<>();
        CompanyKt comp = new CompanyKt("999","Mocked F999","", 0,"","","",""
                ,"","","","","","","","",""
                ,"","","","");
        mockedlistcompany.add(comp);

        String serverx="www.eshoptest.sk";
        String encrypted="c62296b32155f6afce6b8d3997b52689a37e10372b29b92146fd8e08c5c3d567822e5db86f261f76cba252e8261980bd92e33b83ef84a8a2c9deea42f779ee1a8e5600ccf32aa38bfbd4639c19594809";
        String ds="4.857403475138863";

        when(datamodel.getCompaniesFromMysqlServer(anyString(), anyString(), anyString())).thenReturn(Observable.just(mockedlistcompany));

        return datamodel;

    }

    @Provides @Named("testing")
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        //return ((TestApplication) application).getSchedulerProvider(); to remove getSchedulerProvider from TestAplication
        return new ImmediateSchedulerProvider();
    }

    @Provides
    public ShopperIMvvmViewModel providesShopperIMvvmViewModel(ShopperIDataModel dataModel
            , @Named("testing") ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences
            , ConnectivityManager connectivityManager) {
        return new ShopperMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, connectivityManager);
    }


}
