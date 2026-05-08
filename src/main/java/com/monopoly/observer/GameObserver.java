package com.monopoly.observer;

// Observer Interface
public interface GameObserver {
    void update(String event, Object data);
}