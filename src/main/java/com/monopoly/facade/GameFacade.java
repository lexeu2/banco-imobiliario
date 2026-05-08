package com.monopoly.facade;

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
        return currentGame;
    }
    
    public int rollDice() {
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
        
        return total;
    }
    
    private void handleSpaceAction(Player player) {
        Property property = currentGame.getPropertyAt(player.getPosition());
        if (property != null && property.getPrice() > 0 && property.getOwner() == null) {
            eventManager.notify("LANDED_ON_PROPERTY", property.getName());
        }
        if (property != null && property.getOwner() != null && property.getOwner() != player) {
            double rent = property.getRent();
            player.pay(rent);
            property.getOwner().receive(rent);
            eventManager.notify("RENT_PAID", player.getName() + " paid $" + rent + " to " + property.getOwner().getName());
        }
    }
    
    public void buyProperty() {
        if (!hasRolled) return; // Must roll first
        
        Player player = currentGame.getCurrentPlayer();
        Property property = currentGame.getPropertyAt(player.getPosition());
        
        if (property != null && property.getOwner() == null && player.canAfford(property.getPrice())) {
            player.pay(property.getPrice());
            property.setOwner(player);
            player.getProperties().add(property);
            
            dbManager.saveGame(currentGame);
            eventManager.notify("PROPERTY_BOUGHT", property.getName());
        }
    }
    
    public void endTurn() {
        hasRolled = false;
        currentGame.nextTurn();
        eventManager.notify("TURN_ENDED", currentGame.getCurrentPlayer().getName());
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
    
    public void loadGame(Long id) {
        currentGame = dbManager.loadGame(id);
        if (currentGame != null) {
            hasRolled = false;
            eventManager.notify("GAME_LOADED", currentGame.getName());
        }
    }
    
    public void close() {
        dbManager.close();
    }
}