import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Sergiu
 */

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        while (continua) {
            afisareMeniu();
            System.out.print("\t\t\tAlegeți o opțiune: ");
            try{
                int optiune = scanner.nextInt();
                switch (optiune) {
                    case 1:
                        clearConsole();
                        // Crează și rulează instanța DES
                        System.out.println("**** Algoritmul DES a fost selectat.****\n");
                        DES des = new DES();
                        des.Encryption();
                        break;
                    case 2:
                        clearConsole();
                        // Crează și rulează instanța DSA
                        System.out.println("**** Algoritmul DSA a fost selectat.****\n");
                        DSA dsa = new DSA();
                        dsa.main(null);
                        break;
                    case 3:
                        clearConsole();
                        // Crează și rulează instanța RSA
                        System.out.println("**** Algoritmul RSA a fost selectat.****\n");
                        RSA rsa = new RSA();
                        rsa.main(null);
                        break;
                    case 0:
                        System.out.println("Ieșire din program.");
                        continua = false;
                        break;
                    default:
                        System.out.println("Opțiune invalidă. Vă rugăm să selectați din nou.");
                        break;
                }

            }catch (InputMismatchException e){
                System.out.println("Opțiune invalidă. Vă rugăm să selectați din nou.");
            }





            if (continua) {
                System.out.println("\nDoriți să reveniți la meniul principal? (Y/N)");
                scanner.nextLine();  // Consumă newline rămas
                String raspuns = scanner.nextLine();
                if (raspuns.equalsIgnoreCase("N")) {
                    continua = false;
                }
            }
        }

        scanner.close();
    }

    private static void afisareMeniu() {
        System.out.println("===========================================");
        System.out.println("              Meniu Principal");
        System.out.println("===========================================");
        System.out.println("[1]. DES");
        System.out.println("[2]. DSA");
        System.out.println("[3]. RSA");
        System.out.println("[0]. Ieșire");
        System.out.println("===========================================");
    }
    // Funcție pentru curățarea consolei
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Dacă sistemul de operare este Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Dacă sistemul de operare este Unix/Linux
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            // Tratați excepțiile dacă apar
            System.out.println("Eroare la curățarea consolei: " + e.getMessage());
        }
    }
}