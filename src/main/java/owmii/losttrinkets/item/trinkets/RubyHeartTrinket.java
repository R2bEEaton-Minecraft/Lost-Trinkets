package owmii.losttrinkets.item.trinkets;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.util.Server;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class RubyHeartTrinket extends Trinket<RubyHeartTrinket> {
    private static HashMap<UUID, Float> lastHealths = new HashMap<>();

    public RubyHeartTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void saveHealthTickStart(TickEvent.ServerTickEvent event) {
        // Save player health at the beginning of the server tick
        if (event.phase == TickEvent.Phase.START) {
            lastHealths = Server.get().getPlayerList().getPlayers().stream()
                    .collect(Collectors.toMap(Entity::getUUID, LivingEntity::getHealth, Math::max, HashMap::new));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void saveHealthHurt(LivingHurtEvent event) {
        // Save player health before the player is hurt
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            lastHealths.merge(entity.getUUID(), entity.getHealth(), Math::max);
        }
    }

    public static void onDeath(LivingDeathEvent event) {
        if (!event.getSource().isCreativePlayer()) {
            LivingEntity entity = event.getEntity();
            if (entity instanceof Player player) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                boolean flag = false;
                if (trinkets.isActive(Itms.RUBY_HEART)) {
                    if (lastHealths.getOrDefault(player.getUUID(), player.getHealth()) > 6.0F) {
                        player.setHealth(1.0F);
                        event.setCanceled(true);
                        flag = true;
                    }
                }
                if (!flag && trinkets.isActive(Itms.BROKEN_TOTEM)) {
                    if (player.level().random.nextInt(4) == 0) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                            CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, new ItemStack(Items.TOTEM_OF_UNDYING));
                        }
                        player.setHealth(1.0F);
                        player.removeAllEffects();
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                        player.level().broadcastEntityEvent(player, (byte) 35);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
