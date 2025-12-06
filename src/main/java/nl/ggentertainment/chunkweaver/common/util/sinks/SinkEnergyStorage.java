package nl.ggentertainment.chunkweaver.common.util.sinks;

import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.function.Consumer;

public class SinkEnergyStorage implements IEnergyStorage {

    private final Consumer<Integer> sink;

    public SinkEnergyStorage(Consumer<Integer> sink) {
        this.sink = sink;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (!simulate) {
            sink.accept(toReceive);
        }
        return toReceive;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
