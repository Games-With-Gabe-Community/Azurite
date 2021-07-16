package util;

/**
 * <h1>Azurite</h1>
 * A Class describing a tupel of none, one or multiple elements of the same type.
 * An extension to an array that uses {@link Pair} and {@link Triple}.
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 05.07.2021
 */
public class Tuple<Type> {

    private final Type[] content;

    /**
     * Create a tupel representing a {@link Pair} of two values.
     *
     * @param data the pair containing two values
     */
    public Tuple(Pair<Type, Type> data) {
        this(data.getLeft(), data.getRight());
    }

    /**
     * Create a tupel representing a {@link Triple} of three values.
     *
     * @param data the triple containing three values
     */
    public Tuple(Triple<Type, Type, Type> data) {
        this(data.getLeft(), data.getMiddle(), data.getRight());
    }

    /**
     * Create a tupel of n values.
     *
     * @param content n values for the tupel
     */
    @SafeVarargs
    public Tuple(Type... content) {
        this.content = content;
    }

    /**
     * The n-th value of the tupel.
     *
     * @param n the index inside the tupel starting at 0
     * @return the value inside the tupel at position n
     * @throws IndexOutOfBoundsException if there is no element with the given index
     */
    public Type getN(int n) {
        return content[n];
    }

    /**
     * Set element n in the tupel.
     *
     * @param n    the index inside the tupel starting at 0
     * @param data the new value for inserting into the tupel
     * @throws IndexOutOfBoundsException if there is no position with the given index
     */
    public void setN(int n, Type data) {
        this.content[n] = data;
    }

    /**
     * All contents inside the tupel.
     *
     * @return all contents
     * @see #content
     */
    public Type[] getContent() {
        return content;
    }

    /**
     * Receive a {@link Pair} from a set n and m position.
     *
     * @param n the first position
     * @param m the second position
     * @return a pair(n,m) with elements of the tupel at the specified indices
     * @throws IndexOutOfBoundsException if there is no position with the given index
     * @see #getN(int)
     */
    public Pair<Type, Type> getPair(int n, int m) {
        return new Pair<>(getN(n), getN(m));
    }

    /**
     * Receive a {@link Triple} from a set n and m position.
     *
     * @param o the first position
     * @param n the second position
     * @param m the third position
     * @return a triple(o,n,m) with elements of the tupel at the specified indices
     * @throws IndexOutOfBoundsException if there is no position with the given index
     * @see #getN(int)
     */
    public Triple<Type, Type, Type> getTriple(int o, int n, int m) {
        return new Triple<>(getN(o), getN(n), getN(m));
    }

}