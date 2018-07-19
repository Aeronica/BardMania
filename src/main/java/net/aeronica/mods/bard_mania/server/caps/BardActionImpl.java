package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.client.action.ModelDummy;

public class BardActionImpl implements IBardAction
{
    private volatile boolean isInstEquipped = false;
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
