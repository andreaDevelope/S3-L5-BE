package it.epicode.dao;

import it.epicode.entity.Prestito;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class PrestitoDAO {
    private EntityManager em;

    public PrestitoDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Prestito prestito) {
        try {
            em.getTransaction().begin();
            em.persist(prestito);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public List<Prestito> findPrestitiScaduti() {
        return em.createQuery(
                        "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :oggi AND p.dataRestituzioneEffettiva IS NULL",
                        Prestito.class)
                .setParameter("oggi", LocalDate.now())
                .getResultList();
    }
}
