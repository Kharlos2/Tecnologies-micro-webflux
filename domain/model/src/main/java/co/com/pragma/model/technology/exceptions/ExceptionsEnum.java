package co.com.pragma.model.technology.exceptions;

public enum ExceptionsEnum {
    ALREADY_EXIST(409, "Product name already in use"),
    INVALID_NAME( 400 , "El nombre es obligatorio y no debe exceder 50 caracteres"),
    INVALID_DESCRIPTION(400, "La descripción es obligatoria y no debe exceder 90 caracteres"),
    INVALID_QUERY(400, "Se necesita un array de ids");

    private final int httpStatus;
    private final String message;

    ExceptionsEnum(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
