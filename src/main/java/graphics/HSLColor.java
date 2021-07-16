package graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;

/**
 * <h1>Azurite</h1>
 * Represents a HSLA color.
 *
 * @author Juyas
 * @version 14.07.2021
 * @since 14.07.2021
 */
public class HSLColor {

    public static final int HUE = 0;
    public static final int SATURATION = 1;
    public static final int LUMINANCE = 2;
    public static final int ALPHA = 3;

    private final float[] components;
    private float alpha;

    /**
     * Create a HSLColor.
     *
     * @param h the hue ranging from 0 to 360, values outside get smoothly fit into the range
     * @param s the saturation ranging from 0 to 100, values are limited to this range
     * @param l the luminance ranging from 0 to 100, values are limited to this range
     * @param a the alpha ranging from 0 to 1, values are limited to this range
     */
    public HSLColor(float h, float s, float l, float a) {
        this.components = new float[3];
        this.alpha = Math.max(0.0f, Math.min(a, 1.0f));
        this.components[0] = h >= 0 ? h % 360 : (h % 360) + 360;
        this.components[1] = Math.max(0.0f, Math.min(s, 100.0f));
        this.components[2] = Math.max(0.0f, Math.min(l, 100.0f));
    }

    public static HSLColor getHSLColor(float x, float y, float z, float a, int inputType) {
        switch (inputType) {
            case Color.TYPE_RGBA:
                return new Color(x, y, z, a).toHSLColor();
            case Color.TYPE_HSLA:
            default:
                return new HSLColor(x, y, z, a);
        }
    }

    /**
     * Convert this {@link HSLColor} to a RGB {@link Color} using {@link HSLColor#toRGBA(float[])}
     *
     * @return a RGB {@link Color} representing the same color
     */
    public Color toRGBColor() {
        float[] floats = toRGBA(components);
        return new Color(floats[0] * 255, floats[1] * 255, floats[2] * 255, alpha * 255);
    }

    /**
     * Convert this {@link HSLColor} to a normalized RGB {@link Color} using {@link HSLColor#toRGBA(float[])}
     *
     * @return a normalized RGB {@link Color} representing the same color
     */
    public Color toNormalizedRGBColor() {
        float[] floats = toRGBA(components);
        return new Color(floats[0], floats[1], floats[2], alpha);
    }

    /**
     * Set a component of this color.
     *
     * @param component the component from 0-3.
     *                  Named representations are {@link #HUE}, {@link #SATURATION}, {@link #LUMINANCE} and {@link #ALPHA}
     * @param newValue  the new value for the component
     */
    public void set(int component, float newValue) {
        if (component == ALPHA)
            this.alpha = newValue;
        this.components[component] = newValue;
    }

    /**
     * Get a component of this color.
     *
     * @param component the component from 0-3.
     *                  Named representations are {@link #HUE}, {@link #SATURATION}, {@link #LUMINANCE} and {@link #ALPHA}
     * @return the value of the component
     */
    public float get(int component) {
        if (component == ALPHA) return alpha;
        return components[component];
    }

    /**
     * Create a {@link Vector3f} containing the HSL values.
     *
     * @return a vector containing (h,s,l)
     */
    public Vector3f toHSLVector() {
        return new Vector3f(components[0], components[1], components[2]);
    }

    /**
     * Create a {@link Vector4f} containing the HSLA values.
     *
     * @return a vector containing (h,s,l,a)
     */
    public Vector4f toHSLAVector() {
        return new Vector4f(components[0], components[1], components[2], alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HSLColor hslColor = (HSLColor) o;
        if (Float.compare(hslColor.alpha, alpha) != 0) return false;
        return Arrays.equals(components, hslColor.components);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(components);
        result = 31 * result + (alpha != +0.0f ? Float.floatToIntBits(alpha) : 0);
        return result;
    }

    /**
     * Convert a RGBA color to it corresponding HSLA color.
     * RGBA ranges are [0-1,0-1,0-1,0-1].
     * HSLA output ranges are [0-360,0-100,0-100,0-1].
     *
     * @param rgba a float array containing [r,g,b,a], where a is an optional parameter
     * @return normalized hsla array [h,s,l,a]
     */
    public static float[] toHSLA(float[] rgba) {
        //normalized RGB values
        float r = rgba[0];
        float g = rgba[1];
        float b = rgba[2];
        //minimum and maximum RGB values are used in the HSL calculations
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        //calculate the hue radiant
        float h = 0;
        if (max == min)
            h = 0;
        else if (max == r)
            h = ((60 * (g - b) / (max - min)) + 360) % 360;
        else if (max == g)
            h = (60 * (b - r) / (max - min)) + 120;
        else if (max == b)
            h = (60 * (r - g) / (max - min)) + 240;
        //calculate the luminance
        float l = (max + min) / 2;
        //calculate the saturation
        float s = 0;
        if (max == min)
            s = 0;
        else if (l <= .5f)
            s = (max - min) / (max + min);
        else
            s = (max - min) / (2 - max - min);
        //take alpha
        if (rgba.length == 3)
            return new float[]{h, s * 100, l * 100};
        else
            return new float[]{h, s * 100, l * 100, rgba[3]};
    }

    /**
     * Convert a HSLA color to RGBA.
     * HSLA ranges are [0-360,0-100,0-100,0-1].
     * RGBA output ranges are [0-1,0-1,0-1,0-1].
     * If there is no alpha in the input, there won't be an alpha in the output as well.
     *
     * @param hsla a float array containing [h,s,l,a], where a is an optional parameter
     * @return normalized rgba array [r,g,b,a]
     */
    public static float[] toRGBA(float[] hsla) {
        float[] rgba = new float[hsla.length];
        //alpha is untouched if present
        if (hsla.length >= 4)
            rgba[3] = hsla[3];
        //map all values to a range 0-1
        float hue = hsla[0] % 360.0f;
        hue = hue / 360.0f;
        float sat = hsla[1] / 100.0f;
        float lum = hsla[2] / 100.0f;
        //helper values
        float q = 0;
        if (lum < 0.5) q = lum * (1 + sat);
        else q = (lum + sat) - (sat * lum);
        float p = 2 * lum - q;
        //third to shift hue for rgb
        float third = 1.0f / 3.0f;
        //calculate rgb according to parts in hue and with luminance and saturation
        //r
        rgba[0] = Math.min(1.0f, Math.max(0.0f, hueRGBTransform(p, q, hue + third)));
        //g
        rgba[1] = Math.min(1.0f, Math.max(0.0f, hueRGBTransform(p, q, hue)));
        //b
        rgba[2] = Math.min(1.0f, Math.max(0.0f, hueRGBTransform(p, q, hue - third)));
        return rgba;
    }

    private static float hueRGBTransform(float p, float q, float h) {
        if (h < 0) h += 1;
        if (h > 1) h -= 1;
        if (6 * h < 1) return p + ((q - p) * 6 * h);
        if (2 * h < 1) return q;
        if (3 * h < 2) return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        return p;
    }

}