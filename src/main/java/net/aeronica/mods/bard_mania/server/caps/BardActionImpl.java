package net.aeronica.mods.bard_mania.server.caps;

public class BardActionImpl implements IBardAction
{
    private boolean isInstEquipped = false;

    @Override
    public void setInstrumentEquipped() { isInstEquipped = true; }

    @Override
    public void setInstrumentRemoved() { isInstEquipped = false; }

    @Override
    public boolean isInstrumentEquipped() { return isInstEquipped; }

}
