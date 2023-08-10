package com.laipel.ensorcellcraft.common.registry;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.client.render.entity.SoulRenderer;
import com.laipel.ensorcellcraft.common.entity.SoulEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Ensorcellcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ensorcellcraft.MODID);

    public static final RegistryObject<EntityType<SoulEntity>> SOUL = ENTITIES.register("soul_orb", () ->
            EntityType.Builder.<SoulEntity>of(SoulEntity::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f)
                    .setUpdateInterval(1)
                    .build("soul_orb"));

    public static void register() {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SOUL.get(), SoulRenderer::new);
    }
}
