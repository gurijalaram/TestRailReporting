package main.java.utils;

/**
 * @author kpatel
 */
public class ConsoleLogError extends RuntimeException {

    public ConsoleLogError() {
    }

    public ConsoleLogError(String message) {
        super(message);
    }

    public ConsoleLogError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleLogError(Throwable cause) {
        super(cause);
    }

    public ConsoleLogError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
