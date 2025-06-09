package com.morecreepsrevival.morecreeps.common;

import com.morecreepsrevival.morecreeps.common.capabilities.CreepsCapabilityHandler;
import com.morecreepsrevival.morecreeps.common.capabilities.IPlayerJumping;
import com.morecreepsrevival.morecreeps.common.capabilities.PlayerJumpingProvider;
import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.entity.*;
import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageDismountEntity;
import com.morecreepsrevival.morecreeps.common.networking.message.MessagePlayWelcomeSound;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageSetJumping;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import com.morecreepsrevival.morecreeps.common.world.JailManager;
import com.morecreepsrevival.morecreeps.common.world.WorldGenCastle;
import com.morecreepsrevival.morecreeps.common.world.WorldGenStructures;
import com.morecreepsrevival.morecreeps.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Mod(modid = MoreCreepsAndWeirdos.modid, name = MoreCreepsAndWeirdos.name, version = MoreCreepsAndWeirdos.version, useMetadata = true)
@EventBusSubscriber(modid = MoreCreepsAndWeirdos.modid)
public class MoreCreepsAndWeirdos {

    public static final String modid = "morecreeps";
    public static final String name = "More Creeps And Weirdos Revival";
    public static final String version = "1.0.25";

    public static final CreativeTabs creativeTab = new CreativeTabs("creepsTab") {
        public ItemStack getTabIconItem() {
            return new ItemStack(CreepsItemHandler.floobAchievement);
        }
    };
    private static final Random rand = new Random();

    @SidedProxy(clientSide = "com.morecreepsrevival.morecreeps.proxy.ClientProxy", serverSide = "com.morecreepsrevival.morecreeps.proxy.ServerProxy")
    public static IProxy proxy;

    @Instance(modid)
    private static MoreCreepsAndWeirdos INSTANCE;

