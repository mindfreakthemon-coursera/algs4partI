import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private int N;
    private int T;

    private double[] results;
    private double cachedMean = Double.NaN;
    private double cachedStddev = Double.NaN;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        this.T = T;

        int L = N * N;

        results = new double[T];

        for (int i = 0; i < T; ++i) {
            results[i] = experiment() / (double) L;
        }
    }

    public double mean() {
        if (Double.isNaN(cachedMean)) {
            double sum = 0;

            for (int i = 0; i < T; ++i) {
                sum += results[i];
            }

            cachedMean = sum / T;
        }

        return cachedMean;
    }

    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        }

        if (Double.isNaN(cachedStddev)) {
            double mean = mean();
            double sum = 0;

            for (int i = 0; i < T; ++i) {
                sum += Math.pow(results[i] - mean, 2);
            }

            cachedStddev = Math.sqrt(sum / (T - 1));
        }

        return cachedStddev;
    }

    public double confidenceLo() {
        return confidence(-1.96d);
    }

    public double confidenceHi() {
        return confidence(1.96d);
    }

    private double confidence(double sign) {
        double mean = mean();
        double stddev = stddev();
        double sqrtT = Math.sqrt(T);

        return mean + sign * stddev / sqrtT;
    }

    private int experiment() {
        Percolation p = new Percolation(N);

        int count = 0;

        while (true) {
            int row = StdRandom.uniform(N) + 1;
            int col = StdRandom.uniform(N) + 1;

            if (!p.isOpen(row, col)) {
                p.open(row, col);

                count++;
            }

            if (p.percolates()) {
                break;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int N = 10, T = 10;

        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
        }

        if (args.length > 1) {
            T = Integer.parseInt(args[1]);
        }

        PercolationStats ps = new PercolationStats(N, T);

        StdOut.printf("%-23s = %.16f\n%-23s = %.16f\n%-23s = %.16f, %.16f",
                "mean", ps.mean(),
                "stddev", ps.stddev(),
                "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
    }
}
