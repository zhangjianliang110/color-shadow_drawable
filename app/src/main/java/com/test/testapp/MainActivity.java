package com.test.testapp;

import com.test.testapp.R;
import com.test.testapp.view.ShadowDrawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mIvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvTest = findViewById(R.id.ivTest);
        ViewCompat.setBackground(mIvTest, getShadowBg(
                ShadowDrawable.ShadowCorner.ALL, ShadowDrawable.ShadowSide.ALL
        ));
    }

    private ShadowDrawable getShadowBg(int cornerType, int sideType) {
        Resources resources = getResources();
        ColorStateList backgroundColor = ColorStateList.valueOf(resources.getColor(android.R.color.white));//设置背景色
        ShadowDrawable shadowBg = new ShadowDrawable(resources, backgroundColor, dip2px(25), dip2px(3), dip2px(6));
        shadowBg.setShadowCorner(cornerType);
        shadowBg.setShadowSide(sideType);
        int startColor = resources.getColor(R.color.shadowview_shadow_start_color);//阴影内侧颜色
        int endColor = resources.getColor(R.color.shadowview_shadow_end_color);//阴影外侧颜色
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
