package com.laipel.ensorcellcraft.common.event;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.common.entity.SoulEntity;
import com.laipel.ensorcellcraft.common.registry.EntityRegistry;
import com.laipel.ensorcellcraft.common.registry.SoulRegistry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Ensorcellcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestTickEvent {

    @SubscribeEvent
    public static void tick(PlayerInteractEvent.RightClickBlock event) {

        if(event.getEntity().level.isClientSide)
            return;

        for (int i = 0; i < 40; i++) {

            Collection<RegistryObject<ISoul>> collection = SoulRegistry.SOULS.getEntries();

            int index = event.getEntity().getRandom().nextInt(collection.size());
            Iterator<RegistryObject<ISoul>> iter = collection.iterator();
            for (int j = 0; j < index; j++)
                iter.next();

            ISoul soul = iter.next().get();
            SoulEntity soulEntity = new SoulEntity(
                    EntityRegistry.SOUL.get(), event.getEntity().level, soul, 10);
            soulEntity.setPos(event.getHitVec().getLocation());
            soulEntity.setMaxAge(240 + event.getEntity().getRandom().nextInt(100));

            event.getEntity().level.addFreshEntity(soulEntity);
        }
    }
}
