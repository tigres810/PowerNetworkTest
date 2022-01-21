package com.tigres810.pnetwork.core.interfaces;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public interface IPipeConnect {

	public List<Direction> getConnectableSides(BlockState state);
}