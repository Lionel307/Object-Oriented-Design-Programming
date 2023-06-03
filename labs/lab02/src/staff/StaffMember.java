package staff;

import java.time.LocalDate;

/**
 * A staff member
 * @author Lionel
 *
 */
public class StaffMember {
    private String name;
    private int salary;
    private LocalDate hireDate;
    private LocalDate endDate;
    

    public StaffMember(String name, int salary, LocalDate hireDate, LocalDate endDate) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.endDate = endDate;
    }


    /**
     * Getter for hire date
     * @return the date of hire
     */
    public LocalDate getHireDate() {
        
        return hireDate;
    }
    /**
     * Setter for hireDate
     * @param hireDate
     */
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Getter for end date
     * @return the date the employee stops working
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for endDate
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for name
     * @return the name of the employee
     */
    public String getName() {
        return name;
    }

     /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for salary
     * @return the salary of the employee
     */
    public int getSalary() {
        return salary;
    }

     /**
     * Setter for name
     * @param salary
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    
    @Override
    public String toString() {
        String msg = "Name = " + name + ", Salary = " + salary + ", Date of hire = " + hireDate + ", Date of end = " + endDate + "";
        return msg;
    }

    
    @Override
    public boolean equals(Object obj) {
   
        if(obj == null) { return false; }
        if(obj == this) { return true; }

        if(this.getClass() != obj.getClass()){
            return false;
        }

        StaffMember other = (StaffMember) obj;
        if(this.name == other.name && this.salary == other.salary && this.hireDate == other.hireDate && this.endDate == other.endDate){
            return true;
        }
        else {
            return false;
        }
         
    }

}
