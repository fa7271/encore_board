package com.encore.board.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

//ControllerAdvice 와, ExceptionHandler 를 통해 예외처리의 공통화 로직 작성
@Slf4j
@ControllerAdvice
public class CommonException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException e) {
        log.error("핸들러가 처리한 EntityNotFoundException message : " + e.getMessage());
        return this.errResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> IllegalArguHandler(IllegalArgumentException e) {
        log.error("핸들러가 처리한 IllegalArgumentException message : " + e.getMessage());
        return this.errResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }
    private ResponseEntity<Map<String, Object>> errResponseMessage(HttpStatus status, String Message) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value()));
        body.put("status message", status.getReasonPhrase());
        body.put("error message", Message);

        return new ResponseEntity<>(body, status);
    }
}
