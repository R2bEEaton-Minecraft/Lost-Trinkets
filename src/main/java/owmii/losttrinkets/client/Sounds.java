package owmii.losttrinkets.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import owmii.lib.registry.Registry;
import owmii.losttrinkets.LostTrinkets;

public class Sounds {
    public static final Registry<SoundEvent> REG = new Registry<>(SoundEvent.class, LostTrinkets.MOD_ID);
    public static final SoundEvent UNLOCK = register("unlock");

    static SoundEvent register(String name) {
        return REG.register(name, SoundEvent.createVariableRangeEvent(new ResourceLocation(LostTrinkets.MOD_ID, name)));
    }
}
