1、root build.gradle中添加仓库地址：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
2、app module build.gradle中添加依赖

	dependencies {
	        implementation 'com.github.zhangjianliang110:color-shadow_drawable:v1.0'
	}