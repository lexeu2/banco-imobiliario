package com.monopoly.observer;

public class AchievementSystem implements GameObserver {
    public void update(String event, Object data) {
        if (event.equals("PROPERTY_BOUGHT")) {
            System.out.println("[ACHIEVEMENT] Property purchased!");
        }
    }
}