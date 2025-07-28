import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class ShamirSecretSharing {

    private static final SecureRandom random = new SecureRandom();
    private static final BigInteger PRIME = new BigInteger("208351617316091241234326746312124448251235562226470491514186331217050270460481");

    // Split the secret into shares
    public static Map<Integer, BigInteger> splitSecret(BigInteger secret, int n, int k) {
        BigInteger[] coeffs = new BigInteger[k];
        coeffs[0] = secret;

        // Generate random coefficients for polynomial
        for (int i = 1; i < k; i++) {
            coeffs[i] = new BigInteger(PRIME.bitLength() - 1, random);
        }

        Map<Integer, BigInteger> shares = new HashMap<>();

        for (int x = 1; x <= n; x++) {
            BigInteger y = BigInteger.ZERO;
            for (int i = 0; i < k; i++) {
                y = y.add(coeffs[i].multiply(BigInteger.valueOf(x).pow(i))).mod(PRIME);
            }
            shares.put(x, y);
        }

        return shares;
    }

    // Reconstruct the secret using k shares
    public static BigInteger reconstructSecret(Map<Integer, BigInteger> shares) {
        BigInteger secret = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> entry1 : shares.entrySet()) {
            int x1 = entry1.getKey();
            BigInteger y1 = entry1.getValue();

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (Map.Entry<Integer, BigInteger> entry2 : shares.entrySet()) {
                int x2 = entry2.getKey();
                if (x1 != x2) {
                    numerator = numerator.multiply(BigInteger.valueOf(-x2)).mod(PRIME);
                    denominator = denominator.multiply(BigInteger.valueOf(x1 - x2)).mod(PRIME);
                }
            }

            BigInteger lagrange = numerator.multiply(denominator.modInverse(PRIME)).mod(PRIME);
            secret = secret.add(y1.multiply(lagrange)).mod(PRIME);
        }

        return secret;
    }

    public static void main(String[] args) {
        int n = 5;  // total shares
        int k = 3;  // threshold
        BigInteger secret = new BigInteger("123456789");

        System.out.println("Original Secret: " + secret);

        // Split secret
        Map<Integer, BigInteger> shares = splitSecret(secret, n, k);
        System.out.println("\nGenerated Shares:");
        for (Map.Entry<Integer, BigInteger> entry : shares.entrySet()) {
            System.out.println("Share " + entry.getKey() + ": " + entry.getValue());
        }

        // Pick k shares to reconstruct
        Map<Integer, BigInteger> subset = new HashMap<>();
        subset.put(1, shares.get(1));
        subset.put(2, shares.get(2));
        subset.put(4, shares.get(4));

        BigInteger recovered = reconstructSecret(subset);
        System.out.println("\nRecovered Secret using 3 shares: " + recovered);
    }
}
