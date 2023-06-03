package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class StandardRoom extends Room {
    private LocalDate arrival;
    private LocalDate departure;
    private List<Booking> bookings = new ArrayList<Booking>();

    public StandardRoom(List<Booking> bookings, LocalDate arrival, LocalDate departure) {
        super(bookings);
        this.arrival = arrival;
        this.departure = departure;
    }

    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
    
}
