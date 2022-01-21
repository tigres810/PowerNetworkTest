package com.tigres810.pnetwork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tigres810.pnetwork.core.init.BlockInit;
import com.tigres810.pnetwork.core.init.ContainerInit;
import com.tigres810.pnetwork.core.init.ItemInit;
import com.tigres810.pnetwork.core.init.TileEntityInit;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PNetwork.MOD_ID)
public class PNetwork {
	
	public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "pnet";
    
    public PNetwork() {
    	IEventBus Bus = FMLJavaModLoadingContext.get().getModEventBus();
        Bus.addListener(this::setup);
        Bus.addListener(this::doClientStuff);
        Bus.addListener(this::commonSetup);
        
        ItemInit.ITEMS.register(Bus);
        BlockInit.BLOCKS.register(Bus);
        TileEntityInit.TILEENTITYS.register(Bus);
        ContainerInit.CONTAINERS.register(Bus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
    	
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	/*
    	event.enqueueWork(() -> {
    		ScreenManager.register(ContainerInit.TESTGENERATOR_CONTAINER.get(), TestGeneratorScreen::new);
    	});
    	*/
    }
    
    public static final ItemGroup TAB = new ItemGroup("testTab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.ENERGYGENERATOR_BLOCK_ITEM.get());
		}
    };
    
    public void commonSetup(final FMLCommonSetupEvent event) {
    	//MainNetwork.init();
    }

}