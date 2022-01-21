package com.tigres810.pnetwork.core.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

	public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
	
	@OnlyIn(Dist.CLIENT)
	public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}
	
	protected void onEnergyChanged() {

    }
	
	public int addEnergy(int energyToAdd, boolean simulate) {
		int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxExtract, energyToAdd));
		if(!simulate) this.energy += energyReceived;
		return energyReceived;
	}
	
	public boolean canAdd(int energyToAdd) {
		return energyToAdd == addEnergy(energyToAdd, true);
	}
	
	public CompoundNBT readFromNBT(CompoundNBT nbt) {
		this.capacity = nbt.getInt("capacity");
		this.energy = nbt.getInt("energy");
		this.maxExtract = nbt.getInt("maxExtract");
		this.maxReceive = nbt.getInt("maxReceive");
		
		return nbt;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
    	nbt.putInt("capacity", this.capacity);
    	nbt.putInt("energy", this.energy);
    	nbt.putInt("maxReceive", this.maxReceive);
    	nbt.putInt("maxExtract", this.maxExtract);
		
		return nbt;
    }

}
