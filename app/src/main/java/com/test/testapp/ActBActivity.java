package com.test.testapp;

import com.test.testapp.logger.LoggerUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActBActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ActBActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_b);
        LoggerUtils.d("------------------onCreate:" + getClass().getSimpleName());
        findViewById(R.id.tvActB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActBActivity.launch(ActBActivity.this);
            }
        });
        FragmentA fmtA = new FragmentA();
        FragmentUtils.showFragmentWithOutAnimation(getSupportFragmentManager(), fmtA, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoggerUtils.d("------------------onStart:" + getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoggerUtils.d("------------------onResume:" + getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoggerUtils.d("------------------onPause:" + getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoggerUtils.d("------------------onStop:" + getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggerUtils.d("------------------onDestroy:" + getClass().getSimpleName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LoggerUtils.d("------------------onNewIntent:" + getClass().getSimpleName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoggerUtils.d("------------------onRestart:" + getClass().getSimpleName());
    }
}
