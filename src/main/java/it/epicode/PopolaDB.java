package it.epicode;

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
        popolaLibri();
        popolaRiviste();
        popolaUtenti();
        popolaPrestiti();
    }

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

    private static void popolaPrestiti() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            List<Utente> utenti = em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
            List<Libro> libri = em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();

            if (utenti.size() < 2 || libri.size() < 2) {
                throw new RuntimeException("Non ci sono abbastanza utenti o libri per creare due prestiti.");
            }

            Prestito prestitoInTempo = new Prestito();
            prestitoInTempo.setUtente(utenti.get(0));
            prestitoInTempo.setStampa(libri.get(0));
            prestitoInTempo.setDataInizioPrestito(LocalDate.of(2024, 1, 1));
            prestitoInTempo.setDataRestituzionePrevista(LocalDate.of(2024, 1, 31));
            prestitoInTempo.setDataRestituzioneEffettiva(LocalDate.of(2024, 1, 30));

            em.persist(prestitoInTempo);

            Prestito prestitoInRitardo = new Prestito();
            prestitoInRitardo.setUtente(utenti.get(1));
            prestitoInRitardo.setStampa(libri.get(1));
            prestitoInRitardo.setDataInizioPrestito(LocalDate.of(2023, 2, 1));
            prestitoInRitardo.setDataRestituzionePrevista(LocalDate.of(2023, 3, 2));

            //lo commento in modo da testare il metodo 9 che richiede un restituzione non effettuata entro i temrini
            //quindi deve essere oltre il tempo limite ma anche non restituito
            //prestitoInRitardo.setDataRestituzioneEffettiva(LocalDate.of(2024, 3, 10));

            em.persist(prestitoInRitardo);

            em.getTransaction().commit();
            System.out.println("2 Prestiti popolati nel database!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Errore durante la popolazione dei prestiti: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}

