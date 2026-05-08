package com.monopoly.decorator;

import com.monopoly.model.Property;

class BasicProperty implements PropertyComponent {
    protected Property property;
    
    public BasicProperty(Property property) {
        this.property = property;
    }
    
    public double getRent() { return property.getRent(); }
    public String getDescription() { return property.getName(); }
}