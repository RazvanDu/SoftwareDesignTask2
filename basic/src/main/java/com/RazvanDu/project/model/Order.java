package com.RazvanDu.project.model;

import javax.persistence.*;

@Entity
@Table(name = "order", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class Order {

    private Integer id;
    private Integer restaurantID;
    private Integer userID;
    private String foodsOrdered;
    private Integer status;

    public Order(Integer restaurantID, Integer userID, String foodsOrdered, Integer status) {
        this.restaurantID = restaurantID;
        this.userID = userID;
        this.foodsOrdered = foodsOrdered;
        this.status = status;
    }

    public Order() {

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
    @Column(name = "restaurantID")
    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    @Basic
    @Column(name = "userID")
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Basic
    @Column(name = "foodsOrdered")
    public String getFoodsOrdered() {
        return foodsOrdered;
    }

    public void setFoodsOrdered(String foodsOrdered) {
        this.foodsOrdered = foodsOrdered;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
