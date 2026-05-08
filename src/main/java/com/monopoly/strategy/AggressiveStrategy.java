package com.monopoly.strategy;

import com.monopoly.model.Player;
import com.monopoly.model.Property;

public class AggressiveStrategy implements PlayerStrategy {
    public boolean shouldBuyProperty(Player player, Property property) {
        return player.getMoney() - property.getPrice() > 50;
    }
    public boolean shouldBuildHouse(Player player, Property property) {
        return player.getMoney() > 200 && property.getHouses() < 4;
    }
    public boolean shouldTrade(Player player, Property property) {
        return true;
    }
}