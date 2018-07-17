package net.aeronica.mods.bardmania.caps;

public interface IBardAction
{
    void setInstrumentEquipped();

    void setInstrumentRemoved();

    void toggleEquippedState();

    boolean isInstrumentEquipped();
}
