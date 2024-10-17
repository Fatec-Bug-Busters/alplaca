package org.bugbusters.database.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "plates")
public class Plate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "identification", nullable = false, length = 127)
    private String identification;

    @Column(name = "location", length = 127)
    private String location;

    @Column(name = "qrcode")
    private String qrcode;

    @Column(name = "color", length = 63)
    private String color;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "id_country",nullable = false)
    private Country country;

    @Column(name ="id_photo", nullable = false)
    private int idPhoto;

    //Getters and Setters

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }
}
