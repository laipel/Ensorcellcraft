package com.laipel.ensorcellcraft.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.client.particle.circleTint.CircleTintData;
import com.laipel.ensorcellcraft.client.particle.circleTint.CircleTintFactory;
import com.laipel.ensorcellcraft.client.particle.controllers.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Ensorcellcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Ensorcellcraft.MODID);

    public static final RegistryObject<ParticleType<CircleTintData>> CIRCLE_TINT = PARTICLES.register("wisp", CircleTintFactory.CircleTintType::new);

    public static void register() {
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onParticleRegistry(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.CIRCLE_TINT.get(), CircleTintFactory::new);
    }

    public static class ParticleBehaviorRegistry {

        private static final BiMap<Integer, ControllerType<?>> MAP = HashBiMap.create();

        public static final ControllerType<StandartController> STANDART_CONTROLLER = new StandartController();
        public static final ControllerType<ZeroMovementController> ZERO_MOVEMENT = new ZeroMovementController();
        public static final ControllerType<RotatingData> ROTATING_CONTROLLER = new RotatingType();
        public static final ControllerType<ScatterController> SCATTER_CONTROLLER = new ScatterController();


        static  {
            int id = 0;

            MAP.put((id++), STANDART_CONTROLLER);
            MAP.put((id++), ROTATING_CONTROLLER);
            MAP.put((id++), SCATTER_CONTROLLER);
            MAP.put((id++), ZERO_MOVEMENT);
        }

        public static int getId(@Nullable ControllerType<?> type) {
            return MAP.inverse().get(type);
        }

        public static ControllerType<?> byId(int id) {
            return MAP.get(id);
        }

    }
}
