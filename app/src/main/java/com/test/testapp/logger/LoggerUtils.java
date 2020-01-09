package com.test.testapp.logger;

import com.test.testapp.BuildConfig;
import com.test.testapp.logger.util.JsonParser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Fallwater潘建波 on 2018/4/10
 * @mail 1667376033@qq.com
 * 功能描述:
 * 包装的logger工具，
 * 请不要直接使用第三方Log库，需要需要新的打印效果，请扩充此类
 */
public class LoggerUtils {

    public final static boolean DEBUG = BuildConfig.DEBUG;

    public static void init() {
        Logger.initialize(
                new LogBuilder()
                        .logPrintStyle(new LogPrintStyle())
                        .showMethodLink(false)
                        .showThreadInfo(false)
                        .tagPrefix("")
                        .globalTag("AkuSocial")
                        .methodOffset(1)
                        .logPriority(DEBUG ? Log.VERBOSE : Log.ASSERT)
                        .build());
    }


    public static void d(String tag, String message) {
        if (DEBUG) {
            Logger.t(tag).d(message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Logger.t(tag).i(message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Logger.t(tag).w(message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Logger.t(tag).e(message);
        }
    }

    public static void json(String tag, String msg) {
        if (DEBUG) {
            Logger.json(TextUtils.isEmpty(msg) ? "" : msg, tag);
        }
    }

    public static void exception(String tag, String message, Throwable e) {
        if (DEBUG) {
            Logger.t(tag).e(e, message);
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            Logger.t(tag).v(message);
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            Logger.d(message);
        }
    }

    public static void i(String message) {
        if (DEBUG) {
            Logger.i(message);
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            Logger.w(message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Logger.e(message);
        }
    }

    public static void exception(String message, Throwable e) {
        if (DEBUG) {
            Logger.e(e, message);
        }
    }

    public static void v(String message) {
        if (DEBUG) {
            Logger.v(message);
        }
    }

    public static void json(String msg) {
        if (DEBUG) {
            Logger.json(TextUtils.isEmpty(msg) ? "" : msg);
        }
    }

    public static void json(Object obj) {
        if (DEBUG) {
            if (obj == null) {
                Logger.i("null");
            } else if (obj instanceof String) {
                Logger.json((String) obj);
            } else {
                Logger.json(JsonParser.getInstance().parseObjectByGson(obj));
            }
        }
    }

    public static void xml(String xml) {
        if (DEBUG) {
            Logger.xml(TextUtils.isEmpty(xml) ? "" : xml);
        }
    }

    public static void obj(Object obj) {
        if (DEBUG) {
            Logger.object(obj);
        }
    }

    /**
     * 把对象转换为json然后格式化打印
     */
    public static void objAsJson(Object obj) {
        if (DEBUG) {
            String json = JsonParser.getInstance().parseObjectByGson(obj);
            Logger.json(json);
        }
    }

    public static void objAsJson(Object obj, String tag) {
        if (DEBUG) {
            String json = JsonParser.getInstance().parseObjectByGson(obj);
            Logger.json(json, tag);
        }
    }

    public static void obj(String tag, Object obj) {
        if (DEBUG) {
            Logger.object(tag, obj);
        }
    }

    public static void format(String format, Object... args) {
        if (DEBUG) {
            Logger.i(format, args);
        }
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.d(message, args);
        }
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.e(null, message, args);
        }
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.e(throwable, message, args);
        }
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.i(message, args);
        }
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.v(message, args);
        }
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        if (DEBUG) {
            Logger.w(message, args);
        }
    }
}
