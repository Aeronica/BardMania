package net.aeronica.mods.bardmania.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Instrument {
    public String id;
    public General general = new General();
    public Sounds sounds;

    public static class General
    {
        @Optional public boolean wearable = false;
        /*
         * Only useful when wearable = true
         * For held items like mallets, drum sticks, etc.
         */
        @Optional public AccessoryType leftHand = AccessoryType.EMPTY;
        @Optional public AccessoryType rightHand = AccessoryType.EMPTY;
    }

    public static class Sounds
    {
        public String timbre;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Optional {}
}
