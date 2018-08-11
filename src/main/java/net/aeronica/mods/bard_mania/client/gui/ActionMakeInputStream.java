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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ActionMakeInputStream implements ISelectorAction
{
    public static final ActionMakeInputStream INSTANCE = new ActionMakeInputStream();
    private FileInputStream fis = null;
    private File file;

    private String fileName;
    @Override
    public void select(File fileIn)
    {
        try
        {
            file = fileIn;
            fis = new FileInputStream(fileIn);
            fileName = fileIn.getName();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public FileInputStream getFileInputStream() { return fis; }

    public String getFileName() { return fileName; }

    public File getFile() { return file; }
}
