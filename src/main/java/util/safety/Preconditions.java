package util.safety;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 18.06.2021
 * @since 18.06.2021
 */
public final class Preconditions {

    private static final long mainThreadID = Thread.currentThread().getId();

    /**
     * Test if the current call is executed in the main thread.
     * This is especially necessary if you run a mac, since mac doesnt like OpenGl calls in ANY thread except the first one.
     * This method does NOT provide a valid exception if the whole program wasnt launched with VM argument -XstartOnFirstThread,
     * because only with this argument, its guaranteed that the main thread is actually is the first.
     */
    public static void ensureMainThread(String location) {
        if (Thread.currentThread().getId() != mainThreadID)
            throw new IllegalThreadStateException(location + ": This code is supposed to run in main thread.");
    }

    /**
     * Improve code stability by doing non-null checks inline.
     *
     * @param obj any object expected to be non-null
     * @param <T> any type
     * @return the object, if and only if its not null. throws a {@link NullPointerException} otherwise.
     */
    public static <T> T nonNull(T obj) {
        if (obj == null)
            throw new NullPointerException("a non-null object is null");
        return obj;
    }

    /**
     * Improve code stability by doing non-null checks inline.
     *
     * @param obj  any object expected to be non-null
     * @param name the name of the object to improve locating process
     * @param <T>  any type
     * @return the object, if and only if its not null. throws a {@link NullPointerException} otherwise.
     */
    public static <T> T nonNull(String name, T obj) {
        if (obj == null)
            throw new NullPointerException(name + " is null");
        return obj;
    }

}