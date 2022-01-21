package com.tigres810.pnetwork.core.init;

import com.tigres810.pnetwork.PNetwork;
import com.tigres810.pnetwork.common.blocks.EnergyGeneratorBlock;
import com.tigres810.pnetwork.common.blocks.EnergyMachineChargerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PNetwork.MOD_ID);
	
	//Blocks
	public static final RegistryObject<EnergyMachineChargerBlock> ENERGYMACHINECHARGER_BLOCK = BLOCKS.register("energymachinecharger_block", () -> new EnergyMachineChargerBlock(AbstractBlock.Properties.of(Material.METAL).strength(8.0f, 4.000f).sound(SoundType.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<EnergyGeneratorBlock> ENERGYGENERATOR_BLOCK = BLOCKS.register("energygenerator_block", () -> new EnergyGeneratorBlock(AbstractBlock.Properties.of(Material.METAL).strength(5.0F, 2.000F).sound(SoundType.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));

}