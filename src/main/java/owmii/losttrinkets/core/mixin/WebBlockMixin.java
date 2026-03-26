package owmii.losttrinkets.core.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IForgeShearable;
import org.spongepowered.asm.mixin.Mixin;
import owmii.lib.util.Stack;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(WebBlock.class)
public class WebBlockMixin extends Block implements IForgeShearable {
    public WebBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        boolean flag = false;
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.GLASS_SHARD)) {
                world.destroyBlock(pos, false);
                Stack.drop(entity, new ItemStack(Items.STRING, 1 + world.rand.nextInt(2)));
                flag = true;
            }
        }
        if (!flag) {
            entity.setMotionMultiplier(state, new Vector3d(0.25D, (double) 0.05F, 0.25D));
        }
    }
}
