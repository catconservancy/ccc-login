package org.rmcc.ccc.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CccLogger {

    private static boolean enabled = true;
    private static boolean verboseMode = false;
    private static Level logLevel = Level.DEBUG;

    private CccLogger() {
        // Unreachable
    }

    public static void log(Logger logger, Level level, String txt) {
        if (logger != null && level != null && enabled && level.ordinal() <= logLevel.ordinal()) {
            switch (level) {
                case TRACE:
                    logger.trace(txt);
                    break;
                case DEBUG:
                    logger.debug(txt);
                    break;
                case INFO:
                    logger.info(txt);
                    break;
                case WARN:
                    logger.warn(txt);
                    break;
                case ERROR:
                    logger.error(txt);
                    break;
            }
        }
    }

    public static void log(Logger logger, Level level, String format, Object... argArray) {
        if (logger != null && level != null && enabled && level.ordinal() <= logLevel.ordinal()) {
            switch (level) {
                case TRACE:
                    logger.trace(format, argArray);
                    break;
                case DEBUG:
                    logger.debug(format, argArray);
                    break;
                case INFO:
                    logger.info(format, argArray);
                    break;
                case WARN:
                    logger.warn(format, argArray);
                    break;
                case ERROR:
                    logger.error(format, argArray);
                    break;
            }
        }
    }

    public static void log(Logger logger, Level level, String txt, Throwable throwable) {
        if (logger != null && level != null && enabled && level.ordinal() <= logLevel.ordinal()) {
            switch (level) {
                case TRACE:
                    logger.trace(txt, throwable);
                    break;
                case DEBUG:
                    logger.debug(txt, throwable);
                    break;
                case INFO:
                    logger.info(txt, throwable);
                    break;
                case WARN:
                    logger.warn(txt, throwable);
                    break;
                case ERROR:
                    logger.error(txt, throwable);
                    break;
            }
        }
    }

    /**
     * Error methods
     */
    public static void error(String txt) {
        log(getLogger(), Level.ERROR, txt);
    }

    public static void error(String format, Object... argArray) {
        log(getLogger(), Level.ERROR, format, argArray);
    }

    public static void error(String txt, Throwable throwable) {
        log(getLogger(), Level.ERROR, txt, throwable);
    }

    /**
     * Warn methods
     */
    public static void warn(String txt) {
        log(getLogger(), Level.WARN, txt);
    }

    public static void warn(String format, Object... argArray) {
        log(getLogger(), Level.WARN, format, argArray);
    }

    public static void warn(String txt, Throwable throwable) {
        log(getLogger(), Level.WARN, txt, throwable);
    }

    /**
     * Info methods
     */
    public static void info(String txt) {
        log(getLogger(), Level.INFO, txt);
    }

    public static void info(String format, Object... argArray) {
        log(getLogger(), Level.INFO, format, argArray);
    }

    public static void info(String txt, Throwable throwable) {
        log(getLogger(), Level.INFO, txt, throwable);
    }

    /**
     * Debug methods
     */
    public static void debug(String txt) {
        log(getLogger(), Level.DEBUG, txt);
    }

    public static void debug(String format, Object... argArray) {
        log(getLogger(), Level.DEBUG, format, argArray);
    }

    public static void debug(String txt, Throwable throwable) {
        log(getLogger(), Level.DEBUG, txt, throwable);
    }

    /**
     * Trace methods
     */
    public static void trace(String txt) {
        log(getLogger(), Level.TRACE, txt);
    }

    public static void trace(String format, Object... argArray) {
        log(getLogger(), Level.TRACE, format, argArray);
    }

    public static void trace(String txt, Throwable throwable) {
        log(getLogger(), Level.TRACE, txt, throwable);
    }

    public static boolean isEnabledFor(Logger logger, Level level) {
        boolean res = false;
        if (logger != null && level != null) {
            switch (level) {
                case TRACE:
                    res = logger.isTraceEnabled();
                    break;
                case DEBUG:
                    res = logger.isDebugEnabled();
                    break;
                case INFO:
                    res = logger.isInfoEnabled();
                    break;
                case WARN:
                    res = logger.isWarnEnabled();
                    break;
                case ERROR:
                    res = logger.isErrorEnabled();
                    break;
            }
        }
        return res;
    }

    public static boolean isVerboseMode() {
        return verboseMode;
    }

    public static void setVerboseMode(boolean v) {
        verboseMode = v;
    }

    public static void setLevel(Level l) {
        logLevel = l;
    }

    public static Level getLevel() {
        return logLevel;
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(getCallingClass(3));
    }

    private static Class getCallingClass(int level) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement te = elements[level + 1];
        String raw = te.toString().split("\\(")[0];

        Class c;
        try {
            c = Class.forName(raw.substring(0, raw.lastIndexOf('.')));
        } catch (ClassNotFoundException ex) {
            c = CccLogger.class;
        }

        return c;
    }

    public enum Level {
        ERROR, WARN, INFO, DEBUG, TRACE
    }
}
