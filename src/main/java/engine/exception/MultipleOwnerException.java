package engine.exception;

public class MultipleOwnerException extends RuntimeException {
    public MultipleOwnerException(String message) {
        super(message);
    }
}
