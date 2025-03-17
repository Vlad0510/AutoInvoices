package com.Invoices.AutoInvoices.Entity;

public class AttendanceForm {

    private int studentId;
    private String attendanceDate;

    public AttendanceForm(int studentId, String attendanceDate) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
    }

    public AttendanceForm(){
        super();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
}
