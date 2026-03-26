package owmii.losttrinkets.item;

import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import owmii.losttrinkets.LostTrinkets;

public final class ItemGroups {
    public static final DeferredRegister<CreativeModeTab> REG =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LostTrinkets.MOD_ID);
    public static final RegistryObject<CreativeModeTab> MAIN = REG.register(LostTrinkets.MOD_ID, () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + LostTrinkets.MOD_ID))
                    .icon(() -> new ItemStack(Itms.CREEPO.get()))
                    .displayItems((parameters, output) -> Itms.REG.forEach(output::accept))
                    .build());

    private ItemGroups() {
    }

    public static void init(IEventBus bus) {
        REG.register(bus);
    }
}
