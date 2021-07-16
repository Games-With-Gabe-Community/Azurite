package util;

import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Test;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class UtilsTest {

    final float minimalDelta = Float.MIN_VALUE;
    final float smallDelta = 0.0001f;

    @Test
    public void encode() {
        Assert.assertEquals(0b110100000000000, Utils.encode(new int[]{0, 1, 3}));
        Assert.assertEquals(0b000100000000000, Utils.encode(3));
        Assert.assertEquals(0b000000000000001, Utils.encode(14));
    }

    @Test
    public void copy() {
        Vector2f[] vectors = new Vector2f[3];
        vectors[0] = new Vector2f(1, 0);
        vectors[1] = new Vector2f(17, -4);
        vectors[2] = new Vector2f(-9, -5);
        Vector2f[] copy = Utils.copy(vectors);
        //not the same, but equals
        Assert.assertArrayEquals(vectors, copy);
        Assert.assertNotSame(vectors, copy);
        Assert.assertNotSame(vectors[0], copy[0]);
        Assert.assertNotSame(vectors[1], copy[1]);
        Assert.assertNotSame(vectors[2], copy[2]);
        Assert.assertEquals(vectors[0], copy[0]);
        Assert.assertEquals(vectors[1], copy[1]);
        Assert.assertEquals(vectors[2], copy[2]);
    }

    @Test
    public void map() {
        Assert.assertEquals(75, Utils.map(0.5f, 0, 1, 50, 100), 0.0001f);
        Assert.assertEquals(0, Utils.map(0.5f, 0, 1, -100, 100), 0.0001f);
    }

    @Test
    public void random() {
        double last = -1;
        for (int i = 0; i < 100; i++) {
            float random = Utils.random(0, 1);
            Assert.assertNotEquals(last, random, minimalDelta);
        }

        int lastInt = -1;
        //THIS TEST MIGHT FAIL TO RANDOMNESS - TRY AGAIN IN THIS CASE
        for (int i = 0; i < 100; i++) {
            int rand = Utils.randomInt(0, Integer.MAX_VALUE);
            Assert.assertNotEquals(lastInt, rand);
        }
    }

    @Test
    public void round() {
        for (int i = 0; i < 100; i++) {
            float random = Utils.random(0, 100);
            Assert.assertEquals(Math.round(random), Utils.round(random), minimalDelta);
        }
    }

    @Test
    public void dist() {
        for (int i = 0; i < 100; i++) {
            Vector2f a = new Vector2f(Utils.random(-100, 100), Utils.random(-100, 100));
            Vector2f b = new Vector2f(Utils.random(-100, 100), Utils.random(-100, 100));
            Assert.assertEquals(a.distance(b), Utils.dist(a, b), smallDelta);
            Assert.assertEquals(a.distance(b), Utils.dist(a.x, a.y, b.x, b.y), smallDelta);
        }
    }

    @Test
    public void constrain() {
        for (int i = 0; i < 10; i++) {
            int constrained = Utils.constrain(i, 3, 6);
            float constrainedF = Utils.constrain(i, 3.0f, 6.0f);
            if (i <= 3) {
                Assert.assertEquals(3, constrained);
                Assert.assertEquals(3.0f, constrainedF, minimalDelta);
            } else if (i < 6) {
                Assert.assertEquals(i, constrained);
                Assert.assertEquals(i, constrainedF, minimalDelta);
            } else {
                Assert.assertEquals(6, constrained);
                Assert.assertEquals(6.0f, constrainedF, minimalDelta);
            }
        }
    }

    @Test
    public void lerp() {
        Assert.assertEquals(0.5f, Utils.lerp(0, 1, 0.5f), minimalDelta);
    }

}