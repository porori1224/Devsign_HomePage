package kr.ac.devsign.HomePage.infrastructure.exception;

public class DuplicateDiscordIdException extends RuntimeException{
    public DuplicateDiscordIdException(String message) {
        super(message);
    }
}
