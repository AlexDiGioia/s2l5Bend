package alexdigioia.libreria;

import java.io.*;
import java.util.*;

public class Catalogo {
    private List<Lettura> elementi;
    private final Set<String> isbnSet;

    public Catalogo() {
        this.elementi = new ArrayList<>();
        this.isbnSet = new HashSet<>();
    }
    
    public List<Lettura> getElementi() {
        return elementi;
    }

    public void setElementi(List<Lettura> elementi) {
        this.elementi = elementi;
    }

    public String generaISBN() {
        String nuovoISBN;
        do {
            nuovoISBN = UUID.randomUUID().toString(); //genera un identificatore universale univoco (UUID) che è molto difficile da duplicare
        } while (isbnSet.contains(nuovoISBN));
        isbnSet.add(nuovoISBN);
        return nuovoISBN;
    }

    public void aggiungiElemento(Lettura elemento) {
        elementi.add(elemento);
        isbnSet.add(elemento.getCodiceISBN());
    }

    public void rimuoviElemento(String codiceISBN) {
        elementi.removeIf(e -> e.getCodiceISBN().equals(codiceISBN));
        isbnSet.remove(codiceISBN);
    }

    public Optional<Lettura> ricercaPerISBN(String codiceISBN) {
        return elementi.stream()
                .filter(e -> e.getCodiceISBN().equals(codiceISBN))
                .findFirst();
    }

    public List<Lettura> ricercaPerAnno(int anno) {
        return elementi.stream()
                .filter(e -> e.getAnnoPubblicazione() == anno)
                .toList();
    }

    public List<Libro> ricercaPerAutore(String autore) {
        return elementi.stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .filter(libro -> libro.getAutore().equalsIgnoreCase(autore))
                .toList();
    }

    public void salvaSuDisco(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Lettura elemento : elementi) {
                if (elemento instanceof Libro libro) {
                    writer.printf("LIBRO;%s;%s;%d;%d;%s;%s%n", //PLACEHOLDERS: %s => Stringa, %d => int, %n => newLine
                            libro.getCodiceISBN(),
                            libro.getTitolo(),
                            libro.getAnnoPubblicazione(),
                            libro.getNumeroPagine(),
                            libro.getAutore(),
                            libro.getGenere());
                } else if (elemento instanceof Rivista rivista) {
                    writer.printf("RIVISTA;%s;%s;%d;%d;%s%n",
                            rivista.getCodiceISBN(),
                            rivista.getTitolo(),
                            rivista.getAnnoPubblicazione(),
                            rivista.getNumeroPagine(),
                            rivista.getPeriodicita().name());
                }
            }
        }
    }

    public void caricaDaDisco(String filePath) throws IOException {
        List<Lettura> loadedElements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String tipo = parts[0];
                String codiceISBN = parts[1];
                String titolo = parts[2];
                int annoPubblicazione = Integer.parseInt(parts[3]);
                int numeroPagine = Integer.parseInt(parts[4]);

                if (tipo.equals("LIBRO")) {
                    String autore = parts[5];
                    String genere = parts[6];
                    Libro libro = new Libro(codiceISBN, titolo, annoPubblicazione, numeroPagine, autore, genere);
                    loadedElements.add(libro);
                    isbnSet.add(codiceISBN);
                } else if (tipo.equals("RIVISTA")) {
                    Periodicita periodicita = Periodicita.valueOf(parts[5]);
                    Rivista rivista = new Rivista(codiceISBN, titolo, annoPubblicazione, numeroPagine, periodicita);
                    loadedElements.add(rivista);
                    isbnSet.add(codiceISBN);
                }
            }
        }
        this.elementi = loadedElements;
    }


}
