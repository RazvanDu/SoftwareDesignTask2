package com.RazvanDu.project.model;

import javax.persistence.*;

@Entity
@Table(name = "food", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class Food {

    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer category;

    //private List<Restaurant> restaurants;

    public Food(String name, String description, Integer price, Integer category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Food() {

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
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "category")
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    /*@ManyToMany(mappedBy = "foods")
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }*/

}
