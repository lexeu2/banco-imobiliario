package com.monopoly.strategy;

import com.monopoly.model.Player;
import com.monopoly.model.Property;

public class ConservativeStrategy implements PlayerStrategy {
    public boolean shouldBuyProperty(Player player, Property property) {
        return player.getMoney() - property.getPrice() > 500;
    }
    public boolean shouldBuildHouse(Player player, Property property) {
        return player.getMoney() > 1000 && property.getHouses() < 4;
    }
    public boolean shouldTrade(Player player, Property property) {
        return property.getColorGroup().equals("DARK_BLUE");
    }
}