import java.util.*;
class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> servicesByReservation;

    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        servicesByReservation
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services
    public List<AddOnService> getServices(String reservationId) {
        return servicesByReservation.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = servicesByReservation.get(reservationId);

        if (services == null) return 0.0;

        double total = 0.0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "RES-101";

        // Guest selects services
        manager.addService(reservationId, new AddOnService("Breakfast", 500));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 1200));
        manager.addService(reservationId, new AddOnService("Extra Bed", 800));

        // Display selected services
        System.out.println("Services for Reservation: " + reservationId);

        List<AddOnService> services = manager.getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No services selected.");
        } else {
            for (AddOnService service : services) {
                System.out.println("- " + service.getServiceName() + " : ₹" + service.getCost());
            }
        }

        // Calculate total additional cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("--------------------------------");
        System.out.println("Total Add-On Cost: ₹" + totalCost);
    }
}
