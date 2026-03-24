import java.util.*;
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}



class Booking {
    String bookingId;
    String roomType;
    List<String> allocatedRooms;
    boolean isCancelled;

    public Booking(String bookingId, String roomType, List<String> allocatedRooms) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.allocatedRooms = allocatedRooms;
        this.isCancelled = false;
    }
}


class BookingSystem {


    private Map<String, Integer> inventory;


    private Map<String, Stack<String>> availableRooms;


    private Map<String, Booking> bookings;


    private Stack<String> rollbackStack;

    public BookingSystem() {
        inventory = new HashMap<>();
        availableRooms = new HashMap<>();
        bookings = new HashMap<>();
        rollbackStack = new Stack<>();

        // Initialize rooms
        initializeRooms("single", 5);
        initializeRooms("double", 3);
        initializeRooms("suite", 2);
    }


    private void initializeRooms(String type, int count) {
        inventory.put(type, count);
        Stack<String> stack = new Stack<>();

        for (int i = count; i >= 1; i--) {
            stack.push(type.toUpperCase() + "-" + i);
        }
        availableRooms.put(type, stack);
    }


    private void validateBooking(String type, int qty) throws BookingException {
        if (!inventory.containsKey(type)) {
            throw new BookingException(" Invalid room type");
        }
        if (qty <= 0) {
            throw new BookingException("Quantity must be greater than 0");
        }
        if (inventory.get(type) < qty) {
            throw new BookingException("Not enough rooms available");
        }
    }


    public String bookRoom(String type, int qty) {
        try {
            validateBooking(type, qty);

            List<String> allocated = new ArrayList<>();


            for (int i = 0; i < qty; i++) {
                String roomId = availableRooms.get(type).pop();
                allocated.add(roomId);
            }


            inventory.put(type, inventory.get(type) - qty);

            String bookingId = UUID.randomUUID().toString();
            bookings.put(bookingId, new Booking(bookingId, type, allocated));

            System.out.println(" Booking successful. ID: " + bookingId);
            System.out.println("Allocated Rooms: " + allocated);

            return bookingId;

        } catch (BookingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void cancelBooking(String bookingId) {
        try {

            if (!bookings.containsKey(bookingId)) {
                throw new BookingException("Booking does not exist");
            }

            Booking booking = bookings.get(bookingId);


            if (booking.isCancelled) {
                throw new BookingException("Booking already cancelled");
            }


            for (String roomId : booking.allocatedRooms) {
                rollbackStack.push(roomId); // track rollback


                availableRooms.get(booking.roomType).push(roomId);
            }


            int restored = booking.allocatedRooms.size();
            inventory.put(
                    booking.roomType,
                    inventory.get(booking.roomType) + restored
            );


            booking.isCancelled = true;

            System.out.println("Cancellation successful for Booking ID: " + bookingId);
            System.out.println("Rollback Stack: " + rollbackStack);

        } catch (BookingException e) {
            System.out.println(e.getMessage());
        }
    }


    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }

    public void displayBookings() {
        System.out.println("\n--- Bookings ---");
        for (Booking b : bookings.values()) {
            System.out.println(
                    "ID: " + b.bookingId +
                            " | Type: " + b.roomType +
                            " | Rooms: " + b.allocatedRooms +
                            " | Cancelled: " + b.isCancelled
            );
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingSystem system = new BookingSystem();

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Show Inventory");
            System.out.println("4. Show Bookings");
            System.out.println("5. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter room type: ");
                    String type = sc.next();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    system.bookRoom(type, qty);
                    break;

                case 2:
                    System.out.print("Enter Booking ID: ");
                    String id = sc.next();
                    system.cancelBooking(id);
                    break;

                case 3:
                    system.displayInventory();
                    break;

                case 4:
                    system.displayBookings();
                    break;

                case 5:
                    System.out.println("Exiting safely...");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}

