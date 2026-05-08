package com.monopoly.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "games")
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int currentPlayerIndex = 0;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> players = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private List<Property> properties = new ArrayList<>();
    
    @Transient
    private String[] colorGroups = {"BROWN", "LIGHT_BLUE", "PINK", "ORANGE", 
                                     "RED", "YELLOW", "GREEN", "DARK_BLUE"};
    
    public Game() {}
    
    public Game(String name) {
        this.name = name;
    }
    
    public void initializeBoard() {
        String[] propertyNames = {
            "PARTIDA", "Rua Augusta", "Sorte", "Jardim Europa", "Imposto de Renda",
            "Cia. do Oeste", "Rua 25 de Marco", "Reves", "Avenida Sao Joao", "Largo do Arouche",
            "PRISAO", "Rua Antonio Carlos", "Cia. do Centro", "Rua Frei Caneca", "Rua da Consolacao",
            "Cia. do Sul", "Rua 7 de Setembro", "Sorte", "Rua Direita", "Rua 15 de Novembro",
            "ESTACIONAMENTO", "Rua Barao de Itapetininga", "Reves", "Rua do Ouvidor", "Rua General Camara",
            "Cia. do Leste", "Avenida Vieira Souto", "Avenida Atlantica", "Cia. do Norte", "Avenida Copacabana",
            "VA PARA PRISAO", "Avenida Ipiranga", "Rua dos Gusmoes", "Sorte", "Rua da Quitanda",
            "Cia. Noroeste", "Reves", "Avenida Paulista", "Imposto de Renda", "Avenida Brasil"
};
        
        double[] prices = {
            0, 60, 0, 60, 0, 200, 100, 0, 100, 120,
            0, 140, 150, 140, 160, 200, 180, 0, 180, 200,
            0, 220, 0, 220, 240, 200, 260, 260, 150, 280,
            0, 300, 300, 0, 320, 200, 0, 350, 0, 400
        };
        
        double[] rents = {
            0, 2, 0, 4, 0, 25, 6, 0, 6, 8,
            0, 10, 0, 10, 12, 25, 14, 0, 14, 16,
            0, 18, 0, 18, 20, 25, 22, 22, 0, 24,
            0, 26, 26, 0, 28, 25, 0, 35, 0, 50
        };
        
        for (int i = 0; i < 40; i++) {
            Property p = new Property();
            p.setName(propertyNames[i]);
            p.setPosition(i);
            p.setPrice(prices[i]);
            p.setBaseRent(rents[i]);
            p.setColorGroup(getColorGroup(i));
            properties.add(p);
        }
    }
    
    private String getColorGroup(int position) {
        int[][] groups = {
            {1, 3}, {6, 8, 9}, {11, 13, 14}, {16, 18, 19},
            {21, 23, 24}, {26, 27, 29}, {31, 32, 34}, {37, 39}
        };
        String[] colors = {"BROWN", "LIGHT_BLUE", "PINK", "ORANGE", 
                          "RED", "YELLOW", "GREEN", "DARK_BLUE"};
        
        for (int g = 0; g < groups.length; g++) {
            for (int pos : groups[g]) {
                if (position == pos) return colors[g];
            }
        }
        return "SPECIAL";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Player> getPlayers() { return players; }
    public List<Property> getProperties() { return properties; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public void setCurrentPlayerIndex(int index) { this.currentPlayerIndex = index; }
    
    public Player getCurrentPlayer() {
        return players.isEmpty() ? null : players.get(currentPlayerIndex);
    }
    
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    public Property getPropertyAt(int position) {
        for (Property p : properties) {
            if (p.getPosition() == position) return p;
        }
        return null;
    }
}