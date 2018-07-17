package net.aeronica.mods.bard_mania.server.caps;

public interface IBardAction
{
    void setInstrumentEquipped();

    void setInstrumentRemoved();

    void toggleEquippedState();

    boolean isInstrumentEquipped();
}
