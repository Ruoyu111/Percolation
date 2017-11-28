import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final boolean BLOCKED = false;
    private static final boolean OPEN = true;

    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufHelper;
    private boolean[] arr;
    private final int dimension;
    private int numberOfOpenSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("input should be positive.");
        this.dimension = n;
        this.numberOfOpenSites = 0;
        // initialize array, this array does not contain virtual nodes
        // this array only record blocked or open
        arr = new boolean[n * n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2);
        // another uf to gurantee full check is right.
        ufHelper = new WeightedQuickUnionUF(n * n + 2);
        // open first and last (virtual) nodes
        arr[0] = true;
        arr[n * n + 1] = true;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isInBound(row, col))
            throw new IllegalArgumentException("Site location out of bound.");
        int pos = position(row, col);
        if (arr[pos] == BLOCKED) {
            arr[pos] = OPEN;
            numberOfOpenSites++;
            // connect this site with all ajacent sites
            // left
            if ((col != 1) && isOpen(row, col - 1)) {
                uf.union(pos, position(row, col - 1));
                ufHelper.union(pos, position(row, col - 1));
            }
            // right
            if ((col != dimension) && isOpen(row, col + 1)) {
                uf.union(pos, position(row, col + 1));
                ufHelper.union(pos, position(row, col + 1));
            }

            // up
            if (row == 1) {
                uf.union(pos, 0);
                ufHelper.union(pos, 0);
            } else if (isOpen(row - 1, col)) {
                uf.union(pos, position(row - 1, col));
                ufHelper.union(pos, position(row - 1, col));
            }

            // down
            if (row == dimension) {
                uf.union(pos, dimension * dimension + 1);
            } else if (isOpen(row + 1, col)) {
                uf.union(pos, position(row + 1, col));
                ufHelper.union(pos, position(row + 1, col));
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isInBound(row, col))
            throw new IllegalArgumentException("Site location out of bound.");
        int pos = position(row, col);
        return arr[pos] == OPEN;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isInBound(row, col))
            throw new IllegalArgumentException("Site location out of bound.");
        int pos = position(row, col);
        return ufHelper.connected(pos, 0);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(0, dimension * dimension + 1);
    }

    // from 2-dimension location to 1-dimension location
    private int position(int row, int col) {
        return (row - 1) * dimension + col;
    }

    // check whether the site is inbound
    private boolean isInBound(int row, int col) {
        return row >= 1 && row <= dimension && col >= 1 && col <= dimension;
    }
}
