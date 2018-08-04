/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bard_mania.server.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Instrument
{
    public String id;
    public General general = new General();
    public Translations equipped_first_person = new Translations();
    public Translations equipped_third_person = new Translations();
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

    public static class Translations
    {
        @Optional public float[] translation = {0f, 0f, 0f};
        @Optional public float[] rotation = {0f ,0f ,0f};
        @Optional public float[] scale = {1f, 1f, 1f};
    }

    public static class Sounds
    {
        public String timbre;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Optional {}
}
