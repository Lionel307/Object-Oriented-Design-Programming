package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class Hotel {
    private List<Room> rooms = new ArrayList<Room>();
    private List<Booking> bookings = new ArrayList<Booking>();

    private String name;
    
    /**
     * Makes a booking in any available room with the given preferences.
     * 
     * @param arrival
     * @param departure
     * @param standard - does the client want a standard room?
     * @param ensuite - does the client want an ensuite room?
     * @param penthouse - does the client want a penthouse room?
     * @return If there were no available rooms for the given preferences, returns false.
     * Returns true if the booking was successful
     */
    public boolean makeBooking(LocalDate arrival, LocalDate departure, boolean standard, boolean ensuite, boolean penthouse) {
        if (standard) {
            for (Booking booking : bookings) {
                if (booking.overlaps(arrival, departure)) {
                    return false;
                }
            }
            Booking booking = new Booking(arrival, departure);
            bookings.add(booking);
            return true;
        } else if (ensuite) {
            for (Booking booking : bookings) {
                if (booking.overlaps(arrival, departure)) {
                    return false;
                }
            }
            Booking booking = new Booking(arrival, departure);
            bookings.add(booking);
            return true;
        } else if (penthouse) {
            for (Booking booking : bookings) {
                if (booking.overlaps(arrival, departure)) {
                    return false;
                }
            }
            Booking booking = new Booking(arrival, departure);
            bookings.add(booking);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return A JSON Object of the form:
     * { "name": name, "rooms": [ each room as a JSON object, in order of creation ]}
     */
    public JSONObject toJSON() {
        JSONObject room = new JSONObject();
        room.put("name", name);
        room.put("rooms", rooms);

        return room;
    }

    public static void main(String[] args) {

    }   
}