package owmii.losttrinkets.api.trinket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.config.Configs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Trinkets implements INBTSerializable<CompoundTag> {
    private final List<ITrinket> available = new ArrayList<>();
    private final List<ITrinket> active = new ArrayList<>();
    private final List<ITickableTrinket> tickable = new ArrayList<>();
    private final List<ITargetingTrinket> targeting = new ArrayList<>();
    private final PlayerData data;
    private int slots = 1;
    private boolean slotsSet;

    public Trinkets(PlayerData data) {
        this.data = data;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("slots", this.slots);
        nbt.putBoolean("slots_set", this.slotsSet);
        ListTag availableTrinkets = new ListTag();
        this.available.forEach((trinket) -> {
            CompoundTag nbt1 = new CompoundTag();
            ResourceLocation location = ForgeRegistries.ITEMS.getKey(trinket.asItem());
            Objects.requireNonNull(location);
            nbt1.putString("trinket", location.toString());
            availableTrinkets.add(nbt1);
        });
        nbt.put("available_trinkets", availableTrinkets);
        ListTag activeTrinkets = new ListTag();
        this.active.forEach((trinket) -> {
            CompoundTag nbt1 = new CompoundTag();
            ResourceLocation location = ForgeRegistries.ITEMS.getKey(trinket.asItem());
            Objects.requireNonNull(location);
            nbt1.putString("trinket", location.toString());
            activeTrinkets.add(nbt1);
        });
        nbt.put("active_trinkets", activeTrinkets);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slots = nbt.getInt("slots");
        this.slotsSet = nbt.getBoolean("slots_set");
        ListTag availableTrinkets = nbt.getList("available_trinkets", Tag.TAG_COMPOUND);
        this.available.clear();
        for (int i = 0; i < availableTrinkets.size(); i++) {
            CompoundTag nbt1 = availableTrinkets.getCompound(i);
            Item trinket = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt1.getString("trinket")));
            if (trinket instanceof ITrinket) {
                this.available.add((ITrinket) trinket);
            }
        }
        ListTag activeTrinkets = nbt.getList("active_trinkets", Tag.TAG_COMPOUND);
        this.active.clear();
        this.tickable.clear();
        this.targeting.clear();
        for (int i = 0; i < activeTrinkets.size(); i++) {
            CompoundTag nbt1 = activeTrinkets.getCompound(i);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt1.getString("trinket")));
            if (item instanceof ITrinket) {
                ITrinket trinket = (ITrinket) item;
                if (this.active.size() < this.slots) {
                    this.active.add(trinket);
                    if (trinket instanceof ITickableTrinket) {
                        this.tickable.add((ITickableTrinket) trinket);
                    }
                    if (trinket instanceof ITargetingTrinket) {
                        this.targeting.add((ITargetingTrinket) trinket);
                    }
                }
            }
        }
    }

    public boolean unlockSlot() {
        if (this.slots < Configs.GENERAL.maxSlots.get()) {
            this.slots++;
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public int getSlots() {
        return this.slots;
    }

    public void initSlots(int slots) {
        if (!this.slotsSet) {
            setSlots(slots);
            this.slotsSet = true;
        }
    }

    public void setSlots(int slots) {
        this.slots = slots;
        this.data.setSync(true);
    }

    public boolean clear() {
        if (!this.available.isEmpty() || !this.active.isEmpty()) {
            this.available.clear();
            this.active.clear();
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean give(ITrinket trinket) {
        if (!has(trinket)) {
            this.available.add(trinket);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean give(RegistryObject<? extends ITrinket> trinket) {
        return give(trinket.get());
    }

    public boolean setActive(ITrinket trinket, Player player) {
        if (isAvailable(trinket)) {
            forceActive(trinket, player);
            this.available.remove(trinket);
            return true;
        }
        return false;
    }

    public boolean setActive(RegistryObject<? extends ITrinket> trinket, Player player) {
        return setActive(trinket.get(), player);
    }

    public boolean setInactive(ITrinket trinket, Player player) {
        if (isActive(trinket)) {
            this.available.add(trinket);
            this.active.remove(trinket);
            if (trinket instanceof ITickableTrinket) {
                this.tickable.remove(trinket);
            }
            if (trinket instanceof ITargetingTrinket) {
                this.targeting.remove(trinket);
            }
            if (trinket instanceof Trinket) {
                ((Trinket) trinket).removeAttributes(player);
            }
            trinket.onDeactivated(player.level(), player.blockPosition(), player);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean setInactive(RegistryObject<? extends ITrinket> trinket, Player player) {
        return setInactive(trinket.get(), player);
    }

    public boolean forceActive(ITrinket trinket, Player player) {
        if (!isActive(trinket) && this.active.size() < this.slots) {
            this.active.add(trinket);
            if (trinket instanceof ITickableTrinket) {
                this.tickable.add((ITickableTrinket) trinket);
            }
            if (trinket instanceof ITargetingTrinket) {
                this.targeting.add((ITargetingTrinket) trinket);
            }
            if (trinket instanceof Trinket) {
                ((Trinket) trinket).applyAttributes(player);
            }
            trinket.onActivated(player.level(), player.blockPosition(), player);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean forceActive(RegistryObject<? extends ITrinket> trinket, Player player) {
        return forceActive(trinket.get(), player);
    }

    public void removeDisabled(Player player) {
        getActiveTrinkets().stream().filter(LostTrinketsAPI.get()::isDisabled).collect(Collectors.toList())
                .forEach(trinket -> setInactive(trinket, player));
        if (getAvailableTrinkets().removeIf(LostTrinketsAPI.get()::isDisabled)) {
            this.data.setSync(true);
        }
    }

    public boolean has(ITrinket trinket) {
        return isActive(trinket) || isAvailable(trinket);
    }

    public boolean has(RegistryObject<? extends ITrinket> trinket) {
        return has(trinket.get());
    }

    public boolean isActive(ITrinket trinket) {
        return this.active.contains(trinket);
    }

    public boolean isActive(RegistryObject<? extends ITrinket> trinket) {
        return isActive(trinket.get());
    }

    public boolean isAvailable(ITrinket trinket) {
        return this.available.contains(trinket);
    }

    public boolean isAvailable(RegistryObject<? extends ITrinket> trinket) {
        return isAvailable(trinket.get());
    }

    public List<ITrinket> getActiveTrinkets() {
        return this.active;
    }

    public List<ITrinket> getAvailableTrinkets() {
        return this.available;
    }

    public List<ITickableTrinket> getTickable() {
        return this.tickable;
    }

    public List<ITargetingTrinket> getTargeting() {
        return this.targeting;
    }
}
