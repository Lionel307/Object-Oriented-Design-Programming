package dungeonmania;

import java.util.List;

import dungeonmania.response.models.ItemResponse;

public class InventoryTestHelper {
    static public long countNumOfType(List<ItemResponse> itemResponses, String type) {
        return itemResponses.stream()
        .filter(itemResponse->itemResponse.getType().equals(type))
        .count();
    }
}
