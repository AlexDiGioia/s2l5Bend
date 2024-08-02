package alexdigioia;

import alexdigioia.libreria.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {
    private static final String FILE_PATH = "catalogo.txt";

    public static void main(String[] args) {

        Catalogo catalogo = new Catalogo();
        Scanner scanner = new Scanner(System.in);

        //------MENU------------
        while (true) {
            mostraMenu();
            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    aggiungiElemento(catalogo);
                    break;
                case 2:
                    rimuoviElemento(catalogo);
                    break;
                case 3:
                    ricercaPerISBN(catalogo);
                    break;
                case 4:
                    ricercaPerAnno(catalogo);
                    break;
                case 5:
                    ricercaPerAutore(catalogo);
                    break;
                case 6:
                    salvaSuDisco(catalogo);
                    break;
                case 7:
                    caricaArchivio(catalogo);
                    break;
                case 8:
                    System.out.println("Chiusura Programma...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }

    private static void mostraMenu() {
        System.out.println("1. Aggiungi un elemento");
        System.out.println("2. Rimuovi un elemento");
        System.out.println("3. Ricerca per ISBN");
        System.out.println("4. Ricerca per anno di pubblicazione");
        System.out.println("5. Ricerca per autore");
        System.out.println("6. Salva su disco");
        System.out.println("7. Carica da disco");
        System.out.println("8. Esci");
        System.out.print("Scegli un'opzione: ");
    }

    //-----METODI------
    private static void aggiungiElemento(Catalogo catalogo) {
        System.out.println("1. Aggiungi Libro");
        System.out.println("2. Aggiungi Rivista");
        System.out.print("Scegli un'opzione: ");
        Scanner scanner = new Scanner(System.in);
        int tipo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Inserisci il titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Inserisci l'anno di pubblicazione: ");
        int annoPubblicazione = scanner.nextInt();
        System.out.print("Inserisci il numero di pagine: ");
        int numeroPagine = scanner.nextInt();
        scanner.nextLine();

        String codiceISBN = catalogo.generaISBN();

        if (tipo == 1) {
            System.out.print("Inserisci l'autore: ");
            String autore = scanner.nextLine();
            System.out.print("Inserisci il genere: ");
            String genere = scanner.nextLine();
            Libro libro = new Libro(codiceISBN, titolo, annoPubblicazione, numeroPagine, autore, genere);
            catalogo.aggiungiElemento(libro);
        } else if (tipo == 2) {
            System.out.print("Inserisci la periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
            Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().toUpperCase());
            Rivista rivista = new Rivista(codiceISBN, titolo, annoPubblicazione, numeroPagine, periodicita);
            catalogo.aggiungiElemento(rivista);
        } else {
            System.out.println("Tipo di elemento non valido.");
        }

        System.out.println("Elemento aggiunto con successo.");
    }

    private static void rimuoviElemento(Catalogo catalogo) {
        System.out.print("Inserisci il codice ISBN dell'elemento da rimuovere: ");
        Scanner scanner = new Scanner(System.in);
        String codiceISBN = scanner.nextLine();
        catalogo.rimuoviElemento(codiceISBN);
        System.out.println("Elemento rimosso con successo.");
    }

    //--------METODI DI RICERCA-------
    private static void ricercaPerISBN(Catalogo catalogo) {
        System.out.print("Inserisci il codice ISBN da cercare: ");
        Scanner scanner = new Scanner(System.in);
        String codiceISBN = scanner.nextLine();
        Optional<Lettura> elemento = catalogo.ricercaPerISBN(codiceISBN);
        elemento.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Elemento non trovato.")
        );
    }

    private static void ricercaPerAnno(Catalogo catalogo) {
        System.out.print("Inserisci l'anno di pubblicazione da cercare: ");
        Scanner scanner = new Scanner(System.in);
        int anno = scanner.nextInt();
        scanner.nextLine();  // Consuma il newline
        List<Lettura> risultati = catalogo.ricercaPerAnno(anno);
        if (risultati.isEmpty()) {
            System.out.println("Nessun elemento trovato per l'anno specificato.");
        } else {
            risultati.forEach(System.out::println);
        }
    }

    private static void ricercaPerAutore(Catalogo catalogo) {
        System.out.print("Inserisci il nome dell'autore da cercare: ");
        Scanner scanner = new Scanner(System.in);
        String autore = scanner.nextLine();
        List<Libro> risultati = catalogo.ricercaPerAutore(autore);
        if (risultati.isEmpty()) {
            System.out.println("Nessun libro trovato per l'autore specificato.");
        } else {
            risultati.forEach(System.out::println);
        }
    }

    //---METODI FILES------
    private static void salvaSuDisco(Catalogo catalogo) {
        try {
            catalogo.salvaSuDisco(FILE_PATH);
            System.out.println("Catalogo salvato con successo.");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    private static void caricaArchivio(Catalogo catalogo) {
        try {
            catalogo.caricaDaDisco(FILE_PATH);
            System.out.println("Catalogo caricato con successo.");
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento: " + e.getMessage());
        }
    }
}
