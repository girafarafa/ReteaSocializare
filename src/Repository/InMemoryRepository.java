package Repository;
import domain.Entity;
import domain.Validation.Validator;
import domain.Exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E>{

    private final Validator<E> validator;
    //complexitate O(1) pt find, save,update,delete
    private Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        //foloseste hashcode transmis de id pt a localiza instant zona din memorie
        this.entities = new HashMap<>();
    }

    @Override
    public E find(ID id) {
        if(id==null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

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
