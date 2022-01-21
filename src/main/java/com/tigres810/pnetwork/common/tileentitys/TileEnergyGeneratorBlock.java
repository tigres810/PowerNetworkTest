package com.tigres810.pnetwork.common.tileentitys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tigres810.pnetwork.core.energy.CustomEnergyStorage;
import com.tigres810.pnetwork.core.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEnergyGeneratorBlock extends TileEntity implements ITickableTileEntity {
	
	protected CustomEnergyStorage storage = new CustomEnergyStorage(50, 0, 5) { protected void onEnergyChanged() { setChanged(); }; };
	
	protected LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional.of(() -> storage);

	public TileEnergyGeneratorBlock() {
		super(TileEntityInit.ENERGYGENERATOR_BLOCK_TILE.get());
	}
	
	public boolean sendEnergy = false;
	
	private int ticks = 0;
	private int tickstogenerate = 10;

	@Override
	public void tick() {
		if(level.isClientSide) return;
		
		if(ticks >= tickstogenerate) {
			if(!sendEnergy) { // Generate energy
				storage.addEnergy(1, false);
				sendEnergy = true;
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), ticks);
				setChanged();
				ticks = 0;
			} else { // Send energy to other blocks
				sendEnergy();
				sendEnergy = false;
				ticks = 0;
			}
		} else {
			ticks++;
		}
	}
	
	private void sendEnergy() {
		AtomicInteger capacity = new AtomicInteger(storage.getEnergyStored());
		if (capacity.get() <= 0) return;
		
		List<Direction> dirs = getConnectableSides(getBlockState());
		for(Direction dir : dirs) {
			TileEntity te = this.level.getBlockEntity(worldPosition.relative(dir));
			if(te != null) {
				boolean canContinue = te.getCapability(CapabilityEnergy.ENERGY, dir).map(handler -> {
					if (handler.canReceive()) {
						int received = handler.receiveEnergy(capacity.get(), false);
						capacity.addAndGet(-received);
						storage.extractEnergy(received, false);
						setChanged();
						return capacity.get() > 0;
					}
					return true;
				}).orElse(true);
				if(!canContinue) return;
			}
		}
	}
	
	public List<Direction> getConnectableSides(BlockState state) {
		List<Direction> faces = new ArrayList<Direction>();
		faces.add(Direction.UP);
		faces.add(Direction.DOWN);
		switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
		case NORTH:
			faces.add(Direction.NORTH);
			faces.add(Direction.EAST);
			faces.add(Direction.WEST);
			break;
		case SOUTH:
			faces.add(Direction.SOUTH);
			faces.add(Direction.EAST);
			faces.add(Direction.WEST);
			break;
		case EAST:
			faces.add(Direction.NORTH);
			faces.add(Direction.SOUTH);
			faces.add(Direction.EAST);
			break;
		case WEST:
			faces.add(Direction.NORTH);
			faces.add(Direction.SOUTH);
			faces.add(Direction.WEST);
			break;
		default:
			break;
		}
		return faces;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return energyHandler.cast();
        return super.getCapability(capability, facing);
    }
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbtTagCompound = new CompoundNBT();
		save(nbtTagCompound);
		int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.  You can use it, or not, as you want.
		return new SUpdateTileEntityPacket(worldPosition, tileEntityType, nbtTagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState blockState = level.getBlockState(worldPosition);
	    load(blockState, pkt.getTag());   // read from the nbt in the packet
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbtTagCompound = new CompoundNBT();
	    save(nbtTagCompound);
	    return nbtTagCompound;
	}
	
	@Override
	public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound)
	{
		load(blockState, parentNBTTagCompound);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt = super.save(nbt);
		nbt = storage.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		storage.readFromNBT(nbt);
	}
	
}