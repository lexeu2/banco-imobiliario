package com.monopoly.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.monopoly.model.Game;
import com.monopoly.model.Player;
import com.monopoly.model.Property;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class BoardPanel extends StackPane {
    private Label[][] cells = new Label[11][11];
    private GridPane grid;
    
    private static final int[][] POSITION_MAP = {
        {10,10}, {10,9}, {10,8}, {10,7}, {10,6}, {10,5}, {10,4}, {10,3}, {10,2}, {10,1},
        {10,0}, {9,0}, {8,0}, {7,0}, {6,0}, {5,0}, {4,0}, {3,0}, {2,0}, {1,0},
        {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6}, {0,7}, {0,8}, {0,9},
        {0,10}, {1,10}, {2,10}, {3,10}, {4,10}, {5,10}, {6,10}, {7,10}, {8,10}, {9,10}
    };
    
    private static final Map<String, String> COLORS = new HashMap<>();
    static {
        COLORS.put("BROWN", "#8B4513");
        COLORS.put("LIGHT_BLUE", "#87CEEB");
        COLORS.put("PINK", "#DDA0DD");
        COLORS.put("ORANGE", "#FFA500");
        COLORS.put("RED", "#FF4444");
        COLORS.put("YELLOW", "#FFD700");
        COLORS.put("GREEN", "#228B22");
        COLORS.put("DARK_BLUE", "#00008B");
        COLORS.put("RAILROAD", "#D3D3D3");
        COLORS.put("UTILITY", "#F5F5DC");
        COLORS.put("SPECIAL", "#F0F0F0");
    }
    
    public BoardPanel() {
        grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #C8E6C9;");
        
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Label cell = new Label();
                cell.setPrefSize(65, 45);
                cell.setMinSize(65, 45);
                cell.setMaxSize(65, 45);
                cell.setAlignment(Pos.TOP_CENTER);
                cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-font-size: 7px; -fx-padding: 2px;");
                cell.setTextAlignment(TextAlignment.CENTER);
                cell.setWrapText(true);
                cell.setMaxWidth(60);
                cells[row][col] = cell;
                grid.add(cell, col, row);
                
                if (row > 0 && row < 10 && col > 0 && col < 10) {
                    cell.setVisible(false);
                }
            }
        }
        
        Label title = new Label("BANCO\nIMOBILIARIO");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1B5E20;");
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        grid.add(title, 4, 4, 3, 3);
        
        getChildren().add(grid);
    }
    
    public void updateBoard(Game game) {
        if (game == null) return;
        
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (row == 0 || row == 10 || col == 0 || col == 10) {
                    cells[row][col].setText("");
                    cells[row][col].setTooltip(null);
                }
            }
        }
        
        List<Property> properties = game.getProperties();
        for (int pos = 0; pos < properties.size() && pos < 40; pos++) {
            Property p = properties.get(pos);
            int row = POSITION_MAP[pos][0];
            int col = POSITION_MAP[pos][1];
            
            Label cell = cells[row][col];
            String bg = COLORS.getOrDefault(p.getColorGroup(), "#F0F0F0");
            
            String text = p.getName();
            
            if (p.getOwner() != null) {
                String n = p.getOwner().getName();
                text += "\n[" + n.substring(0, Math.min(3, n.length())) + "]";
            }
            
            if (p.getHouses() > 0 && p.getHouses() < 5) {
                text += " H" + p.getHouses();
            } else if (p.hasHotel()) {
                text += " HTL";
            }
            
            if (p.getPrice() > 0) {
                Tooltip tip = new Tooltip(p.getName() + "\nPreco: R$" + (int)p.getPrice());
                cell.setTooltip(tip);
            }
            
            cell.setText(text);
            cell.setStyle("-fx-background-color: " + bg + 
                "; -fx-border-color: black; -fx-border-width: 1px; " +
                "-fx-font-size: 7px; -fx-padding: 2px;");
        }
        
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player p = game.getPlayers().get(i);
            int pos = p.getPosition() % 40;
            if (pos < 0) pos += 40;
            
            int row = POSITION_MAP[pos][0];
            int col = POSITION_MAP[pos][1];
            
            String current = cells[row][col].getText();
            cells[row][col].setText(current + "\nP" + (i + 1));
            
            if (p.equals(game.getCurrentPlayer())) {
                cells[row][col].setStyle(cells[row][col].getStyle() + 
                    "; -fx-border-color: #4CAF50; -fx-border-width: 2px;");
            }
        }
    }
}