package com.RazvanDu.project.model;

import javax.persistence.*;

@Entity
@Table(name = "restaurant", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class Restaurant {
    private Integer id;
    private String name;
    private String location;
    private String deliveryZones;

    public Restaurant(String name, String location, String deliveryZones) {
        //this.id = new Random().nextInt();
        this.name = name;
        this.location = location;
        this.deliveryZones = deliveryZones;
    }

    public Restaurant() {

    }

    @Id
    @Basic
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "deliveryZones")
    public String getDeliveryZones() {
        return deliveryZones;
    }

    public void setDeliveryZones(String deliveryZones) {
        this.deliveryZones = deliveryZones;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
