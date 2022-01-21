package com.tigres810.pnetwork.core.init;

import com.tigres810.pnetwork.PNetwork;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PNetwork.MOD_ID);
	
	//Containers
	//public static final RegistryObject<ContainerType<GeneratorContainer>> GENERATOR_CONTAINER = CONTAINERS.register("energygenerator_block", () -> IForgeContainerType.create(GeneratorContainer::getClientContainer));

}