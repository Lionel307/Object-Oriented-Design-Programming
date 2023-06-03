package dungeonmania;

import dungeonmania.response.models.ItemResponse;

public interface Storable {

    /**
     * @return the ItemResponse of the Storable
     */
    default public ItemResponse getItemResponse() {
        return new ItemResponse(getId(), getType());
    }

    /**
     * @return the id of the entity
     */
    abstract public String getId();

    /**
     * @return the type String that represent the entity
     */
    abstract public String getType();

}