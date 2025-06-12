package com.example.finalproject;

public class Student {
    private int studentId;
    private int userId;
    private String studentNumber;
    private String address;
    private String birthDate;
    private String major;
    private String enrollmentDate;
    private String graduationDate;
    private int currentSemester;

    // Constructors
    private int classId;

    public Student(int userId, String studentNumber, String address, String birthDate, String major,
                   String enrollmentDate, String graduationDate, int currentSemester, int classId) {
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.address = address;
        this.birthDate = birthDate;
        this.major = major;
        this.enrollmentDate = enrollmentDate;
        this.graduationDate = graduationDate;
        this.currentSemester = currentSemester;
        this.classId = classId;
    }

    public int getClassId() {
        return classId;
    }


    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }
}

