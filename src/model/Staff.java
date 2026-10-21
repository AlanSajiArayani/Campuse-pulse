package model;

import util.AppConstants;

public class Staff extends User {

    private String staffId;
    private String department;

    public Staff() {
        super();
    }

    public Staff(String userId, String name, String email, String phone,
                 String password, String staffId, String department) {
        super(userId, name, email, phone, password, AppConstants.ROLE_STAFF);
        this.staffId    = staffId;
        this.department = department;
    }

    public String getStaffId()                { return staffId; }
    public void   setStaffId(String staffId)  { this.staffId = staffId; }

    public String getDepartment()                   { return department; }
    public void   setDepartment(String department)  { this.department = department; }

    @Override
    public String toString() {
        return "Staff{staffId='" + staffId + "', name='" + getName() +
               "', department='" + department + "'}";
    }
}

