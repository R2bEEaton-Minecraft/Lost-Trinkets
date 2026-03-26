package owmii.losttrinkets.handler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.config.Configs;
import owmii.losttrinkets.network.packet.SyncDataPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DataManager implements ICapabilitySerializable<CompoundTag> {
    private final PlayerData data = new PlayerData();
    private final LazyOptional<PlayerData> holder = LazyOptional.of(() -> this.data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PlayerData.CAP) {
            return this.holder.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.data.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.data.deserializeNBT(nbt);
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(LostTrinkets.MOD_ID, "player_data"), new DataManager());
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player player = event.getEntity();
        oldPlayer.reviveCaps();
        PlayerData oldData = LostTrinketsAPI.getData(oldPlayer);
        PlayerData newData = LostTrinketsAPI.getData(player);
        newData.deserializeNBT(oldData.serializeNBT());
        oldPlayer.invalidateCaps();

        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        trinkets.getActiveTrinkets().forEach(trinket -> {
            if (trinket instanceof Trinket) {
                ((Trinket) trinket).applyAttributes(player);
            }
        });
        if (!event.isWasDeath()) {
            player.setHealth(oldPlayer.getHealth());
        }
    }

    @SubscribeEvent
    public static void update(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer player) {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (data.isSync()) {
                LostTrinkets.NET.toTrackingAndSelf(new SyncDataPacket(player), player);
                data.setSync(false);
            }
        }
    }

    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync(event.getEntity());
    }

    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(event.getEntity());
        trinkets.initSlots(Configs.GENERAL.startSlots.get());
        trinkets.removeDisabled(event.getEntity());
        sync(event.getEntity());
    }

    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        PlayerData data = LostTrinketsAPI.getData(player);
        data.wasFlying = player.getAbilities().flying;
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        sync(event.getEntity());
    }

    @SubscribeEvent
    public static void trackPlayer(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target instanceof ServerPlayer serverPlayer && event.getEntity() instanceof ServerPlayer trackingPlayer) {
            LostTrinkets.NET.toClient(new SyncDataPacket(serverPlayer), trackingPlayer);
        }
    }

    static void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            LostTrinkets.NET.toClient(new SyncDataPacket(player), serverPlayer);
        }
    }
}
