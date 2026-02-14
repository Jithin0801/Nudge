package com.jithin.nudge;

import com.jithin.nudge.controlleradvice.GlobalExceptionHandler;
import com.jithin.nudge.exception.JobApplicationException;
import com.jithin.nudge.exception.NudgeException;
import com.jithin.nudge.exception.UserAlreadyPresentException;
import com.jithin.nudge.util.WrappedResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleUserAlreadyPresentException_ShouldReturnBadRequest() {
        String errorMessage = "User already exists";
        UserAlreadyPresentException ex = new UserAlreadyPresentException(errorMessage);

        ResponseEntity<WrappedResponse<Object>> response = exceptionHandler.handleUserAlreadyPresentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void handleJobApplicationException_ShouldReturnBadRequest() {
        String errorMessage = "Job application error";
        JobApplicationException ex = new JobApplicationException(errorMessage);

        ResponseEntity<WrappedResponse<Object>> response = exceptionHandler.handleJobApplicationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void handleNudgeException_ShouldReturnBadRequest() {
        String errorMessage = "General application error";
        // Anonymous subclass for testing abstract class
        NudgeException ex = new NudgeException(errorMessage) {};

        ResponseEntity<WrappedResponse<Object>> response = exceptionHandler.handleNudgeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void handleException_ShouldReturnInternalServerError() {
        String errorMessage = "Something went wrong";
        Exception ex = new Exception(errorMessage);

        ResponseEntity<WrappedResponse<Object>> response = exceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred: " + errorMessage, response.getBody().message());
    }
}
