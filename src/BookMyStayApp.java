import java.util.*;
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private double basePrice;

    public Reservation(String reservationId, String guestName, String roomType, double basePrice) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.basePrice = basePrice;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Price: ₹" + basePrice;
    }
}
class BookingHistory {

    // Maintains insertion order (chronological)
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Retrieve all bookings (read-only copy for safety)
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(confirmedBookings);
    }
}
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Print all bookings
    public void printAllBookings() {
        List<Reservation> bookings = history.getAllReservations();

        System.out.println("=== Booking History ===");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        List<Reservation> bookings = history.getAllReservations();

        int totalBookings = bookings.size();
        double totalRevenue = 0.0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : bookings) {
            totalRevenue += r.getBasePrice();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();


        Reservation r1 = new Reservation("RES-101", "Alice", "Deluxe", 3000);
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite", 5000);
        Reservation r3 = new Reservation("RES-103", "Charlie", "Deluxe", 3000);


        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);


        BookingReportService reportService = new BookingReportService(history);


        reportService.printAllBookings();


        reportService.generateSummaryReport();
    }
}
