package alexdigioia;

import alexdigioia.libreria.Catalogo;

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
}
