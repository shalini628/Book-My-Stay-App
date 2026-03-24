import java.util.*;
class BookingException extends Exception {
    public BookingException(String msg) {
        super(msg);
    }
}


class ConcurrentBookingSystem {

    private Map<String, Integer> inventory;

    public ConcurrentBookingSystem() {
        inventory = new HashMap<>();
        inventory.put("single", 5);
        inventory.put("double", 3);
        inventory.put("suite", 2);
    }


    public synchronized void book(String roomType, int qty, String user) {
        try {
            validate(roomType, qty);

            int available = inventory.get(roomType);
            inventory.put(roomType, available - qty);

            System.out.println(" " + user + " booked " + qty + " " + roomType + " room(s)");

        } catch (BookingException e) {
            System.out.println(" " + user + ": " + e.getMessage());
        }
    }


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


    public synchronized void showInventory() {
        System.out.println("\n--- Final Inventory ---");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }
}


class BookingTask extends Thread {

    private ConcurrentBookingSystem system;
    private String roomType;
    private int qty;
    private String user;

    public BookingTask(ConcurrentBookingSystem system, String roomType, int qty, String user) {
        this.system = system;
        this.roomType = roomType;
        this.qty = qty;
        this.user = user;
    }

    public void run() {
        system.book(roomType, qty, user);
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {
        ConcurrentBookingSystem system = new ConcurrentBookingSystem();

        Thread t1 = new BookingTask(system, "single", 2, "User-A");
        Thread t2 = new BookingTask(system, "single", 3, "User-B");
        Thread t3 = new BookingTask(system, "single", 2, "User-C"); // should fail

        Thread t4 = new BookingTask(system, "double", 2, "User-D");
        Thread t5 = new BookingTask(system, "double", 2, "User-E"); // may fail


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {

            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }


        system.showInventory();
    }
}

