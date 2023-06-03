package dungeonmania;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Player;
import dungeonmania.response.models.ItemResponse;

public class Inventory implements Iterable<Storable>{
    private List<Storable> collections = new ArrayList<>();
    private Player player; // the player who owns this inventory

    public Inventory(Player player) {
        this.player = player;
    }

    public List<Storable> getCollections() {
        return collections;
    }

    public Iterator<Storable> iterator() {
        return collections.iterator();
    }

    /**
     * 
     * @return the IemResponses of the Storable in collections
     */
    public List<ItemResponse> getItemResponses() {
        return collections.stream()
            .map(item->item.getItemResponse())
            .collect(Collectors.toList());
    }

    /**
     * add the entity to the collections
     * @param entity a storable item
     */
    public void add(Storable entity) {
        collections.add(entity);
        this.getPlayer().updateObservers();
    }

    /**
     * remove the entity from the collections
     * @param entity
     */
    public void remove(Storable entity) {
        collections.remove(entity);
        this.getPlayer().updateObservers();
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * @param type the class of the required object type
     * @return the number of the items in the inventory which has the given type
     */
    public long getNumOfTypes(Class<? extends Storable> type) {
        return collections.stream()
            .filter(item->type.isAssignableFrom(item.getClass()))
            .count();
    }

    /**
     * remove a given number of items that is an instance of the given type
     * @param num the number of items to be removed
     * @param typeString the class of the required bject type
     */
    public void removeNumOfTypes(int num, Class<? extends Storable> type) {
        Iterator<Storable> it = collections.iterator();
        int count = 0;
        while (it.hasNext() && count < num) {
            Storable item = it.next();
            if (type.isAssignableFrom(item.getClass())) {
                it.remove();
                count++;
            }
        }
    }

    /*
     * @param itemId
     * @return the storable with the given id if existed in the collection, 
     * otherwise return null
     */
    public Storable getItem(String itemId) {
        return collections.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElse(null);
    }

}
