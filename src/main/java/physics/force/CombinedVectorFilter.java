package physics.force;

import org.joml.Vector2f;

import java.util.Collection;
import java.util.HashMap;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 05.07.2021
 * @since 05.07.2021
 */
public class CombinedVectorFilter implements VectorFilter {

    private final HashMap<Integer, VectorFilter> filters = new HashMap<>();

    @Override
    public int id() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Vector2f filter(Vector2f vector) {
        Collection<VectorFilter> values = filters.values();
        for (VectorFilter forceFilter : values) {
            vector = forceFilter.filter(vector);
            if (forceFilter.isInvalid())
                removeFilters(forceFilter.id());
        }
        return vector;
    }

    @Override
    public boolean isInvalid() {
        return filters.isEmpty();
    }

    public void addFilter(VectorFilter forceFilter) {
        if (this.filters.containsKey(forceFilter.id()))
            this.filters.put(forceFilter.id(), this.filters.get(forceFilter.id()).andThen(forceFilter));
        else this.filters.put(forceFilter.id(), forceFilter);
    }

    public void removeFilters(int id) {
        this.filters.remove(id);
    }

}