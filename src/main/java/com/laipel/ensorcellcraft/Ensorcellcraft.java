package com.laipel.ensorcellcraft;

import com.laipel.ensorcellcraft.common.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Ensorcellcraft.MODID)
public class Ensorcellcraft {

    public static final String MODID = "ensorcellcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Ensorcellcraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        SoulRegistry.register();
        BlockRegistry.register();
        ItemRegistry.register();
        EntityRegistry.register();
        ParticleRegistry.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

}
