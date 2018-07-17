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

package net.aeronica.mods.bard_mania.client;

import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KeyHelper
{
    private KeyHelper() {/* NOP */}

    public static final int MIDI_NOTE_LOW = 48;
    public static final int MIDI_NOTE_HIGH = 72;
    public static final Integer[][] KEYNOTE_VALUES = new Integer[][]{
            {Keyboard.KEY_Z, 0}, {Keyboard.KEY_S, 1}, {Keyboard.KEY_X, 2}, {Keyboard.KEY_D, 3},
            {Keyboard.KEY_C, 4}, {Keyboard.KEY_V, 5}, {Keyboard.KEY_G, 6}, {Keyboard.KEY_B, 7},
            {Keyboard.KEY_H, 8}, {Keyboard.KEY_N, 9}, {Keyboard.KEY_J, 10}, {Keyboard.KEY_M, 11},
            {Keyboard.KEY_COMMA, 12},
            {Keyboard.KEY_Q, 12}, {Keyboard.KEY_2, 13}, {Keyboard.KEY_W, 14}, {Keyboard.KEY_3, 15},
            {Keyboard.KEY_E, 16}, {Keyboard.KEY_R, 17}, {Keyboard.KEY_5, 18}, {Keyboard.KEY_T, 19},
            {Keyboard.KEY_6, 20}, {Keyboard.KEY_Y, 21}, {Keyboard.KEY_7, 22}, {Keyboard.KEY_U, 23},
            {Keyboard.KEY_I, 24}};
    private static final Map<Integer, Integer> keyNoteMap;

    static
    {
        Map<Integer, Integer> aMap = new HashMap<>();
        for (Integer[] key : KeyHelper.KEYNOTE_VALUES)
        {
            aMap.put(key[0], key[1]+ KeyHelper.MIDI_NOTE_LOW);
        }
        keyNoteMap = Collections.unmodifiableMap(aMap);
    }

    public static boolean hasKey(int scanCode) {return keyNoteMap.containsKey(scanCode);}

    public static int getKey(int scanCode) {return keyNoteMap.get(scanCode);}

    public static boolean hasNote(int midiNote) {return keyNoteMap.containsValue(midiNote);}

    public static boolean isMidiNoteInRange(byte midiNote) {return (midiNote >= MIDI_NOTE_LOW) && (midiNote <= MIDI_NOTE_HIGH);}

    /**
     * Returns a zero based normalized note from a midi note. In vanilla note block context 0-24
     * @param noteIn
     * @return
     */
    public static int normalizeNote(int noteIn) {return Math.max(noteIn - KeyHelper.MIDI_NOTE_LOW, 0);}

    public static float calculatePitch(byte noteIn) {return (float) Math.pow(2.0D, (double) (normalizeNote(noteIn) - 12) / 12.0D);}
}
