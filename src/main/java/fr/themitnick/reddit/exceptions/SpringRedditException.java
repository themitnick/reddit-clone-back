package fr.themitnick.reddit.exceptions;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String error) {
        super(error);
    }
}
