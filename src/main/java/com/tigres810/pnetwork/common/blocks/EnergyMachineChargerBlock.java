package com.tigres810.pnetwork.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.tigres810.pnetwork.core.init.TileEntityInit;
import com.tigres810.pnetwork.core.interfaces.IPipeConnect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineChargerBlock extends Block implements IPipeConnect {
	
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	
	private static final VoxelShape SHAPE = Stream.of(
		Block.box(6, 6, 5, 10, 10, 6),
		Block.box(6, 5, 6, 10, 6, 10),
		Block.box(6, 10, 6, 10, 11, 10),
		Block.box(6, 6, 10, 10, 10, 11),
		Block.box(5, 6, 6, 6, 10, 10),
		Block.box(10, 6, 6, 11, 10, 10)
	).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_N = Stream.of(
			Block.box(6, 6, 5, 10, 10, 6),
			Block.box(6, 6, 4, 10, 10, 5),
			Block.box(6, 6, 1, 10, 10, 4),
			Block.box(6, 6, 0, 10, 10, 1)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static final VoxelShape SHAPE_S = Stream.of(
			Block.box(6, 6, 10, 10, 10, 11),
			Block.box(6, 6, 11, 10, 10, 12),
			Block.box(6, 6, 12, 10, 10, 15),
			Block.box(6, 6, 15, 10, 10, 16)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static final VoxelShape SHAPE_E = Stream.of(
			Block.box(10, 6, 6, 11, 10, 10),
			Block.box(11, 6, 6, 12, 10, 10),
			Block.box(12, 6, 6, 15, 10, 10),
			Block.box(15, 6, 6, 16, 10, 10)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static final VoxelShape SHAPE_W = Stream.of(
			Block.box(5, 6, 6, 6, 10, 10),
			Block.box(4, 6, 6, 5, 10, 10),
			Block.box(1, 6, 6, 4, 10, 10),
			Block.box(0, 6, 6, 1, 10, 10)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static final VoxelShape SHAPE_U = Stream.of(
			Block.box(6, 10, 6, 10, 11, 10),
			Block.box(6, 11, 6, 10, 12, 10),
			Block.box(6, 12, 6, 10, 15, 10),
			Block.box(6, 15, 6, 10, 16, 10)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static final VoxelShape SHAPE_D = Stream.of(
			Block.box(6, 5, 6, 10, 6, 10),
			Block.box(6, 4, 6, 10, 5, 10),
			Block.box(6, 1, 6, 10, 4, 10),
			Block.box(6, 0, 6, 10, 1, 10)
			).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	
	static VoxelShape[] shapes = initializeShapes();
	
	private static VoxelShape[] initializeShapes() {
        VoxelShape shapeN = SHAPE_N, shapeS = SHAPE_S, shapeE = SHAPE_E, shapeW = SHAPE_W, shapeU = SHAPE_U, shapeD = SHAPE_D; // initialize these to your basic part shapes
        VoxelShape[] shapes = new VoxelShape[64];
        for(int i=0; i<64; i++) {
            VoxelShape shape = SHAPE;
            if((i&1) != 0) shape = VoxelShapes.or(shape, shapeN);
            if((i&2) != 0) shape = VoxelShapes.or(shape, shapeS);
            if((i&4) != 0) shape = VoxelShapes.or(shape, shapeE);
            if((i&8) != 0) shape = VoxelShapes.or(shape, shapeW);
            if((i&16) != 0) shape = VoxelShapes.or(shape, shapeU);
            if((i&32) != 0) shape = VoxelShapes.or(shape, shapeD);
            shapes[i] = shape.optimize();
        }
        return shapes;
    }
	
	public EnergyMachineChargerBlock(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.defaultBlockState().setValue(UP, false).setValue(DOWN, false).setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		World world = (World) worldIn;
		BlockPos pos = currentPos;
		return super.updateShape(stateIn.setValue(DOWN, this.isSideConnectable(world, pos, Direction.DOWN)).setValue(EAST, this.isSideConnectable(world, pos, Direction.EAST)).setValue(NORTH, this.isSideConnectable(world, pos, Direction.NORTH)).setValue(SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH)).setValue(UP, this.isSideConnectable(world, pos, Direction.UP)).setValue(WEST, this.isSideConnectable(world, pos, Direction.WEST)), facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(DOWN, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.DOWN)).setValue(EAST, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.EAST)).setValue(NORTH, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.NORTH)).setValue(SOUTH, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.SOUTH)).setValue(UP, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.UP)).setValue(WEST, this.isSideConnectable(context.getLevel(), context.getClickedPos(), Direction.WEST));
	}
	
	private boolean isSideConnectable (World world, BlockPos pos, Direction side) {
		final BlockState state = world.getBlockState(pos.offset(side.getNormal()));
		if(state == null) return false;
		TileEntity te = world.getBlockEntity(pos.offset(side.getNormal()));
		if(te == null) return false;
		LazyOptional<IEnergyStorage> energyHandlerCap = te.getCapability(CapabilityEnergy.ENERGY);
		if(state.getBlock() instanceof IPipeConnect) {
			List<Direction> faces = ((IPipeConnect)state.getBlock()).getConnectableSides(state); 
            if(!faces.contains(side)) return false;
            return true;
		} else {
			if(energyHandlerCap.isPresent()) return true;
			return false;
		}			
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.ENERGYMACHINECHARGER_BLOCK_TILE.get().create();
	}
	
	private int makeShapeIndex(BlockState pState) {
        int i = 0;
        if(pState.getValue(BlockStateProperties.NORTH)) i |= 1;
        if(pState.getValue(BlockStateProperties.SOUTH)) i |= 2;
        if(pState.getValue(BlockStateProperties.EAST)) i |= 4;
        if(pState.getValue(BlockStateProperties.WEST)) i |= 8;
        if(pState.getValue(BlockStateProperties.UP)) i |= 16;
        if(pState.getValue(BlockStateProperties.DOWN)) i |= 32;
        return i;
    }

    Map<BlockState, VoxelShape> cache = new HashMap<>();

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
    	return cache.computeIfAbsent(pState, s -> shapes[makeShapeIndex(s)]);
    }
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(new BooleanProperty[] {UP, DOWN, NORTH, SOUTH, EAST, WEST}));
	}

	@Override
	public List<Direction> getConnectableSides(BlockState state) {
		List<Direction> faces = new ArrayList<Direction>();
		faces.add(Direction.UP);
		faces.add(Direction.DOWN);
		faces.add(Direction.NORTH);
		faces.add(Direction.SOUTH);
		faces.add(Direction.EAST);
		faces.add(Direction.WEST);
		return faces;
	}
}
