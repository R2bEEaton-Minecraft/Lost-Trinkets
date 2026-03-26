package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaSpiritTrinket extends Trinket<ThaSpiritTrinket> implements ITickableTrinket {
    public ThaSpiritTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        if (!world.isClientSide && player.getHealth() <= 2.0F && player.tickCount % 90 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 1, false, false));
        }
    }
}
