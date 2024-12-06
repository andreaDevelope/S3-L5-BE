package it.epicode.dao;

import it.epicode.entity.Stampa;

import jakarta.persistence.EntityManager;
import java.util.List;

public class StampaDAO {
    private EntityManager em;

    public StampaDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Stampa stampa) {
        try {
            em.getTransaction().begin();
            em.persist(stampa);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Stampa read(String isbn) {
        return em.find(Stampa.class, isbn);
    }

    public void delete(String isbn) {
        try {
            em.getTransaction().begin();
            Stampa stampa = read(isbn);
            if (stampa != null) {
                em.remove(stampa);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public List<Stampa> findByAnnoPubblicazione(int anno) {
        return em.createQuery("SELECT s FROM Stampa s WHERE s.annoPublicazione = :anno", Stampa.class)
                .setParameter("anno", anno)
                .getResultList();
    }

    public List<Stampa> findByTitolo(String titolo) {
        return em.createQuery("SELECT s FROM Stampa s WHERE LOWER(s.titolo) LIKE LOWER(:titolo)", Stampa.class)
                .setParameter("titolo", "%" + titolo + "%")
                .getResultList();
    }

    public List<Stampa> findByAutore(String autore) {
        return em.createQuery("SELECT l FROM Libro l WHERE l.autore = :autore", Stampa.class)
                .setParameter("autore", autore)
                .getResultList();
    }
}
