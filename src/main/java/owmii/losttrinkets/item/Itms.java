package owmii.losttrinkets.item;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import owmii.lib.registry.Registry;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.block.Blcks;
import owmii.losttrinkets.item.trinkets.*;

public class Itms {
    public static final Registry<Item> REG = new Registry<>(Item.class, Blcks.REG.getBlockItems(null), LostTrinkets.MOD_ID);
    public static final RegistryObject<Trinket> PIGGY = trinket("piggy", () -> new PiggyTrinket(Rarity.COMMON, props()));
    public static final RegistryObject<Trinket> CREEPO = trinket("creepo", () -> new CreepoTrinket(Rarity.COMMON, props()));
    public static final RegistryObject<Trinket> HORSESHOE = trinket("horseshoe", () -> new HorseshoeTrinket(Rarity.COMMON, props()));
    public static final RegistryObject<Trinket> BUTCHERS_CLEAVER = trinket("butchers_cleaver", () -> new ButchersCleaverTrinket(Rarity.COMMON, props()));
    public static final RegistryObject<Trinket> SLINGSHOT = trinket("slingshot", () -> new SlingshotTrinket(Rarity.COMMON, props()));
    public static final RegistryObject<Trinket> MAGNETO = trinket("magneto", () -> new MagnetoTrinket(Rarity.COMMON, props()));

