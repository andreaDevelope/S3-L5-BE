package it.epicode.dao;

import it.epicode.entity.Utente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class UtenteDAO {
    private final EntityManager em;

    public UtenteDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Utente utente) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Usa merge per gestire correttamente entità detached
            em.merge(utente);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante la creazione dell'utente: " + e.getMessage());
        }
    }


    public Utente read(Long id) {
        return em.find(Utente.class, id);
    }

    public void update(Utente utente) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(utente);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Utente utente = em.find(Utente.class, id);
            if (utente != null) {
                em.remove(utente);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }

    public List<Utente> findAll() {
        return em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
    }
}

