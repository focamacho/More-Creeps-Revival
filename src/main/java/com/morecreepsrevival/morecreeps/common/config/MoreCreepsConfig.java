package com.morecreepsrevival.morecreeps.common.config;

import net.minecraftforge.common.config.Config;

@Config(modid = "morecreeps", category = "General")
public class MoreCreepsConfig {

    @Config.Comment("Toggles the playing of the welcome sound when you load the world.")
    public static boolean playWelcomeSound = true;

    @Config.Comment("Sends a welcome message in the chat to each player joining.")
    public static boolean sendWelcomeMessage = true;

    @Config(modid = "morecreeps", category = "Spawn")
    public static class Spawn {

        @Config.Comment("This setting controls the overall spawn rate. The spawn rate of every mob is multiplied by this number. So 0.5 is the default rate, 1.0 is 2x the rate, 0.25 is half the rate, etc.")
        public static double globalSpawnRate = 0.75f;

        @Config.Comment("Setting this to true will cause More Creeps mobs to spawn in biomes added by other mods.")
        public static boolean spawnInNonVanillaBiomes = false;

        @Config.Comment("This a list of Non-Vanilla biomes that More Creeps mobs should spawn in.")
        public static String[] nonVanillaBiomes = {};

    }

    @Config(modid = "morecreeps", category = "WorldGen")
    public static class WorldGen {

        @Config.Comment("Enable Pyramid Gen")
        public static boolean pyramidGen = true;

        @Config.Comment("Pyramid Rarity. Given as a percentage chance per chunk, so 1 = 1% chance per Desert chunk.")
        public static float pyramidRarityChance = 1f;

        @Config.Comment("Enable Castle Gen")
        public static boolean castleGen = true;

        @Config.Comment("Castle Rarity. Given as a percentage chance per chunk, so 0.5 = 0.5% chance per chunk.")
        public static float castleRarityChance = 0.1f;

        @Config.Comment("Enable Jail")
        public static boolean jailActive = true;

    }

    @Config(modid = "morecreeps", category = "Miscellaneous")
    public static class Miscellaneous {

        @Config.Comment("Enable Blood")
        public static boolean blood = true;

        @Config.Comment("Enable Raygun Fire")
        public static boolean rayGunFire = true;

        @Config.Comment("Enable Raygun Melt")
        public static boolean rayGunMelt = true;

        @Config.Comment("Allow Floobs to Target Villagers")
        public static boolean floobTargetVillagers = true;

        @Config.Comment("Allow Floobship Explosion")
        public static boolean floobShipExplode = false;

        @Config.Comment("Allow Bum Public Urination")
        public static boolean publicUrination = true;

    }

    @Config(modid = "morecreeps", category = "Spawn Numbers")
    public static class SpawnNumbers {

        public static int guineaPigSpawnAmt = 5;
        public static int babyMummySpawnAmt = 1;
        public static int blackSoulSpawnAmt = 1;
        public static int mummySpawnAmt = 1;
        public static int armyGuySpawnAmt = 1;
        public static int hotdogSpawnAmt = 5;
        public static int gooGoatSpawnAmt = 3;
        public static int kidSpawnAmt = 1;
        public static int lollimanSpawnAmt = 1;
        public static int gSpawnAmt = 3;
        public static int robotTedSpawnAmt = 2;
        public static int robotToddSpawnAmt = 2;
        public static int lawyerFromHellSpawnAmt = 3;
        public static int bigBabySpawnAmt = 4;
        public static int thiefSpawnAmt = 3;
        public static int floobSpawnAmt = 5;
        public static int floobShipSpawnAmt = 1;
        public static int horseHeadSpawnAmt = 1;
        public static int digBugSpawnAmt = 3;
        public static int bubbleScumSpawnAmt = 5;
        public static int sneakySalSpawnAmt = 1;
        public static int snowDevilSpawnAmt = 3;
        public static int blorpSpawnAmt = 3;
        public static int camelSpawnAmt = 1;
        public static int camelJockeySpawnAmt = 1;
        public static int bumSpawnAmt = 3;
        public static int cavemanSpawnAmt = 1;
        public static int desertLizardSpawnAmt = 4;
        public static int evilScientistSpawnAmt = 3;
        public static int hippoSpawnAmt = 3;
        public static int hunchbackSpawnAmt = 2;
        public static int invisibleManSpawnAmt = 2;
        public static int mandogSpawnAmt = 1;
        public static int nonSwimmerSpawnAmt = 1;
        public static int preacherSpawnAmt = 1;
        public static int rockMonsterSpawnAmt = 1;
        public static int rocketGiraffeSpawnAmt = 4;
        public static int zebraSpawnAmt = 5;
        public static int growbotGreggSpawnAmt = 1;
        public static int ponyGirlSpawnAmt = 1;

    }

