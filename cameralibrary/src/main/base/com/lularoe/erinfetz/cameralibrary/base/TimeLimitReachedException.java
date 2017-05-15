package com.lularoe.erinfetz.cameralibrary.base;

/**
 *
 */
public class TimeLimitReachedException extends Exception {

    public TimeLimitReachedException() {
        super("You've reached the time limit without starting a recording.");
    }
}