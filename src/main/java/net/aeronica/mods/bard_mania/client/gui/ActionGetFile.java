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

public class ActionGetFile implements ISelectorAction
{
    public static final ActionGetFile INSTANCE = new ActionGetFile();
    private File file;

    @Override
    public void select(File fileIn) { file = fileIn; }

    public String getFileName() { return file.getName(); }

    public File getFile() { return file; }
}
