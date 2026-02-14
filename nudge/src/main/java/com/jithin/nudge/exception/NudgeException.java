package com.jithin.nudge.exception;

public abstract class NudgeException extends RuntimeException {
    public NudgeException(String message) {
        super(message);
    }
}
