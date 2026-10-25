package Repository;
import domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    //returneaza null daca a fost salvat cu succes, sau E daca id ul exista deja in memorie
    E save (E entity);
    E find (ID id);
    E update (E entity);
    E delete (ID id);
    Iterable<E> findAll();

}
