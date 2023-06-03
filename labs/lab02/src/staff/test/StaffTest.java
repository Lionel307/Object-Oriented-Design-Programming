package staff.test;

import java.time.LocalDate;

import staff.Lecturer;
import staff.StaffMember;

public class StaffTest {
    public static void printStaffDetails(StaffMember staff) {
        System.out.println(staff.toString());
    }

    public static void main(String[] args) {
        StaffMember Clark = new StaffMember("Clark", 100, LocalDate.of(2008,12,11), LocalDate.now());
        Lecturer Emma = new Lecturer("Emma", 100000, LocalDate.of(2005,4,7), LocalDate.now(), "UNSW", "A");
        printStaffDetails(Clark);
        printStaffDetails(Emma);
        
        StaffMember Clark2 = new StaffMember("Clark", 100, LocalDate.of(2008,12,11), LocalDate.now());
        Lecturer Emma2 = new Lecturer("Emma", 100000, LocalDate.of(2005,4,7), LocalDate.now(), "UNSW", "A");
        
        

        // reflexive
        if (Clark.equals(Clark)) {
            System.out.println("reflective passed");
        } else {
            System.out.println("reflective failed");
        }
        // symmetric
        if (Emma.equals(Emma2)) {
            System.out.println("symmetric passed");
        } else {
            System.out.println("symmetric failed");
        }

        if (Clark2.equals(Clark)) {
            System.out.println("symmetric passed");
        } else {
            System.out.println("symmetric failed");
        }

        if (Clark.equals(Emma)) {
            System.out.println("test failed");
        } else {
            System.out.println("test passed");
        }
        if (Emma.equals(Clark)) {
            System.out.println("test2 failed");
        } else {
            System.out.println("test2 passed");
        }

        // transitive ???

        // consistent ???

        // Null test
        if (Clark.equals(null) || Emma.equals(null)) {
            System.out.println("Null test failed");
        } else {
            System.out.println("Null test passed");
        }
    }
}
