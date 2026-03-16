import java.util.*;

public class BookMyStayApp {

    static class Reservation {
        String guestName;
        String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }
    public static void main(String[] args) {

        // FIFO booking request queue
        Queue<Reservation> requestQueue = new LinkedList<>();


        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);


        Set<String> allocatedRoomIds = new HashSet<>();


        Map<String, Set<String>> roomAllocations = new HashMap<>();


        requestQueue.offer(new Reservation("Alice", "Deluxe"));
        requestQueue.offer(new Reservation("Bob", "Suite"));
        requestQueue.offer(new Reservation("Charlie", "Standard"));
        requestQueue.offer(new Reservation("David", "Standard"));
        requestQueue.offer(new Reservation("Eva", "Suite"));

        System.out.println("Processing booking requests...\n");

        // Process queue in FIFO order
        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll();
            String roomType = request.roomType;

            int available = inventory.getOrDefault(roomType, 0);


            if (available > 0) {


                String roomId;
                do {
                    roomId = roomType.substring(0,1).toUpperCase() + (100 + new Random().nextInt(900));
                } while (allocatedRoomIds.contains(roomId));

                // Record allocated room
                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(roomType, new HashSet<>());
                roomAllocations.get(roomType).add(roomId);


                inventory.put(roomType, available - 1);

                System.out.println("Reservation confirmed for " + request.guestName +
                        " | Room Type: " + roomType +
                        " | Assigned Room ID: " + roomId);

            } else {
                System.out.println("Reservation failed for " + request.guestName +
                        " | No " + roomType + " rooms available.");
            }
        }

        System.out.println("\nAllocated Rooms by Type:");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }

        System.out.println("\nRemaining Inventory:");
        System.out.println(inventory);
    }
}
