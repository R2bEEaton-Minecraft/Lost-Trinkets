package owmii.losttrinkets.core.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.item.trinkets.DragonBreathTrinket;

import java.util.List;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    @Inject(method = "getDrops", at = @At("TAIL"), cancellable = true)
    public void getDrops(LootParams.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockState state = (BlockState) (Object) this;
        LootParams context = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
        List<ItemStack> drops = cir.getReturnValue();
        Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player player) {
            cir.setReturnValue(DragonBreathTrinket.autoSmelt(drops, player));
        }
    }
}
