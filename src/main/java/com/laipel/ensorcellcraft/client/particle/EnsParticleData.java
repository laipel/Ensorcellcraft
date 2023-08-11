package com.laipel.ensorcellcraft.client.particle;

import com.laipel.ensorcellcraft.client.particle.controllers.ControllerData;
import net.minecraft.core.particles.ParticleOptions;

public interface EnsParticleData<T extends ParticleOptions> extends ParticleOptions {

    ControllerData getController();

    T setController(ControllerData data);

}
