package com.jithin.nudge.controlleradvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.jithin.nudge.util.ResponseWrapper;
import com.jithin.nudge.util.WrappedResponse;
import com.jithin.nudge.exception.JobApplicationException;
import com.jithin.nudge.exception.UserAlreadyPresentException;
import com.jithin.nudge.exception.NudgeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity<WrappedResponse<Object>> handleUserAlreadyPresentException(UserAlreadyPresentException ex) {
        return ResponseWrapper.badRequest(ex.getMessage());
    }

    @ExceptionHandler(JobApplicationException.class)
    public ResponseEntity<WrappedResponse<Object>> handleJobApplicationException(JobApplicationException ex) {
        return ResponseWrapper.badRequest(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<WrappedResponse<Object>> UsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseWrapper.unauthorized(ex.getMessage());
    }

    @ExceptionHandler(NudgeException.class)
    public ResponseEntity<WrappedResponse<Object>> handleNudgeException(NudgeException ex) {
        return ResponseWrapper.badRequest(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WrappedResponse<Object>> handleException(Exception ex) {
        return ResponseWrapper.internalServerError("An unexpected error occurred: " + ex.getMessage());
    }
}
