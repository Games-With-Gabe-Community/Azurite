package util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <h1>Azurite</h1>
 * <p>
 * A LinkedList override to create an order-preserving list to improve speed in a special use case: <br>
 * You want to remove and add object from/to a list and
 * keep the elements in the list sorted without having to re-sort each time.
 *
 * @author Juyas
 * @version 18.06.2021
 * @since 18.06.2021
 */
public class OrderPreservingList<T extends Comparable<T>> extends ArrayList<T> {


    public OrderPreservingList(Collection<T> data) {
        super();
        addAll(data);
    }

    public OrderPreservingList() {
        super();
    }

    //find the next matching index for a given obj by its natural order (small to big)
    //if the obj is bigger than any other obj in the list, just add the new obj at the end i=size()
    private int findIndexToAdd(T obj) {
        //artificial limit, in general higher length increase cost of iterative solution
        if (size() > 10) {
            int i = binarySearch(obj);
            if (get(i).compareTo(obj) < 0) i++;
            return i;
        }
        for (int i = 0; i < size(); i++) {
            if (get(i).compareTo(obj) > 0)
                return i;
        }
        return size();
    }

    //uses binary search to find the first greater value
    private int binarySearch(T obj) {
        int low = 0;
        int high = size() - 1;

        int mid = 0;
        while (low <= high) {
            mid = (low + high) >>> 1;
            T midVal = get(mid);
            if (midVal.compareTo(obj) < 0)
                low = mid + 1;
            else if (midVal.compareTo(obj) > 0)
                high = mid - 1;
            else
                return mid;
        }
        return mid;
    }

    @Override
    public boolean add(T t) {
        int indexToAdd = findIndexToAdd(t);
        super.add(indexToAdd, t);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(c);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        add(element);
    }


}