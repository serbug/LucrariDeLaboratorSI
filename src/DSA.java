import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class DSA {
    public static void main(String[] args) throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            // Generare cheie privată și publică
            KeyPair keyPair = generateKeyPair();
            PrivateKey privKey = keyPair.getPrivate();
            PublicKey pubKey = keyPair.getPublic();

            // Afișare chei
            System.out.println("Private Key: " + Base64.getEncoder().encodeToString(privKey.getEncoded()));
            System.out.println("Public Key: " + Base64.getEncoder().encodeToString(pubKey.getEncoded()));

            // Reconstruiește cheile din bytes
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey reconstructedPrivKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKey.getEncoded()));
            PublicKey reconstructedPubKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubKey.getEncoded()));

            // Creare obiect Signature
            Signature sign = Signature.getInstance("SHA256withDSA");

            // Initializează semnătura cu cheia privată
            sign.initSign(reconstructedPrivKey);

            // Introducerea textului pentru semnare
            String msg = Arrays.toString(getInputWithValidation(sc, "Enter text to sign: "));
            byte[] bytes = msg.getBytes();

            // Adaugă datele la semnătură
            sign.update(bytes);

            // Calculează semnătura
            byte[] signature = sign.sign();

            // Afișare semnătura digitală rezultată
            System.out.println("Digital signature: " + Base64.getEncoder().encodeToString(signature));

            // Verificare semnătură
            boolean isVerified = verifySignature(reconstructedPubKey, bytes, signature);
            System.out.println("Signature verification result: " + isVerified);
        }
    }

    // Metodă pentru generarea cheii private și publice
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // Metodă pentru introducerea datelor cu validare
    private static byte[] getInputWithValidation(Scanner sc, String prompt) {
        byte[] keyBytes = null;

        while (keyBytes == null) {
            printMessage(prompt);
            String keyText = sc.nextLine();
            String cleanKeyText = keyText.replaceAll("\\s", "");

            if (isValidBase64(cleanKeyText)) {
                keyBytes = Base64.getDecoder().decode(cleanKeyText);
            } else {
                printMessage("Invalid key format. Please enter a valid Base64-encoded key.");
            }
        }

        return keyBytes;
    }

    // Metodă pentru verificarea semnăturii
    private static boolean verifySignature(PublicKey publicKey, byte[] data, byte[] signature) {
        try {
            Signature verifySign = Signature.getInstance("SHA256withDSA");
            verifySign.initVerify(publicKey);
            verifySign.update(data);
            return verifySign.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodă pentru afișarea mesajelor
    private static void printMessage(String message) {
        System.out.print(message);
    }

    // Metodă pentru validarea formatului Base64
    private static boolean isValidBase64(String text) {
        try {
            Base64.getDecoder().decode(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
