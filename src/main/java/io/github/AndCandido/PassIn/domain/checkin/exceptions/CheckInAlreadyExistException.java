package io.github.AndCandido.PassIn.domain.checkin.exceptions;

public class CheckInAlreadyExistException extends RuntimeException {
    public CheckInAlreadyExistException(String message) {
        super(message);
    }
}
