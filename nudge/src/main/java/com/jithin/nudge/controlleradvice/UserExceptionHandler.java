package com.jithin.nudge.controlleradvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.jithin.nudge.util.ResponseWrapper;
import com.jithin.nudge.util.WrappedResponse;
import com.jithin.nudge.controller.SecurityController;
import com.jithin.nudge.exception.UserAlreadyPresentException;
import com.jithin.nudge.repository.UserPersonalRepository;
import com.jithin.nudge.repository.UserSecurityRepository;
import com.jithin.nudge.security.UserAuthenticationFilter;
import com.jithin.nudge.security.UserAuthenticationManager;
import com.jithin.nudge.security.UserAuthenticationProvider;
import com.jithin.nudge.service.UserService;

@ControllerAdvice(assignableTypes = {UserService.class, SecurityController.class, UserPersonalRepository.class, UserSecurityRepository.class, UserAuthenticationManager.class,
        UserAuthenticationProvider.class, UserAuthenticationFilter.class})
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity<WrappedResponse<Object>> handleUserAlreadyPresentException(UserAlreadyPresentException ex) {
        return ResponseWrapper.badRequest(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<WrappedResponse<Object>> UsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseWrapper.unauthorized(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WrappedResponse<Object>> handleException(Exception ex) {
        return ResponseWrapper.internalServerError("An unexpected error occurred: " + ex.getMessage());
    }

}
