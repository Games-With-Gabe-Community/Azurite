package graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 15.07.2021
 * @since 15.07.2021
 */
public class HSLColorTest {

    HSLColor color1 = new HSLColor(200, 50, 50, 1); //rgb: 63,148,191
    HSLColor color2 = new HSLColor(100, 25, 75, 1); //rgb: 186,207,175
    HSLColor color3 = new HSLColor(150, 10, 25, 1); //rgb: 57,70,64

    float[] hslcolor1 = new float[]{200, 50, 50, 1};
    float[] hslcolor2 = new float[]{100, 25, 75, 1};
    float[] hslcolor3 = new float[]{150, 10, 25, 1};

    float[] rgacolor1 = new float[]{63, 148, 191, 255};
    float[] rgacolor2 = new float[]{186, 207, 175, 255};
    float[] rgacolor3 = new float[]{57, 70, 64, 255};

    float[] rgacolor1N = normalizedRGB(rgacolor1);
    float[] rgacolor2N = normalizedRGB(rgacolor2);
    float[] rgacolor3N = normalizedRGB(rgacolor3);

    //3% off is fine
    final double deltaAccepted = 0.03;

    private double delta(double a) {
        return a * deltaAccepted;
    }

    private float[] normalizedRGB(float[] color) {
        float[] floats = Arrays.copyOf(color, 4);
        for (int i = 0; i < 4; i++)
            floats[i] = floats[i] / 255;
        return floats;
    }

    @Test
    public void getHSLColor() {
        HSLColor c1 = HSLColor.getHSLColor(hslcolor1[0], hslcolor1[1], hslcolor1[2], hslcolor1[3], Color.TYPE_HSLA);
        HSLColor c2 = HSLColor.getHSLColor(hslcolor2[0], hslcolor2[1], hslcolor2[2], hslcolor2[3], Color.TYPE_HSLA);
        HSLColor c3 = HSLColor.getHSLColor(hslcolor3[0], hslcolor3[1], hslcolor3[2], hslcolor3[3], Color.TYPE_HSLA);

        Assert.assertEquals(color1, c1);
        Assert.assertEquals(color2, c2);
        Assert.assertEquals(color3, c3);
    }

    @Test
    public void setAndGet() {
        HSLColor hslColor = new HSLColor(hslcolor1[0], hslcolor1[1], hslcolor1[2], hslcolor1[3]);
        hslColor.set(HSLColor.LUMINANCE, 80);
        Assert.assertEquals(80, hslColor.get(HSLColor.LUMINANCE), delta(80));
    }

    @Test
    public void toHSLVector() {
        Assert.assertEquals(new Vector3f(hslcolor1), color1.toHSLVector());
        Assert.assertEquals(new Vector3f(hslcolor2), color2.toHSLVector());
        Assert.assertEquals(new Vector3f(hslcolor3), color3.toHSLVector());
    }

    @Test
    public void toHSLAVector() {
        Assert.assertEquals(new Vector4f(hslcolor1), color1.toHSLAVector());
        Assert.assertEquals(new Vector4f(hslcolor2), color2.toHSLAVector());
        Assert.assertEquals(new Vector4f(hslcolor3), color3.toHSLAVector());
    }

    @Test
    public void toHSLA() {
        float[] hsla1 = HSLColor.toHSLA(rgacolor1N);
        float[] hsla2 = HSLColor.toHSLA(rgacolor2N);
        float[] hsla3 = HSLColor.toHSLA(rgacolor3N);
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(color1.get(i), hsla1[i], delta(color1.get(i)));
            Assert.assertEquals(color2.get(i), hsla2[i], delta(color2.get(i)));
            Assert.assertEquals(color3.get(i), hsla3[i], delta(color3.get(i)));
        }
    }

    @Test
    public void toRGBA() {
        float[] rgba1 = HSLColor.toRGBA(hslcolor1);
        float[] rgba2 = HSLColor.toRGBA(hslcolor2);
        float[] rgba3 = HSLColor.toRGBA(hslcolor3);
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(rgacolor1[i], rgba1[i] * 255, delta(rgacolor1[i]));
            Assert.assertEquals(rgacolor2[i], rgba2[i] * 255, delta(rgacolor2[i]));
            Assert.assertEquals(rgacolor3[i], rgba3[i] * 255, delta(rgacolor3[i]));
        }
    }
}