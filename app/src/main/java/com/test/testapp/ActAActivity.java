package com.test.testapp;

import com.test.testapp.logger.LoggerUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActAActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ActAActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggerUtils.d("------------------onCreate:" + getClass().getSimpleName());
        setContentView(R.layout.activity_act_a);
        TextView tvActA = findViewById(R.id.tvActA);
        tvActA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActBActivity.launch(ActAActivity.this);
            }
        });
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
