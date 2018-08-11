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

import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChannelSelectors
{
    private List<GuiCheckBox> guiCheckBoxes = new ArrayList<>();
    private int id;
    private int xPos;
    private int yPos;
    private Set<Integer> channels = new HashSet<>();

    public ChannelSelectors(int id, int xPos, int yPos)
    {
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        initialize();
    }

    private void initialize()
    {
        guiCheckBoxes.clear();
        int x = 0;
        // first row
        for(int i = 0; i < 8; i++)
        {
            guiCheckBoxes.add(new GuiCheckBox(i, xPos + (x++ * 30), yPos + 22, Integer.toString(i+1), true));
        }
        x = 0;
        // second row
        for(int i = 8; i < 16; i++)
        {
            guiCheckBoxes.add(new GuiCheckBox(i, xPos + (x++ * 30), yPos + 32, Integer.toString(i+1), true));
        }
    }

    public List<GuiCheckBox> getCheckBoxes()
    {
        return guiCheckBoxes;
    }

    public Set<Integer> getChannels()
    {
        channels.clear();
        for (GuiCheckBox guiCheckBox: guiCheckBoxes)
        {
            if (guiCheckBox.isChecked())
                channels.add(guiCheckBox.id);
        }
        return channels;
    }

    public void setChannels(Set<Integer> channelsIn)
    {
        channels.clear();
        for (GuiCheckBox guiCheckBox: guiCheckBoxes)
        {
            if (channelsIn.contains(guiCheckBox.id))
            {
                guiCheckBox.setIsChecked(true);
                channels.add(guiCheckBox.id);
            }
            else
                guiCheckBox.setIsChecked(false);
        }
    }

    public void setAll(boolean state)
    {
        channels.clear();
        for (GuiCheckBox guiCheckBox: guiCheckBoxes)
        {
            guiCheckBox.setIsChecked(state);
            if (state) channels.add(guiCheckBox.id);
        }
    }
}
