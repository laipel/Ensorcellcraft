package com.laipel.ensorcellcraft.common.event;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.common.entity.SoulEntity;
import com.laipel.ensorcellcraft.common.registry.EntityRegistry;
import com.laipel.ensorcellcraft.common.registry.SoulRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ensorcellcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestTickEvent {

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {

        if(event.player.level.isClientSide || event.phase == TickEvent.Phase.END)
            return;

        if (event.player.tickCount % 10 == 0) {
            SoulEntity soul = new SoulEntity(
                    EntityRegistry.SOUL.get(), event.player.level,
                    SoulRegistry.INFERNAL_SOUL.get(),
                    10);
            soul.setPos(event.player.position());
            soul.setMaxAge(200);

            event.player.level.addFreshEntity(soul);
        }
    }
}
