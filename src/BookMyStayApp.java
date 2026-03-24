import java.util.*;
import java.io.*;
class BookingException extends Exception {
    public BookingException(String msg) {
        super(msg);
    }
}

// ---------------- BOOKING CLASS ----------------
class Booking implements Serializable {
    String bookingId;
    String roomType;
    int quantity;

    public Booking(String bookingId, String roomType, int quantity) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.quantity = quantity;
    }
}

// ---------------- SYSTEM STATE ----------------
class SystemState implements Serializable {
    Map<String, Integer> inventory;
    Map<String, Booking> bookings;

    public SystemState(Map<String, Integer> inventory, Map<String, Booking> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// ---------------- PERSISTENCE SERVICE ----------------
class PersistenceService {

    private static final String FILE_NAME = "booking_data.ser";

    // SAVE STATE
    public static void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println(" State saved successfully.");
        } catch (IOException e) {
            System.out.println(" Error saving data: " + e.getMessage());
        }
    }

    // LOAD STATE
    public static SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) in.readObject();
            System.out.println(" State restored successfully.");
            return state;
        } catch (FileNotFoundException e) {
            System.out.println(" No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(" Corrupted data. Starting with clean state.");
        }
        return null;
    }
}

// ---------------- BOOKING SYSTEM ----------------
class BookingSystem {

    private Map<String, Integer> inventory;
    private Map<String, Booking> bookings;

    public BookingSystem() {
        // Try restoring state
        SystemState state = PersistenceService.load();

        if (state != null) {
            this.inventory = state.inventory;
            this.bookings = state.bookings;
        } else {
            // Fresh state
            inventory = new HashMap<>();
            bookings = new HashMap<>();

            inventory.put("single", 5);
            inventory.put("double", 3);
            inventory.put("suite", 2);
        }
    }

    // ---------------- BOOK ----------------
    public void book(String type, int qty) {
        try {
            validate(type, qty);

            inventory.put(type, inventory.get(type) - qty);

            String id = UUID.randomUUID().toString();
            bookings.put(id, new Booking(id, type, qty));

            System.out.println(" Booking successful. ID: " + id);

        } catch (BookingException e) {
            System.out.println(" " + e.getMessage());
        }
    }

    // ---------------- VALIDATION ----------------
    private void validate(String type, int qty) throws BookingException {
        if (!inventory.containsKey(type)) {
            throw new BookingException("Invalid room type");
        }
        if (qty <= 0) {
            throw new BookingException("Invalid quantity");
        }
        if (inventory.get(type) < qty) {
            throw new BookingException("Not enough rooms available");
        }
    }

    // ---------------- DISPLAY ----------------
    public void show() {
        System.out.println("\n--- Inventory ---");
        for (String k : inventory.keySet()) {
            System.out.println(k + " : " + inventory.get(k));
        }

        System.out.println("\n--- Bookings ---");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.roomType + " | " + b.quantity);
        }
    }

    // ---------------- SAVE BEFORE EXIT ----------------
    public void shutdown() {
        SystemState state = new SystemState(inventory, bookings);
        PersistenceService.save(state);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingSystem system = new BookingSystem();

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Show Data");
            System.out.println("3. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter room type: ");
                    String type = sc.next();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    system.book(type, qty);
                    break;

                case 2:
                    system.show();
                    break;

                case 3:
                    system.shutdown(); // SAVE STATE
                    System.out.println(" Exiting safely...");
                    return;

                default:
                    System.out.println(" Invalid choice");
            }
        }
    }
}

