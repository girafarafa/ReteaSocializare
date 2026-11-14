package Repository;
import domain.Entity;
import domain.Validation.Validator;
import domain.Exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <ID> the type of the entity identifier
 * @param <E>  the type of the entity, extending {@link Entity}
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E>{

    private final Validator<E> validator;
    //complexitate O(1) pt find, save,update,delete
    private Map<ID, E> entities;

    /**
     * @param validator the validator used to validate entities stored in this repository
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        //foloseste hashcode transmis de id pt a localiza instant zona din memorie
        this.entities = new HashMap<>();
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the entity with the given ID, or {@code null} if no such entity exists
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    public E find(ID id) {
        if(id==null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return entities.get(id);
    }

    /**
     * Retrieves all entities stored in the repository.
     *
     * @return an {@link Iterable} containing all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Saves a new entity in the repository after validating it.
     *
     * @param entity the entity to save
     * @return {@code null} if the entity was saved successfully, or the entity itself if an entity with the same ID already exists
     * @throws IllegalArgumentException if the entity is null
     * @throws domain.Validation.ValidationException if the entity fails domain validation rules
     */
    @Override
    public E save(E entity) {
        if(entity==null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);
        if(entities.containsKey(entity.getID())) {
            return entity;
        }
        entities.put(entity.getID(), entity);
        return null;
    }

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @return the removed entity
     * @throws IllegalArgumentException if the provided ID is null
     * @throws RepositoryException  if no entity with the given ID exists in the repository
     */
    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }

        if (!entities.containsKey(id)) {
            throw new RepositoryException("Entity with ID " + id + " does not exist!");
        }

        return entities.remove(id);
    }

    /**
     * Updates an existing entity in the repository after validating it.
     *
     * @param entity the entity to update
     * @return {@code null} if the entity was updated successfully, or the entity itself if it does not exist in the repository
     * @throws IllegalArgumentException if the entity is null
     * @throws domain.Validation.ValidationException if the entity fails domain validation rules
     */
    @Override
    public E update(E entity) {
        if(entity==null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        if(entities.containsKey(entity.getID())) {
            entities .put(entity.getID(), entity);
            return null;
        }
        return entity;
    }


}