    public static final RegistryObject<Trinket> ROCK_CANDY = trinket("rock_candy", () -> new RockCandyTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> LUNCH_BAG = trinket("lunch_bag", () -> new LunchBagTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> LUCK_COIN = trinket("luck_coin", () -> new LuckCoinTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> MINERS_PICK = trinket("miners_pick", () -> new MinersPickTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> THA_CLOUD = trinket("tha_cloud", () -> new ThaCloudTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> TURTLE_SHELL = trinket("turtle_shell", () -> new TurtleShellTrinket(Rarity.UNCOMMON, props()));
    public static final RegistryObject<Trinket> ICE_SHARD = trinket("ice_shard", () -> new IceShardTrinket(Rarity.UNCOMMON, props()));

    public static final RegistryObject<Trinket> EMPTY_AMULET = trinket("empty_amulet", () -> new EmptyAmuletTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> THA_SPIDER = trinket("tha_spider", () -> new Trinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> GLASS_SHARD = trinket("glass_shard", () -> new Trinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> BLAZE_HEART = trinket("blaze_heart", () -> new BlazeHeartTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> THA_GHOST = trinket("tha_ghost", () -> new ThaGhostTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> TREBLE_HOOKS = trinket("treble_hooks", () -> new TrebleHooksTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> THA_WIZARD = trinket("tha_wizard", () -> new ThaWizardTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> THA_BAT = trinket("tha_bat", () -> new ThaBatTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> BLANK_EYES = trinket("blank_eyes", () -> new BlankEyesTrinket(Rarity.RARE, props()));
    public static final RegistryObject<Trinket> BIG_FOOT = trinket("big_foot", () -> new BigFootTrinket(Rarity.RARE, props()));

    public static final RegistryObject<Trinket> BOOK_O_ENCHANTING = trinket("book_o_enchanting", () -> new Trinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> WARM_VOID = trinket("warm_void", () -> new WarmVoidTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> GOLDEN_MELON = trinket("golden_melon", () -> new GoldenMelonTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> WITHER_NAIL = trinket("wither_nail", () -> new WitherNailTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> SERPENT_TOOTH = trinket("serpent_tooth", () -> new SerpentToothTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> MAD_PIGGY = trinket("mad_piggy", () -> new MadPiggyTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> MINDS_EYE = trinket("minds_eye", () -> new Trinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> GOLDEN_SWATTER = trinket("golden_swatter", () -> new GoldenSwatterTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> STICKY_MIND = trinket("sticky_mind", () -> new StickyMindTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> FIRE_MIND = trinket("fire_mind", () -> new FireMindTrinket(Rarity.MASTER, props()));
    public static final RegistryObject<Trinket> THA_GOLEM = trinket("tha_golem", () -> new Trinket(Rarity.MASTER, props()).add(Attributes.KNOCKBACK_RESISTANCE, "afb13d18-56f2-4e1f-8281-8cc7e3005eef", 1.0D));
    public static final RegistryObject<Trinket> DRAGON_BREATH = trinket("dragon_breath", () -> new DragonBreathTrinket(Rarity.MASTER, props()));

    public static final RegistryObject<Trinket> KARMA = trinket("karma", () -> new KarmaTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> DARK_DAGGER = trinket("dark_dagger", () -> new DarkDaggerTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> STARFISH = trinket("starfish", () -> new StarfishTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> DROP_SPINDLE = trinket("drop_spindle", () -> new DropSpindleTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> EMBER = trinket("ember", () -> new EmberTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> TEA_LEAF = trinket("tea_leaf", () -> new TeaLeafTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> COFFEE_BEAN = trinket("coffee_bean", () -> new CoffeeBeanTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> OXALIS = trinket("oxalis", () -> new OxalisTrinket(Rarity.ELITE, props()));
    public static final RegistryObject<Trinket> GOLDEN_SKULL = trinket("golden_skull", () -> new GoldenSkullTrinket(Rarity.ELITE, props()));

    public static final RegistryObject<Trinket> DARK_EGG = trinket("dark_egg", () -> new DarkEggTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> PILLOW_OF_SECRETS = trinket("pillow_of_secrets", () -> new PillowOfSecretsTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> THA_SPIRIT = trinket("tha_spirit", () -> new ThaSpiritTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> MIRROR_SHARD = trinket("mirror_shard", () -> new MirrorShardTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> MOSSY_RING = trinket("mossy_ring", () -> new MossyRingTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> MOSSY_BELT = trinket("mossy_belt", () -> new MossyBeltTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> TREASURE_RING = trinket("treasure_ring", () -> new TreasureRingTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> OCTOPICK = trinket("octopick", () -> new OctopickTrinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> SILVER_NAIL = trinket("silver_nail", () -> new Trinket(Rarity.EPIC, props()));
    public static final RegistryObject<Trinket> GLORY_SHARDS = trinket("glory_shards", () -> new Trinket(Rarity.EPIC, props()));

    public static final RegistryObject<Trinket> ASH_GLOVES = trinket("ash_gloves", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.ATTACK_SPEED, "1a71bd06-0d8b-459e-b961-fbd992d61c5d", 1024.0D));
    public static final RegistryObject<Trinket> RUBY_HEART = trinket("ruby_heart", () -> new RubyHeartTrinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> GOLDEN_HORSESHOE = trinket("golden_horseshoe", () -> new Trinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> GOLDEN_TOOTH = trinket("golden_tooth", () -> new Trinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> BROKEN_HEART_1 = trinket("broken_heart_1", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.MAX_HEALTH, "092962d0-2711-48e0-9a84-f768ea4aeeb2", 4.0D));
    public static final RegistryObject<Trinket> BROKEN_HEART_2 = trinket("broken_heart_2", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.MAX_HEALTH, "bf4f459a-d398-4cc1-a146-9d3828f2201a", 4.0D));
    public static final RegistryObject<Trinket> BROKEN_HEART_3 = trinket("broken_heart_3", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.MAX_HEALTH, "cb979db5-2f24-40b5-b2ef-4b7d29491ef4", 4.0D));
    public static final RegistryObject<Trinket> BROKEN_HEART_4 = trinket("broken_heart_4", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.MAX_HEALTH, "1816e016-b569-4258-889e-d45829628248", 4.0D));
    public static final RegistryObject<Trinket> BROKEN_HEART_5 = trinket("broken_heart_5", () -> new Trinket(Rarity.LEGENDARY, props()).add(Attributes.MAX_HEALTH, "a3fee661-c9e0-40d9-8386-ac245576bed0", 4.0D));
    public static final RegistryObject<Trinket> OCTOPUS_LEG = trinket("octopus_leg", () -> new OctopusLegTrinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> MAGICAL_HERBS = trinket("magical_herbs", () -> new MagicalHerbsTrinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> MAGICAL_FEATHERS = trinket("magical_feathers", () -> new MagicalFeathersTrinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> MAD_AURA = trinket("mad_aura", () -> new MadAuraTrinket(Rarity.LEGENDARY, props()));
    public static final RegistryObject<Trinket> BROKEN_TOTEM = trinket("broken_totem", () -> new Trinket(Rarity.LEGENDARY, props()));

    public static final RegistryObject<Item> TREASURE_BAG = REG.register("treasure_bag", () -> new TreasureBagItem(props()));

    private static Item.Properties props() {
        return new Item.Properties();
    }

    @SuppressWarnings("unchecked")
    private static RegistryObject<Trinket> trinket(String name, java.util.function.Supplier<? extends Trinket> supplier) {
        return (RegistryObject<Trinket>) (RegistryObject<?>) REG.register(name, supplier);
    }
}
