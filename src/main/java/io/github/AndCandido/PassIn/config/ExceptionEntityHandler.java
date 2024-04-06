package io.github.AndCandido.PassIn.config;

import io.github.AndCandido.PassIn.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import io.github.AndCandido.PassIn.domain.attendee.exceptions.AttendeeNotFoundExceptions;
import io.github.AndCandido.PassIn.domain.checkin.exceptions.CheckInAlreadyExistException;
import io.github.AndCandido.PassIn.domain.event.exceptions.EventFullAttendeesException;
import io.github.AndCandido.PassIn.domain.event.exceptions.EventNotFoundException;
import io.github.AndCandido.PassIn.dto.api.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(value = {
        EventNotFoundException.class,
        AttendeeNotFoundExceptions.class
    })
    public ResponseEntity<ErrorResponseDTO> handlerNotFoundExceptions(RuntimeException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(value = {
        AttendeeAlreadyRegisteredException.class,
        CheckInAlreadyExistException.class
    })
    public ResponseEntity<ErrorResponseDTO> handlerConflictExceptions(RuntimeException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @ExceptionHandler(value = {
        EventFullAttendeesException.class
    })
    public ResponseEntity<ErrorResponseDTO> handlerBadRequestExceptions(RuntimeException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

}
