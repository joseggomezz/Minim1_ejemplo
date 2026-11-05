package org.example;

public class NoPendingOrdersException extends RuntimeException {
    public NoPendingOrdersException(String message) {
        super(message);
    }
    
}
