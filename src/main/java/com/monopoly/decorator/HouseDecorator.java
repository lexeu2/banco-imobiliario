package com.monopoly.decorator;

public class HouseDecorator extends PropertyDecorator {
    private int houses;
    
    public HouseDecorator(PropertyComponent property, int houses) {
        super(property);
        this.houses = houses;
    }
    
    public double getRent() {
        return super.getRent() * (1 + houses * 0.5);
    }
    
    public String getDescription() {
        return super.getDescription() + " (" + houses + " houses)";
    }
}