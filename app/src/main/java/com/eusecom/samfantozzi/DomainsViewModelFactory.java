package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;

public class DomainsViewModelFactory implements ViewModelProvider.Factory {

    private final DomainsClickInterceptor loggingInterceptor;
    DgAllEmpsAbsIDataModel mDataModel;

    public DomainsViewModelFactory(DomainsClickInterceptor loggingInterceptor, DgAllEmpsAbsIDataModel dataModel) {
        this.loggingInterceptor = loggingInterceptor;
        this.mDataModel = dataModel;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DomainsViewModel.class)) {
            return (T) new DomainsViewModel(loggingInterceptor, mDataModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