    private static int entityId = 0;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                createEntity(EntityGuineaPig.class, "guineapig", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.guineaPigSpawnAmt), 1, 4, EnumCreatureType.CREATURE, 0xA38447, 0xF7F0E1, getBiomesForType(Type.FOREST, Type.PLAINS, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityTombstone.class, "tombstone", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityTrophy.class, "trophy", 0, 0, 0, EnumCreatureType.AMBIENT, 0xF6C126, 0xFBD25B),
                createEntity(EntityBabyMummy.class, "babymummy", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.babyMummySpawnAmt), 1, 4, EnumCreatureType.MONSTER, 0xDCDEA8, 0xB1F080, getBiomesForType(Type.DRY, Type.SAVANNA, Type.SANDY, Type.HOT, Type.DEAD)),
                createEntity(EntityBlackSoul.class, "blacksoul", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.blackSoulSpawnAmt), 1, 4, EnumCreatureType.MONSTER, 0x335D29, 0x000000, getBiomesForType(Type.DRY, Type.SAVANNA, Type.SANDY, Type.NETHER)),
                createEntity(EntityMummy.class, "mummy", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.mummySpawnAmt), 1, 3, EnumCreatureType.MONSTER, 0xD5C76E, 0x756918, getBiomesForType(Type.DRY, Type.SAVANNA, Type.SANDY, Type.HOT, Type.DEAD)),
                createEntity(EntityGooGoat.class, "googoat", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.gooGoatSpawnAmt), 1, 4, EnumCreatureType.CREATURE, 0x24F50F, 0xC5FFDE, getBiomesForType(Type.FOREST, Type.PLAINS, Type.HILLS, Type.MOUNTAIN, Type.SAVANNA)),
                createEntity(EntityKid.class, "kid", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.kidSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xFF5B4D, 0x9E9E9E, getBiomesForType(Type.FOREST, Type.PLAINS, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityLolliman.class, "lolliman", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.lollimanSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xFF0000, 0x00FFEC, getBiomesForType(Type.FOREST, Type.PLAINS, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityPyramidGuardian.class, "pyramid_guardian", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityEvilCreature.class, "evilcreature", 0, 1, 1, EnumCreatureType.MONSTER, 0x3BBD62, 0x90702D),
                createEntity(EntityCastleGuard.class, "castleguard", 0, 1, 2, EnumCreatureType.MONSTER, 0xFB91F2, 0xF691FB),
                createEntity(EntityCastleCritter.class, "castlecritter", 0, 1, 2, EnumCreatureType.MONSTER, 0xE32C2C, 0x000000),
                createEntity(EntityCastleKing.class, "castleking", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityG.class, "g", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.gSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xFF9700, 0x00FF08, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityRobotTed.class, "robot_ted", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.robotTedSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x0068FF, 0xA4A4A4, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityRobotTodd.class, "robot_todd", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.robotToddSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xA4A4A4, 0xFFC500, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityLawyerFromHell.class, "lawyer_from_hell", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.lawyerFromHellSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x7A7D7B, 0x000000, getBiomesForType(Type.FOREST, Type.PLAINS, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityMoney.class, "money", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityBigBaby.class, "bigbaby", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.bigBabySpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xC2B76E, 0xF8A9FF, getBiomesForType(Type.SANDY, Type.MESA, Type.FOREST)),
                createEntity(EntityShrink.class, "shrink", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntitySchlump.class, "schlump", 0, 0, 0, EnumCreatureType.CREATURE, 0x69572A, 0x000000),
                createEntity(EntityThief.class, "thief", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.thiefSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xDC9E22, 0x000000, getBiomesForType(Type.FOREST, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityFloob.class, "floob", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.floobSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x29FF17, 0xE5E7E4, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityRay.class, "ray", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityDevRay.class, "dev_ray", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityFloobShip.class, "floobship", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.floobShipSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xF9C41C, 0xEAF72A, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityHorseHead.class, "horsehead", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.horseHeadSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xFF07F3, 0x84653A, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityHotdog.class, "hotdog", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.hotdogSpawnAmt), 1, 2, EnumCreatureType.CREATURE, 0x7C5C32, 0x000000, getBiomesForType(Type.FOREST, Type.PLAINS, Type.MOUNTAIN, Type.HILLS)),
                createEntity(EntityDigBug.class, "digbug", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.digBugSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0x58BA4C, 0xE6DD58, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityBubbleScum.class, "bubblescum", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.bubbleScumSpawnAmt), 2, 4, EnumCreatureType.CREATURE, 0xCE51BE, 0x67E6ED, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntitySneakySal.class, "sneakysal", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.sneakySalSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x0E1317, 0x959595, getBiomesForType(Type.COLD, Type.SNOWY, Type.CONIFEROUS)),
                createEntity(EntityRatMan.class, "ratman", 0, 1, 2, EnumCreatureType.MONSTER, 0x565656, 0xF0A7F1),
                createEntity(EntityPrisoner.class, "prisoner", 0, 1, 1, EnumCreatureType.CREATURE, 0xFBFBFB, 0xAAAAAA),
                createEntity(EntityBullet.class, "bullet", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntitySnowDevil.class, "snowdevil", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.snowDevilSpawnAmt), 1, 2, EnumCreatureType.MONSTER, 0xFFFFFF, 0x000000, getBiomesForType(Type.SNOWY)),
                createEntity(EntityEvilChicken.class, "evilchicken", 0, 0, 0, EnumCreatureType.MONSTER, 0xC9EBAA, 0xE1E8DB),
                createEntity(EntityEvilPig.class, "evilpig", 0, 0, 0, EnumCreatureType.MONSTER, 0x5FDD5F, 0xF4AFFF),
                createEntity(EntityDogHouse.class, "doghouse", 0, 0, 0, EnumCreatureType.AMBIENT, 0x5EDAEB, 0xDBA0E9),
                createEntity(EntityBlorp.class, "blorp", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.blorpSpawnAmt), 1, 3, EnumCreatureType.MONSTER, 0xCE00FF, 0xFFB200, getBiomesForType(Type.FOREST, Type.PLAINS, Type.DENSE, Type.JUNGLE, Type.CONIFEROUS)),
                createEntity(EntityCamel.class, "camel", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.camelSpawnAmt), 1, 2, EnumCreatureType.CREATURE, 0xD6BF8C, 0xFFFFFF, getBiomesForType(Type.SANDY, Type.MESA)),
                createEntity(EntityZebra.class, "zebra", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.zebraSpawnAmt), 1, 5, EnumCreatureType.CREATURE, 0xFFFFFF, 0x000000, getBiomesForType(Type.SAVANNA, Type.MESA)),
                createEntity(EntityRocketGiraffe.class, "rocketgiraffe", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.rocketGiraffeSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xFF8E00, 0xFFEF00, getBiomesForType(Type.SANDY, Type.MESA)),
                createEntity(EntityEvilScientist.class, "evilscientist", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.evilScientistSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xD7D7D7, 0x464347, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityFrisbee.class, "frisbee", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityManDog.class, "mandog", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.mandogSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xB36209, 0xD1D8A, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityCaveman.class, "caveman", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.cavemanSpawnAmt), 1, 2, EnumCreatureType.CREATURE, 0xC2880C, 0x33D8F0, getBiomesForType(Type.COLD, Type.SNOWY)),
                createEntity(EntityEvilLight.class, "evil_light", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityRocket.class, "rocket", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityEvilEgg.class, "evilegg", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityGooDonut.class, "goodonut", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityHunchback.class, "hunchback", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.hunchbackSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0x00FF1C, 0xFEFFE9, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityHunchbackSkeleton.class, "hunchbackskeleton", 0, 0, 0, EnumCreatureType.MONSTER, 0x00FF1C, 0xFF0000),
                createEntity(EntityBum.class, "bum", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.bumSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x2B723D, 0xFFEC15, getBiomesForType(Type.FOREST, Type.HILLS, Type.PLAINS)),
                createEntity(EntityEvilSnowman.class, "evilsnowman", 0, 0, 0, EnumCreatureType.MONSTER, 0xFFFFFF, 0x000000),
                createEntity(EntityPreacher.class, "preacher", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.preacherSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x433F3F, 0xFFFFFF, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityGrowbotGregg.class, "growbot_gregg", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.growbotGreggSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0xFC61FF, 0xFF0000, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityGrow.class, "grow", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityCamelJockey.class, "cameljockey", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.camelJockeySpawnAmt), 1, 1, EnumCreatureType.MONSTER, getBiomesForType(Type.BEACH, Type.DRY, Type.HOT, Type.DEAD)),
                createEntity(EntityInvisibleMan.class, "invisible_man", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.invisibleManSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0xFFFFFF, 0xFF0000, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityPonyGirl.class, "ponygirl", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.ponyGirlSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0x74706A, 0x373532/*, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)*/),
                createEntity(EntityPony.class, "pony", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityPonyCloud.class, "ponycloud", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityRockMonster.class, "rock_monster", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.rockMonsterSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x74706A, 0x373532, getBiomesForType(Type.MOUNTAIN, Type.DRY, Type.MOUNTAIN, Type.PLAINS)),
                createEntity(EntityVHS.class, "vhs", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.vhsSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x858585, 0x4D4D4D, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityS.class, "s", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.moneyManSSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x1F1F1F, 0x87AE73, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityDesertLizard.class, "desert_lizard", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.desertLizardSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x1C5300, 0x609445, getBiomesForType(Type.HOT, Type.DEAD, Type.DRY, Type.SAVANNA, Type.SANDY)),
                createEntity(EntityHippo.class, "hippo", MoreCreepsConfig.calculateSpawnRate(MoreCreepsConfig.hippoSpawnAmt), 1, 1, EnumCreatureType.CREATURE, 0x858585, 0x4D4D4D, getBiomesForType(Type.PLAINS, Type.HOT, Type.WET, Type.BEACH, Type.SAVANNA, Type.SWAMP, Type.RIVER))
        );
    }

    public static EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, int primaryColor, int secondaryColor, Biome... biomes) {
        EntityEntryBuilder<?> builder = EntityEntryBuilder.create().entity(classz).name(modid + "." + name).id(new ResourceLocation(modid, name), entityId++).tracker(40, 1, true);

        if (EntityCreepBase.class.isAssignableFrom(classz)) {
            builder.spawn(creatureType, weight, min, max, biomes);
        }

        if (primaryColor > -1 && secondaryColor > -1) {
            builder.egg(primaryColor, secondaryColor);
        }

        return builder.build();
    }

    public static EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, int primaryColor, int secondaryColor) {
        return createEntity(classz, name, weight, min, max, creatureType, primaryColor, secondaryColor, Biomes.VOID);
    }

    public static EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, Biome... biomes) {
        return createEntity(classz, name, weight, min, max, creatureType, -1, -1, biomes);
    }

    public static EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType) {
        return createEntity(classz, name, weight, min, max, creatureType, -1, -1, Biomes.VOID);
    }

    public static Biome[] getBiomesForType(Type... types) {
        ArrayList<Biome> biomes = new ArrayList<>();

        for (Type type : types) {
            for (Biome biome : BiomeDictionary.getBiomes(type)) {
                if ((MoreCreepsConfig.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft")) {
                    biomes.add(biome);
                }
            }
        }

        int size = biomes.size();

        Biome[] biomesArray = new Biome[size];

        for (int i = 0; i < size; i++) {
            biomesArray[i] = biomes.get(i);
        }

        return biomesArray;
    }

    public static Biome[] getBiomesNotType(Type... types) {
        ArrayList<Biome> biomes = new ArrayList<>();

        HashSet<Type> typesHash = new HashSet<>(Arrays.asList(types));

        for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
            boolean skip = false;

            for (Type type : BiomeDictionary.getTypes(biome)) {
                if (typesHash.contains(type)) {
                    skip = true;

                    break;
                }
            }

            if (!skip && ((MoreCreepsConfig.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft"))) {
                biomes.add(biome);
            }
        }

        int size = biomes.size();

        Biome[] biomesArray = new Biome[size];

        for (int i = 0; i < size; i++) {
            biomesArray[i] = biomes.get(i);
        }

        return biomesArray;
    }

    public static Biome[] getAllBiomes() {
        Collection<Biome> biomes = ForgeRegistries.BIOMES.getValuesCollection();

        Biome[] biomesArray = new Biome[biomes.size()];

        int i = 0;

        for (Biome biome : biomes) {
            if ((MoreCreepsConfig.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft")) {
                biomesArray[i++] = biome;
            }
        }

        return biomesArray;
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void loadWorld(WorldEvent.Load event) {
        JailManager.tryCasheStructure(event.getWorld());
        WorldGenCastle.tryCasheStructure(event.getWorld());
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (MoreCreepsConfig.sendWelcomeMessage) {
            event.player.sendMessage(new TextComponentTranslation("other.morecreeps.welcome." + rand.nextInt(35)));
        }

        if (MoreCreepsConfig.playWelcomeSound) {
            CreepsPacketHandler.INSTANCE.sendTo(new MessagePlayWelcomeSound(), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void playerArchievementEvent(AdvancementEvent event) {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote) return;

        //((EntityPlayerMP) player).getAdvancements().getProgress()

        String domain = event.getAdvancement().getId().getResourceDomain();

        String advancementname = event.getAdvancement().getId().getResourcePath();

        if (!domain.equals("morecreeps") || advancementname.equals("root")) return;

        EntityTrophy trophy = new EntityTrophy(player.world);

        trophy.positionCorrectlyAround(player);

        trophy.playSound(CreepsSoundHandler.achievementSound, 1.0f, 1.0f);

        player.world.spawnEntity(trophy);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.CLIENT) {
            return;
        }

        IPlayerJumping capability = event.player.getCapability(PlayerJumpingProvider.capability, null);

        if (capability != null) {
            boolean isJumping = proxy.isJumpKeyDown(event.player);

            if (isJumping != capability.getJumping()) {
                CreepsPacketHandler.INSTANCE.sendToServer(new MessageSetJumping(isJumping));

                capability.setJumping(isJumping);
            }
        }
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (Mouse.getEventButton() == 1) {
            for (Entity entity : minecraft.player.getPassengers()) {
                if (entity instanceof EntityBubbleScum && ((EntityBubbleScum) entity).getUnmountTimer() < 1) {
                    entity.dismountRidingEntity();

                    CreepsPacketHandler.INSTANCE.sendToServer(new MessageDismountEntity(entity.getEntityId()));

                    break;
                }
            }
        }
    }

    public static boolean isBlackFriday() {
        LocalDate now = LocalDate.now();

        return LocalDate.of(now.getYear(), 11, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)).with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).equals(now);
    }

    @EventHandler
    public static void serverStopping(FMLServerStoppingEvent event) {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            for (Entity entity : player.getPassengers()) {
                if (entity instanceof EntityCreepBase) {
                    EntityCreepBase creep = (EntityCreepBase) entity;

                    creep.cloneEntity();
                }
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MoreCreepsConfig.preInit(event);
        CreepsItemHandler.initItems();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CreepsPacketHandler.registerMessages();

        CreepsCapabilityHandler.registerCapabilities();

        GameRegistry.registerWorldGenerator(new WorldGenStructures(), 0);

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static MoreCreepsAndWeirdos getInstance() {
        return INSTANCE;
    }

}