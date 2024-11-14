package org.bugbusters.database.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "plates")
public class Plate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "identification", nullable = false,unique = true, length = 127)
    private String identification;

    @Column(name = "location", length = 127)
    private String location;


    @Column(name = "color", length = 63)
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehicle", nullable = false,unique = true)
    private Vehicle vehicle;

    //Getters and Setters


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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Plate{" +
            "id=" + id +
            ", identification='" + identification + '\'' +
            ", location='" + location + '\'' +
            ", color='" + color + '\'' +
            ", vehicle=" + vehicle +
            '}';
    }
}
