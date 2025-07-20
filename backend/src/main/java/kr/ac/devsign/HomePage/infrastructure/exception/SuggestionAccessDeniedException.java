package kr.ac.devsign.HomePage.infrastructure.exception;

public class SuggestionAccessDeniedException extends RuntimeException {
    public SuggestionAccessDeniedException(String message) {
        super(message);
    }

    public SuggestionAccessDeniedException() {
        super("건의사항에 대한 권한이 없습니다.");
    }
}
