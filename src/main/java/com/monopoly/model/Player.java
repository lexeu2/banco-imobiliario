package com.monopoly.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "players")
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double money = 1500.0;
    private int position = 0;
    private boolean bankrupt = false;
    private String color;
    
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Property> properties = new ArrayList<>();
    
    @Transient
    private int doublesRolled = 0;
    
    public Player() {}
    
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }
    
    public void move(int steps) {
        position = (position + steps) % 40;
    }
    
    public void pay(double amount) {
        money -= amount;
        if (money < 0) bankrupt = true;
    }
    
    public void receive(double amount) {
        money += amount;
    }
    
    public boolean canAfford(double amount) {
        return money >= amount;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean isBankrupt() { return bankrupt; }
    public void setBankrupt(boolean bankrupt) { this.bankrupt = bankrupt; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public List<Property> getProperties() { return properties; }
    public int getDoublesRolled() { return doublesRolled; }
    public void setDoublesRolled(int d) { this.doublesRolled = d; }
}