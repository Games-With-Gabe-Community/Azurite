package event;

import java.util.LinkedList;
import java.util.List;

/**
 * A class which can handle Event listeners. Automatically calls all listeners when event occurs
 *
 * @param <T> Type of Event Data this event is for
 */
public class EventNode<T extends EventData> {
    /**
     * The listeners that are subscribed to this event
     */
    private List<EventListener<T>> listeners;

    /**
     * A default constructor
     */
    public EventNode() {
        listeners = new LinkedList<>();
    }

    /**
     * Add a function to the listeners list. The function will be called when the event occurs
     *
     * @param listener The function that is to be called when the event occurs
     */
    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Notifies the node that an event has occurred. Notifies all listeners
     *
     * @param t the data that is to be given to all the listeners.
     */
    public void onEvent(T t) {
        listeners.forEach(listener -> listener.onEvent(t));
    }
}
