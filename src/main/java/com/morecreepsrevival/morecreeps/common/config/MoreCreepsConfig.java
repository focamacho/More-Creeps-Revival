package com.morecreepsrevival.morecreeps.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MoreCreepsConfig {
    private static final String miscProperty = "Misc Property";
    private static final String spawnNbr = "Spawn Number";
    private static final String mobProperty = "Mob Property";
    private static final String itemProperty = "Item Property";
    private static final String worldGen = "World Gen";
    private static final String worldGenRarity = "World Gen Rarity";
    public static boolean pyramidGen = false;
    public static int pyramidRarity = 0;
    public static boolean castleGen = false;
    public static int castleRarity = 0;
    public static boolean rayGunFire = false;
    public static boolean rayGunMelt = false;
    public static boolean floobTargetVillagers = true;
    public static boolean floobShipExplode = false;
    public static boolean publicUrination = true;
    public static boolean jailActive = true;
    public static boolean blood = true;
    public static int blorpMaxSize = 0;
    public static int guineaPigSpawnAmt = 0;
    public static int babyMummySpawnAmt = 0;
    public static int blackSoulSpawnAmt = 0;
    public static int mummySpawnAmt = 0;
    public static int armyGuySpawnAmt = 0;
    public static int hotdogSpawnAmt = 0;
    public static int gooGoatSpawnAmt = 0;
    public static int kidSpawnAmt = 0;
    public static int lollimanSpawnAmt = 0;
    public static int gSpawnAmt = 0;
    public static int robotTedSpawnAmt = 0;
    public static int robotToddSpawnAmt = 0;
    public static int lawyerFromHellSpawnAmt = 0;
    public static boolean playWelcomeSound = true;
    public static int bigBabySpawnAmt = 0;
    public static int thiefSpawnAmt = 0;
    public static int floobSpawnAmt = 0;
    public static int floobShipSpawnAmt = 0;
    public static boolean sendWelcomeMessage = true;
    public static boolean sendVersionInfo = true;
    public static int horseHeadSpawnAmt = 0;
    public static int digBugSpawnAmt = 0;
    public static int bubbleScumSpawnAmt = 0;
    public static int sneakySalSpawnAmt = 0;
    public static int snowDevilSpawnAmt = 0;
    public static boolean classicMode = true;
    public static int blorpSpawnAmt = 0;
    public static int camelSpawnAmt = 0;
    public static int camelJockeySpawnAmt = 0;
    public static int bumSpawnAmt = 0;
    public static int cavemanSpawnAmt = 0;
    public static int desertLizardSpawnAmt = 0;
    public static int evilScientistSpawnAmt = 0;
    public static int hippoSpawnAmt = 0;
    public static int hunchbackSpawnAmt = 0;
    public static int invisibleManSpawnAmt = 0;
    public static int mandogSpawnAmt = 0;
    public static int nonSwimmerSpawnAmt = 0;
    public static int preacherSpawnAmt = 0;
    public static int rockMonsterSpawnAmt = 0;
    public static int rocketGiraffeSpawnAmt = 0;
    public static int zebraSpawnAmt = 0;
    public static int growbotGreggSpawnAmt = 0;
    public static int ponyGirlSpawnAmt = 0;
    public static int vhsSpawnAmt = 0;
    public static int moneyManSSpawnAmt = 0;
    public static String hideUpdateGuiVersion = "";
    public static double globalSpawnRate = 0.75f;
    public static boolean spawnInNonVanillaBiomes = false;
    public static String[] nonVanillaBiomes = new String[]{};
    private static Configuration config;
    private static boolean unlimitedSpawn = false;

    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());

        try {
            config.load();

            playWelcomeSound = config.get(miscProperty, "Play Welcome Sound", true, "Toggles the playing of the welcome sound when you load the world.").getBoolean();

            sendVersionInfo = config.get(miscProperty, "Send Version Info", true, "Sends the current version of MoreCreeps in the chat when a player joins.").getBoolean();

            sendWelcomeMessage = config.get(miscProperty, "Send Welcome Message", true, "Sends a welcome message in the chat to each player joining.").getBoolean();

            classicMode = config.get(miscProperty, "Classic Mode", true, "Keep this on if you want to play the mod the way Freakstritch intended it. Turning this off will add additional content the original mod didn't have.").getBoolean();

            unlimitedSpawn = config.get("Spawn", "Unlimited Spawn Length", false, "Once activated, you can set what value you want for mob spawning.").getBoolean();

            globalSpawnRate = config.get("Spawn", "Global Spawn Rate", 0.75f, "This setting controls the overall spawn rate. The spawn rate of every mob is multiplied by this number. So 0.5 is the default rate, 1.0 is 2x the rate, 0.25 is half the rate, etc.").getDouble();

            spawnInNonVanillaBiomes = config.get("Spawn", "Spawn In Non-Vanilla Biomes", false, "Setting this to true will cause More Creeps mobs to spawn in biomes added by other mods.").getBoolean();

            nonVanillaBiomes = config.get("Spawn", "Non-Vanilla Biomes", new String[]{}, "This a list of Non-Vanilla biomes that More Creeps mobs should spawn in.").getStringList();

            pyramidGen = config.get(worldGen, "Enable Pyramid Gen", true).getBoolean();

            pyramidRarity = config.get(worldGenRarity, "Pyramid Rarity", 500, "By default : 500").getInt();

            castleGen = config.get(worldGen, "Enable Castle", true).getBoolean();

            castleRarity = config.get(worldGenRarity, "Castle Rarity", 1000, "By default : 1000").getInt();

            rayGunFire = config.get(itemProperty, "Enable Raygun Fire", true).getBoolean();

            rayGunMelt = config.get(itemProperty, "Enable Raygun Melt", true).getBoolean();

            floobShipExplode = config.get(mobProperty, "Allow Floobship Explosion", false).getBoolean();

            floobTargetVillagers = config.get(mobProperty, "Allow Floobs to Target Villagers", true).getBoolean();

            publicUrination = config.get(mobProperty, "Allow Bum Public Urination", true).getBoolean();

            jailActive = config.get(worldGen, "Enable Jail", true).getBoolean();

            blood = config.get(mobProperty, "Enable Blood", true).getBoolean();

            blorpMaxSize = config.get(mobProperty, "Blorp Max Size", 4).getInt();

            guineaPigSpawnAmt = config.get(spawnNbr, "Guinea Pig", 5).getInt();

            babyMummySpawnAmt = config.get(spawnNbr, "Baby Mummy", 1).getInt();

            blackSoulSpawnAmt = config.get(spawnNbr, "Black Soul", 1).getInt();

            mummySpawnAmt = config.get(spawnNbr, "Mummy", 1).getInt();

            armyGuySpawnAmt = config.get(spawnNbr, "Army Guy", 1).getInt();

            hotdogSpawnAmt = config.get(spawnNbr, "Hot Dog", 5).getInt();

            gooGoatSpawnAmt = config.get(spawnNbr, "Goo Goat", 3).getInt();

            kidSpawnAmt = config.get(spawnNbr, "Kid", 1).getInt();

            lollimanSpawnAmt = config.get(spawnNbr, "Lolliman", 1).getInt();

            gSpawnAmt = config.get(spawnNbr, "G", 3).getInt();

            robotTedSpawnAmt = config.get(spawnNbr, "Robot Ted", 2).getInt();

            robotToddSpawnAmt = config.get(spawnNbr, "Robot Todd", 2).getInt();

            lawyerFromHellSpawnAmt = config.get(spawnNbr, "Lawyer From Hell", 3).getInt();

            bigBabySpawnAmt = config.get(spawnNbr, "Big Baby", 4).getInt();

            thiefSpawnAmt = config.get(spawnNbr, "Thief", 3).getInt();

            floobSpawnAmt = config.get(spawnNbr, "Floob", 5).getInt();

            floobShipSpawnAmt = config.get(spawnNbr, "Floob Ship", 1).getInt();

            horseHeadSpawnAmt = config.get(spawnNbr, "Horse Head", 1).getInt();

            digBugSpawnAmt = config.get(spawnNbr, "Dig Bug", 3).getInt();

            bubbleScumSpawnAmt = config.get(spawnNbr, "Bubble Scum", 5).getInt();

            sneakySalSpawnAmt = config.get(spawnNbr, "Sneaky Sal", 1).getInt();

            snowDevilSpawnAmt = config.get(spawnNbr, "Snow Devil", 3).getInt();

            blorpSpawnAmt = config.get(spawnNbr, "Blorp", 3).getInt();

            camelSpawnAmt = config.get(spawnNbr, "Camel", 1).getInt();

            camelJockeySpawnAmt = config.get(spawnNbr, "Camel Jockey", 1).getInt();

            bumSpawnAmt = config.get(spawnNbr, "Bum", 3).getInt();

            cavemanSpawnAmt = config.get(spawnNbr, "Caveman", 1).getInt();

            desertLizardSpawnAmt = config.get(spawnNbr, "Desert Lizard", 4).getInt();

            evilScientistSpawnAmt = config.get(spawnNbr, "Evil Scientist", 3).getInt();

            hippoSpawnAmt = config.get(spawnNbr, "Hippo", 3).getInt();

            hunchbackSpawnAmt = config.get(spawnNbr, "Hunchback", 2).getInt();

            invisibleManSpawnAmt = config.get(spawnNbr, "Invisible Man", 2).getInt();

            mandogSpawnAmt = config.get(spawnNbr, "Mandog", 1).getInt();

            nonSwimmerSpawnAmt = config.get(spawnNbr, "Non Swimmer", 1).getInt();

            preacherSpawnAmt = config.get(spawnNbr, "Preacher", 1).getInt();

            rockMonsterSpawnAmt = config.get(spawnNbr, "Rock Monster", 1).getInt();

            rocketGiraffeSpawnAmt = config.get(spawnNbr, "Rocket Giraffe", 4).getInt();

            zebraSpawnAmt = config.get(spawnNbr, "Zebra", 5).getInt();

            growbotGreggSpawnAmt = config.get(spawnNbr, "Growbot Gregg", 1).getInt();

            ponyGirlSpawnAmt = config.get(spawnNbr, "Pony Girl", 1).getInt();

            moneyManSSpawnAmt = config.get(spawnNbr, "Money Man S", 1).getInt();

            vhsSpawnAmt = config.get(spawnNbr, "Walking VHS", 3).getInt();

            hideUpdateGuiVersion = config.get(miscProperty, "Hide Update for Version", "", "This property is set when you choose to ignore the Update Available popup with the version you're being offered.").getString();

            config.save();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }

        applySpawnLimit();
    }

    private static void applySpawnLimit() {
        if (unlimitedSpawn) {
            return;
        }

        if (moneyManSSpawnAmt < 0 || moneyManSSpawnAmt > 12) {
            moneyManSSpawnAmt = 1;
        }

        if (vhsSpawnAmt < 0 || vhsSpawnAmt > 12) {
            vhsSpawnAmt = 3;
        }

        if (armyGuySpawnAmt < 0 || armyGuySpawnAmt > 12) {
            armyGuySpawnAmt = 1;
        }

        if (babyMummySpawnAmt < 0 || babyMummySpawnAmt > 12) {
            babyMummySpawnAmt = 1;
        }

        if (bigBabySpawnAmt < 0 || bigBabySpawnAmt > 12) {
            bigBabySpawnAmt = 4;
        }

        if (blorpSpawnAmt < 0 || blorpSpawnAmt > 12) {
            blorpSpawnAmt = 3;
        }

        if (bubbleScumSpawnAmt < 0 || bubbleScumSpawnAmt > 12) {
            bubbleScumSpawnAmt = 5;
        }

        if (bumSpawnAmt < 0 || bumSpawnAmt > 12) {
            bumSpawnAmt = 3;
        }

        if (camelSpawnAmt < 0 || camelSpawnAmt > 12) {
            camelSpawnAmt = 1;
        }

        if (camelJockeySpawnAmt < 0 || camelJockeySpawnAmt > 12) {
            camelJockeySpawnAmt = 1;
        }

        if (cavemanSpawnAmt < 0 || cavemanSpawnAmt > 12) {
            cavemanSpawnAmt = 1;
        }

        if (desertLizardSpawnAmt < 0 || desertLizardSpawnAmt > 12) {
            desertLizardSpawnAmt = 4;
        }

        if (digBugSpawnAmt < 0 || digBugSpawnAmt > 12) {
            digBugSpawnAmt = 3;
        }

        if (evilScientistSpawnAmt < 0 || evilScientistSpawnAmt > 12) {
            evilScientistSpawnAmt = 3;
        }

        if (floobSpawnAmt < 0 || floobShipSpawnAmt > 12) {
            floobSpawnAmt = 5;
        }

        if (floobShipSpawnAmt < 0 || floobShipSpawnAmt > 12) {
            floobShipSpawnAmt = 1;
        }

        if (gSpawnAmt < 0 || gSpawnAmt > 12) {
            gSpawnAmt = 3;
        }

        if (gooGoatSpawnAmt < 0 || gooGoatSpawnAmt > 12) {
            gooGoatSpawnAmt = 3;
        }

        if (guineaPigSpawnAmt < 0 || guineaPigSpawnAmt > 12) {
            guineaPigSpawnAmt = 5;
        }

        if (hippoSpawnAmt < 0 || hippoSpawnAmt > 12) {
            hippoSpawnAmt = 3;
        }

        if (horseHeadSpawnAmt < 0 || horseHeadSpawnAmt > 12) {
            horseHeadSpawnAmt = 1;
        }

        if (hunchbackSpawnAmt < 0 || hunchbackSpawnAmt > 12) {
            hunchbackSpawnAmt = 2;
        }

        if (invisibleManSpawnAmt < 0 || invisibleManSpawnAmt > 12) {
            invisibleManSpawnAmt = 2;
        }

        if (kidSpawnAmt < 0 || kidSpawnAmt > 12) {
            kidSpawnAmt = 1;
        }

        if (lawyerFromHellSpawnAmt < 0 || lawyerFromHellSpawnAmt > 12) {
            lawyerFromHellSpawnAmt = 3;
        }

        if (lollimanSpawnAmt < 0 || lollimanSpawnAmt > 12) {
            lollimanSpawnAmt = 1;
        }

        if (mandogSpawnAmt < 0 || mandogSpawnAmt > 12) {
            mandogSpawnAmt = 1;
        }

        if (mummySpawnAmt < 0 || mummySpawnAmt > 12) {
            mummySpawnAmt = 1;
        }

        if (nonSwimmerSpawnAmt < 0 || nonSwimmerSpawnAmt > 12) {
            nonSwimmerSpawnAmt = 1;
        }

        if (sneakySalSpawnAmt < 0 || sneakySalSpawnAmt > 24) {
            sneakySalSpawnAmt = 1;
        }

        if (preacherSpawnAmt < 0 || preacherSpawnAmt > 12) {
            preacherSpawnAmt = 1;
        }

        if (robotTedSpawnAmt < 0 || robotTedSpawnAmt > 12) {
            robotTedSpawnAmt = 2;
        }

        if (robotToddSpawnAmt < 0 || robotToddSpawnAmt > 12) {
            robotToddSpawnAmt = 2;
        }

        if (rockMonsterSpawnAmt < 0 || rockMonsterSpawnAmt > 5) {
            rockMonsterSpawnAmt = 1;
        }

        if (rocketGiraffeSpawnAmt < 0 || rocketGiraffeSpawnAmt > 12) {
            rocketGiraffeSpawnAmt = 4;
        }

        if (snowDevilSpawnAmt < 0 || snowDevilSpawnAmt > 12) {
            snowDevilSpawnAmt = 3;
        }

        if (thiefSpawnAmt < 0 || thiefSpawnAmt > 12) {
            thiefSpawnAmt = 3;
        }

        if (zebraSpawnAmt < 0 || zebraSpawnAmt > 12) {
            zebraSpawnAmt = 5;
        }

        if (growbotGreggSpawnAmt < 0 || growbotGreggSpawnAmt > 12) {
            growbotGreggSpawnAmt = 1;
        }

        if (ponyGirlSpawnAmt < 0 || ponyGirlSpawnAmt > 10) {
            ponyGirlSpawnAmt = 1;
        }

        if (pyramidRarity < 1 || pyramidRarity > 10) {
            pyramidRarity = 5;
        }

        if (castleRarity < 1 || castleRarity > 10) {
            castleRarity = 2;
        }

        if (blorpMaxSize < 4 || blorpMaxSize > 99) {
            blorpMaxSize = 4;
        }
    }

    public static boolean shouldShowUpdateGuiForVersion(String version) {
        return (!hideUpdateGuiVersion.equals(version));
    }

    public static void hideUpdateGuiForVersion(String version) {
        try {
            config.get(miscProperty, "Hide Update for Version", "").set(version);

            config.save();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static int calculateSpawnRate(int baseRate) {
        return (int) Math.floor(baseRate * globalSpawnRate);
    }

    public static boolean hasBiome(String biome) {
        for (String str : nonVanillaBiomes) {
            if (str.equals(biome)) {
                return true;
            }
        }

        return false;
    }
}