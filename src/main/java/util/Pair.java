package util;

/**
 * <h1>Azurite</h1>
 * A {@link Pair} is a tupel of two different values with potentially two different types.
 * If both types are identical, its worth considering {@link Tuple} instead.
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 25.06.2021
 */
public class Pair<L, R> {

    private L left;
    private R right;

    /**
     * Create a {@link Pair} of two values.
     *
     * @param left  first value
     * @param right second value
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Create a {@link Pair} with no values.
     */
    public Pair() {
    }

    /**
     * @see #left
     */
    public L getLeft() {
        return left;
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
     * @see #right
     */
    public void setRight(R right) {
        this.right = right;
    }

    /**
     * Extends this {@link Pair} with a third value to a triple.
     *
     * @see Triple
     */
    public <RR> Triple<L, R, RR> extend(RR right) {
        return new Triple<>(getLeft(), getRight(), right);
    }

}