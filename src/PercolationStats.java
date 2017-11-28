import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int t; // trials
    private final double[] fractions; // fractions of open sites in computational experiment t
    private double mean;
    private double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.t = trials;
        fractions = new double[t];
        // initialize mean and stddev
        mean = -1;
        stddev = -1;

        // perform n trials
        for (int i = 0; i < trials; i++) {
            // perform a trial
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                // generate random position
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            // calculate fraction
            double fraction = (double) p.numberOfOpenSites() / (double) (n * n);
            fractions[i] = fraction;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == -1)
            mean = StdStats.mean(fractions);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev == -1) {
            if (t == 1) {
                stddev = Double.NaN;
            } else {
                stddev = StdStats.stddev(fractions);
            }
        }
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(t)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(t)));
    }

    // test client
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("%-24.24s= %s\n", "mean", ps.mean());
        System.out.printf("%-24.24s= %s\n", "stddev", ps.stddev());
        System.out.printf("%-24.24s= [%s, %s]\n", "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
    }

}
