package staff;

import java.time.LocalDate;

public class Lecturer extends StaffMember{
    private String school;
    private String academicStat;

    public Lecturer(String name, int salary, LocalDate hireDate, LocalDate endDate, String school, String academicStat) {
        super(name, salary, hireDate, endDate);
        this.academicStat = academicStat;
        this.school = school;
    } 

    

    /**
     * Getter for academic status
     * @return the academic status of the lecturer
     */
    public String getAcademicStat() {
        return academicStat;
    }

    /**
     * Setter for academic status
     * @param academicStat
     */
    public void setAcademicStat(String academicStat) {
        this.academicStat = academicStat;
    }

    /**
     * Getter for the school
     * @return the school of which the lecturer teaches at
     */
    public String getSchool() {
        return school;
    }
    
    /**
     * Setter for school
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return super.toString() + ", school = " + this.school + ", Academic status = " + this.academicStat;
    }

    @Override
    public boolean equals(Object obj) {
   
        if(obj == null) { return false; }
        if(obj == this) { return true; }

        if(this.getClass() != obj.getClass()){
            return false;
        }

        Lecturer other = (Lecturer) obj;
        if(school == other.school && academicStat == other.academicStat){
            return true;
        }
        else {
            return false;
        }
         
    }

    
}
