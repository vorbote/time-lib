package cn.vorbote.time.exceptions;

public class TimeOutRangeException extends RuntimeException {
    public TimeOutRangeException() {
    }

    public TimeOutRangeException(String message) {
        super(message);
    }
}
