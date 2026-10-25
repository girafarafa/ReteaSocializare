package domain;

public abstract class Entity<ID> {

    private ID id;

    /**
     * Retrieves the unique identifer of the entity
     * @return the id of the entity
     * */
    public ID getID() {
        return id;
    }

    /**
     * Sets the unique identifier of the entity
     * @param id_ the new id to be set
     * */
    public void setID(ID id_) {
        this.id = id_;
    }

}
