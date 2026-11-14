package domain.Validation;

/*
* Functional interface for entity validation.
* @param <T> the type of entity to validate
* */

import domain.User;
@FunctionalInterface
public interface Validator<T> {
    /*
    * validares a given entity.
    * @param entity the entity to be validated
    * @throws ValidationException if validation rules are violated
    * */
    void validate(T entity) throws ValidationException;

    /*
    * Combines this validator with another validator sequentially
    * @param other the next validator to execute after this one
    * @return a combined {@link Validator} instance
    * */
    default Validator<T> andThen(Validator<T> other) {
        return entity ->{
            this.validate(entity);
            other.validate(entity);
        };
    }
}
