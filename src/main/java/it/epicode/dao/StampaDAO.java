package it.epicode.dao;

import it.epicode.entity.Stampa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class StampaDAO {
    private final EntityManager em;

    public StampaDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Stampa stampa) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(stampa);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante la creazione della stampa: " + e.getMessage());
        }
    }

    public Stampa read(String isbn) {
        return em.find(Stampa.class, isbn);
    }

    public void update(Stampa stampa) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(stampa);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'aggiornamento della stampa: " + e.getMessage());
        }
    }

    public void delete(String isbn) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Stampa stampa = em.find(Stampa.class, isbn);
            if (stampa != null) {
                em.remove(stampa);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'eliminazione della stampa: " + e.getMessage());
        }
    }

    public List<Stampa> findAll() {
        return em.createQuery("SELECT s FROM Stampa s", Stampa.class).getResultList();
    }
}

