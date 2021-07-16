package physics.collision.shape;

import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class PointTest {

    @Test
    public void supportPoint() {
        //test random 100 different points, if the support point is always identical
        Random random = new Random(123456789);
        for (int i = 0; i < 100; i++) {
            float f1 = random.nextFloat() * 500 - 250;
            float f2 = random.nextFloat() * 500 - 250;
            float f3 = random.nextFloat() * 500 - 250;
            float f4 = random.nextFloat() * 500 - 250;
            Vector2f point = new Vector2f(f1, f2);
            Vector2f pos = new Vector2f(f3, f4);
            Point p = new Point(point);
            p.setPosition(pos);
            Assert.assertEquals(point.add(pos, new Vector2f()), p.supportPoint(new Vector2f(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)));
        }
    }

    @Test
    public void generalizedTest() {
        Assert.assertEquals(ShapeType.POINT, new Point(new Vector2f(0, 0)).type());
        Assert.assertEquals(1, new Point(new Vector2f(0, 0)).vertices());
        Assert.assertEquals(1, new Point(new Vector2f(0, 0)).faces().length);
    }

}