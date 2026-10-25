package domain.Validation;

public interface Validator<E> {
    void validate(E Entity) throws ValidationException;
}
