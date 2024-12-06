package it.epicode.dao;

import it.epicode.entity.Prestito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PrestitoDAO {
    private final EntityManager em;

    public PrestitoDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Prestito prestito) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(prestito);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante la creazione del prestito: " + e.getMessage());
        }
    }

    public Prestito read(Long id) {
        return em.find(Prestito.class, id);
    }

    public void update(Prestito prestito) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(prestito);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'aggiornamento del prestito: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Prestito prestito = em.find(Prestito.class, id);
            if (prestito != null) {
                em.remove(prestito);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Errore durante l'eliminazione del prestito: " + e.getMessage());
        }
    }

    public List<Prestito> findAll() {
        return em.createQuery("SELECT p FROM Prestito p", Prestito.class).getResultList();
    }
}
