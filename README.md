1、root build.gradle中添加仓库地址：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
2、app module build.gradle中添加依赖

	dependencies {
	        implementation 'com.github.zhangjianliang110:color-shadow_drawable:v1.0.1'
	}
	
3、Demo

```
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

```