    @Config(modid = "morecreeps", category = "Tamed Names")
    public static class TamedNames {

        @Config.Comment("List of names that will be randomly chosen when a Guinea Pig is tamed.")
        public static String[] guineaPigNames = {
                "Sugar", "Clover", "CoCo", "Sprinkles", "Mr. Rabies", "Stinky", "The Incredible Mr. CoCoPants", "Butchie", "Lassie", "Fuzzy",
                "Nicholas", "Natalie", "Pierre", "Priscilla", "Mrs. McGillicutty", "Dr. Tom Jones", "Peter the Rat", "Wiskers", "Penelope", "Sparky",
                "Tinkles", "Ricardo", "Jimothy", "Captain Underpants", "CoCo Van Gough", "Chuck Norris", "PeeWee", "Quasimodo", "ZSA ZSA", "Yum Yum",
                "Deputy Dawg", "Henrietta Pussycat", "Bob Dog", "King Friday", "Jennifer", "The Situation", "Prince Charming", "Sid", "Sunshine", "Bubbles",
                "Carl", "Snowy", "Dorf", "Chilly Willy", "Angry Bob", "George W. Bush", "Ted Lange from The Love Boat", "Notch", "Frank", "A Very Young Pig",
                "Blaster", "Darwin", "Ruggles", "Chang", "Spaz", "Fluffy", "Fuzzy", "Charrlotte", "Tootsie", "Mary",
                "Caroline", "Michelle", "Sandy", "Peach", "Scrappy", "Roxanne", "James the Pest", "Lucifer", "Shaniqua", "Wendy",
                "Zippy", "Prescott Pig", "Pimpin' Pig", "Big Daddy", "Little Butchie", "The Force", "The Handler", "Little Louie", "Satin", "Sparkly Puff",
                "Dr. Chews", "Pickles", "Longtooth", "Jeffry", "Pedro the Paunchy", "Wee Willy Wiskers", "Tidy Smith", "Johnson", "Big Joe", "Tiny Mackeral",
                "Wonderpig", "Wee Wonderpig", "The Polish Baron", "Inconceivable", "Double Danny Dimples", "Jackie Jones", "Pistol", "Tiny Talker", "Strum", "Disco the Pig",
                "Banjo", "Fingers", "Clean Streak", "Little Sweet", "Fern", "Youngblood", "Lazy Cottonball", "Foxy", "SlyFoxHound",
                "Namjoon", "Seokjin", "Yoongi", "Hoseok", "Jimin", "Taehyung", "Jungkook", "Suga", "Jinnie",
                "Halsey", "Rose", "Lisa", "Jennie", "Jisoo", "Momo", "Mina", "Sana", "BamBam",
                "Yuna", "Soobin", "Solar", "Hwasa", "Hyuna", "DAWN", "Mini", "Kai", "GlockBoyKari", "Wonho", "ㅇㅅㅇ",
                "Joongie", "Sannie", "Jongho", "Mingi", "Wooyoung", "Yunho", "Hwa", "Yeosang", "Binnie", "Felix", "Chan", "Han", "MINO", "G-Dragon",
                "BOBBY", "Joshua", "Vernon", "Yuto", "Stan Loona", "Jannabi", "Irene", "Joy", "Mr. Chu", "Rap Monster", "ThreeToe"
        };

        @Config.Comment("List of names that will be randomly chosen when a HotDog is tamed.")
        public static String[] entityHotDogNames = {
                "Pogo", "Spot", "King", "Prince", "Bosco", "Ralph", "Wendy", "Trixie", "Bowser", "The Heat",
                "Weiner", "Wendon the Weiner", "Wallace the Weiner", "William the Weiner", "Terrance", "Elijah", "Good Boy", "Boy", "Girl", "Tennis Shoe",
                "Rusty", "Mean Joe Green", "Lawrence", "Foxy", "SlyFoxHound", "Leroy Brown",
                "Mickey", "Holly", "Yeontan"
        };

