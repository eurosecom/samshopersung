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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.eusecom.samshopersung.rxbus.RxBus;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class  ProductDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    RxBus rxBus;
    @Inject
    ProductDetailFragment mProductDetailFragment;
    @Inject
    ProductDetailKtFragment mProductDetailKtFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prodetail);

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        mProductDetailFragment,
                        mProductDetailKtFragment
                };
                private final String[] mFragmentNames = new String[]{
                        getString(R.string.detail1),
                        getString(R.string.detail2)
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



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position == 0){

                }
                if(position == 1){

                }

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //String serverx = "From act " + mSharedPreferences.getString("servername", "");
        //Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

                    rxBus.send(new ClickFobEvent());

                }
        );

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager=null;
        mPagerAdapter=null;

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public static class ClickFobEvent {}

}
