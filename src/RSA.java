import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSA {
    private byte p;
    private byte q;
    private short phi;
    private short n;
    private short e;
    private int d;

    private class ExtendedEuclideanResult {
        public int u1;
        public int u2;
        public int gcd;
    }

    public RSA() {}

    public void initKeyData() {
        Random random = new Random();
        byte[] simple = getNotDivideable();

        if (simple.length == 0) {
            // Gestionați cazul în care array-ul este gol, de exemplu, aruncând o excepție sau luând măsuri adecvate.
            throw new IllegalStateException("Nu s-au găsit valori potrivite pentru p și q.");
        }

        this.p = simple[random.nextInt(simple.length)];
        this.q = simple[random.nextInt(simple.length)];
        this.n = (short) (this.p * this.q);
        this.phi = (short) ((p - 1) * (q - 1));
        List<Short> possibleE = getAllPossibleE(this.phi);
        do {
            this.e = possibleE.get(random.nextInt(possibleE.size()));
            this.d = extendedEuclide(this.e % this.phi, this.phi).u1;
        } while (this.d < 0);
    }

    public short getNKey() {
        return this.n;
    }

    public int getDKey() {
        return this.d;
    }

    public String encode(String text) {
        initKeyData();
        StringBuilder outStr = new StringBuilder();
        byte[] strBytes = text.getBytes();
        for (byte value : strBytes) {
            int encryptedValue = moduloPow(value, this.e, this.n);
            outStr.append(encryptedValue).append("!");
        }
        return outStr.toString();
    }

    public String decode(String text, String n_s, String d_s) {
        StringBuilder outStr = new StringBuilder();
        int n = Integer.parseInt(n_s);
        int d = Integer.parseInt(d_s);
        int[] arr = getDecArrayFromText(text);
        byte[] bytes = new byte[arr.length];
        int j = 0;
        for (int i : arr) {
            byte decryptedValue = (byte) moduloPow(i, d, n);
            bytes[j] = decryptedValue;
            j++;
        }
        outStr.append(new String(bytes));
        return outStr.toString();
    }

    private int[] getDecArrayFromText(String text) {
        int i = 0;
        for (char c : text.toCharArray()) {
            if (c == '!') {
                i++;
            }
        }
        int[] result = new int[i];
        i = 0;
        StringBuilder tmp = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c != '!') {
                tmp.append(c);
            } else {
                result[i] = Integer.parseInt(tmp.toString());
                i++;
                tmp = new StringBuilder();
            }
        }
        return result;
    }

    static int moduloPow(int value, int pow, int modulo) {
        int result = value;
        for (int i = 0; i < pow - 1; i++) {
            result = (result * value) % modulo;
        }
        return result;
    }

    List<Short> getAllPossibleE(short phi) {
        List<Short> result = new ArrayList<>();
        for (short i = 2; i < phi; i++) {
            if (extendedEuclide(i, phi).gcd == 1) {
                result.add(i);
            }
        }
        return result;
    }

    private ExtendedEuclideanResult extendedEuclide(int a, int b) {
        int u1 = 1;
        int u3 = a;
        int v1 = 0;
        int v3 = b;
        while (v3 > 0) {
            int q0 = u3 / v3;
            int q1 = u3 % v3;
            int tmp = v1 * q0;
            int tn = u1 - tmp;
            u1 = v1;
            v1 = tn;
            u3 = v3;
            v3 = q1;
        }
        int tmp2 = u1 * (a);
        tmp2 = u3 - (tmp2);
        int res = tmp2 / (b);
        ExtendedEuclideanResult result = new ExtendedEuclideanResult();
        result.u1 = u1;
        result.u2 = res;
        result.gcd = u3;
        return result;
    }

    static private byte[] getNotDivideable() {
        List<Byte> notDivideable = new ArrayList<>();
        for (int x = 2; x < 256; x++) {
            int n = 0;
            for (int y = 1; y <= x; y++) {
                if (x % y == 0)
                    n++;
            }
            if (n <= 2)
                notDivideable.add((byte) x);
        }
        byte[] result = new byte[notDivideable.size()];
        for (int i = 0; i < notDivideable.size(); i++) {
            result[i] = notDivideable.get(i);
        }
        return result;
    }
}
