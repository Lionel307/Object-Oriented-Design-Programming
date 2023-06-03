package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public class Booking {
    
    LocalDate arrival;
    LocalDate departure;

    public Booking(LocalDate arrival, LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    /**
     * @return a JSONObject of the form {"arrival": arrival, "departure": departure}
     */
    public JSONObject toJSON() {
        JSONObject booking = new JSONObject();
        booking.put("arrival", arrival);
        booking.put("departure", departure);

        return booking;
    }

    /**
     * 
     * @param start
     * @param end
     * if date1.compareTo(date2) < 0, date2 occurs before
     * if date1.compareTo(date2) > 0 date occurs after
     * @return true if the dates are unavailable, false if available
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        
        if (start.compareTo(departure) < 0 &&  end.compareTo(arrival) > 0) {
            return true;
        }
        return false;
    }

}