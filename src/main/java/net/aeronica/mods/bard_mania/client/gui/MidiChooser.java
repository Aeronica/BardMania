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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * <p>Based on the FileSelector class from MineTunes by Vazkii</p>
 * <p>https://github.com/Vazkii/MineTunes</p>
 * <p><a ref="https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB">
 * Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0)</a></p>
 */
public class MidiChooser extends JFrame
{
    private static Preferences preferences = Preferences.userNodeForPackage(MidiChooser.class);

    MidiChooser(ISelectorAction action)
    {
        super("");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JFileChooser chooser = new JFileChooser()
        {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException
            {
                JDialog dialog = super.createDialog(parent);
                dialog.setLocationByPlatform(true);
                dialog.setAlwaysOnTop(true);
                return dialog;
            }
        };

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new MidiFilter());
        chooser.setCurrentDirectory(loadPreferences(chooser));

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setAlwaysOnTop(true);

        // set Details view
        Action details = chooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            savePreferences(chooser);
            action.select(chooser.getSelectedFile());
            dispose();
        }
    }

    private void savePreferences(JFileChooser chooser)
    {
        preferences.put("path", chooser.getSelectedFile().getParent());
    }

    private File loadPreferences(JFileChooser chooser)
    {
        return new File(preferences.get("path", chooser.getCurrentDirectory().getParent()));
    }
}
