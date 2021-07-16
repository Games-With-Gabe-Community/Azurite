package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 21.06.2021
 * @since 21.06.2021
 */
public class RayCastResult {

    private final Vector2f point;
    private final Vector2f normal;
    private final Vector2f strike;
    private final float strikeLength;
    private final boolean hit;

    /**
     * Create a RayCastResult, where the target was not hit.
     */
    public RayCastResult() {
        this.point = null;
        this.normal = null;
        this.strike = null;
        this.strikeLength = 0;
        this.hit = false;
    }

    /**
     * Create a successful raycast result with details collision information.
     *
     * @param point        the point, where the raycast hit
     * @param normal       the normal on the face of the object that got hit
     * @param strike       the vector from raycast start to the point
     * @param strikeLength the distance from the raycast start to the point
     */
    public RayCastResult(Vector2f point, Vector2f normal, Vector2f strike, float strikeLength) {
        this.point = point;
        this.normal = normal;
        this.strike = strike;
        this.strikeLength = strikeLength;
        this.hit = true;
    }

    /**
     * @see this#strikeLength
     */
    public float getStrikeLength() {
        return strikeLength;
    }

    /**
     * @see this#normal
     */
    public Vector2f getNormal() {
        return normal;
    }

    /**
     * @see this#point
     */
    public Vector2f getPoint() {
        return point;
    }

    /**
     * @see this#strike
     */
    public Vector2f getStrike() {
        return strike;
    }

    /**
     * @see this#hit
     */
    public boolean didHit() {
        return hit;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/