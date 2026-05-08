package com.monopoly.singleton;

import com.monopoly.observer.GameObserver;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameEventManager {
    private static volatile GameEventManager instance;
    private List<GameObserver> observers = new CopyOnWriteArrayList<>();
    
    private GameEventManager() {}
    
    public static GameEventManager getInstance() {
        if (instance == null) {
            synchronized (GameEventManager.class) {
                if (instance == null) {
                    instance = new GameEventManager();
                }
            }
        }
        return instance;
    }
    
    public void subscribe(GameObserver observer) {
        observers.add(observer);
    }
    
    public void notify(String event, Object data) {
        for (GameObserver observer : observers) {
            observer.update(event, data);
        }
    }
}