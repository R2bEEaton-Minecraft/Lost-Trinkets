package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class OctopusLegTrinket extends Trinket<OctopusLegTrinket> {
    public OctopusLegTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        if (!(world instanceof ServerLevel serverLevel)) return;
        DamageSource source = event.getSource();
        Entity immediateSource = source.getDirectEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (immediateSource instanceof LivingEntity living) {
                if (trinkets.isActive(Itms.OCTOPUS_LEG)) {
                    MinecraftServer server = serverLevel.getServer();
                    // Delay disarming till after goal ticking to avoid crashing
                    server.tell(new TickTask(server.getTickCount(), () -> disarm(world, living)));
                }
            }
        }
    }

    private static void disarm(Level world, LivingEntity living) {
        if (!living.isAlive()) return;
        ItemStack stack = living.getMainHandItem();
        if (!stack.isEmpty() && world.random.nextInt(5) == 0) {
            ItemStack stack1 = stack.copy();
            if (stack1.isDamageableItem()) {
                if (!stack1.isDamaged()) {
                    int damage = stack1.getMaxDamage();
                    if (damage > 10) {
                        damage /= 2;
                        damage = 10 + world.random.nextInt(damage);
                    }
                    stack1.setDamageValue(damage);
                }
            }
            living.spawnAtLocation(stack1);
            living.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }
}
