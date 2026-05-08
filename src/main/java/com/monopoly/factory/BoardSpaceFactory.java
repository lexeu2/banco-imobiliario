package com.monopoly.factory;

import com.monopoly.model.Property;

public class BoardSpaceFactory {
    
    public static Property createPropertySpace(int position, String name, 
                                                double price, double rent, 
                                                String colorGroup) {
        Property property = new Property();
        property.setPosition(position);
        property.setName(name);
        property.setPrice(price);
        property.setBaseRent(rent);
        property.setColorGroup(colorGroup);
        return property;
    }
    
    public static Property createRailroad(int position, String name) {
        return createPropertySpace(position, name, 200, 25, "RAILROAD");
    }
    
    public static Property createUtility(int position, String name) {
        return createPropertySpace(position, name, 150, 0, "UTILITY");
    }
}