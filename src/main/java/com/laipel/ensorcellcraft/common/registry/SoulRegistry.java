package com.laipel.ensorcellcraft.common.registry;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.common.souls.Soul;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.function.Supplier;

public class SoulRegistry {

    public static final DeferredRegister<ISoul> SOULS = DeferredRegister.create(new ResourceLocation(Ensorcellcraft.MODID, "soul"), Ensorcellcraft.MODID);

    public static final Supplier<IForgeRegistry<ISoul>> SOUL_REGISTRY = SOULS.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<ISoul> INFERNAL_SOUL = SOULS.register("infernal_soul", () -> new Soul(3, new Color(0xff390d)));
    public static final RegistryObject<ISoul> PURE_SOUL = SOULS.register("pure_soul", () -> new Soul(4, new Color(0xfffdea)));
    public static final RegistryObject<ISoul> HOLLOW_SOUL = SOULS.register("hollow_soul", () -> new Soul(1, new Color(0x3bb0ff)));
    public static final RegistryObject<ISoul> NATURE_SOUL = SOULS.register("nature_soul", () -> new Soul(2, new Color(0x31ff61)));
    public static final RegistryObject<ISoul> ENDER_SOUL = SOULS.register("ender_soul", () -> new Soul(3, new Color(0xce3bff)));

    public static void register() {
        SOULS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
