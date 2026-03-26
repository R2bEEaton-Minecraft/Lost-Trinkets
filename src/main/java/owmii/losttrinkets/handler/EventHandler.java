package owmii.losttrinkets.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.item.trinkets.BigFootTrinket;
import owmii.losttrinkets.item.trinkets.BlazeHeartTrinket;
import owmii.losttrinkets.item.trinkets.ButchersCleaverTrinket;
import owmii.losttrinkets.item.trinkets.CoffeeBeanTrinket;
import owmii.losttrinkets.item.trinkets.CreepoTrinket;
import owmii.losttrinkets.item.trinkets.DarkDaggerTrinket;
import owmii.losttrinkets.item.trinkets.DarkEggTrinket;
import owmii.losttrinkets.item.trinkets.DropSpindleTrinket;
import owmii.losttrinkets.item.trinkets.EmberTrinket;
import owmii.losttrinkets.item.trinkets.FireMindTrinket;
import owmii.losttrinkets.item.trinkets.GoldenMelonTrinket;
import owmii.losttrinkets.item.trinkets.GoldenSkullTrinket;
import owmii.losttrinkets.item.trinkets.GoldenSwatterTrinket;
import owmii.losttrinkets.item.trinkets.LunchBagTrinket;
import owmii.losttrinkets.item.trinkets.MadAuraTrinket;
import owmii.losttrinkets.item.trinkets.MadPiggyTrinket;
import owmii.losttrinkets.item.trinkets.MagicalHerbsTrinket;
import owmii.losttrinkets.item.trinkets.MinersPickTrinket;
import owmii.losttrinkets.item.trinkets.MirrorShardTrinket;
import owmii.losttrinkets.item.trinkets.OctopickTrinket;
import owmii.losttrinkets.item.trinkets.OctopusLegTrinket;
import owmii.losttrinkets.item.trinkets.OxalisTrinket;
import owmii.losttrinkets.item.trinkets.RubyHeartTrinket;
import owmii.losttrinkets.item.trinkets.SerpentToothTrinket;
import owmii.losttrinkets.item.trinkets.SlingshotTrinket;
import owmii.losttrinkets.item.trinkets.StarfishTrinket;
import owmii.losttrinkets.item.trinkets.TeaLeafTrinket;
import owmii.losttrinkets.item.trinkets.TreasureRingTrinket;
import owmii.losttrinkets.item.trinkets.WitherNailTrinket;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (data.unlockDelay > 0) {
                data.unlockDelay--;
            }
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            BlockPos pos = player.blockPosition();
            trinkets.getTickable().forEach(trinket -> trinket.tick(player.level(), pos, player));
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        FireMindTrinket.onLivingUpdate(event);
    }

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Entity entity = event.getExplosion().getDirectSourceEntity();
        if (entity instanceof Creeper creeper) {
            LivingEntity target = creeper.getTarget();
            if (target instanceof Player player) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.CREEPO)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinLevelEvent event) {
        OctopickTrinket.collectDrops(event);
        BigFootTrinket.addAvoidGoal(event);
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if (BlazeHeartTrinket.isImmuneToFire(event.getEntity(), source)) {
            event.setCanceled(true);
        }
        MadAuraTrinket.onAttack(event);
        OctopusLegTrinket.onAttack(event);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        DarkDaggerTrinket.onHurt(event);
        DarkEggTrinket.onHurt(event);
        DropSpindleTrinket.onHurt(event);
        EmberTrinket.onHurt(event);
        GoldenSwatterTrinket.onHurt(event);
        MadPiggyTrinket.onHurt(event);
        MirrorShardTrinket.onHurt(event);
        SerpentToothTrinket.onHurt(event);
        StarfishTrinket.onHurt(event);
        SlingshotTrinket.onHurt(event);
        WitherNailTrinket.onHurt(event);

        if (source.getEntity() instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            float amount = event.getAmount();
            if (trinkets.isActive(Itms.SILVER_NAIL)) {
                amount *= 1.1F;
            }
            if (trinkets.isActive(Itms.GLORY_SHARDS)) {
                amount *= 1.2F;
            }
            event.setAmount(amount);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        RubyHeartTrinket.onDeath(event);
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        ButchersCleaverTrinket.dropExtra(event);
        TreasureRingTrinket.onDrops(event);
        GoldenSkullTrinket.onDrops(event);
    }

    @SubscribeEvent
    public static void onPotion(MobEffectEvent.Applicable event) {
        CoffeeBeanTrinket.onPotion(event);
        MagicalHerbsTrinket.onPotion(event);
        OxalisTrinket.onPotion(event);
        TeaLeafTrinket.onPotion(event);
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        CreepoTrinket.resetExplosion(event);
    }

    @SubscribeEvent
    public static void onLooting(LootingLevelEvent event) {
        DamageSource source = event.getDamageSource();
        if (source.getEntity() instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            int looting = event.getLootingLevel();
            if (trinkets.isActive(Itms.GOLDEN_HORSESHOE)) {
                looting++;
            }
            if (trinkets.isActive(Itms.GOLDEN_TOOTH)) {
                looting++;
            }
            event.setLootingLevel(looting);
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        GoldenMelonTrinket.onUseFinish(event);
        LunchBagTrinket.onUseFinish(event);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        MinersPickTrinket.onBreakSpeed(event);
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        OctopickTrinket.onBreak(event);
    }
}
