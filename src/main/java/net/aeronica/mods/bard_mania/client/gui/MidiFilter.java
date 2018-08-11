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

package net.aeronica.mods.bard_mania.client.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MidiFilter extends FileFilter
{
    private final static String mid = "mid";
    private final static String midi = "midi";

    @Override
    public boolean accept(File f)
    {
        if (f.isDirectory()) { return true; }

        String extension = getExtension(f);
        if (extension != null)
        {
            return extension.equals(midi) ||
                    extension.equals(mid);
        }
        return false;
    }

    @Override
    public String getDescription()
    {
        return "*.midi, *.mid";
    }

    private static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
