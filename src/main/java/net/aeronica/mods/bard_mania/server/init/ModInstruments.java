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

package net.aeronica.mods.bard_mania.server.init;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.aeronica.mods.bard_mania.server.item.ItemAccessory;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModInstruments
{
    static final List<ItemInstrument> INSTRUMENTS = new ArrayList<>();

    public static final Item DRUM_STICK;
    public static final Item MALLET;

    static
    {
        Reader reader = new InputStreamReader(ModInstruments.class.getResourceAsStream("/assets/bard_mania/instruments.json"));
        JsonParser parser = new JsonParser();
        JsonArray elements = parser.parse(reader).getAsJsonArray();

        try
        {
            Gson gson = new Gson();
            for (JsonElement element : elements)
            {
                Instrument instrument = gson.fromJson(element, new TypeToken<Instrument>()
                {
                }.getType());
                if (!validateFields(instrument))
                {
                    if (instrument.id != null)
                    {
                        throw new NullPointerException("The instrument '" + instrument.id + "' is missing required attributes");
                    } else
                    {
                        throw new NullPointerException("Invalid instrument entry");
                    }
                }
                INSTRUMENTS.add(new ItemInstrument(instrument));
            }
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        DRUM_STICK = new ItemAccessory("drum_stick");
        MALLET = new ItemAccessory("mallet");
    }

    public static void register()
    {
        for (Item item : INSTRUMENTS)
        {
            register(item);
        }
        register(DRUM_STICK);
        register(MALLET);
    }

    private static void register(Item item)
    {
        ModItems.RegistrationHandler.add(item);
    }

    private static <T> boolean validateFields(@Nonnull T t) throws IllegalAccessException
    {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            if (field.getDeclaredAnnotation(Instrument.Optional.class) == null)
            {
                if (field.get(t) == null)
                {
                    return false;
                }

                if (!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
                {
                    return validateFields(field.get(t));
                }
            }
        }
        return true;
    }
}
