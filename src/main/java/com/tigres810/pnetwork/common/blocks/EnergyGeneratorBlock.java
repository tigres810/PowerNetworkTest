package com.tigres810.pnetwork.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.tigres810.pnetwork.core.init.TileEntityInit;
import com.tigres810.pnetwork.core.interfaces.IPipeConnect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class EnergyGeneratorBlock extends Block implements IPipeConnect {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = Stream.of(
		Block.box(0, 0, 0, 16, 16, 16)
	).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

	public EnergyGeneratorBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.ENERGYGENERATOR_BLOCK_TILE.get().create();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1F;
	}
	
	@Override
	public List<Direction> getConnectableSides(BlockState state) {
		List<Direction> faces = new ArrayList<Direction>();
		faces.add(Direction.UP);
		faces.add(Direction.DOWN);
		switch (state.getValue(FACING)) {
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

}