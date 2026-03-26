package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class LuckCoinTrinket extends Trinket<LuckCoinTrinket> implements ITickableTrinket {
    public LuckCoinTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        if (!world.isClientSide && player.tickCount % 90 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 300, 1, false, false));
        }
    }
}
