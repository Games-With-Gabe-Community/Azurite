package physics.collision.shape;

import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import physics.collision.RayCastResult;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class CircleTest {

    Circle small;
    Circle big;

    @Before
    public void setUp() throws Exception {
        small = new Circle(new Vector2f(0, 0), 5);
        big = new Circle(new Vector2f(0, 0), 500);
        //so that they should intersection at a small point
        small.setPosition(505, 0);
        big.setPosition(0, 0);
    }

    @Test
    public void supportPoint() {
        Assert.assertEquals(new Vector2f(505, 5), small.supportPoint(new Vector2f(0, 1)));
        Assert.assertEquals(new Vector2f(505, -5), small.supportPoint(new Vector2f(0, -1)));
        Assert.assertEquals(new Vector2f(510, 0), small.supportPoint(new Vector2f(1, 0)));
        Assert.assertEquals(new Vector2f(500, 0), small.supportPoint(new Vector2f(-1, 0)));

        Assert.assertEquals(new Vector2f(0, 500), big.supportPoint(new Vector2f(0, 1)));
        Assert.assertEquals(new Vector2f(0, -500), big.supportPoint(new Vector2f(0, -1)));
        Assert.assertEquals(new Vector2f(500, 0), big.supportPoint(new Vector2f(1, 0)));
        Assert.assertEquals(new Vector2f(-500, 0), big.supportPoint(new Vector2f(-1, 0)));
    }

    @Test
    public void intersection() {
        Assert.assertTrue(small.intersection(big));
        Assert.assertTrue(big.intersection(small));
        small.setPosition(505, 1);
        Assert.assertFalse(small.intersection(big));
        Assert.assertFalse(big.intersection(small));
        small.setPosition(505, 0);
    }

    @Test
    public void rayCast() {
        RayCastResult rayCastResult = small.rayCast(new Vector2f(515, 0), new Vector2f(-1, 0), 5f);
        RayCastResult rayCastResult2 = small.rayCast(new Vector2f(515, 0), new Vector2f(-1, 0), 4.99f);
        Assert.assertTrue(rayCastResult.didHit());
        Assert.assertFalse(rayCastResult2.didHit());
        Assert.assertEquals(new Vector2f(510, 0), rayCastResult.getPoint());
        Assert.assertEquals(5, rayCastResult.getStrikeLength(), 0.001);
    }

}