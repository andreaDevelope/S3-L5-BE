package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.*;
import com.github.javafaker.Faker;
import it.epicode.enums.Periodicita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class PopolaDB {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");
    private static final Faker faker = new Faker();

    public static void main(String[] args) {
        // Popola il database con 10 record per ciascuna entità
        popolaLibri();
        popolaRiviste();
        popolaUtenti();
        popolaPrestiti();
    }

    // Popola la tabella Libri con dati casuali
    private static void popolaLibri() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (int i = 0; i < 10; i++) {
                Libro libro = new Libro();
                libro.setTitolo(faker.book().title());
                libro.setAnnoPublicazione(faker.number().numberBetween(1900, 2024));
                libro.setNumeroPagine(faker.number().numberBetween(100, 1000));
                libro.setAutore(faker.book().author());
                libro.setGenere(faker.book().genre());

                em.persist(libro);
            }

            em.getTransaction().commit();
            System.out.println("10 Libri popolati nel database!");
        } finally {
            em.close();
        }
    }

    // Popola la tabella Riviste con dati casuali
    private static void popolaRiviste() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (int i = 0; i < 10; i++) {
                Rivista rivista = new Rivista();
                rivista.setTitolo(faker.book().title());
                rivista.setAnnoPublicazione(faker.number().numberBetween(1900, 2024));
                rivista.setNumeroPagine(faker.number().numberBetween(30, 200));
                rivista.setPeriodicita(Periodicita.values()[faker.number().numberBetween(0, Periodicita.values().length)]);

                em.persist(rivista);
            }

            em.getTransaction().commit();
            System.out.println("10 Riviste popolati nel database!");
        } finally {
            em.close();
        }
    }

    // Popola la tabella Utenti con dati casuali
    private static void popolaUtenti() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (int i = 0; i < 10; i++) {
                Utente utente = new Utente();
                utente.setNumeroTessera(faker.number().randomNumber(6, true));
                utente.setNome(faker.name().firstName());
                utente.setCognome(faker.name().lastName());

                em.merge(utente);
            }

            em.getTransaction().commit();
            System.out.println("10 Utenti popolati nel database!");
        } finally {
            em.close();
        }
    }

    // Popola la tabella Prestiti con dati casuali
    private static void popolaPrestiti() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            List<Utente> utenti = em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
            List<Libro> libri = em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();

            for (int i = 0; i < 10; i++) {
                Utente utente = utenti.get(faker.number().numberBetween(0, utenti.size()));
                Libro libro = libri.get(faker.number().numberBetween(0, libri.size()));

                Prestito prestito = new Prestito();
                prestito.setUtente(utente);
                prestito.setStampa(libro);  // Può essere anche una rivista
                prestito.setDataInizioPrestito(LocalDate.now());
                prestito.setDataRestituzionePrevista(LocalDate.now().plusDays(faker.number().numberBetween(5, 30)));

                em.persist(prestito);
            }

            em.getTransaction().commit();
            System.out.println("10 Prestiti popolati nel database!");
        } finally {
            em.close();
        }
    }
}

