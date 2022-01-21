package com.tigres810.pnetwork.core.init;

import com.tigres810.pnetwork.PNetwork;
import com.tigres810.pnetwork.common.blocks.BlockItemBase;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PNetwork.MOD_ID);
	
	//Items
	
	//Block Items
	public static final RegistryObject<Item> ENERGYMACHINECHARGER_BLOCK_ITEM = ITEMS.register("energymachinecharger_block", () -> new BlockItemBase(BlockInit.ENERGYMACHINECHARGER_BLOCK.get(), new Item.Properties().tab(PNetwork.TAB)));
	public static final RegistryObject<Item> ENERGYGENERATOR_BLOCK_ITEM = ITEMS.register("energygenerator_block", () -> new BlockItemBase(BlockInit.ENERGYGENERATOR_BLOCK.get(), new Item.Properties().tab(PNetwork.TAB)));

}