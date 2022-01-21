package com.tigres810.pnetwork.client.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tigres810.pnetwork.PNetwork;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = PNetwork.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
	
	@SuppressWarnings("resource")
	public static BlockPos lookingAt(){
	    RayTraceResult rt = Minecraft.getInstance().hitResult;

	    double x = (rt.getLocation().x);
	    double y = (rt.getLocation().y);
	    double z = (rt.getLocation().z);

	    double xla = Minecraft.getInstance().player.getLookAngle().x;
	    double yla = Minecraft.getInstance().player.getLookAngle().y;
	    double zla = Minecraft.getInstance().player.getLookAngle().z;

	    if ((x%1==0)&&(xla<0))x-=0.01;
	    if ((y%1==0)&&(yla<0))y-=0.01;
	    if ((z%1==0)&&(zla<0))z-=0.01;

	    BlockPos ps = new BlockPos(x,y,z);
	    //BlockState bl = Minecraft.getInstance().level.getBlockState(ps);

	    return ps;
	}
	
	private static final int WIDTH = 200;
    private static final int HEIGHT = 151;
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onRenderEnergy(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.TEXT) {
			FontRenderer renderer = Minecraft.getInstance().font;
			MatrixStack stack = event.getMatrixStack();
			BlockPos pos = lookingAt();
			World worldIn = Minecraft.getInstance().level;
			MainWindow screen = event.getWindow();
			if(!pos.equals(null)) {
				TileEntity te = worldIn.getBlockEntity(pos);
				if(te != null) {
					LazyOptional<IEnergyStorage> energyHandlerCap = te.getCapability(CapabilityEnergy.ENERGY);
					if(energyHandlerCap.isPresent()) {
						IEnergyStorage energyhandler = energyHandlerCap.orElseThrow(IllegalStateException::new);
						
						int relX = (screen.getWidth() - WIDTH) / 2;
				        int relY = (screen.getHeight() - HEIGHT) / 2;
						renderer.draw(stack, "| Energy: " + energyhandler.getEnergyStored() + " |", relX, relY + 10, 0xffffff);
					}
				}
			}
		}
	}
}