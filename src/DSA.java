import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 *
 * @author Sergiu
 */
public class DSA {

    // Declarații pentru variabilele folosite în algoritm
    public static BigInteger p;
    public static BigInteger q;
    public static BigInteger g;
    public static BigInteger x;
    public static BigInteger y;
    public static BigInteger k;

    // Generatorul de numere aleatoare securizat
    private static SecureRandom rnd = new SecureRandom();

    public static void main(String[] args) {
        // Inițializarea variabilelor q, p, g, x, y, și k
        q = getQ(16);  // Obține q (număr prim de 16 biți)
        p = getP(q, 48);  // Obține p (număr prim de 48 biți)
        g = generateG();  // Generează g (generator)
        x = generateXorK();  // Generează x (cheia secretă)
        y = generateY();  // Generează y (cheia publică)
        k = generateXorK();  // Generează k (valoare aleatoare)

        boolean menu = true;
        BigInteger[] RS = null;
        String plainText = null;

        Scanner scanner = new Scanner(System.in);

        while (menu) {
            // Afișarea meniului principal
            System.out.println("===========================================");
            System.out.println("              Meniu Principal");
            System.out.println("===========================================");
            System.out.println("[1] - Creare semnătură");
            System.out.println("[2] - Verificare semnătură");
            System.out.println("[0] - Ieșire");
            System.out.print("\nOpțiune>> ");
            int option = scanner.nextInt();

            switch (option) {
                case 1: {
                    // Crearea semnăturii
                    System.out.println("Introduceți textul simplu:");
                    scanner.nextLine();  // Consumă newline înainte de citirea textului
                    plainText = scanner.nextLine();
                    System.out.println("\nNumăr prim Q = " + q + "\nNumăr prim P = " + p
                            + "\nNumăr G = " + g + "\nCheia secretă X = " + x + "\nCheia publică Y = " + y);
                    RS = new BigInteger[2];
                    RS = createSignature(plainText);
                }
                break;

                case 2: {
                    // Verificarea semnăturii
                    System.out.println("Introduceți textul simplu pentru verificare: ");
                    scanner.nextLine();  // Consumă newline înainte de citirea textului
                    plainText = scanner.nextLine();
                    System.out.println("\nIntroduceți R");
                    BigInteger newR = scanner.nextBigInteger();
                    System.out.println("Introduceți S");
                    BigInteger newS = scanner.nextBigInteger();
                    System.out.println();
                    if (verifySignature(newR, newS, plainText) == 0) {
                        System.out.println("Semnătura verificată!");
                    } else {
                        System.out.println("Semnătura falsă!");
                    }
                }
                break;

                case 0:
                   // System.exit(0);
                    return;

                default:
                    System.out.println("Opțiune greșită! Încercați din nou.");
                    break;
            }
        }

        scanner.close();
    }

