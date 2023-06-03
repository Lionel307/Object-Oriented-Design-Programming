
- Lots of repeated code in the room classes
    - public Booking book(...)
    - public JSONObject toJSON()
    - public void removeBooking(...)
    - public Booking changeBooking(...)
    
- they all do the same thing so it can be changed so the code is not repeated
    - I achieved this by changeing the room class from an interface to an inheritance class.
    - I implemented the repeated the code into the methods of Room.


- for the method ovelaps(...) in Booking.java I used the compareTo(...) method to check if the booking dates.
    - if date1.compareTo(date2) < 0, date2 occurs before
      if date1.compareTo(date2) > 0 date occurs after


- The following methods I am not sure how to fully implement
    - makeBooking(...)
        - not sure how to use instanceof to test the instance of the booking
    - toJson() (in Room.java)
        - not sure how to represent the type (as String?)
    - removeBooking(...)
        - I iterated through the list of bookings to find desired booking to remove it
    - changeBooking(...)
        - similar to book(...)



