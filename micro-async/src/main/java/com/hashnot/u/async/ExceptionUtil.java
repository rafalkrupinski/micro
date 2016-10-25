package com.hashnot.u.async;

/**
 * @author Rafał Krupiński
 */
public class ExceptionUtil {
    public static void updateStackTrace(Throwable t, StackTraceElement[] stackTrace) {
        StackTraceElement[] syncTrace = t.getStackTrace();
        StackTraceElement[] combinedTrace = new StackTraceElement[syncTrace.length + stackTrace.length];
        System.arraycopy(syncTrace, 0, combinedTrace, 0, syncTrace.length);
        System.arraycopy(stackTrace, 0, combinedTrace, syncTrace.length, stackTrace.length);
        t.setStackTrace(combinedTrace);
    }

    public static StackTraceElement[] truncateAndPrepend(StackTraceElement[] stackTrace, int truncate, StackTraceElement prepend) {

        int copyLen = stackTrace.length - truncate;
        int nLength = copyLen;

        if (prepend != null)
            ++nLength;

        if (nLength <= 0)
            return new StackTraceElement[0];
/*
        if (nLength >= stackTrace.length)
            return stackTrace;
*/


        StackTraceElement[] result = new StackTraceElement[nLength];

        int destPos;
        if (prepend != null) {
            destPos = 1;
            result[0] = prepend;
        } else
            destPos = 0;

        System.arraycopy(stackTrace, truncate, result, destPos, copyLen);
        return result;
    }

    public static StackTraceElement[] createStackTrace() {
        return new Exception().getStackTrace();
    }
}
