package com.monopoly.strategy;

import com.monopoly.model.Player;
import com.monopoly.model.Property;

public class BalancedStrategy implements PlayerStrategy {
    public boolean shouldBuyProperty(Player player, Property property) {
        return player.getMoney() - property.getPrice() > 200;
    }
    public boolean shouldBuildHouse(Player player, Property property) {
        return player.getMoney() > 500 && property.getHouses() < 4;
    }
    public boolean shouldTrade(Player player, Property property) {
        return property.getPrice() < 300;
    }
}