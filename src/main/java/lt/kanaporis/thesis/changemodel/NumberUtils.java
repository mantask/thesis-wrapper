package lt.kanaporis.thesis.changemodel;

final class NumberUtils {

    public static final double THRESHOLD = 1E-6;

    public static boolean eq(double a, double b) {
        return Math.abs(a - b) < THRESHOLD;
    }

    public static boolean lt(double a, double b) {
        return gt(b, a);
    }

    public static boolean gt(double a, double b) {
        return a - b > THRESHOLD;
    }

    public static boolean le(double a, double b) {
        return lt(a, b) || eq(a, b);
    }

}
