package owmii.losttrinkets.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import owmii.lib.registry.Registry;
import owmii.losttrinkets.LostTrinkets;

public class Sounds {
    public static final Registry<SoundEvent> REG = new Registry<>(SoundEvent.class, LostTrinkets.MOD_ID);
    public static final RegistryObject<SoundEvent> UNLOCK = register("unlock");

    static RegistryObject<SoundEvent> register(String name) {
        return REG.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(LostTrinkets.MOD_ID, name)));
    }
}
