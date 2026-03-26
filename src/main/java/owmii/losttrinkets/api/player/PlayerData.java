package owmii.losttrinkets.api.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import owmii.losttrinkets.api.trinket.Trinkets;

public class PlayerData implements INBTSerializable<CompoundTag> {
    private final Trinkets trinkets = new Trinkets(this);
    public long unlockDelay;
    public boolean allowFlying;
    public boolean wasFlying;
    private boolean sync;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("trinkets", this.trinkets.serializeNBT());
        nbt.putLong("unlock_delay", this.unlockDelay);
        nbt.putBoolean("allow_flying", this.allowFlying);
        nbt.putBoolean("was_flying", this.wasFlying);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.trinkets.deserializeNBT(nbt.getCompound("trinkets"));
        this.unlockDelay = nbt.getLong("unlock_delay");
        this.allowFlying = nbt.getBoolean("allow_flying");
        this.wasFlying = nbt.getBoolean("was_flying");
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isSync() {
        return this.sync;
    }

    public Trinkets getTrinkets() {
        return this.trinkets;
    }

    public static final Capability<PlayerData> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
}
