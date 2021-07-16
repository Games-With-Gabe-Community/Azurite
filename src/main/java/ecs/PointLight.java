package ecs;

import graphics.Color;
import org.joml.Vector3f;
import physics.Transform;
import physics.TransformSensitive;

/**
 * A Point Light Component.
 *
 * @author VoxelRifts
 */
public class PointLight extends Component implements TransformSensitive {
    /**
     * Colour of the light
     */
    public Vector3f color;

    /**
     * Intensity of the light
     * It controls how far the light's attenuation will reach
     */
    public float intensity;

    /**
     * Transform of the parent GameObject to get the position
     */
    public Transform lastTransform;

    /**
     * Constructor which sets color of the light by default to white.
     *
     * @param intensity float: Intensity of the light
     */
    public PointLight(float intensity) {
        this(Color.WHITE, intensity);
    }

    /**
     * @param color     Vector3f: Color of the light
     * @param intensity float: Intensity of the light
     */
    public PointLight(Color color, float intensity) {
        this.color = color.toNormalizedVec3f();
        this.intensity = intensity;
        this.order = SpriteRenderer.ORDER + 1;
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.getReadOnlyTransform();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void update(Transform changedTransform) {
        //update the lastTransform
        this.gameObject.getReadOnlyTransform().copy(this.lastTransform);
    }

    @Override
    public boolean transformingObject() {
        return false;
    }
}
