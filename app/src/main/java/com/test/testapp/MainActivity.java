package com.test.testapp;

import com.stupidbird.view.ShadowDrawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView mIvTest;
    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvTest = findViewById(R.id.ivTest);
        mTvTest = findViewById(R.id.tvTest);
        ViewCompat.setBackground(mIvTest, getShadowBg(
                ShadowDrawable.ShadowCorner.ALL, ShadowDrawable.ShadowSide.ALL, 25
        ));
        mIvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActAActivity.launch(MainActivity.this);
            }
        });
        ShadowDrawable textBg = getShadowBg(ShadowDrawable.ShadowCorner.ALL, ShadowDrawable.ShadowSide.ALL, 2);
        textBg.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        ViewCompat.setBackground(mTvTest, textBg);
    }

    private ShadowDrawable getShadowBg(int cornerType, int sideType, float radius) {
        Resources resources = getResources();
        ColorStateList backgroundColor = ColorStateList.valueOf(resources.getColor(android.R.color.white));//设置背景色
        ShadowDrawable shadowBg = new ShadowDrawable(resources, backgroundColor, dip2px(radius), dip2px(3), dip2px(6));
        shadowBg.setShadowCorner(cornerType);
        shadowBg.setShadowMultiplier(1.5f);
        shadowBg.setShadowSide(sideType);
        int startColor = resources.getColor(R.color.shadow_start_color);//阴影内侧颜色
        int endColor = resources.getColor(R.color.shadow_end_color);//阴影外侧颜色
        shadowBg.setShadowColor(startColor, endColor);
        return shadowBg;
    }

    private int dip2px(float dipValue) {
        if (dipValue == 0) {
            return 0;
        }
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
