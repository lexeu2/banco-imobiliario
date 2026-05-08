package com.monopoly.factory;

import com.monopoly.model.Property;

public class PropertyFactory {
    
    public static Property createProperty(String type, int position) {
        switch(type) {
            case "BROWN":
                return new PropertyBuilder()
                    .name(getName(position))
                    .position(position)
                    .price(60)
                    .baseRent(4)
                    .colorGroup("BROWN")
                    .build();
            case "RAILROAD":
                return new PropertyBuilder()
                    .name(getName(position))
                    .position(position)
                    .price(200)
                    .baseRent(25)
                    .colorGroup("RAILROAD")
                    .build();
            default:
                return new PropertyBuilder()
                    .name("Property " + position)
                    .position(position)
                    .price(100)
                    .baseRent(10)
                    .colorGroup("GENERIC")
                    .build();
        }
    }
    
    private static String getName(int position) {
        String[] names = {
            "GO", "Mediterranean Ave", "Community Chest", "Baltic Ave", "Income Tax",
            "Reading Railroad", "Oriental Ave", "Chance", "Vermont Ave", "Connecticut Ave"
        };
        return position < names.length ? names[position] : "Space " + position;
    }
    
    // Builder Pattern (extra!)
    static class PropertyBuilder {
        private Property property = new Property();
        
        public PropertyBuilder name(String name) { property.setName(name); return this; }
        public PropertyBuilder position(int pos) { property.setPosition(pos); return this; }
        public PropertyBuilder price(double price) { property.setPrice(price); return this; }
        public PropertyBuilder baseRent(double rent) { property.setBaseRent(rent); return this; }
        public PropertyBuilder colorGroup(String color) { property.setColorGroup(color); return this; }
        public Property build() { return property; }
    }
}