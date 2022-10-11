package org.starling.exceptions;

public class APIException extends Exception{

    int statusCode;
    public APIException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return statusCode;
    }
}