        @Config.Comment("List of names that will be randomly chosen when a Zebra is tamed.")
        public static String[] entityZebraNames = {
                "Stanley", "Cid", "Hunchy", "The Heat", "Herman the Hump", "Dr. Hump", "Little Lousie", "Spoony G", "Mixmaster C", "The Maestro",
                "Duncan the Dude", "Charlie Camel", "Chip", "Charles Angstrom III", "Mr. Charles", "Cranky Carl", "Carl the Rooster", "Tiny the Peach", "Desert Dan", "Dungby",
                "Doofus"
        };

        @Config.Comment("List of names that will be randomly chosen when a Camel is tamed.")
        public static String[] entityCamelNames = {
                "Stanley", "Cid", "Hunchy", "The Heat", "Herman the Hump", "Dr. Hump", "Little Lousie", "Spoony G", "Mixmaster C", "The Maestro",
                "Duncan the Dude", "Charlie Camel", "Chip", "Charles Angstrom III", "Mr. Charles", "Cranky Carl", "Carl the Rooster", "Tiny the Peach", "Desert Dan", "Dungby",
                "Doofus"
        };

        @Config.Comment("List of names that will be randomly chosen when a Snow Devil is tamed.")
        public static String[] entitySnowDevilNames = {
                "Satan", "The Butcher", "Killer", "Tad", "Death Spanker", "Death Toll", "Bruiser", "Bones", "The Devil", "Little Devil",
                "Skinny", "Death to All", "I Will Hurt You", "Pierre", "Bonecruncher", "Bone Breaker", "Blood 'N Guts", "Kill Kill", "Murder", "The Juicer",
                "Scream", "Bloody Buddy", "Sawblade", "Ripper", "Razor", "Valley Strangler", "Choppy Joe", "Wiconsin Shredder", "Urinal", "Johnny Choke",
                "Annihilation", "Bloodshed", "Destructo", "Rub Out", "Massacre", "Felony", "The Mangler", "Destroyer", "The Marauder", "Wreck",
                "Vaporizer", "Wasteland", "Demolition Duo", "Two Knocks", "Double Trouble", "Thing One & Thing Two", "Wipeout", "Devil Duo", "Two Shot", "Misunderstood",
                "Twice As Nice"
        };

        @Config.Comment("List of names that will be randomly chosen when a Pony is tamed.")
        public static String[] entityPonyNames = {
                "Chester", "Tugbert the Terrible", "Edward", "Prancer", "Paul", "Ralph", "Captain Sparkles", "Little Mo", "Percy", "Percival the Brave", "Sammy", "Thunderhoof", "Thunderbolt", "Bolt", "Benji", "Rasberry Ron", "Peter Sprinkles", "Captain Rainbow", "Chuckles", "Trigger", "Petuna", "Matilda", "Molly the Magnificent", "Betty", "Tom", "Caronline", "Hillary Hoof", "Paula", "Chaz", "Twinkletoes", "The Fortune Hunter", "Carl C Cluxton", "George", "Betty the Beast", "Nancy Neigh", "Susan Swift", "Claire De Lune", "L.A. Sizzle", "Bunwarmer", "Dirty Dutchess", "Pilar", "Gusty Dreams", "Guts and Glory", "Wiggler", "Shakin' Bacon", "Mr. Maniac", "Little Hoof"
        };

        @Config.Comment("List of names that will be randomly chosen when a Giraffe is tamed.")
        public static String[] entityRocketGiraffeNames = {
                "Rory", "Stan", "Clarence", "FirePower", "Lightning", "Rocket Jockey", "Rocket Ralph", "Tim"
        };

    }

    public static int calculateSpawnRate(int baseRate) {
        return (int) Math.floor(baseRate * Spawn.globalSpawnRate);
    }

    public static boolean hasBiome(String biome) {
        for (String str : Spawn.nonVanillaBiomes) {
            if (str.equals(biome)) {
                return true;
            }
        }

        return false;
    }
}