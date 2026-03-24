import java.util.*;
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}


class BookingSystem {


    private Map<String, Integer> rooms;

    public BookingSystem() {
        rooms = new HashMap<>();
        rooms.put("single", 5);
        rooms.put("double", 3);
        rooms.put("suite", 2);
    }


    private void validate(String roomType, int quantity) throws InvalidBookingException {


        if (!rooms.containsKey(roomType.toLowerCase())) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }


        if (quantity <= 0) {
            throw new InvalidBookingException("not Quantity must be greater than 0");
        }


        int available = rooms.get(roomType.toLowerCase());
        if (quantity > available) {
            throw new InvalidBookingException(
                    "Not enough rooms available. Only " + available + " left."
            );
        }
    }


    public void bookRoom(String roomType, int quantity) {
        try {
            validate(roomType, quantity);

            int available = rooms.get(roomType.toLowerCase());
            rooms.put(roomType.toLowerCase(), available - quantity);

            System.out.println("✅ Booking successful!");
            System.out.println("Remaining " + roomType + " rooms: " + rooms.get(roomType.toLowerCase()));

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println(e.getMessage());
        }
    }


    public void displayRooms() {
        System.out.println("\n--- Current Room Availability ---");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingSystem system = new BookingSystem();

        while (true) {
            system.displayRooms();

            System.out.println("\nEnter room type (single/double/suite) or 'exit': ");
            String type = sc.next();

            if (type.equalsIgnoreCase("exit")) {
                System.out.println("Exiting system safely...");
                break;
            }

            System.out.println("Enter quantity: ");
            int qty;

            try {
                qty = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Quantity must be a number.");
                sc.next();
                continue;
            }


            system.bookRoom(type, qty);
        }

        sc.close();

    }
}
