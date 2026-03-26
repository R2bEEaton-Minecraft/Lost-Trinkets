package owmii.losttrinkets.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;
import owmii.lib.registry.Registry;
import owmii.losttrinkets.LostTrinkets;

public class Entities {
    @SuppressWarnings("unchecked")
    public static final Registry<EntityType<?>> REG = new Registry(EntityType.class, LostTrinkets.MOD_ID);
    public static final RegistryObject<EntityType<DarkVexEntity>> DARK_VEX = REG.register("dark_vex", DarkVexEntity::new, MobCategory.MONSTER, 0.4F, 0.8F, 3, 80, true);

    public static void register(EntityAttributeCreationEvent event) {
        event.put(DARK_VEX.get(), DarkVexEntity.getAttribute().build());
    }
}