    // Funcție pentru crearea semnăturii
    public static BigInteger[] createSignature(String text) {
        // Calcularea unui cod hash de 2 lungimi
        byte[] binaryText = text.getBytes();
        byte[] binaryHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            binaryHash = md.digest(binaryText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String stringHexHash = "";
        for (byte b : binaryHash) {
            stringHexHash += String.format("%02x", b);
        }
        stringHexHash = stringHexHash.substring(0, 2);
        BigInteger numberHash = new BigInteger(stringHexHash, 16);

        BigInteger R = g.modPow(k, p).mod(q); //(g^k mod p) mod q

        BigInteger tempH = numberHash.add(x.multiply(R));
        BigInteger tempPow = modularLinearEquationSolver(k, BigInteger.ONE, q);
        BigInteger tempS = tempPow.multiply(tempH);
        BigInteger S = tempS.mod(q);

        System.out.println("R = " + R + ", S = " + S);

        return new BigInteger[]{R, S};
    }

    // Funcție pentru verificarea semnăturii
    public static int verifySignature(BigInteger R, BigInteger S, String text) {
        // Calcularea unui cod hash de 2 lungimi
        byte[] binaryText = text.getBytes();
        byte[] binaryHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            binaryHash = md.digest(binaryText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String stringHexHash = "";
        for (byte b : binaryHash) {
            stringHexHash += String.format("%02x", b);
        }
        stringHexHash = stringHexHash.substring(0, 2);
        BigInteger numberHash = new BigInteger(stringHexHash, 16);

        BigInteger tempPowW = modularLinearEquationSolver(S, BigInteger.ONE, q);
        BigInteger w = tempPowW.mod(q);

        BigInteger u1 = numberHash.multiply(w).mod(q); //(H*w) mod q
        BigInteger u2 = R.multiply(w).mod(q); //(R*w) mod q

        BigInteger tempV1 = g.modPow(BigInteger.valueOf(u1.intValue()), p); //g^u1
        BigInteger tempV2 = y.modPow(BigInteger.valueOf(u2.intValue()), p); //g^u2
        BigInteger tempV = tempV1.multiply(tempV2).mod(p); // (g^u1 * g^u2) mod p
        BigInteger V = tempV.mod(q);  // ((g^u1 * g^u2) mod p) mod q

        return V.compareTo(R);
    }

    // Funcție pentru generarea cheii publice Y
    private static BigInteger generateY() {
        return g.modPow(x, p);
    }

    // Funcție pentru generarea cheii private X sau a valorii aleatoare K
    private static BigInteger generateXorK() {
        byte[] bytes = new byte[32 / 8];
        rnd.nextBytes(bytes);

        BigInteger q = new BigInteger(1, bytes);
        return q.abs();
    }

    // Funcție pentru obținerea numărului prim Q
    public static BigInteger getQ(int length) {
        boolean isDone = true;

        while (isDone) {
            byte[] bytes = new byte[length / 8];
            rnd.nextBytes(bytes);

            BigInteger q = new BigInteger(1, bytes);

            if (q.signum() == -1) q = q.negate();

            if (q.isProbablePrime(20)) {
                isDone = false;
                return q;
            }
        }

        return BigInteger.ONE.negate();
    }

    // Funcție pentru obținerea numărului prim P
    public static BigInteger getP(BigInteger q, int length) {
        BigInteger pTemp;

        do {
            byte[] bytes = new byte[length / 8];
            rnd.nextBytes(bytes);

            pTemp = new BigInteger(1, bytes);

            BigInteger temp2 = pTemp.subtract(BigInteger.ONE);
            BigInteger temp2Remeider = temp2.mod(q);
            pTemp = pTemp.subtract(temp2Remeider);

        } while (!pTemp.isProbablePrime(20));

        return pTemp;
    }

    // Funcție pentru generarea generatorului G
    public static BigInteger generateG() {
        int h = rnd.nextInt(q.intValue());

        BigInteger arg1 = p.subtract(BigInteger.ONE).divide(q); //(p − 1)/q

        return BigInteger.valueOf(h).modPow(arg1, p); //h^(p − 1)/q mod p
    }

    // Funcție pentru calculul Eculid extins
    public static EuclideVar extendedEuclide(BigInteger a, BigInteger b) {
        EuclideVar dxy;

        if (b.equals(BigInteger.ZERO)) {
            dxy = new EuclideVar(a, BigInteger.ONE, BigInteger.ZERO);
            return dxy;
        }

        EuclideVar dxy1 = extendedEuclide(b, a.mod(b));

        dxy = new EuclideVar(dxy1.D, dxy1.Y, dxy1.X.subtract(a.divide(b).multiply(dxy1.Y)));

        return dxy;
    }

    // Funcție pentru rezolvarea ecuației liniare modulare
    public static BigInteger modularLinearEquationSolver(BigInteger a, BigInteger b, BigInteger n) {
        EuclideVar dxy1 = extendedEuclide(a, n);

        BigInteger x0 = dxy1.X.mod(n);

        if (x0.compareTo(BigInteger.ONE) < 0)
            x0 = n.add(x0);

        return x0;
    }

    // Clasă internă pentru stocarea valorilor rezultate din Euclid extins
    static class EuclideVar {
        BigInteger D;
        BigInteger X;
        BigInteger Y;

        public EuclideVar(BigInteger d, BigInteger x, BigInteger y) {
            D = d;
            X = x;
            Y = y;
        }
    }
}
