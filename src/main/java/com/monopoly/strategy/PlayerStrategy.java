package com.monopoly.strategy;

import com.monopoly.model.Player;
import com.monopoly.model.Property;

public interface PlayerStrategy {
    boolean shouldBuyProperty(Player player, Property property);
    boolean shouldBuildHouse(Player player, Property property);
    boolean shouldTrade(Player player, Property property);
}