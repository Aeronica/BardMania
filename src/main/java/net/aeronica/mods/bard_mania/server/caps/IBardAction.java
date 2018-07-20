package net.aeronica.mods.bard_mania.server.caps;

import net.aeronica.mods.bard_mania.client.actions.ModelDummy;

public interface IBardAction
{
    void setInstrumentEquipped();

    void setInstrumentRemoved();

    boolean isInstrumentEquipped();

    ModelDummy getModelDummy();

    void setModelDummy(ModelDummy modelDummyIn);

    void setTempOff();

    void setTempOn();

    boolean getTemp();
}
