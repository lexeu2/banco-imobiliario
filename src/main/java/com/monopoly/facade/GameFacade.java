package com.monopoly.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.monopoly.model.Game;
import com.monopoly.model.Player;
import com.monopoly.model.Property;
import com.monopoly.singleton.DatabaseManager;
import com.monopoly.singleton.GameEventManager;

public class GameFacade {
    private static GameFacade instance;
    private Game currentGame;
    private DatabaseManager dbManager;
    private GameEventManager eventManager;
    private Random dice = new Random();
    private boolean hasRolled = false;
    private boolean gameOver = false;
    
    private GameFacade() {
        this.dbManager = DatabaseManager.getInstance();
        this.eventManager = GameEventManager.getInstance();
    }
    
    public static GameFacade getInstance() {
        if (instance == null) instance = new GameFacade();
        return instance;
    }
    
    public Game createNewGame(String name, String[] playerNames) {
        currentGame = new Game(name);
        currentGame.initializeBoard();
        
        String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};
        for (int i = 0; i < playerNames.length; i++) {
            Player player = new Player(playerNames[i].trim(), colors[i % colors.length]);
            player.setGame(currentGame);
            currentGame.getPlayers().add(player);
        }
        
        currentGame = dbManager.saveGame(currentGame);
        eventManager.notify("GAME_CREATED", currentGame);
        hasRolled = false;
        gameOver = false;
        return currentGame;
    }
    
    public int rollDice() {
        if (gameOver) {
            System.out.println("Game is over!");
            return -1;
        }
        
        if (hasRolled) {
            System.out.println("Already rolled this turn!");
            return -1;
        }
        
        int die1 = dice.nextInt(6) + 1;
        int die2 = dice.nextInt(6) + 1;
        int total = die1 + die2;
        
        hasRolled = true;
        
        Player player = currentGame.getCurrentPlayer();
        player.move(total);
        
        eventManager.notify("DICE_ROLLED", total);
        handleSpaceAction(player);
        
        // Check if current player went bankrupt after paying rent
        checkBankruptPlayers();
        
        return total;
    }
    
    private void handleSpaceAction(Player player) {
    Property property = currentGame.getPropertyAt(player.getPosition());
    
    System.out.println("DEBUG: Player " + player.getName() + " at position " + player.getPosition());
    
    if (property == null) {
        System.out.println("DEBUG: No property here");
        return;
    }
    
    System.out.println("DEBUG: Property = " + property.getName());
    System.out.println("DEBUG: Owner = " + (property.getOwner() != null ? property.getOwner().getName() : "none"));
    System.out.println("DEBUG: Price = " + property.getPrice());
    
    if (property.getPrice() > 0 && property.getOwner() == null) {
        System.out.println("DEBUG: Available to buy!");
        eventManager.notify("LANDED_ON_PROPERTY", property.getName());
    }
    
    if (property.getOwner() != null && property.getOwner() != player) {
        double rent = property.getRent();
        System.out.println("DEBUG: Paying rent: " + rent);
        player.pay(rent);
        property.getOwner().receive(rent);
        System.out.println("DEBUG: " + player.getName() + " now has $" + player.getMoney());
        System.out.println("DEBUG: " + property.getOwner().getName() + " now has $" + property.getOwner().getMoney());
        eventManager.notify("RENT_PAID", player.getName() + " paid R$" + (int)rent + " to " + property.getOwner().getName());
    } else {
        System.out.println("DEBUG: No rent to pay (owner is null or same player)");
    }
}
    
    public void buyProperty() {
        if (gameOver) return;
        if (!hasRolled) return;
        
        Player player = currentGame.getCurrentPlayer();
        Property property = currentGame.getPropertyAt(player.getPosition());
        
        if (property != null && property.getOwner() == null && player.canAfford(property.getPrice())) {
            player.pay(property.getPrice());
            property.setOwner(player);
            player.getProperties().add(property);
            
            dbManager.saveGame(currentGame);
            eventManager.notify("PROPERTY_BOUGHT", property.getName() + " por " + player.getName());
        }
    }
    
    public void buildHouse() {
    if (gameOver || !hasRolled) return;
    
    Player player = currentGame.getCurrentPlayer();
    Property property = currentGame.getPropertyAt(player.getPosition());
    
    if (property == null) {
        eventManager.notify("BUILD_FAILED", "No property here");
        return;
    }
    
    if (property.getOwner() != player) {
        eventManager.notify("BUILD_FAILED", "You don't own this property");
        return;
    }
    
    if (property.getHouses() >= 5) {
        eventManager.notify("BUILD_FAILED", "Already has a hotel!");
        return;
    }
    
    if (!property.getColorGroup().equals("SPECIAL") && 
        !property.getColorGroup().equals("RAILROAD") && 
        !property.getColorGroup().equals("UTILITY")) {
        // Check monopoly
        if (!hasMonopoly(player, property.getColorGroup())) {
            eventManager.notify("BUILD_FAILED", "Need all properties of this color first!");
            return;
        }
    }
    
    double cost = property.getHouseCost();
    if (!player.canAfford(cost)) {
        eventManager.notify("BUILD_FAILED", "Not enough money! Need R$" + (int)cost);
        return;
    }
    
    player.pay(cost);
    property.addHouse();
    
    String what = property.hasHotel() ? "a HOTEL" : "house #" + property.getHouses();
    eventManager.notify("HOUSE_BUILT", player.getName() + " built " + what + " on " + property.getName() + " for R$" + (int)cost);
}

