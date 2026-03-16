import java.util.HashMap;
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability of a room type
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        inventory.displayInventory();

        // Example update
        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Update:");
        inventory.displayInventory();

    }
}
