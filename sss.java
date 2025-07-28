import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import org.json.*;  // Add org.json dependency or manually parse JSON if not using Maven

public class SecretSolver {

    public static class Point {
        BigInteger x, y;
        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    // Lagrange interpolation at x = 0
    public static BigInteger lagrangeInterpolation(List<Point> points, BigInteger prime) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < points.size(); i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    BigInteger xj = points.get(j).x;
                    numerator = numerator.multiply(xj.negate()).mod(prime);
                    denominator = denominator.multiply(xi.subtract(xj)).mod(prime);
                }
            }

            BigInteger term = yi.multiply(numerator).multiply(denominator.modInverse(prime)).mod(prime);
            result = result.add(term).mod(prime);
        }

        return result;
    }

    public static List<Point> parseJsonFile(String filePath, List<Integer> selectedKeys) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject json = new JSONObject(content);

        JSONObject keysObj = json.getJSONObject("keys");
        int k = keysObj.getInt("k");

        List<Point> points = new ArrayList<>();

        for (Integer key : selectedKeys) {
            if (!json.has(String.valueOf(key))) continue;

            JSONObject pointObj = json.getJSONObject(String.valueOf(key));
            int base = Integer.parseInt(pointObj.getString("base"));
            String value = pointObj.getString("value");

            BigInteger x = BigInteger.valueOf(key);
            BigInteger y = new BigInteger(value, base);
            points.add(new Point(x, y));
        }

        return points;
    }

    public static void main(String[] args) throws Exception {
        // Use a large prime number for modular arithmetic (larger than any expected value)
        BigInteger prime = new BigInteger("340282366920938463463374607431768211507"); // A 128-bit prime

        // First test case
        List<Integer> selected1 = Arrays.asList(1, 2, 3); // Use any 3 of the 4 roots
        List<Point> points1 = parseJsonFile("testcase1.json", selected1);
        BigInteger secret1 = lagrangeInterpolation(points1, prime);

        // Second test case
        List<Integer> selected2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7); // Any 7 of 10 roots
        List<Point> points2 = parseJsonFile("testcase2.json", selected2);
        BigInteger secret2 = lagrangeInterpolation(points2, prime);

        // Output both secrets
        System.out.println("Secret 1: " + secret1);
        System.out.println("Secret 2: " + secret2);
    }
}
