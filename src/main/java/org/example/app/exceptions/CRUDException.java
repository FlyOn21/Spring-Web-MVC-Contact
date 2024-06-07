package org.example.app.exceptions;

public class CRUDException extends RuntimeException{

        public CRUDException(String message) {
            super(message);
        }
}
