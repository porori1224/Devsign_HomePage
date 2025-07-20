package kr.ac.devsign.HomePage.infrastructure.exception;

public class SuggestionModificationException extends RuntimeException {
    public SuggestionModificationException(String message) {
        super(message);
    }

    public SuggestionModificationException() {
        super("건의사항을 수정할 수 없습니다.");
    }
}
