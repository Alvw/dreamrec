package com.crostec.ads;

/**
 *
 */
public class ApplicationException extends Throwable{

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
