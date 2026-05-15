package com.monopoly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class Property {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private double baseRent;
    private String colorGroup;
    private int position;
    private int houses = 0;
    private boolean mortgaged = false;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Player owner;
    
    public double getRent() {
        if (mortgaged || owner == null) return 0;
        if (houses == 0) return baseRent;
        return baseRent * (1 + houses);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getBaseRent() { return baseRent; }
    public void setBaseRent(double rent) { this.baseRent = rent; }
    public String getColorGroup() { return colorGroup; }
    public void setColorGroup(String color) { this.colorGroup = color; }
    public int getPosition() { return position; }
    public void setPosition(int pos) { this.position = pos; }
    public int getHouses() { return houses; }
    public void setHouses(int houses) { this.houses = houses; }
    public boolean isMortgaged() { return mortgaged; }
    public void setMortgaged(boolean m) { this.mortgaged = m; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    
    public void addHouse() { if (houses < 5) houses++; }
    public boolean hasHotel() { return houses == 5; }
    
    public double getHouseCost() {
        return price * 0.5;
    }
}
