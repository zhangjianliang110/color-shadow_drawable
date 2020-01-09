package com.test.testapp.logger;

import com.test.testapp.logger.timmer.Timber;
import com.test.testapp.logger.util.ObjParser;
import com.test.testapp.logger.util.XmlJsonParser;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Logger is a wrapper of {@link Timber}
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
class Logger {

    public static final int STACK_OFFSET = 9;

    private static LogPrinter printer;

    public static volatile boolean isTagJson = false;

    // @formatter:off
    protected Logger() {
    }
    // @formatter:on

    public static void initialize(LogBuilder logBuilder) {
        PrintStyle style = logBuilder.style;
        if (style == null) {
            style = new LogPrintStyle();
            logBuilder.logPrintStyle(style);
        }
        printer = new LogPrinter(logBuilder);
        style.setPrinter(printer);
        Timber.plant(printer);
    }

    /**
     * @param priority one of
     *                 {@link Log#VERBOSE},
     *                 {@link Log#DEBUG},
     *                 {@link Log#INFO},
     *                 {@link Log#WARN},
     *                 {@link Log#ERROR}
     */
    public static void openLog(int priority) {
        changeLogLev(priority);
    }

    /**
     * @param priority one of
     *                 {@link Log#VERBOSE},
     *                 {@link Log#DEBUG},
     *                 {@link Log#INFO},
     *                 {@link Log#WARN},
     *                 {@link Log#ERROR}
     */
    public static void changeLogLev(int priority) {
        printer.getLogBuilder().logPriority(priority).build();
    }

    public static void closeLog() {
        printer.getLogBuilder().logPriority(Log.ASSERT);
    }

    public static LogBuilder getBuild() {
        return printer.getLogBuilder();
    }

    public static Timber.Tree t(String tag) {
        tag = printer.maybeAddPrefix(tag);
        return Timber.tag(tag);
    }

    public static void v(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.v(message, args);
    }

    public static void d(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.d(message, args);
    }

    public static void i(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.i(message, args);
    }

    public static void w(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.w(message, args);
    }

    public static void w(Throwable throwable, String message, Object... args) {
        message = handleNullMsg(message);
        Timber.w(throwable, message, args);
    }

    public static void e(String message, Object... args) {
        message = handleNullMsg(message);
        Timber.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        message = handleNullMsg(message);
        Timber.e(throwable, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Timber.i(XmlJsonParser.json(json));
    }

    public static void json(String json, String tag) {
        String str = XmlJsonParser.json(json);
        isTagJson = true;
        Timber.tag(tag).i(str);
        isTagJson = false;
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Timber.d(XmlJsonParser.xml(xml));
    }

    /**
     * Formats the json content and print it
     *
     * @param object Bean,Array,Collection,Map,Pojo and so on
     */
    public static void object(Object object) {
        Timber.i(ObjParser.parseObj(object));
    }

    public static void object(String tag, Object object) {
        String str = ObjParser.parseObj(object);
        isTagJson = true;
        Timber.tag(tag).i(str);
        isTagJson = false;
    }

    /**
     * 插入自定义的tree
     */
    public static void plant(Timber.Tree tree) {
        Timber.plant(tree);
    }

    /**
     * 清空所有tree
     */
    public static void uprootAll() {
        Timber.uprootAll();
    }

    /**
     * Timber will swallow message if it's null and there's no throwable.
     */
    @NonNull
    private static String handleNullMsg(String message) {
        if (message == null) {
            message = "null";
        }
        return message;
    }

}
