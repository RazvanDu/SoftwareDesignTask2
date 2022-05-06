package com.RazvanDu.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurant", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
public class Restaurant {
    private Integer id;
    private String name;
    private String location;
    private String delivery;
    private Integer adminID;

    private List<Food> foods;

    private List<Food> breakfast;
    private List<Food> lunch;

    public Restaurant(String name, String location, String delivery) {
        this.name = name;
        this.location = location;
        this.delivery = delivery;
    }

    public Restaurant() {

    }

    @Id
    @Basic
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "delivery")
    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "adminID")
    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    /*@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "foods_restaurant",
            joinColumns = { @JoinColumn(name = "RestaurantID", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "FoodID", referencedColumnName = "ID") }
    )*/
    @OneToMany(mappedBy = "restaurantID")
    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

}
