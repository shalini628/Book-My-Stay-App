import java.util.*;
import java.time.LocalDateTime;

public class BookMyStayApp {
    // Reservation class representing a booking request
    static class Reservation {
        private String guestName;
        private String roomType;
        private int nights;
        private LocalDateTime requestTime;

        public Reservation(String guestName, String roomType, int nights) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.nights = nights;
            this.requestTime = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return "Reservation{" +
                    "guestName='" + guestName + '\'' +
                    ", roomType='" + roomType + '\'' +
                    ", nights=" + nights +
                    ", requestTime=" + requestTime +
                    '}';
        }
    }

    public static void main(String[] args) {

        // Queue to store booking requests (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Alice", "Deluxe", 2);
        Reservation r2 = new Reservation("Bob", "Suite", 3);
        Reservation r3 = new Reservation("Charlie", "Standard", 1);

        // Add requests to queue
        bookingQueue.offer(r1);
        bookingQueue.offer(r2);
        bookingQueue.offer(r3);

        System.out.println("Booking requests added to queue.\n");

        // Display queued requests (arrival order preserved)
        System.out.println("Current Booking Queue:");
        for (Reservation r : bookingQueue) {
            System.out.println(r);
        }

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        System.out.println(bookingQueue.peek());

        // No allocation or inventory update happens here
        System.out.println("\nRequests are waiting for the allocation system.");
    }
}
