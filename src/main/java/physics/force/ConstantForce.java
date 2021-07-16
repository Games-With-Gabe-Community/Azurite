package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 21.06.2021
 * @since 21.06.2021
 */
public class ConstantForce implements Force {

    private final Vector2f direction;
    private final String identifier;

    public ConstantForce(String identifier, Vector2f direction) {
        this.direction = direction;
        this.identifier = identifier;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public boolean update(float dt) {
        return false;
    }

    @Override
    public Vector2f direction() {
        return direction;
    }

}