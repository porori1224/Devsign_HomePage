package kr.ac.devsign.HomePage.infrastructure.exception;

public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(String message) {
        super(message);
    }
}
