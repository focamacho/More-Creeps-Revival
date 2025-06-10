package com.morecreepsrevival.morecreeps.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MoreCreepsConfig
{
    private static Configuration config;

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

    public static boolean floobShipExplode = false;

    public static boolean publicUrination = true;

    public static boolean jailActive = true;

    public static boolean blood = true;

    public static int blorpMaxSize = 0;

    public static int guineaPigSpawnAmt = 0;

    public static int babyMummySpawnAmt = 0;

    public static int blackSoulSpawnAmt = 0;

    public static int mummySpawnAmt = 0;

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

    public static boolean sendDiscordLink = true;

    public static int horseHeadSpawnAmt = 0;

    public static int digBugSpawnAmt = 0;

    public static int bubbleScumSpawnAmt = 0;

    public static int sneakySalSpawnAmt = 0;

    public static int snowDevilSpawnAmt = 0;

    public static int blorpSpawnAmt = 0;

    public static int camelSpawnAmt = 0;

    //public static int camelJockeySpawnAmt = 0;

    public static int bumSpawnAmt = 0;

    public static int cavemanSpawnAmt = 0;

    public static int desertLizardSpawnAmt = 0;

    public static int evilScientistSpawnAmt = 0;

    public static int hippoSpawnAmt = 0;

    public static int hunchbackSpawnAmt = 0;

    public static int invisibleManSpawnAmt = 0;

    public static int mandogSpawnAmt = 0;

    public static int preacherSpawnAmt = 0;

    public static int rockMonsterSpawnAmt = 0;

    public static int rocketGiraffeSpawnAmt = 0;

    public static int zebraSpawnAmt = 0;

    public static int growbotGreggSpawnAmt = 0;

    public static String hideUpdateGuiVersion = "";

    public static double globalSpawnRate = 1.0f;

    public static boolean spawnInNonVanillaBiomes = false;

    public static String[] nonVanillaBiomes = new String[] { };

    public static void preInit(FMLPreInitializationEvent event)
    {
        config = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            config.load();

            playWelcomeSound = config.get(miscProperty, "Play Welcome Sound", true, "Toggles the playing of the welcome sound when you load the world.").getBoolean();

            sendVersionInfo = config.get(miscProperty, "Send Version Info", true, "Sends the current version of MoreCreeps in the chat when a player joins.").getBoolean();

            sendWelcomeMessage = config.get(miscProperty, "Send Welcome Message", true, "Sends a welcome message in the chat to each player joining.").getBoolean();

            sendDiscordLink = config.get(miscProperty, "Send Discord Link", true, "Sends a link to the official MoreCreeps Discord when a player joins.").getBoolean();

            globalSpawnRate = config.get("Spawn", "Global Spawn Rate", 1.0f, "This setting controls the overall spawn rate. The spawn rate of every mob is multiplied by this number. So 1.0 is the default rate, 2.0 is 2x the rate, 0.5 is half the rate, etc.").getDouble();

            spawnInNonVanillaBiomes = config.get("Spawn", "Spawn In Non-Vanilla Biomes", true, "Setting this to true will cause More Creeps mobs to spawn in biomes added by other mods.").getBoolean();

            nonVanillaBiomes = config.get("Spawn", "Non-Vanilla Biomes", new String[] { }, "This a list of Non-Vanilla biomes that More Creeps mobs should spawn in.").getStringList();

            pyramidGen = config.get(worldGen, "Enable Pyramid Gen", true).getBoolean();

            pyramidRarity = config.get(worldGenRarity, "Pyramid Rarity", 500, "By default : 500").getInt();

            castleGen = config.get(worldGen, "Enable Castle", true).getBoolean();

            castleRarity = config.get(worldGenRarity, "Castle Rarity", 500, "By default : 500").getInt();

            rayGunFire = config.get(itemProperty, "Enable Raygun Fire", true).getBoolean();

            rayGunMelt = config.get(itemProperty, "Enable Raygun Melt", true).getBoolean();

            floobShipExplode = config.get(mobProperty, "Allow Floobship Explosion", false).getBoolean();

            publicUrination = config.get(mobProperty, "Allow Bum Public Urination", true).getBoolean();

            jailActive = config.get(worldGen, "Enable Jail", true).getBoolean();

            blood = config.get(mobProperty, "Enable Blood", true).getBoolean();

            blorpMaxSize = config.get(mobProperty, "Blorp Max Size", 6).getInt();

            guineaPigSpawnAmt = config.get(spawnNbr, "Guinea Pig", 8).getInt();

            babyMummySpawnAmt = config.get(spawnNbr, "Baby Mummy", 38).getInt();

            blackSoulSpawnAmt = config.get(spawnNbr, "Black Soul", 90).getInt();

            mummySpawnAmt = config.get(spawnNbr, "Mummy", 90).getInt();

            hotdogSpawnAmt = config.get(spawnNbr, "Hot Dog", 8).getInt();

            gooGoatSpawnAmt = config.get(spawnNbr, "Goo Goat", 8).getInt();

            kidSpawnAmt = config.get(spawnNbr, "Kid", 6).getInt();

            lollimanSpawnAmt = config.get(spawnNbr, "Lolliman", 7).getInt();

            gSpawnAmt = config.get(spawnNbr, "G", 37).getInt();

            robotTedSpawnAmt = config.get(spawnNbr, "Robot Ted", 38).getInt();

            robotToddSpawnAmt = config.get(spawnNbr, "Robot Todd", 38).getInt();

            lawyerFromHellSpawnAmt = config.get(spawnNbr, "Lawyer From Hell", 38).getInt();

            bigBabySpawnAmt = config.get(spawnNbr, "Big Baby", 22).getInt();

            thiefSpawnAmt = config.get(spawnNbr, "Thief", 38).getInt();

            floobSpawnAmt = config.get(spawnNbr, "Floob", 37).getInt();

            floobShipSpawnAmt = config.get(spawnNbr, "Floob Ship", 12).getInt();

            //horseHeadSpawnAmt = config.get(spawnNbr, "Horse Head", 8).getInt();

            digBugSpawnAmt = config.get(spawnNbr, "Dig Bug", 7).getInt();

            bubbleScumSpawnAmt = config.get(spawnNbr, "Bubble Scum", 8).getInt();

            sneakySalSpawnAmt = config.get(spawnNbr, "Sneaky Sal", 38).getInt();

            snowDevilSpawnAmt = config.get(spawnNbr, "Snow Devil", 38).getInt();

            blorpSpawnAmt = config.get(spawnNbr, "Blorp", 8).getInt();

            camelSpawnAmt = config.get(spawnNbr, "Camel", 7).getInt();

            bumSpawnAmt = config.get(spawnNbr, "Bum", 38).getInt();

            cavemanSpawnAmt = config.get(spawnNbr, "Caveman", 7).getInt();

            desertLizardSpawnAmt = config.get(spawnNbr, "Desert Lizard", 38).getInt();

            evilScientistSpawnAmt = config.get(spawnNbr, "Evil Scientist", 32).getInt();

            hippoSpawnAmt = config.get(spawnNbr, "Hippo", 7).getInt();

            hunchbackSpawnAmt = config.get(spawnNbr, "Hunchback", 37).getInt();

            invisibleManSpawnAmt = config.get(spawnNbr, "Invisible Man", 38).getInt();

            mandogSpawnAmt = config.get(spawnNbr, "Mandog", 8).getInt();

            preacherSpawnAmt = config.get(spawnNbr, "Preacher", 38).getInt();

            rockMonsterSpawnAmt = config.get(spawnNbr, "Rock Monster", 38).getInt();

            rocketGiraffeSpawnAmt = config.get(spawnNbr, "Rocket Giraffe", 7).getInt();

            zebraSpawnAmt = config.get(spawnNbr, "Zebra", 7).getInt();

            growbotGreggSpawnAmt = config.get(spawnNbr, "Growbot Gregg", 26).getInt();

            hideUpdateGuiVersion = config.get(miscProperty, "Hide Update for Version", "", "This property is set when you choose to ignore the Update Available popup with the version you're being offered.").getString();

            config.save();
        }
        finally
        {
            if (config.hasChanged())
            {
                config.save();
            }
        }

        if (babyMummySpawnAmt < 0 || babyMummySpawnAmt > 100)
        {
            babyMummySpawnAmt = 38;
        }

        if (bigBabySpawnAmt < 0 || bigBabySpawnAmt > 100)
        {
            bigBabySpawnAmt = 22;
        }

        if (blorpSpawnAmt < 0 || blorpSpawnAmt > 100)
        {
            blorpSpawnAmt = 7;
        }

        if (bubbleScumSpawnAmt < 0 || bubbleScumSpawnAmt > 100)
        {
            bubbleScumSpawnAmt = 7;
        }

        if (bumSpawnAmt < 0 || bumSpawnAmt > 100)
        {
            bumSpawnAmt = 36;
        }

        if (blackSoulSpawnAmt < 0 || blackSoulSpawnAmt > 100)
        {
            blackSoulSpawnAmt = 90;
        }

        if (camelSpawnAmt < 0 || camelSpawnAmt > 100)
        {
            camelSpawnAmt = 7;
        }

        if (cavemanSpawnAmt < 0 || cavemanSpawnAmt > 100)
        {
            cavemanSpawnAmt = 5;
        }

        if (desertLizardSpawnAmt < 0 || desertLizardSpawnAmt > 100)
        {
            desertLizardSpawnAmt = 37;
        }

        if (digBugSpawnAmt < 0 || digBugSpawnAmt > 100)
        {
            digBugSpawnAmt = 7;
        }

        if (evilScientistSpawnAmt < 0 || evilScientistSpawnAmt > 100)
        {
            evilScientistSpawnAmt = 37;
        }

        if (floobSpawnAmt < 0 || floobShipSpawnAmt > 100)
        {
            floobSpawnAmt = 37;
        }

        if (floobShipSpawnAmt < 0 || floobShipSpawnAmt > 100)
        {
            floobShipSpawnAmt = 12;
        }

        if (gSpawnAmt < 0 || gSpawnAmt > 100)
        {
            gSpawnAmt = 37;
        }

        if (gooGoatSpawnAmt < 0 || gooGoatSpawnAmt > 100)
        {
            gooGoatSpawnAmt = 7;
        }

        if (guineaPigSpawnAmt < 0 || guineaPigSpawnAmt > 100)
        {
            guineaPigSpawnAmt = 9;
        }

        if (hippoSpawnAmt < 0 || hippoSpawnAmt > 100)
        {
            hippoSpawnAmt = 7;
        }

        //if (horseHeadSpawnAmt < 0 || horseHeadSpawnAmt > 100)
        //{
          //  horseHeadSpawnAmt = 7;
        //}

        if (hunchbackSpawnAmt < 0 || hunchbackSpawnAmt > 100)
        {
            hunchbackSpawnAmt = 37;
        }

        if (invisibleManSpawnAmt < 0 || invisibleManSpawnAmt > 100)
        {
            invisibleManSpawnAmt = 37;
        }

        if (kidSpawnAmt < 0 || kidSpawnAmt > 100)
        {
            kidSpawnAmt = 6;
        }

        if (lawyerFromHellSpawnAmt < 0 || lawyerFromHellSpawnAmt > 100)
        {
            lawyerFromHellSpawnAmt = 37;
        }

        if (lollimanSpawnAmt < 0 || lollimanSpawnAmt > 100)
        {
            lollimanSpawnAmt = 7;
        }

        if (mandogSpawnAmt < 0 || mandogSpawnAmt > 100)
        {
            mandogSpawnAmt = 7;
        }

        if (mummySpawnAmt < 0 || mummySpawnAmt > 100)
        {
            mummySpawnAmt = 35;
        }

        if (sneakySalSpawnAmt < 0 || sneakySalSpawnAmt > 100)
        {
            sneakySalSpawnAmt = 36;
        }

        if (preacherSpawnAmt < 0 || preacherSpawnAmt > 100)
        {
            preacherSpawnAmt = 37;
        }

        if (robotTedSpawnAmt < 0 || robotTedSpawnAmt > 100)
        {
            robotTedSpawnAmt = 37;
        }

        if (robotToddSpawnAmt < 0 || robotToddSpawnAmt > 100)
        {
            robotToddSpawnAmt = 37;
        }

        if (rockMonsterSpawnAmt < 0 || rockMonsterSpawnAmt > 100)
        {
            rockMonsterSpawnAmt = 37;
        }

        if (rocketGiraffeSpawnAmt < 0 || rocketGiraffeSpawnAmt > 100)
        {
            rocketGiraffeSpawnAmt = 7;
        }

        if (snowDevilSpawnAmt < 0 || snowDevilSpawnAmt > 100)
        {
            snowDevilSpawnAmt = 37;
        }

        if (thiefSpawnAmt < 0 || thiefSpawnAmt > 100)
        {
            thiefSpawnAmt = 37;
        }

        if (zebraSpawnAmt < 0 || zebraSpawnAmt > 100)
        {
            zebraSpawnAmt = 7;
        }

        if (growbotGreggSpawnAmt < 0 || growbotGreggSpawnAmt > 100)
        {
            growbotGreggSpawnAmt = 26;
        }

        if (pyramidRarity < 1 || pyramidRarity > 10)
        {
            pyramidRarity = 5;
        }

        if (castleRarity < 1 || castleRarity > 10)
        {
            castleRarity = 5;
        }

        if (blorpMaxSize < 6 || blorpMaxSize > 99)
        {
            blorpMaxSize = 6;
        }
    }

    public static boolean shouldShowUpdateGuiForVersion(String version)
    {
        return (!hideUpdateGuiVersion.equals(version));
    }

    public static void hideUpdateGuiForVersion(String version)
    {
        try
        {
            config.get(miscProperty, "Hide Update for Version", "").set(version);

            config.save();
        }
        finally
        {
            if (config.hasChanged())
            {
                config.save();
            }
        }
    }

    public static int calculateSpawnRate(int baseRate)
    {
        return (int)Math.floor(baseRate * globalSpawnRate);
    }

    public static boolean hasBiome(String biome)
    {
        for (String str : nonVanillaBiomes)
        {
            if (str.equals(biome))
            {
                return true;
            }
        }

        return false;
    }
}