import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Sergiu
 */

public class RSA {
    private static long p;
    private static long q;
    private static long N;
    private static long phi;
    private static long e;
    private static long d;
    private static int bitLength = 1000;
    private static Random rnd = new SecureRandom();

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        // Inițializare valorile p și q
        p = 103;
        q = 587;
        N = p * q;
        phi = (p - 1) * (q - 1);
        e = randomNumberBetweenOneAndPhi(phi); // Generarea unei valori aleatoare pentru e
        d = modInverse(e, phi); // Calcularea inversului modular al lui e

        // Afișarea cheilor publice și private
        System.out.println("Cheie publică = " + d);
        System.out.println("Cheie privată = " + e);

        /* Solicită utilizatorului să introducă textul simplu */
        System.out.println("Introduceți textul spre criptare: ");

        String mesaj = userInput.nextLine(); // Citirea mesajului de la utilizator

        // Criptarea mesajului
        BigInteger[] criptat = cripteaza(mesaj.getBytes());

        // Decriptarea mesajului
        byte[] decriptat = decripteaza(criptat);

        System.out.println();
        System.out.print("Criptat: ");
        for (BigInteger t : criptat)
            System.out.print(t);

        System.out.println("\nText decriptat: " + new String(decriptat));
    }

    // Criptează mesajul
    public static BigInteger[] cripteaza(byte[] mesaj) {
        BigInteger[] temp = new BigInteger[mesaj.length];

        for (int i = 0; i < mesaj.length; i++) {
            // Aplică operația modulară de putere pentru fiecare caracter
            temp[i] = BigInteger.valueOf(mesaj[i]).modPow(BigInteger.valueOf(e), BigInteger.valueOf(N));
        }

        return temp;
    }

    // Decriptează mesajul
    public static byte[] decripteaza(BigInteger[] mesaj) {
        byte[] temp = new byte[mesaj.length];

        for (int i = 0; i < mesaj.length; i++) {
            // Aplică operația modulară de putere pentru fiecare element din vector
            temp[i] = mesaj[i].modPow(BigInteger.valueOf(d), BigInteger.valueOf(N)).byteValue();
        }

        return temp;
    }

    // Calculează inversul modular
    private static long modInverse(long a, long n) {
        long i = n, v = 0, d = 1;

        while (a > 0) {
            long t = i / a, x = a;
            a = i % x;
            i = x;
            x = d;
            d = v - t * x;
            v = x;
        }

        v %= n;
        if (v < 0) v = (v + n) % n;
        return v;
    }

    // Generează un număr aleator între 1 și phi
    private static long randomNumberBetweenOneAndPhi(long phiParam) {
        boolean find = true;
        int numarAleator; // aceasta este "e"

        while (find) {
            numarAleator = rnd.nextInt(100) + 1;

            if ((numarAleator > 1) && (numarAleator < phiParam) && (gcd(numarAleator, phi) == 1)) {
                find = false;
                return numarAleator;
            }
        }

        return -1;
    }

    // Calculează cel mai mare divizor comun între două numere
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Verifică dacă un număr este prim
    public static boolean estePrim(int numar) {
        if (numar == 1) return false;
        if (numar == 2) return true;

        int limita = (int) Math.floor(Math.sqrt(numar));

        for (int i = 2; i <= limita; ++i) {
            if (numar % i == 0) return false;
        }

        return true;
    }

    // Obține un număr prim
    public static int getPrim() {
        boolean find = true;
        int numarAleator;

        while (find) {
            numarAleator = rnd.nextInt(100) + 1;

            if (estePrim(numarAleator)) {
                find = false;
                return numarAleator;
            }
        }

        return -1;
    }

    // Împarte mesajul în blocuri de caractere
    public static List<String> imparteMesajInBucati(String str, int numarCaractere) {
        List<String> lista = new ArrayList<>();

        for (int i = 0; i < str.length(); i += numarCaractere) {
            if (str.length() > (lista.size() + 1) * numarCaractere)
                lista.add(str.substring(i, i + numarCaractere));
            else
                lista.add(str.substring(i, str.length()));
        }

        return lista;
    }
}
