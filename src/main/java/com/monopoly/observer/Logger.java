package com.monopoly.observer;

public class Logger implements GameObserver {
    public void update(String event, Object data) {
        System.out.println("[LOG] " + event + ": " + data);
    }
}