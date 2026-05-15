package com.monopoly.ui;

import com.monopoly.facade.GameFacade;
import com.monopoly.model.Game;
import com.monopoly.model.Player;
import com.monopoly.model.Property;
import com.monopoly.observer.GameObserver;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameUI implements GameObserver {
    private Stage stage;
    private BorderPane root;
    private Label statusLabel;
    private Label diceLabel;
    private VBox playerList;
    private TextArea gameLog;
    private GameFacade facade;
    private BoardPanel boardPanel;
    
    public GameUI(Stage stage) {
        this.stage = stage;
        this.facade = GameFacade.getInstance();
        initializeUI();
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.setPadding(new Insets(5));
        
        // Top - Menu
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem saveGame = new MenuItem("Save Game");
        MenuItem exit = new MenuItem("Exit");
        
        newGame.setOnAction(e -> showNewGameDialog());
        saveGame.setOnAction(e -> facade.saveGame());
        exit.setOnAction(e -> { Platform.exit(); });
        
        gameMenu.getItems().addAll(newGame, saveGame, new SeparatorMenuItem(), exit);
        menuBar.getMenus().add(gameMenu);
        root.setTop(menuBar);
        
        // Center - Board
        boardPanel = new BoardPanel();
        root.setCenter(boardPanel);
        
        // Right - Players
        playerList = new VBox(5);
        playerList.setPadding(new Insets(5));
        playerList.setPrefWidth(180);
        playerList.setStyle("-fx-background-color: #F5F5F5;");
        root.setRight(playerList);
        
        // Bottom - Controls
        HBox controls = new HBox(8);
        controls.setPadding(new Insets(5));
        
        Button rollBtn = new Button("Roll Dice (2D6)");
        rollBtn.setOnAction(e -> {
            rollDice();
            updateDisplay();
        });
        rollBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        
        Button buyBtn = new Button("Buy Property");
        buyBtn.setOnAction(e -> {
            facade.buyProperty();
            updateDisplay();
        });
        buyBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        
        Button buildBtn = new Button("Build House");
        buildBtn.setOnAction(e -> {
            facade.buildHouse();
            updateDisplay();
        });
        buildBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        
        Button endBtn = new Button("End Turn");
        endBtn.setOnAction(e -> {
            facade.endTurn();
            updateDisplay();
        });
        endBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        
        // Debug jump
        TextField jumpField = new TextField();
        jumpField.setPromptText("Pos (0-39)");
        jumpField.setPrefWidth(70);
        
        Button jumpBtn = new Button("Go");
        jumpBtn.setOnAction(e -> {
            try {
                int pos = Integer.parseInt(jumpField.getText());
                facade.movePlayerTo(pos);
                updateDisplay();
            } catch (NumberFormatException ex) {
                // ignore
            }
        });
        
        statusLabel = new Label("Welcome!");
        diceLabel = new Label("Dice: -");
        
        controls.getChildren().addAll(rollBtn, buyBtn, buildBtn, endBtn,
                                       new Separator(),
                                       jumpField, jumpBtn,
                                       new Separator(),
                                       statusLabel, diceLabel);
        
        // Game Log
        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setPrefRowCount(3);
        
        VBox bottomSection = new VBox(2, controls, gameLog);
        root.setBottom(bottomSection);
    }
    
    private void showNewGameDialog() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("New Game");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField("My Game");
        TextField playersField = new TextField("Player1,Player2,Player3,Player4");
        
        grid.add(new Label("Game Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Players (comma-separated):"), 0, 1);
        grid.add(playersField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return playersField.getText().split(",");
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(players -> {
            facade.createNewGame(nameField.getText(), players);
            updateDisplay();
        });
    }
    
    private void rollDice() {
        int total = facade.rollDice();
        if (total == -1) {
            diceLabel.setText("Already rolled! End turn first.");
            return;
        }
        diceLabel.setText("Dice: " + total);
    }
    
    private void updateDisplay() {
        Game game = facade.getCurrentGame();
        if (game == null) return;
        
        // Check game over
        if (facade.isGameOver()) {
            statusLabel.setText("GAME OVER!");
            return;
        }
        
        // Update board
        boardPanel.updateBoard(game);
        
        // Update player list
        playerList.getChildren().clear();
        for (Player p : game.getPlayers()) {
            VBox playerCard = new VBox(5);
            String bgColor = "#FFFFFF";
            if (p.equals(game.getCurrentPlayer())) {
                bgColor = "#E8F5E9";
            }
            
            playerCard.setStyle("-fx-background-color: " + bgColor + "; -fx-padding: 10px; " +
                               "-fx-border-color: #ddd; -fx-border-radius: 5px;");
            
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            
            Label moneyLabel = new Label("R$" + String.format("%.0f", p.getMoney()));
            
            Label posLabel = new Label("Position: " + p.getPosition());
            
            Property landed = game.getPropertyAt(p.getPosition());
            if (landed != null && landed.getPrice() > 0) {
                Label onLabel = new Label("On: " + landed.getName());
                onLabel.setStyle("-fx-font-size: 10px;");
                playerCard.getChildren().addAll(nameLabel, moneyLabel, posLabel, onLabel);
                
                if (landed.getOwner() != null && landed.getOwner() != p) {
                    Label rentLabel = new Label("Rent: R$" + (int)landed.getRent());
                    rentLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: red;");
                    playerCard.getChildren().add(rentLabel);
                }
                
                if (landed.getOwner() == p) {
                    Label houseLabel = new Label("Houses: " + landed.getHouses() + " | Cost: R$" + (int)landed.getHouseCost());
                    houseLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: blue;");
                    playerCard.getChildren().add(houseLabel);
                }
            } else {
                playerCard.getChildren().addAll(nameLabel, moneyLabel, posLabel);
            }
            
            Label propsLabel = new Label("Properties: " + p.getProperties().size());
            playerCard.getChildren().add(propsLabel);
            
            playerList.getChildren().add(playerCard);
        }
        
        Player current = game.getCurrentPlayer();
        if (current != null) {
            statusLabel.setText("Current: " + current.getName() + 
                               " | Position: " + current.getPosition());
        }
    }
    
    @Override
    public void update(String event, Object data) {
        Platform.runLater(() -> {
            gameLog.appendText(event + ": " + data + "\n");
            updateDisplay();
        });
    }
    
    public BorderPane getRoot() { return root; }
}