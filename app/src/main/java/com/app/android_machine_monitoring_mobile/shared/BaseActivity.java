package com.app.android_machine_monitoring_mobile.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.app.android_machine_monitoring_mobile.BreakdownListActivity;
import com.app.android_machine_monitoring_mobile.LoginActivity;
import com.app.android_machine_monitoring_mobile.MachineDashboard;
import com.app.android_machine_monitoring_mobile.MainDashboard;
import com.app.android_machine_monitoring_mobile.RegisterActivity;
import com.app.android_machine_monitoring_mobile.UserProfile;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressBar mProgressBar;

    public void setProgressBar(int resId) {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    public void goto_LoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goto_RegisterActivity() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void goto_MainDashboard() {
        Intent i = new Intent(this, MainDashboard.class);
        startActivity(i);
        finish();
    }

    public void goto_UserProfile() {
        Intent i = new Intent(this, UserProfile.class);
        startActivity(i);
    }

    public void goto_BreakdownListActivity() {
        Intent i = new Intent(this, BreakdownListActivity.class);
        startActivity(i);
    }

    public void goto_MachineDashboard() {
        Intent i = new Intent(this, MachineDashboard.class);
        startActivity(i);
    }

}
