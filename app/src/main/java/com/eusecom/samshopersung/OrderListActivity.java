/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.samshopersung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Show List of all suppliers documents
 * <p>
 * template from DgAeaActivity.java
 */

public class OrderListActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    ShopperIMvvmViewModel mViewModel;

    int saltype = 0;
    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suppliers);

        Intent i = getIntent();
        //0=ordders, 1=invoices
        Bundle extras = i.getExtras();
        saltype = extras.getInt("saltype");

        if (saltype == 0) {

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        new OrderFragment(),
                        new InvoiceFragment()
                };
                private final String[] mFragmentNames = new String[]{
                        getString(R.string.myorders),
                        getString(R.string.myinvoices)
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };

        } else {

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        new InvoiceFragment(),
                        new OrderClosedFragment()
                };
                private final String[] mFragmentNames = new String[]{
                        getString(R.string.myinvoices),
                        getString(R.string.myordersclosed)
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };

        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if (position == 0) {

                }
                if (position == 1) {

                }

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        String serverx = "From act " + mSharedPreferences.getString("servername", "");
        //Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show();

        mDisposable = new CompositeDisposable();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

                    Intent is = new Intent(getApplicationContext(), OfferKtActivity.class);
                    startActivity(is);
                    finish();

                    //navigateToUpdateRoomItem("b05336a4-88b2-46e4-ad45-0f28jcf3668a");

                }
        );

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager = null;
        mPagerAdapter = null;
        mDisposable.clear();

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public void navigateToUpdateRoomItem(final String reqUuid) {

        Log.d("asave requuid", reqUuid);
        mDisposable.add(mViewModel.updateEkassaReqName(reqUuid, "", "", "", "" )
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    // handle error
                    Log.d("asave completed", "complet");
                })
                .doOnError(throwable -> {
                    // handle completion
                    Log.d("asave completed", "Error Throwable " + throwable.getMessage());
                })
                .subscribe(() -> {
                    // handle completion
                    Log.d("asave completed", "complet");
                }, throwable -> {
                    // handle error
                    Log.d("asave completed", "error");
                }));

    }

}
