package com.monopoly.decorator;

public class HotelDecorator extends PropertyDecorator {
    public HotelDecorator(PropertyComponent property) {
        super(property);
    }
    
    public double getRent() {
        return super.getRent() * 3;
    }
    
    public String getDescription() {
        return super.getDescription() + " (Hotel)";
    }
}