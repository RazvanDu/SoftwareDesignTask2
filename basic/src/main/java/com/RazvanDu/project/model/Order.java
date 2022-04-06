package com.RazvanDu.project.model;

import javax.persistence.*;

@Entity
@Table(name = "orderr", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class Order {

    private Integer id;
    private Integer restaurantID;
    private Integer userID;
    private String foodsOrdered;
    private Integer statusOrder;
    private String restaurantName;
    private String userName;

    public Order(Integer restaurantID, Integer userID, String foodsOrdered, Integer statusOrder) {
        this.restaurantID = restaurantID;
        this.userID = userID;
        this.foodsOrdered = foodsOrdered;
        this.statusOrder = statusOrder;
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
    @Column(name = "foods_ordered")
    public String getFoodsOrdered() {
        return foodsOrdered;
    }

    public void setFoodsOrdered(String foodsOrdered) {
        this.foodsOrdered = foodsOrdered;
    }

    @Basic
    @Column(name = "status_order")
    public Integer getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(Integer statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
