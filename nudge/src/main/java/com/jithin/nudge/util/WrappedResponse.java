package com.jithin.nudge.util;

import java.time.Instant;

public record WrappedResponse<T>(Instant time, String message, T data) {
}
