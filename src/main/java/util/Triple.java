package util;

/**
 * <h1>Azurite</h1>
 * A {@link Triple} is a tupel of three different values with potentially three different types.
 * If all types are identical, its worth considering {@link Tuple} instead.
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 25.06.2021
 */
public class Triple<L, M, R> {

    private L left;
    private M middle;
    private R right;

    /**
     * Creates a triple of three values.
     *
     * @param left   first value
     * @param middle second value
     * @param right  third value
     */
    public Triple(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    /**
     * Creates a Triple with no values.
     */
    public Triple() {
    }

    /**
     * @see #left
     */
    public L getLeft() {
        return left;
    }

    /**
     * @see #middle
     */
    public M getMiddle() {
        return middle;
    }

    /**
     * @see #right
     */
    public R getRight() {
        return right;
    }

    /**
     * @see #left
     */
    public void setLeft(L left) {
        this.left = left;
    }

    /**
     * @see #middle
     */
    public void setMiddle(M middle) {
        this.middle = middle;
    }

    /**
     * @see #right
     */
    public void setRight(R right) {
        this.right = right;
    }

}