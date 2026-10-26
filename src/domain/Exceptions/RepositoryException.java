package domain.Exceptions;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }

    // Constructorul pentru Exception Chaining
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
