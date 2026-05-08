package com.monopoly.decorator;

public abstract class PropertyDecorator implements PropertyComponent {
    protected PropertyComponent decoratedProperty;
    
    public PropertyDecorator(PropertyComponent property) {
        this.decoratedProperty = property;
    }
    
    public double getRent() { 
        return decoratedProperty.getRent(); 
    }
    
    public String getDescription() { 
        return decoratedProperty.getDescription(); 
    }
}