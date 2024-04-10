package it.be.fido.admin.exceptions;

public class RoleNotExistsException extends Exception {
    public RoleNotExistsException(String message) {
        super(message);
    }
}
