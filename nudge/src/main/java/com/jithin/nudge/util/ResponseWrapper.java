package com.jithin.nudge.util;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseWrapper {

    // --- Success (200 OK) ---

    public static ResponseEntity<WrappedResponse<Object>> ok(String message) {
        return build(HttpStatus.OK, message, null);
    }

    public static <T> ResponseEntity<WrappedResponse<T>> ok(String message, T data) {
        return build(HttpStatus.OK, message, data);
    }

    // --- Created (201 Created) ---

    public static ResponseEntity<WrappedResponse<Object>> created(String message) {
        return build(HttpStatus.CREATED, message, null);
    }

    public static <T> ResponseEntity<WrappedResponse<T>> created(String message, T data) {
        return build(HttpStatus.CREATED, message, data);
    }

    // --- Accepted (202 Accepted) ---

    public static ResponseEntity<WrappedResponse<Object>> accepted(String message) {
        return build(HttpStatus.ACCEPTED, message, null);
    }

    public static <T> ResponseEntity<WrappedResponse<T>> accepted(String message, T data) {
        return build(HttpStatus.ACCEPTED, message, data);
    }

    // --- Found (302 Found) ---

    public static <T> ResponseEntity<WrappedResponse<T>> found(String message, T data) {
        return build(HttpStatus.FOUND, message, data); // 302
    }

    // --- Client Errors (4xx) ---

    public static ResponseEntity<WrappedResponse<Object>> notfound(String message) {
        return build(HttpStatus.NOT_FOUND, message, null);
    }

    public static ResponseEntity<WrappedResponse<Object>> badRequest(String message) {
        return build(HttpStatus.BAD_REQUEST, message, null);
    }

    public static <T> ResponseEntity<WrappedResponse<T>> badRequest(String message, T data) {
        return build(HttpStatus.BAD_REQUEST, message, data);
    }

    public static ResponseEntity<WrappedResponse<Object>> unauthorized(String message) {
        return build(HttpStatus.UNAUTHORIZED, message, null);
    }

    public static ResponseEntity<WrappedResponse<Object>> forbidden(String message) {
        return build(HttpStatus.FORBIDDEN, message, null);
    }

    public static ResponseEntity<WrappedResponse<Object>> timeout(String message) {
        return build(HttpStatus.REQUEST_TIMEOUT, message, null);
    }

    // --- Server Errors (5xx) ---

    public static ResponseEntity<WrappedResponse<Object>> internalServerError(String message) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> ResponseEntity<WrappedResponse<T>> internalServerError(String message, T data) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }

    // --- Generic Builder ---

    public static <T> ResponseEntity<WrappedResponse<T>> build(HttpStatus status, String message, T data) {
        WrappedResponse<T> response = new WrappedResponse<>(Instant.now(), message, data);
        return ResponseEntity.status(status).body(response);
    }
}
