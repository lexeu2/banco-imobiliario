package com.monopoly.decorator;

import com.monopoly.model.Property;

public class MonopolyDecorator implements PropertyComponent {
    private Property property;
    
    public MonopolyDecorator(Property property) {
        this.property = property;
    }
    
    public double getRent() {
        return property.getRent() * 2;
    }
    
    public String getDescription() {
        return property.getName() + " [MONOPOLY]";
    }
}