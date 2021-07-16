package util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class OrderPreservingListTest {

    int[] data;

    @Before
    public void setUp() {
        data = new int[20];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
        //swap randomly
        for (int i = 0; i < 1000; i++) {
            int d = data[i % data.length];
            int o = (int) (Math.random() * 20);
            data[i%data.length] = data[o];
            data[o] = d;
        }
    }

    @Test
    public void testList() {
        OrderPreservingList<Integer> list = new OrderPreservingList<>();
        for (int value : data) {
            list.add(value);
            Assert.assertTrue(isSorted(list));
        }
    }

    private boolean isSorted(List<Integer> list) {
        if (list.isEmpty() || list.size() == 1) return true;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) return false;
        }
        return true;
    }

}