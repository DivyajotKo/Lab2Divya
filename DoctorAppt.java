package com.example.lab1divyajot;

public class DoctorAppt {

    private int Id;
    private String Name;
    private String Doctor;
    private String Mail;

    public DoctorAppt(int Id, String Name, String Doctor, String Mail ){
        this.Id = Id;
        this.Name = Name;
        this.Doctor = Doctor;
        this.Mail = Mail;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }
}
