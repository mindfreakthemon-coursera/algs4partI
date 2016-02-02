import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean[] openedState;
    private WeightedQuickUnionUF uf;
    private int topIndex;
    private int bottomIndex;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;

        int size = N * N + 1 /* TOP */ + 1 /* BOTTOM */;

        uf = new WeightedQuickUnionUF(size);
        openedState = new boolean[size];

        topIndex = N * N;
        bottomIndex = topIndex + 1 /* TOP */;
    }

    public boolean isFull(int row, int col) {
        int index = index(row, col);

        return uf.connected(topIndex, index);
    }

    public boolean isOpen(int row, int col) {
        int index = index(row, col);

        return openedState[index];
    }

    public boolean percolates() {
        return uf.connected(topIndex, bottomIndex);
    }

    public void open(int row, int col) {
        int neighbour;
        int index = index(row, col);

        openedState[index] = true;

        if (row > 1) {
            neighbour = index(row - 1, col);

            if (openedState[neighbour]) {
                uf.union(neighbour, index);
            }
        } else {
            uf.union(index, topIndex);
        }

        if (col > 1) {
            neighbour = index(row, col - 1);

            if (openedState[neighbour]) {
                uf.union(neighbour, index);
            }
        }

        if (row < N) {
            neighbour = index(row + 1, col);

            if (openedState[neighbour]) {
                uf.union(neighbour, index);
            }
        } else {
            uf.union(index, bottomIndex);
        }

        if (col < N) {
            neighbour = index(row, col + 1);

            if (openedState[neighbour]) {
                uf.union(neighbour, index);
            }
        }
    }

    private int index(int row, int col) {
        validate(row);
        validate(col);

        return (row - 1) * N + (col - 1);
    }

    private void validate(int i) {
        if (i < 1 || i > N) {
            throw new IndexOutOfBoundsException();
        }
    }
}
