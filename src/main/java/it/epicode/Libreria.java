package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.*;
import it.epicode.enums.Periodicita;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Libreria {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        UtenteDAO utenteDAO = new UtenteDAO(em);
        StampaDAO stampaDAO = new StampaDAO(em);
        PrestitoDAO prestitoDAO = new PrestitoDAO(em);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Aggiungi un libro");
            System.out.println("2. Aggiungi una rivista");
            System.out.println("3. Rimuovi un elemento (ISBN)");
            System.out.println("4. Cerca per ISBN");
            System.out.println("5. Cerca per anno di pubblicazione");
            System.out.println("6. Cerca per autore");
            System.out.println("7. Cerca per titolo o parte di esso");
            System.out.println("8. Effettua un prestito");
            System.out.println("9. Mostra prestiti scaduti e non restituiti");
            System.out.println("10. Esci");
            System.out.print("Scegli un'opzione: ");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline
            switch (scelta) {
                case 1 -> aggiungiLibro(stampaDAO);
                case 2 -> aggiungiRivista(stampaDAO);
                case 3 -> rimuoviElemento(stampaDAO);
                case 4 -> cercaPerISBN(stampaDAO);
                case 5 -> cercaPerAnnoPubblicazione(stampaDAO);
                case 6 -> cercaPerAutore(stampaDAO);
                case 7 -> cercaPerTitolo(stampaDAO);
                case 8 -> effettuaPrestito(prestitoDAO, utenteDAO, stampaDAO);
                case 9 -> mostraPrestitiScaduti(prestitoDAO);
                case 10 -> {
                    em.close();
                    emf.close();
                    System.exit(0);
                }
                default -> System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    private static void aggiungiLibro(StampaDAO stampaDAO) {
        System.out.println("Inserisci i dati del libro:");
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Anno di pubblicazione: ");
        int annoPubblicazione = scanner.nextInt();
        System.out.print("Numero di pagine: ");
        int numeroPagine = scanner.nextInt();
        scanner.nextLine(); // Consuma newline
        System.out.print("Autore: ");
        String autore = scanner.nextLine();
        System.out.print("Genere: ");
        String genere = scanner.nextLine();

        Libro libro = new Libro();
        libro.setTitolo(titolo);
        libro.setAnnoPublicazione(annoPubblicazione);
        libro.setNumeroPagine(numeroPagine);
        libro.setAutore(autore);
        libro.setGenere(genere);

        stampaDAO.create(libro);
        System.out.println("Libro aggiunto con successo!");
    }

    private static void aggiungiRivista(StampaDAO stampaDAO) {
        System.out.println("Inserisci i dati della rivista:");
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Anno di pubblicazione: ");
        int annoPubblicazione = scanner.nextInt();
        System.out.print("Numero di pagine: ");
        int numeroPagine = scanner.nextInt();
        scanner.nextLine(); // Consuma newline
        System.out.print("Periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
        String periodicitaStr = scanner.nextLine();
        Periodicita periodicita = Periodicita.valueOf(periodicitaStr.toUpperCase());

        Rivista rivista = new Rivista();
        rivista.setTitolo(titolo);
        rivista.setAnnoPublicazione(annoPubblicazione);
        rivista.setNumeroPagine(numeroPagine);
        rivista.setPeriodicita(periodicita);

        stampaDAO.create(rivista);
        System.out.println("Rivista aggiunta con successo!");
    }

    private static void rimuoviElemento(StampaDAO stampaDAO) {
        System.out.print("Inserisci il codice ISBN dell'elemento da rimuovere: ");
        String isbn = scanner.nextLine();
        stampaDAO.delete(isbn);
        System.out.println("Elemento rimosso con successo (se esisteva).");
    }

    private static void cercaPerISBN(StampaDAO stampaDAO) {
        System.out.print("Inserisci il codice ISBN: ");
        String isbn = scanner.nextLine();
        Stampa stampa = stampaDAO.read(isbn);
        if (stampa != null) {
            System.out.println("Trovato: " + stampa);
        } else {
            System.out.println("Nessun elemento trovato con questo ISBN.");
        }
    }

    private static void cercaPerAnnoPubblicazione(StampaDAO stampaDAO) {
        System.out.print("Inserisci l'anno di pubblicazione: ");
        int anno = scanner.nextInt();
        scanner.nextLine(); // Consuma newline
        stampaDAO.findAll().stream()
                .filter(s -> s.getAnnoPublicazione() == anno)
                .forEach(System.out::println);
    }

    private static void cercaPerAutore(StampaDAO stampaDAO) {
        System.out.print("Inserisci l'autore: ");
        String autore = scanner.nextLine();
        stampaDAO.findAll().stream()
                .filter(s -> s instanceof Libro)
                .map(s -> (Libro) s)
                .filter(l -> l.getAutore().equalsIgnoreCase(autore))
                .forEach(System.out::println);
    }

    private static void cercaPerTitolo(StampaDAO stampaDAO) {
        System.out.print("Inserisci il titolo o parte di esso: ");
        String titolo = scanner.nextLine();
        stampaDAO.findAll().stream()
                .filter(s -> s.getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .forEach(System.out::println);
    }

    private static void effettuaPrestito(PrestitoDAO prestitoDAO, UtenteDAO utenteDAO, StampaDAO stampaDAO) {
        System.out.print("Numero tessera utente: ");
        Long numeroTessera = scanner.nextLong();
        scanner.nextLine(); // Consuma newline
        Utente utente = utenteDAO.read(numeroTessera);
        if (utente == null) {
            System.out.println("Utente non trovato.");
            return;
        }

        System.out.print("ISBN dell'elemento da prestare: ");
        String isbn = scanner.nextLine();
        Stampa stampa = stampaDAO.read(isbn);
        if (stampa == null) {
            System.out.println("Elemento non trovato.");
            return;
        }

        Prestito prestito = new Prestito();
        prestito.setUtente(utente);
        prestito.setStampa(stampa);
        prestito.setDataInizioPrestito(LocalDate.now());
        prestito.setDataRestituzionePrevista(LocalDate.now().plusDays(30));

        prestitoDAO.create(prestito);
        System.out.println("Prestito effettuato con successo!");
    }

    private static void mostraPrestitiScaduti(PrestitoDAO prestitoDAO) {
        LocalDate oggi = LocalDate.now();
        prestitoDAO.findAll().stream()
                .filter(p -> p.getDataRestituzionePrevista().isBefore(oggi) && p.getDataRestituzioneEffettiva() == null)
                .forEach(System.out::println);
    }
}
