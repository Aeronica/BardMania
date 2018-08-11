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

public class MidiChooser extends JFrame
{
    private static final int WIDTH = 200;
    private static final int HEIGHT = 75;

    private JFileChooser chooser;
    JPanel panel = new JPanel();

    MidiChooser(ISelectorAction action)
    {
        super("");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        chooser = new JFileChooser()
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

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setAlwaysOnTop(true);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            action.select(chooser.getSelectedFile());
            dispose();
        }
    }
}
