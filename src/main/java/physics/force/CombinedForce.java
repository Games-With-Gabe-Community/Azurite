package physics.force;

import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 21.06.2021
 * @since 21.06.2021
 */
public class CombinedForce implements Force {

    private final Set<Force> subForces;
    private Vector2f lastForce;
    private boolean needToUpdate;
    private String identifier;
    private float mass;

    public CombinedForce(String identifier) {
        this.identifier = identifier;
        this.subForces = new HashSet<>();
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public boolean update(float dt) {
        boolean updated = subForces.stream().map(force -> force.update(dt)).reduce(Boolean::logicalOr).orElse(false);
        return this.needToUpdate = updated || this.needToUpdate;
    }

    @Override
    public Vector2f direction() {
        if (needToUpdate) {
            this.needToUpdate = false;
            return lastForce = subForces.stream().map(Force::direction).reduce((vec1, vec2) -> vec1.add(vec2, new Vector2f())).orElse(new Vector2f(0, 0)).div(mass);
        } else return lastForce;
    }

    public void setMass(float mass) {
        this.mass = mass;
        this.needToUpdate = true;
    }

    public void applyForce(Force force) {
        this.subForces.add(force);
    }

    public void removeForces(String identifier) {
        this.subForces.removeIf(force -> force.identifier().equals(identifier));
    }

}