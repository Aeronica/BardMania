package net.aeronica.dorkbox.tweenEngine;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public
class EngineBuilder {

    // by default, we are thread-safe
    private boolean threadSafe = true;

    private Map<Class<?>, TweenAccessor<?>> registeredAccessors = new HashMap<Class<?>, TweenAccessor<?>>();

    private int combinedAttrsLimit = 3;
    private int waypointsLimit = 0;


    EngineBuilder() {}

    public
    TweenEngine build() {
        if (threadSafe) {
            return new TweenEngine(threadSafe, combinedAttrsLimit, waypointsLimit, registeredAccessors);
        } else {
            // skip the volatile field access for faster performance, but NO THREAD VISIBILITY
            return new TweenEngine(threadSafe, combinedAttrsLimit, waypointsLimit, registeredAccessors) {
                @Override
                long flushRead() {
                    return 0;
                }

                @Override
                void flushWrite() {
                }
            };
        }
    }


    /**
     * By default, all Tweens/Timelines are thread safe for VISIBILITY. They are not thread-safe for concurrency, meaning there will be
     * race conditions if one tries to modify/read something from different threads.
     *
     * What is read/written will always be correct, but is not protected against concurrent modification.
     */
    public
    EngineBuilder unsafe() {
        this.threadSafe = false;
        return this;
    }

    /**
     * <b>Must be called **before** Tweens are created</b>
     * <p>
     * Changes the limit for combined attributes. Defaults to 3 to reduce memory footprint.
     */
    public
    EngineBuilder setCombinedAttributesLimit(final int limit) {
        combinedAttrsLimit = limit;
        return this;
    }

    /**
     * <b>Must be called **before** Tweens are created</b>
     * <p>
     * Changes the limit of allowed waypoints for each tween. Defaults to 0 to reduce memory footprint.
     */
    public
    EngineBuilder setWaypointsLimit(final int limit) {
        waypointsLimit = limit;
        return this;
    }

    /**
     * Registers an accessor with the class of an object. This accessor will be used by tweens applied to every objects implementing the
     * registered class, or inheriting from it.
     *
     * @param someClass An object class.
     * @param defaultAccessor The accessor that will be used to tween any object of class "someClass".
     */
    public
    EngineBuilder registerAccessor(final Class<?> someClass, final TweenAccessor<?> defaultAccessor) {
        registeredAccessors.put(someClass, defaultAccessor);
        return this;
    }

    public
    int getCombinedAttrsLimit() {
        return combinedAttrsLimit;
    }

    public
    int getWaypointsLimit() {
        return waypointsLimit;
    }
}
