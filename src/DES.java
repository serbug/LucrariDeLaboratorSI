import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Sergiu
 */

/* DES poate fi folosit pentru a cripta toate tipurile de date, cum ar fi imagini și text */
public class DES {
    // Scanner pentru a citi inputul de la utilizator
    Scanner userInput = new Scanner(System.in);

    // PrintWriter pentru a afișa rezultatele
    PrintWriter pw = new PrintWriter(System.out, true);

    // Aray de chei ale rundelor
    String[] RoundKeyArray = new String[16];

    // Array de chei
    String[] Keys = new String[16];

    // Cheia permutată
    String permutatedKey = "";

    // Textul clar în format binar
    String plainTextBinary = "";

    // Rezultatul final al criptării
    String finalResult = "";

    // Variabila pentru un bloc criptat
    String encipher = "";

    // Textul criptat complet
    String textEncipher = "";

    // Spațiu stânga utilizat în shiftări
    int leftSpace;

    // Textul decriptat
    String decipher = "";

    // Textul decriptat în format binar
    String binaryDecipher = "";

    // Cheia pentru o rundă
    String RoundKey = "";

    // Cheia originală introdusă de utilizator
    String originalKey = "";

    // Numărul de shiftări la stânga pentru fiecare rundă
    int[] NumLeftShifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    // PC1 - Permutarea cheii 1
    int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};

    // PC2 - Permutarea cheii 2
    int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};

    // IP - Permutarea inițială
    int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    // FP - Permutarea finală
    int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    // E - Extinderea
    int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    // S1 - Cutia S1
    int[][] S1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};

    // S2 - Cutia S2
    int[][] S2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 11, 5, 6, 15, 4, 2, 10, 7, 1, 12, 0, 14, 9, 6}};

    // S3 - Cutia S3
    int[][] S3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};

    // S4 - Cutia S4
    int[][] S4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};

    // S5 - Cutia S5
    int[][] S5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};

    // S6 - Cutia S6
    int[][] S6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};

    // S7 - Cutia S7
    int[][] S7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};

    // S8 - Cutia S8
    int[][] S8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

    // P - Permutarea
    int[] P = {16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};

    /* Funcția principală pentru criptare */
    public void Encryption() {

        try {

            /******************************************** 1.SUB CHEI ********************************************************
             ****************************************************************************************************************/
            /* Solicită utilizatorului să introducă o cheie de 64 de biți */
            pw.println("Introduceți cheia de 64 de biți: ");
            originalKey = userInput.nextLine();
            System.out.println("Cheia originală în text: " + originalKey);

            /* Convertă cheia în format binar și afișează */
            String binaryKey = strTobin(originalKey);
            System.out.println("Cheia originală în binar: " + binaryKey);
            binaryKey = binaryKey.replace(" ", "");

            /* Cheia trebuie să aibă cel puțin 8 caractere lungime
               Obține permutarea de 56 de biți din cheia originală folosind PC-1 */
            for (int i : PC1) {
                permutatedKey += binaryKey.charAt(i - 1);
            }
            pw.println("Cheia originală de 56 de biți:    " + permutatedKey.replaceAll("(.{8})(?!$)", "$1 "));

            /* Împarte cheia în două jumătăți, LK și RK, unde fiecare jumătate are 28 de biți. */
            String LK = permutatedKey.substring(0, 28);
            String RK = permutatedKey.substring(28, 56);

            /* Realizează 16 deplasări circulare la stânga asupra LK și RK originale */
            String Lkey = LK;
            String Rkey = RK;
            pw.println();
            int Index = 0;
            for (int a : NumLeftShifts) {
                Lkey = CircularLeftShift(Lkey, a);
                Rkey = CircularLeftShift(Rkey, a);
                Keys[Index] = Lkey + Rkey;
                Index++;
            }

            /* Construiește cele 16 sub-chei de 48 de biți folosind tabela de permutare PC-2 */
            Index = 0;
            for (String key : Keys) {
                for (int j : PC2) {
                    RoundKey += key.charAt(j - 1);
                }
                RoundKeyArray[Index] = RoundKey;
                Index++;
                RoundKey = "";
            }

            /******************************************** 2. CODIFICARE MESAJ **************************************************
             ****************************************************************************************************************/

            /* Solicită utilizatorului să introducă textul simplu */
            pw.println("Introduceți textul simplu: ");
            originalKey = userInput.nextLine();
            pw.println("Textul simplu original: " + originalKey);

            /* Convertă textul în binar și afișează */
            binaryKey = strTobin(originalKey);
            pw.println("Textul simplu în binar: " + binaryKey);
            binaryKey = binaryKey.replace(" ", "");
            String BinaryText = binaryKey;

            /* Utilizează plainTextBinary pentru a forma un ciclu */
            int plainTextLength = binaryKey.length();
            if (plainTextLength < 64) {
                pw.println("Textul simplu are " + plainTextLength + "-biți. Trebuie să aibă cel puțin 64 de biți (8 caractere)");
                System.exit(0);
            } else {
                pw.println("_________________________________________________________________________________");
                int leftpadString = plainTextLength % 64;
                int loop = leftpadString != 0 ? plainTextLength/64 + 1 : plainTextLength/64;
                int start = 0;
                int end = 64;
                for(int id = loop, wordCount = 1; id >0; id--, wordCount++) {
                    int leftpad = 64 - leftpadString;
                    leftSpace = leftpad % 64 != 0 ? leftpad/8 : 0;
                    end = plainTextLength - start < 64 ? plainTextLength : end;
                    plainTextBinary = BinaryText.substring(start, end);
                    while(plainTextBinary.length() != 64) {
                        plainTextBinary = "0" + plainTextBinary;
                    }

                    /* Realizează o permutare inițială pe textul simplu */
                    String IPBinary = "";
                    for (int i : IP) {
                        IPBinary += plainTextBinary.charAt(i - 1);
                    }

                    /* Împarte blocul permutat în două jumătăți de 32 de biți */
                    String LeftIPBinary = IPBinary.substring(0, 32);
                    String RightIPBinary = IPBinary.substring(32, 64);

                    /* Acum, procedează prin 16 runde/iterații folosind o funcție f
                       Există 16 chei, deci 16 runde, așa cum era de așteptat */
                    int counter = 1;
                    for (String k : RoundKeyArray) {
                        pw.println();
                        pw.println("                    64-bit " + wordCount+ " RUNDĂ DE ÎNCODIFICARE " + counter + "                            ");
                        pw.println("CHEIE = " + k);

                        /* Blocul stâng devine blocul drept al rundei anterioare */
                        String LeftBlock = RightIPBinary;
                        pw.println("BLOC STÂNG  = " + LeftBlock);

                        /* Blocul drept este blocul stâng anterior XOR F(bloc stâng anterior, cheie rundă)
                           Pentru a calcula blocul drept, extindem mai întâi blocul anterior de 32 de biți la 48 de biți, deoarece cheia are 48 de biți */
                        String expand = "";
                        for (int i : E) {
                            expand += RightIPBinary.charAt(i - 1);
                            /* Fiecare bloc de 4 biți originali a fost extins la un bloc de 6 biți de ieșire */
                        }

                        /* XOR între 'expand' și cheie, deoarece amândouă au acum 48 de biți lungime */
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < k.length(); i++) {
                            sb.append((k.charAt(i) ^ expand.charAt(i)));
                        }
                        String result = sb.toString();

                        /* "Cutii S": 8 grupuri de șase biți returnează 4 biți în ordine pentru ca blocul stâng să-și recapete dimensiunea originală de 32 de biți */
                        //String binary = String.format("%8s", Integer.toBinaryString(currentByte)).replace(' ', '0');
                        String RB1 = result.substring(0, 6);
                        String row1 = String.valueOf(RB1.substring(0, 1) + RB1.substring(5, 6));
                        String col1 = String.valueOf(RB1.substring(1, 5));
                        int target = S1[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        String binaryTarget = String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB2 = result.substring(6, 12);
                        row1 = String.valueOf(RB2.substring(0, 1) + RB2.substring(5, 6));
                        col1 = String.valueOf(RB2.substring(1, 5));
                        target = S2[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB3 = result.substring(12, 18);
                        row1 = String.valueOf(RB3.substring(0, 1) + RB3.substring(5, 6));
                        col1 = String.valueOf(RB3.substring(1, 5));
                        target = S3[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB4 = result.substring(18, 24);
                        row1 = String.valueOf(RB4.substring(0, 1) + RB4.substring(5, 6));
                        col1 = String.valueOf(RB4.substring(1, 5));
                        target = S4[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB5 = result.substring(24, 30);
                        row1 = String.valueOf(RB5.substring(0, 1) + RB5.substring(5, 6));
                        col1 = String.valueOf(RB5.substring(1, 5));
                        target = S5[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB6 = result.substring(30, 36);
                        row1 = String.valueOf(RB6.substring(0, 1) + RB6.substring(5, 6));
                        col1 = String.valueOf(RB6.substring(1, 5));
                        target = S6[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB7 = result.substring(36, 42);
                        row1 = String.valueOf(RB7.substring(0, 1) + RB7.substring(5, 6));
                        col1 = String.valueOf(RB7.substring(1, 5));
                        target = S7[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                        String RB8 = result.substring(42, 48);
                        row1 = String.valueOf(RB8.substring(0, 1) + RB8.substring(5, 6));
                        col1 = String.valueOf(RB8.substring(1, 5));
                        target = S8[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                        binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');
                        //pw.println(binaryTarget);

                        /* La final, pentru a obține f, permutați ieșirea cutiei S (binaryTarget) folosind tabela P pentru a obține valoarea finală */
                        String function = "";
                        for (int d : P) {
                            function += binaryTarget.charAt(d - 1);
                        }

                        /* În cele din urmă, XOR între blocul stâng anterior și valoarea funcției */
                        sb = new StringBuilder();
                        for (int i = 0; i < LeftIPBinary.length(); i++) {
                            sb.append((LeftIPBinary.charAt(i) ^ function.charAt(i)));
                        }
                        result = sb.toString();

                        RightIPBinary = result;
                        pw.println("BLOC DREPT = " + RightIPBinary);
                        result = "";

                        counter++;
                        if (counter > 16) {
                            /* Inversați combinarea celor două blocuri pentru a forma un bloc de 64 de biți */
                            result = RightIPBinary + LeftBlock;

                            finalResult = "";
                            /* Permutarea Finală FP: Inversa permutării inițiale IP */
                            for (int x : FP) {
                                finalResult += result.charAt(x - 1);
                            }
                            encipher += finalResult;
                            textEncipher += id == 1 && leftSpace != 0 ? intTostr(finalResult, 8).substring(leftSpace) : intTostr(finalResult, 8);
                        }
                        LeftIPBinary = LeftBlock;
                    }
                    end += 64;
                    start += 64;
                    pw.println("CRIPTARE A BLOCULUI DE 64 de biți " + wordCount +" = " + intTostr(finalResult, 8));
                    pw.println("_________________________________________________________________________________");
                }
                /* Afișează textul cifrat final */
                pw.println("\nTEXT CRIPTAT = " + encipher.replaceAll("(.{8})(?!$)", "$1 "));
                pw.println("TEXT CRIPTAT ÎN TEXT PLAIN = " + textEncipher);
            }

            /************************************************ 3. DECRIPTARE *************************************************
             ****************************************************************************************************************/
            pw.println("");
            pw.println("********************************************************************************************************************");
            pw.println("********************************************************************************************************************");
            BinaryText = encipher;

            /* Se folosește encipher pentru a forma un ciclu */
            int encipherLength = encipher.length();
            int leftpadString = encipherLength % 64;
            int loop = leftpadString != 0 ? encipherLength/64 + 1 : encipherLength/64;
            int start = 0;
            int end = 64;
            pw.println("_________________________________________________________________________________");
            for(int id = loop, wordCount = 1; id >0; id--,wordCount++) {
                int leftpad = 64 - leftpadString;
                end = encipherLength - start < 64 ? encipherLength : end;
                String cipherTextBinary = BinaryText.substring(start, end);

                /* Realizează o permutare inițială pe textul binar al cifrului ==> binaryKey */
                String IPBinary = "";
                for (int i : IP) {
                    IPBinary += cipherTextBinary.charAt(i - 1);
                }

                /* Împarte blocul permutat în două jumătăți de 32 de biți */
                String LeftIPBinary = IPBinary.substring(0, 32);
                String RightIPBinary = IPBinary.substring(32, 64);

                /* Parcurge cele 16 runde/iterații fiecare folosind o funcție f unică
                   Parcurge cele 16 chei în ORDINE INVERSĂ */
                int counter = 1;
                String k;
                for (int p = 15; p >= 0; p--) {
                    pw.println();
                    pw.println("                    64-bit " + wordCount+ " RUNDĂ DE DEZCRIPTARE " + counter + "                            ");
                    k = RoundKeyArray[p];
                    pw.println("CHEIE = " + k);

                    /* Blocul stâng devine blocul drept al rundei anterioare */
                    String LeftBlock = RightIPBinary;
                    pw.println("BLOC STÂNG  = " + LeftBlock);

                    /* Blocul drept este blocul stâng anterior XOR F(bloc stâng anterior, cheie rundă) */
                    String expand = "";
                    for (int i : E) {
                        expand += RightIPBinary.charAt(i - 1);
                    }

                    /* XOR între 'expand' și cheie deoarece acum ambele au 48 de biți */
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < k.length(); i++) {
                        sb.append((k.charAt(i) ^ expand.charAt(i)));
                    }
                    String result = sb.toString();

                    /* "Cutii S": 8 grupuri de șase biți sunt transformate în 4 biți pentru a reveni la dimensiunea originală de 32 de biți */
                    String RB1 = result.substring(0, 6);
                    String row1 = String.valueOf(RB1.substring(0, 1) + RB1.substring(5, 6));
                    String col1 = String.valueOf(RB1.substring(1, 5));
                    int target = S1[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    String binaryTarget = String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB2 = result.substring(6, 12);
                    row1 = String.valueOf(RB2.substring(0, 1) + RB2.substring(5, 6));
                    col1 = String.valueOf(RB2.substring(1, 5));
                    target = S2[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB3 = result.substring(12, 18);
                    row1 = String.valueOf(RB3.substring(0, 1) + RB3.substring(5, 6));
                    col1 = String.valueOf(RB3.substring(1, 5));
                    target = S3[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB4 = result.substring(18, 24);
                    row1 = String.valueOf(RB4.substring(0, 1) + RB4.substring(5, 6));
                    col1 = String.valueOf(RB4.substring(1, 5));
                    target = S4[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB5 = result.substring(24, 30);
                    row1 = String.valueOf(RB5.substring(0, 1) + RB5.substring(5, 6));
                    col1 = String.valueOf(RB5.substring(1, 5));
                    target = S5[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB6 = result.substring(30, 36);
                    row1 = String.valueOf(RB6.substring(0, 1) + RB6.substring(5, 6));
                    col1 = String.valueOf(RB6.substring(1, 5));
                    target = S6[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB7 = result.substring(36, 42);
                    row1 = String.valueOf(RB7.substring(0, 1) + RB7.substring(5, 6));
                    col1 = String.valueOf(RB7.substring(1, 5));
                    target = S7[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB8 = result.substring(42, 48);
                    row1 = String.valueOf(RB8.substring(0, 1) + RB8.substring(5, 6));
                    col1 = String.valueOf(RB8.substring(1, 5));
                    target = S8[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    /* În cele din urmă, pentru a obține f, permutăm ieșirea cutiei S folosind tabela P */
                    String function = "";
                    for (int d : P) {
                        function += binaryTarget.charAt(d - 1);
                    }

                    /* În cele din urmă, blocul stâng anterior XOR funcție */
                    sb = new StringBuilder();
                    for (int i = 0; i < LeftIPBinary.length(); i++) {
                        sb.append((LeftIPBinary.charAt(i) ^ function.charAt(i)));
                    }
                    result = sb.toString();

                    RightIPBinary = result;
                    pw.println("BLOC DREPT = " + RightIPBinary);
                    result = "";

                    counter++;
                    if (counter > 16) {
                        /* Inversează ordinea celor două blocuri pentru a forma un bloc de 64 de biți */
                        result = RightIPBinary + LeftBlock;

                        /* Permutarea finală: inversa permutării inițiale */
                        String finalResult = "";
                        for (int x : FP) {
                            finalResult += result.charAt(x - 1);
                        }

                        binaryDecipher += finalResult;
                        decipher += id == 1 && leftSpace != 0 ? intTostr(finalResult, 8).substring(leftSpace) : intTostr(finalResult, 8);
                        pw.println("CIFRUL DECRIS DE 64 de biți " + wordCount +" = " + intTostr(finalResult, 8));
                    }
                    LeftIPBinary = LeftBlock;
                }

                end += 64;
                start += 64;
                pw.println("_________________________________________________________________________________");
            }
            /* Afișează textul clar original */
            pw.println("\nTextul decriptat in biti = " + binaryDecipher.replaceAll("(.{8})(?!$)", "$1 "));
            pw.println("Textul decriptat in format text = " + decipher);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodă pentru a converti un șir de caractere într-un șir binar
    public String strTobin( String str ) {

        byte[] bytes = str.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    // Metodă pentru a converti un șir binar într-un șir de caractere
    public String intTostr( String stream, int size ) {

        String result = "";
        for (int i = 0; i < stream.length(); i += size) {
            result += (stream.substring(i, Math.min(stream.length(), i + size)) + " ");
        }
        String[] ss = result.split( " " );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < ss.length; i++ ) {
            sb.append( (char)Integer.parseInt( ss[i], 2 ) );
        }
        return sb.toString();
    }

    // Metodă pentru shiftarea circulară la stânga
    String CircularLeftShift(String s, int k) {

        String result = s.substring(k);
        for (int i = 0; i < k; i++) {
            result += s.charAt(i);
        }
        return result;
    }

}
