package net.aeronica.mods.bard_mania.caps;

public class BardActionImpl implements IBardAction
{
    private boolean isInstEquipped = false;

    @Override
    public void setInstrumentEquipped() { isInstEquipped = true; }

    @Override
    public void setInstrumentRemoved() { isInstEquipped = false; }

    @Override
    public void toggleEquippedState() { isInstEquipped = !isInstEquipped; }

    @Override
    public boolean isInstrumentEquipped() { return isInstEquipped; }
}
