package com.morecreepsrevival.morecreeps.common.handlers;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class CreepsEntityHandler {  
    
    private int entityId = 0;

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                createEntity(EntityGuineaPig.class, "guineapig", MoreCreepsConfig.SpawnNumbers.guineaPigSpawnAmt, 1, 4, EnumCreatureType.CREATURE, 0xA38447, 0xF7F0E1, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityTombstone.class, "tombstone", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityTrophy.class, "trophy", 0, 0, 0, EnumCreatureType.AMBIENT, 0xF6C126, 0xFBD25B),
                createEntity(EntityBabyMummy.class, "babymummy", MoreCreepsConfig.SpawnNumbers.babyMummySpawnAmt, 1, 4, EnumCreatureType.MONSTER, 0xDCDEA8, 0xB1F080, getBiomesForType(BiomeDictionary.Type.DRY, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD)),
                createEntity(EntityBlackSoul.class, "blacksoul", MoreCreepsConfig.SpawnNumbers.blackSoulSpawnAmt, 1, 4, EnumCreatureType.MONSTER, 0x335D29, 0x000000, getBiomesForType(BiomeDictionary.Type.DRY, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.NETHER)),
                createEntity(EntityMummy.class, "mummy", MoreCreepsConfig.SpawnNumbers.mummySpawnAmt, 1, 3, EnumCreatureType.MONSTER, 0xD5C76E, 0x756918, getBiomesForType(BiomeDictionary.Type.DRY, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD)),
                createEntity(EntityGooGoat.class, "googoat", MoreCreepsConfig.SpawnNumbers.gooGoatSpawnAmt, 1, 4, EnumCreatureType.CREATURE, 0x24F50F, 0xC5FFDE, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SAVANNA)),
                createEntity(EntityKid.class, "kid", MoreCreepsConfig.SpawnNumbers.kidSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xFF5B4D, 0x9E9E9E, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityLolliman.class, "lolliman", MoreCreepsConfig.SpawnNumbers.lollimanSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xFF0000, 0x00FFEC, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityPyramidGuardian.class, "pyramid_guardian", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityEvilCreature.class, "evilcreature", 0, 1, 1, EnumCreatureType.MONSTER, 0x3BBD62, 0x90702D),
                createEntity(EntityCastleGuard.class, "castleguard", 0, 1, 2, EnumCreatureType.MONSTER, 0xFB91F2, 0xF691FB),
                createEntity(EntityCastleCritter.class, "castlecritter", 0, 1, 2, EnumCreatureType.MONSTER, 0xE32C2C, 0x000000),
                createEntity(EntityCastleKing.class, "castleking", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityG.class, "g", MoreCreepsConfig.SpawnNumbers.gSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xFF9700, 0x00FF08, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityRobotTed.class, "robot_ted", MoreCreepsConfig.SpawnNumbers.robotTedSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x0068FF, 0xA4A4A4, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityRobotTodd.class, "robot_todd", MoreCreepsConfig.SpawnNumbers.robotToddSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xA4A4A4, 0xFFC500, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityLawyerFromHell.class, "lawyer_from_hell", MoreCreepsConfig.SpawnNumbers.lawyerFromHellSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x7A7D7B, 0x000000, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityMoney.class, "money", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityBigBaby.class, "bigbaby", MoreCreepsConfig.SpawnNumbers.bigBabySpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xC2B76E, 0xF8A9FF, getBiomesForType(BiomeDictionary.Type.SANDY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.FOREST)),
                createEntity(EntityShrink.class, "shrink", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntitySchlump.class, "schlump", 0, 0, 0, EnumCreatureType.CREATURE, 0x69572A, 0x000000),
                createEntity(EntityThief.class, "thief", MoreCreepsConfig.SpawnNumbers.thiefSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xDC9E22, 0x000000, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityFloob.class, "floob", MoreCreepsConfig.SpawnNumbers.floobSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x29FF17, 0xE5E7E4, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityRay.class, "ray", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityDevRay.class, "dev_ray", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityFloobShip.class, "floobship", MoreCreepsConfig.SpawnNumbers.floobShipSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xF9C41C, 0xEAF72A, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityHorseHead.class, "horsehead", MoreCreepsConfig.SpawnNumbers.horseHeadSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xFF07F3, 0x84653A, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityHotdog.class, "hotdog", MoreCreepsConfig.SpawnNumbers.hotdogSpawnAmt, 1, 2, EnumCreatureType.CREATURE, 0x7C5C32, 0x000000, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)),
                createEntity(EntityDigBug.class, "digbug", MoreCreepsConfig.SpawnNumbers.digBugSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0x58BA4C, 0xE6DD58, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityBubbleScum.class, "bubblescum", MoreCreepsConfig.SpawnNumbers.bubbleScumSpawnAmt, 2, 4, EnumCreatureType.CREATURE, 0xCE51BE, 0x67E6ED, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntitySneakySal.class, "sneakysal", MoreCreepsConfig.SpawnNumbers.sneakySalSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x0E1317, 0x959595, getBiomesForType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.CONIFEROUS)),
                createEntity(EntityRatMan.class, "ratman", 0, 1, 2, EnumCreatureType.MONSTER, 0x565656, 0xF0A7F1),
                createEntity(EntityPrisoner.class, "prisoner", 0, 1, 1, EnumCreatureType.CREATURE, 0xFBFBFB, 0xAAAAAA),
                createEntity(EntityBullet.class, "bullet", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntitySnowDevil.class, "snowdevil", MoreCreepsConfig.SpawnNumbers.snowDevilSpawnAmt, 1, 2, EnumCreatureType.MONSTER, 0xFFFFFF, 0x000000, getBiomesForType(BiomeDictionary.Type.SNOWY)),
                createEntity(EntityEvilChicken.class, "evilchicken", 0, 0, 0, EnumCreatureType.MONSTER, 0xC9EBAA, 0xE1E8DB),
                createEntity(EntityEvilPig.class, "evilpig", 0, 0, 0, EnumCreatureType.MONSTER, 0x5FDD5F, 0xF4AFFF),
                createEntity(EntityDogHouse.class, "doghouse", 0, 0, 0, EnumCreatureType.AMBIENT, 0x5EDAEB, 0xDBA0E9),
                createEntity(EntityBlorp.class, "blorp", MoreCreepsConfig.SpawnNumbers.blorpSpawnAmt, 1, 3, EnumCreatureType.MONSTER, 0xCE00FF, 0xFFB200, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.CONIFEROUS)),
                createEntity(EntityCamel.class, "camel", MoreCreepsConfig.SpawnNumbers.camelSpawnAmt, 1, 2, EnumCreatureType.CREATURE, 0xD6BF8C, 0xFFFFFF, getBiomesForType(BiomeDictionary.Type.SANDY, BiomeDictionary.Type.MESA)),
                createEntity(EntityZebra.class, "zebra", MoreCreepsConfig.SpawnNumbers.zebraSpawnAmt, 1, 5, EnumCreatureType.CREATURE, 0xFFFFFF, 0x000000, getBiomesForType(BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.MESA)),
                createEntity(EntityRocketGiraffe.class, "rocketgiraffe", MoreCreepsConfig.SpawnNumbers.rocketGiraffeSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xFF8E00, 0xFFEF00, getBiomesForType(BiomeDictionary.Type.SANDY, BiomeDictionary.Type.MESA)),
                createEntity(EntityEvilScientist.class, "evilscientist", MoreCreepsConfig.SpawnNumbers.evilScientistSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xD7D7D7, 0x464347, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityFrisbee.class, "frisbee", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityManDog.class, "mandog", MoreCreepsConfig.SpawnNumbers.mandogSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xB36209, 0xD1D8A, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityCaveman.class, "caveman", MoreCreepsConfig.SpawnNumbers.cavemanSpawnAmt, 1, 2, EnumCreatureType.CREATURE, 0xC2880C, 0x33D8F0, getBiomesForType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY)),
                createEntity(EntityEvilLight.class, "evil_light", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityRocket.class, "rocket", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityEvilEgg.class, "evilegg", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityGooDonut.class, "goodonut", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityHunchback.class, "hunchback", MoreCreepsConfig.SpawnNumbers.hunchbackSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0x00FF1C, 0xFEFFE9, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityHunchbackSkeleton.class, "hunchbackskeleton", 0, 0, 0, EnumCreatureType.MONSTER, 0x00FF1C, 0xFF0000),
                createEntity(EntityBum.class, "bum", MoreCreepsConfig.SpawnNumbers.bumSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x2B723D, 0xFFEC15, getBiomesForType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.PLAINS)),
                createEntity(EntityEvilSnowman.class, "evilsnowman", 0, 0, 0, EnumCreatureType.MONSTER, 0xFFFFFF, 0x000000),
                createEntity(EntityPreacher.class, "preacher", MoreCreepsConfig.SpawnNumbers.preacherSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x433F3F, 0xFFFFFF, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityGrowbotGregg.class, "growbot_gregg", MoreCreepsConfig.SpawnNumbers.growbotGreggSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0xFC61FF, 0xFF0000, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityGrow.class, "grow", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityCamelJockey.class, "cameljockey", MoreCreepsConfig.SpawnNumbers.camelJockeySpawnAmt, 1, 1, EnumCreatureType.MONSTER, getBiomesForType(BiomeDictionary.Type.BEACH, BiomeDictionary.Type.DRY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD)),
                createEntity(EntityInvisibleMan.class, "invisible_man", MoreCreepsConfig.SpawnNumbers.invisibleManSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0xFFFFFF, 0xFF0000, getBiomesNotType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END)),
                createEntity(EntityPonyGirl.class, "ponygirl", MoreCreepsConfig.SpawnNumbers.ponyGirlSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0x74706A, 0x373532/*, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)*/),
                createEntity(EntityPony.class, "pony", 0, 0, 0, EnumCreatureType.CREATURE),
                createEntity(EntityPonyCloud.class, "ponycloud", 0, 0, 0, EnumCreatureType.AMBIENT),
                createEntity(EntityRockMonster.class, "rock_monster", MoreCreepsConfig.SpawnNumbers.rockMonsterSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x74706A, 0x373532, getBiomesForType(BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.PLAINS)),
                //createEntity(EntityVHS.class, "vhs", MoreCreepsConfig.SpawnNumbers.vhsSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x858585, 0x4D4D4D, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                //createEntity(EntityS.class, "s", MoreCreepsConfig.SpawnNumbers.moneyManSSpawnAmt), 1, 1, EnumCreatureType.MONSTER, 0x1F1F1F, 0x87AE73, getBiomesNotType(Type.COLD, Type.SNOWY, Type.NETHER, Type.END)),
                createEntity(EntityDesertLizard.class, "desert_lizard", MoreCreepsConfig.SpawnNumbers.desertLizardSpawnAmt, 1, 1, EnumCreatureType.MONSTER, 0x1C5300, 0x609445, getBiomesForType(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SANDY)),
                createEntity(EntityHippo.class, "hippo", MoreCreepsConfig.SpawnNumbers.hippoSpawnAmt, 1, 1, EnumCreatureType.CREATURE, 0x858585, 0x4D4D4D, getBiomesForType(BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.RIVER))
        );
    }

    public EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, int primaryColor, int secondaryColor, Biome... biomes) {
        EntityEntryBuilder<?> builder = EntityEntryBuilder.create()
                .entity(classz)
                .name(MoreCreepsAndWeirdos.modid + "." + name)
                .id(new ResourceLocation(MoreCreepsAndWeirdos.modid, name), entityId++)
                .tracker(40, 1, true);

        if (EntityCreepBase.class.isAssignableFrom(classz)) {
            builder.spawn(creatureType, weight <= 0 ? weight : MoreCreepsConfig.calculateSpawnRate(weight), min, max, biomes);
        }

        if (primaryColor > -1 && secondaryColor > -1) {
            builder.egg(primaryColor, secondaryColor);
        }

        return builder.build();
    }

    public EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, int primaryColor, int secondaryColor) {
        return createEntity(classz, name, weight, min, max, creatureType, primaryColor, secondaryColor, Biomes.VOID);
    }

    public EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType, Biome... biomes) {
        return createEntity(classz, name, weight, min, max, creatureType, -1, -1, biomes);
    }

    public EntityEntry createEntity(Class<? extends Entity> classz, String name, int weight, int min, int max, EnumCreatureType creatureType) {
        return createEntity(classz, name, weight, min, max, creatureType, -1, -1, Biomes.VOID);
    }

    public Biome[] getBiomesForType(BiomeDictionary.Type... types) {
        ArrayList<Biome> biomes = new ArrayList<>();

        for (BiomeDictionary.Type type : types) {
            for (Biome biome : BiomeDictionary.getBiomes(type)) {
                if ((MoreCreepsConfig.Spawn.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft")) {
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

    public Biome[] getBiomesNotType(BiomeDictionary.Type... types) {
        ArrayList<Biome> biomes = new ArrayList<>();

        HashSet<BiomeDictionary.Type> typesHash = new HashSet<>(Arrays.asList(types));

        for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
            boolean skip = false;

            for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome)) {
                if (typesHash.contains(type)) {
                    skip = true;

                    break;
                }
            }

            if (!skip && ((MoreCreepsConfig.Spawn.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft"))) {
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
    
}
