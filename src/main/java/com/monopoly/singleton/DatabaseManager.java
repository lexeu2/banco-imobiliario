package com.monopoly.singleton;

import com.monopoly.model.Game;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private EntityManagerFactory emf;
    private EntityManager em;
    
    private DatabaseManager() {
        try {
            emf = Persistence.createEntityManagerFactory("MonopolyPU");
            em = emf.createEntityManager();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }
    
    public Game saveGame(Game game) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (game.getId() == null) {
                em.persist(game);
            } else {
                game = em.merge(game);
            }
            tx.commit();
            return game;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Save failed", e);
        }
    }
    
    public Game loadGame(Long id) {
        return em.find(Game.class, id);
    }
    
    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }
}