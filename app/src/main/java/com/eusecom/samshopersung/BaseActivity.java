package com.eusecom.samshopersung;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

abstract class BaseActivity extends AppCompatActivity {

    protected ProgressBar mProgressBar;

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


}
