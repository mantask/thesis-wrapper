package lt.kanaporis.thesis.changemodel;

/**
 * Created by mantas on 8/13/14.
 */
public final class NumberUtils {

    public static boolean eq(double a, double b) {
        return Math.abs(a - b) < 1E-6;
    }

    public static boolean lt(double a, double b) {
        return gt(b, a);
    }

    public static boolean gt(double a, double b) {
        return a - b > 1E-6;
    }

    public static boolean le(double a, double b) {
        return lt(a, b) || eq(a, b);
    }

}
