import java.util.HashMap;

/* Abstract Room class */
abstract class Room {
    String type;
    double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type + " | Price: $" + price);
    }
}

/* Concrete Room Types */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 300);
    }
}

/* Inventory holding availability (system state) */
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/* Search service – read-only access */
class SearchService {

    public void searchRooms(RoomInventory inventory, Room[] rooms) {
        System.out.println("Available Rooms:\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.type);

            if (available > 0) { // filter unavailable rooms
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        SearchService search = new SearchService();
        search.searchRooms(inventory, rooms);

    }
}
