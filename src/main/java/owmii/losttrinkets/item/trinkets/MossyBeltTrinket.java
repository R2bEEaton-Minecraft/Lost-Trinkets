package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class MossyBeltTrinket extends Trinket<MossyBeltTrinket> implements ITickableTrinket {
    public MossyBeltTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        if (world.getGameTime() % 40 == 0) {
            for (ItemStack stack : player.inventory.armorInventory) {
                if (!stack.isEmpty() && stack.isDamaged()) {
                    stack.setDamage(stack.getDamage() - 1);
                    break;
                }
            }
        }
    }
}
