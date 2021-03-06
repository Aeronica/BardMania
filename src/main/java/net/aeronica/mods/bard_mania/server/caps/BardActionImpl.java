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

package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.client.actions.base.ModelDummy;

public class BardActionImpl implements IBardAction
{
    private boolean isInstEquipped = false;
    private boolean tempBoolean = false;
    private ModelDummy modelDummy = new ModelDummy();

    @Override
    public void setInstrumentEquipped() { isInstEquipped = true; }

    @Override
    public void setInstrumentRemoved() { isInstEquipped = false; }

    @Override
    public boolean isInstrumentEquipped() { return isInstEquipped; }

    @Override
    public ModelDummy getModelDummy() { return modelDummy; }

    @Override
    public void setModelDummy(ModelDummy modelDummyIn) { modelDummy = modelDummyIn; }

    @Override
    public void setTempOff() { tempBoolean = false; }

    @Override
    public void setTempOn() { tempBoolean = true; }

    @Override
    public boolean getTemp() { return tempBoolean; }
}
