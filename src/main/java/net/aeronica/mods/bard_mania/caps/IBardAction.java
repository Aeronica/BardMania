package net.aeronica.mods.bard_mania.caps;

public interface IBardAction
{
    void setInstrumentEquipped();

    void setInstrumentRemoved();

    void toggleEquippedState();

    boolean isInstrumentEquipped();
}