private boolean hasMonopoly(Player player, String colorGroup) {
    long owned = player.getProperties().stream()
        .filter(p -> p.getColorGroup().equals(colorGroup))
        .count();
    
    int total = (colorGroup.equals("BROWN") || colorGroup.equals("DARK_BLUE")) ? 2 : 3;
    return owned >= total;
}
    
    public void endTurn() {
        if (gameOver) return;
        
        hasRolled = false;
        
        // Check if current player is bankrupt
        checkBankruptPlayers();
        
        // Check win condition
        if (checkGameOver()) return;
        
        // Skip bankrupt players
        do {
            currentGame.nextTurn();
        } while (currentGame.getCurrentPlayer().isBankrupt());
        
        eventManager.notify("TURN_ENDED", currentGame.getCurrentPlayer().getName());
    }
    
    private void checkBankruptPlayers() {
        List<Player> bankruptPlayers = new ArrayList<>();
        
        for (Player p : currentGame.getPlayers()) {
            if (p.getMoney() < 0 && !p.isBankrupt()) {
                p.setBankrupt(true);
                
                // Return all properties to bank
                for (Property prop : new ArrayList<>(p.getProperties())) {
                    prop.setOwner(null);
                    prop.setHouses(0);
                    prop.setMortgaged(false);
                }
                p.getProperties().clear();
                
                bankruptPlayers.add(p);
                eventManager.notify("PLAYER_BANKRUPT", p.getName() + " faliu!");
            }
        }
        
        // Remove bankrupt players from game
        for (Player p : bankruptPlayers) {
            currentGame.getPlayers().remove(p);
        }
    }
    
    private boolean checkGameOver() {
        long activePlayers = currentGame.getPlayers().stream()
            .filter(p -> !p.isBankrupt())
            .count();
        
        if (activePlayers <= 1) {
            Player winner = currentGame.getPlayers().stream()
                .filter(p -> !p.isBankrupt())
                .findFirst()
                .orElse(null);
            
            if (winner != null) {
                gameOver = true;
                eventManager.notify("GAME_OVER", winner.getName() + " venceu o jogo! Parabens!");
                return true;
            }
        }
        return false;
    }
    
    public void saveGame() {
        if (currentGame != null) {
            dbManager.saveGame(currentGame);
            eventManager.notify("GAME_SAVED", currentGame.getName());
        }
    }
    
    public Game getCurrentGame() {
        return currentGame;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void loadGame(Long id) {
        currentGame = dbManager.loadGame(id);
        if (currentGame != null) {
            hasRolled = false;
            gameOver = false;
            eventManager.notify("GAME_LOADED", currentGame.getName());
        }
    }
    
    public void movePlayerTo(int position) {
        if (gameOver) return;
    
    Player player = currentGame.getCurrentPlayer();
        position = position % 40;
        if (position < 0) position += 40;
    
    player.setPosition(position);
    hasRolled = true;  // Prevent rolling again
    eventManager.notify("PLAYER_TELEPORTED", player.getName() + " moved to " + position);
    handleSpaceAction(player);
}
    public void close() {
        dbManager.close();
    }
}