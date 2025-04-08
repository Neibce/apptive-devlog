package apptive.devlog.exception;

public class NicknameAlreadyExistsException extends IllegalArgumentException {
    public NicknameAlreadyExistsException(String message) {
        super(message);
    }
}
