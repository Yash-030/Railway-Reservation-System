import java.util.*;

class Passenger {
    String name;
    int age;
    String gender;
    String berthPreference;
    String berthAllotted;

    Passenger(String name, int age, String gender, String berthPreference) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.berthPreference = berthPreference;
        this.berthAllotted = "";
    }
}

class RailwayReservation {
    private final int TOTAL_BERTHS = 63;
    private final int RAC_BERTHS = 18;
    private final int WAITING_LIST = 10;
    
    private List<Passenger> confirmedList = new ArrayList<>();
    private List<Passenger> racList = new ArrayList<>();
    private List<Passenger> waitingList = new ArrayList<>();
    
    void bookTicket(Passenger p) {
        if (confirmedList.size() < TOTAL_BERTHS) {
            allocateBerth(p);
            confirmedList.add(p);
            System.out.println("Ticket Confirmed for " + p.name);
        } else if (racList.size() < RAC_BERTHS) {
            p.berthAllotted = "RAC";
            racList.add(p);
            System.out.println("RAC Ticket Allocated for " + p.name);
        } else if (waitingList.size() < WAITING_LIST) {
            p.berthAllotted = "Waiting List";
            waitingList.add(p);
            System.out.println("Added to Waiting List: " + p.name);
        } else {
            System.out.println("No tickets available for " + p.name);
        }
    }
    
    void cancelTicket(String name) {
        Optional<Passenger> found = confirmedList.stream().filter(p -> p.name.equals(name)).findFirst();
        if (found.isPresent()) {
            confirmedList.remove(found.get());
            if (!racList.isEmpty()) {
                Passenger racPassenger = racList.remove(0);
                allocateBerth(racPassenger);
                confirmedList.add(racPassenger);
                if (!waitingList.isEmpty()) {
                    racList.add(waitingList.remove(0));
                }
            }
            System.out.println("Ticket Cancelled for " + name);
        } else {
            System.out.println("No confirmed ticket found for " + name);
        }
    }
    
    void printBookedTickets() {
        System.out.println("--- Confirmed Tickets ---");
        confirmedList.forEach(p -> System.out.println(p.name + " - " + p.berthAllotted));
        System.out.println("Total Confirmed: " + confirmedList.size());
    }
    
    void printAvailableTickets() {
        int available = TOTAL_BERTHS - confirmedList.size();
        System.out.println("Available Confirmed Tickets: " + available);
        System.out.println("Available RAC Tickets: " + (RAC_BERTHS - racList.size()));
        System.out.println("Available Waiting List Slots: " + (WAITING_LIST - waitingList.size()));
    }
    
    private void allocateBerth(Passenger p) {
        if (p.age > 60 || (p.gender.equalsIgnoreCase("F") && p.age < 5)) {
            p.berthAllotted = "Lower Berth";
        } else {
            p.berthAllotted = "Any Available";
        }
    }
}

public class RailwayBookingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RailwayReservation system = new RailwayReservation();

        while (true) {
            System.out.println("1. Book Ticket\n2. Cancel Ticket\n3. Print Booked Tickets\n4. Print Available Tickets\n5. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Gender (M/F): ");
                    String gender = sc.nextLine();
                    System.out.print("Enter Berth Preference: ");
                    String berthPreference = sc.nextLine();
                    
                    Passenger p = new Passenger(name, age, gender, berthPreference);
                    system.bookTicket(p);
                    break;
                case 2:
                    System.out.print("Enter Name to Cancel: ");
                    String cancelName = sc.nextLine();
                    system.cancelTicket(cancelName);
                    break;
                case 3:
                    system.printBookedTickets();
                    break;
                case 4:
                    system.printAvailableTickets();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice! Try Again.");
            }
        }
    }
}
