package Repository;
import domain.Entity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface Repository<ID, E extends Entity<ID>> {
    //returneaza null daca a fost salvat cu succes, sau E daca id ul exista deja in memorie
    E save (E entity);
    E find (ID id);
    E update (E entity);
    E delete (ID id);
    Iterable<E> findAll();

    /**
     * Filters all entities based on a given predicate
     *
     * @param filter the predicate given to filter the entities
     * @return a list of entities that satisfy the predicate
     * @throws IllegalArgumentException if filter is null
     * */
    default List<E> findAll(Predicate<E> filter){
        if (filter == null) {
            throw new IllegalArgumentException("Filter predicate cannot be null!");
        }
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(filter)
                .collect(Collectors.toList());
    }

}
