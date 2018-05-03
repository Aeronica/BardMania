package net.aeronica.mods.bardmania.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Instrument {
    public String id;
    public Sounds sounds;

    public static class General
    {
        @Optional public boolean wearable = false;
        public HoldType holdType;
    }

    public static class Sounds
    {
        public String timbre;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Optional
    {

    }
}
