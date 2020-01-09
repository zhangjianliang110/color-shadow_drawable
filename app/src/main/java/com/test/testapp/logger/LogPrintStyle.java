package com.test.testapp.logger;

import android.support.annotation.NonNull;

/**
 * @author Kale
 * @date 2016/12/8
 */

public class LogPrintStyle extends PrintStyle {

    private static final String PREFIX_BORDER = "│ ";
    @Override
    public String beforePrint() {
        return "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    }

    /**
     * ==> onCreate(MainActivity.java:827) Thread:main
     */
    @Override
    protected String printStackAndThread() {
        if (!getSettings().showMethodLink) {
            return "";
        }

        int index = Logger.STACK_OFFSET + getSettings().methodOffset ;
        if (getPrinter().isCustomTag()) {
            index -= 2;
        }
        if(Logger.isTagJson){
            index++;
        }
        final StackTraceElement stack = Thread.currentThread().getStackTrace()[index];

       /* if (sb.length() < 0) {
            sb = new StringBuilder();
        } else {
            sb.setLength(0);

        }*/
         StringBuilder sb = new StringBuilder(100);
       /* sb.append("\n");
        sb.append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        sb.append("\n");*/
           sb.append(PREFIX_BORDER);
           String info = String.format(" ────> %s(%s:%s)",
               stack.getMethodName(),
               stack.getFileName(),
               stack.getLineNumber());
           sb.append(info);


           if (getSettings().showThreadInfo) {
               sb.append(" Thread: ").append(Thread.currentThread().getName()); // Thread:main
           }
           return sb.toString();
    }

    @NonNull
    @Override
    public String printLog(String message, int line, int wholeCount) {
       /* if (line == wholeCount - 1) {
            // last line
            return "│ " + message + getTail();
        } else {
            return PREFIX_BORDER + message;
        }*/

        return PREFIX_BORDER + message;
    }

    @Override
    public String afterPrint() {
        return "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    }



}
