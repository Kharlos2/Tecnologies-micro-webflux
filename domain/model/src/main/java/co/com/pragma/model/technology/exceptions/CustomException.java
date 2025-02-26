package co.com.pragma.model.technology.exceptions;

public class CustomException extends Exception{

    private final int status;

    public CustomException(ExceptionsEnum exception) {
        super(exception.getMessage());
        this.status = exception.getHttpStatus();
    }

    public int getStatus() {
        return status;
    }
}
