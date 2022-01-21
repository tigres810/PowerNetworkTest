package com.tigres810.pnetwork.core.init;

import com.tigres810.pnetwork.PNetwork;
import com.tigres810.pnetwork.common.tileentitys.TileEnergyGeneratorBlock;
import com.tigres810.pnetwork.common.tileentitys.TileEnergyMachineChargerBlock;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
	
	public static final DeferredRegister<TileEntityType<?>> TILEENTITYS = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PNetwork.MOD_ID);
	
	//TileEntitys
	public static final RegistryObject<TileEntityType<TileEnergyMachineChargerBlock>> ENERGYMACHINECHARGER_BLOCK_TILE = TILEENTITYS.register("energymachinecharger_block", () -> TileEntityType.Builder.of(TileEnergyMachineChargerBlock::new, BlockInit.ENERGYMACHINECHARGER_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEnergyGeneratorBlock>> ENERGYGENERATOR_BLOCK_TILE = TILEENTITYS.register("energygenerator_block", () -> TileEntityType.Builder.of(TileEnergyGeneratorBlock::new, BlockInit.ENERGYGENERATOR_BLOCK.get()).build(null));

}