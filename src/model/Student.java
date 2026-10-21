package model;

import util.AppConstants;

public class Student extends User {

    private String studentId;
    private String department;
    private int year;

    public Student() {
        super();
    }

    public Student(String userId, String name, String email, String phone,
                   String password, String studentId, String department, int year) {
        super(userId, name, email, phone, password, AppConstants.ROLE_STUDENT);
        this.studentId  = studentId;
        this.department = department;
        this.year       = year;
    }

    public String getStudentId()                  { return studentId; }
    public void   setStudentId(String studentId)  { this.studentId = studentId; }

    public String getDepartment()                   { return department; }
    public void   setDepartment(String department)  { this.department = department; }

    public int  getYear()           { return year; }
    public void setYear(int year)   { this.year = year; }

    @Override
    public String toString() {
        return "Student{studentId='" + studentId + "', name='" + getName() +
               "', department='" + department + "', year=" + year + "}";
    }
}

