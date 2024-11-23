package com.shr4pnel.logging;

/**
 * Print debug information about the running program
 *
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class Logger {
    private static boolean loggingEnabled = false;

    /**
     * Set true/false to print debugging information
     */
    public static void enable() {
        synchronized (Logger.class) {
            loggingEnabled = true;
        }
    }

    /**
     * Revert to previous debugging state (Not nested)
     */
    public static void disable() {
        loggingEnabled = false;
    }

    /**
     * Display text for debugging purposes
     *
     * @param fmt    The same as printf
     * @param params Arguments
     */
    public static void trace(String fmt, Object... params) {
        if (loggingEnabled) {
            synchronized (Logger.class) {
                System.out.printf(fmt, params);
                System.out.println();
            }
        }
    }

    /**
     * Display always text for debugging purposes
     *
     * @param fmt    The same as printf etc
     * @param params arguments
     */
    public static void traceA(String fmt, Object... params) {
        synchronized (Logger.class) {
            System.out.printf(fmt, params);
            System.out.println();
        }
    }

    /**
     * Display a fatal message if the assertion fails
     *
     * @param ok     Assert that ok is true
     * @param fmt    The same as printf etc
     * @param params arguments
     */
    public static void assertTrue(boolean ok, String fmt, Object... params) {
        if (!ok) {
            error("Assert - " + fmt, params);
        }
    }

    /**
     * Display a fatal error message, then exit
     *
     * @param fmt    The same as printf etc
     * @param params arguments
     */
    public static void error(String fmt, Object... params) {
        synchronized (Logger.class) {
            System.out.printf("FATAL ERROR: " + fmt, params);
            System.out.println();
            System.exit(-1);
        }
    }
}


