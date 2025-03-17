package com.Invoices.AutoInvoices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Valid
    @NotEmpty(message = "Numele nu poate fi gol")
    private String nume;
    @NotEmpty(message = "Numele parintelui nu poate fi gol")
    private String nume_parinte;
    @NotEmpty(message = "Telefonul parintelui nu poate fi gol")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Numarul de telefon nu este valid")
    private String telefon_parinte;
    @NotNull(message = "Email-ul parintelui nu poate fi gol")
    @NotBlank(message = "Email-ul parintelui nu poate fi gol")
    @Email(message = "Emailul nu este valid")
    private String email_parinte;
    @NotEmpty(message = "Data nasterii este obligatorie")
    private String data_nasterii;

    public Student(int id, String nume, String nume_parinte, String telefon_parinte, String email_parinte, String data_nasterii) {
        this.id = id;
        this.nume = nume;
        this.nume_parinte = nume_parinte;
        this.telefon_parinte = telefon_parinte;
        this.email_parinte = email_parinte;
        this.data_nasterii = data_nasterii;
    }

    public Student(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNume_parinte() {
        return nume_parinte;
    }

    public void setNume_parinte(String nume_parinte) {
        this.nume_parinte = nume_parinte;
    }

    public String getTelefon_parinte() {
        return telefon_parinte;
    }

    public void setTelefon_parinte(String telefon_parinte) {
        this.telefon_parinte = telefon_parinte;
    }

    public String getEmail_parinte() {
        return email_parinte;
    }

    public void setEmail_parinte(String email_parinte) {
        this.email_parinte = email_parinte;
    }

    public String getData_nasterii() {
        return data_nasterii;
    }

    public void setData_nasterii(String data_nasterii) {
        this.data_nasterii = data_nasterii;
    }
}
