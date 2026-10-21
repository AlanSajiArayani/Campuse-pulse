package model;
import util.AppConstants;
/**
 * Student.java
 * Represents a student user of CampusPulse.
 * Extends {@link User} — inherits userId, name, email, phone, password, role.
 *
 * TODO [Ananya Raju] – OOP model ownership; add student-specific UI behaviour
 *
 * SHARED MODEL – Do NOT add fields that already exist in User.
 */

public class Student extends User {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String studentId;
    private String department;
    private int    year;          // Academic year (e.g. 1, 2, 3, 4)

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Student() {
        super();
    }

    /** Full constructor. */
    public Student(String userId, String name, String email, String phone,
                   String password, String studentId, String department, int year) {
        super(userId, name, email, phone, password,AppConstants.ROLE_STUDENT );
        this.studentId  = studentId;
        this.department = department;
        this.year       = year;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getStudentId()                  { return studentId; }
    public void   setStudentId(String studentId)  { this.studentId = studentId; }

    public String getDepartment()                   { return department; }
    public void   setDepartment(String department)  { this.department = department; }

    public int  getYear()           { return year; }
    public void setYear(int year)   { this.year = year; }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Student{studentId='" + studentId + "', name='" + getName() +
               "', department='" + department + "', year=" + year + "}";
    }
}

