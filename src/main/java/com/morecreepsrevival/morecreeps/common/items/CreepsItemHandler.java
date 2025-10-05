package com.morecreepsrevival.morecreeps.common.items;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = MoreCreepsAndWeirdos.modid)
public class CreepsItemHandler {

    // Achievements
    public static Item floobAchievement;
    public static Item goToHellAchievement;
    public static Item pigWhispererAchievement;
    public static Item theZipperAchievement;

    // Items
    public static Item guineaPigRadio;
    public static Item lifeGem;
    public static Item bandaid;
    public static Item popsicle;
    public static Item blorpCola;
    public static Item money;
    public static Item armSword;
    public static Item zebraBody;
    public static Item zebraBoots;
    public static Item zebraHelmet;
    public static Item zebraLegs;
    public static Item armyGem;
    public static Item atom;
    public static Item babyJarEmpty;
    public static Item babyJarFull;
    public static Item lolly;
    public static Item gooDonut;
    public static Item raygun;
    public static Item dev_raygun;
    public static Item evilEgg;
    public static Item earthGem;
    public static Item miningGem;
    public static Item skyGem;
    public static Item healingGem;
    public static Item fireGem;
    public static Item donut;
    public static Item gemSword;
    public static Item frisbee;
    public static Item shrinkRay;
    public static Item horseHeadGem;
    public static Item sundae;
    public static Item shrinkShrink;
    public static Item gun;
    public static Item extinguisher;
    public static Item rayRay;
    public static Item dev_rayRay;
    public static Item rocket;
    public static Item floobRaygun;
    public static Item limbs;
    public static Item bulletBullet;
    public static Item smoke;
    public static Item growRay;
    public static Item zebraHide;
    public static Item cavemanClub;
    public static Item growbotGrowRay;
    public static Item salGun;
    public static Item battery;
    public static Item ram16k;
    public static Item mobilePhone;
    public static Item medicine;
    public static Item luckyDress;
    public static Item peeBucket;

    // Currently disabled
    public static Item vhsTape;
    public static Item vhsTape2;
    public static Item vHammer;

    public static ArmorMaterial ZEBRA_ARMOR = EnumHelper.addArmorMaterial("zebra_armor", "zebra_armor", 25, new int[]{2, 4, 6, 2}, 5, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public static void initItems() {
        // Achievements
        floobAchievement = new CreepsItem("floob_achievement");
        goToHellAchievement = new CreepsItem("gotohell_achievement");
        pigWhispererAchievement = new CreepsItem("pigwhisperer_achievement");
        theZipperAchievement = new CreepsItem("thezipper_achievement");

        // Particles and items
        guineaPigRadio = new ItemGuineaPigRadio();
        lifeGem = new ItemLifeGem();
        bandaid = new ItemBandaid();
        popsicle = new ItemPopsicle();
        blorpCola = new ItemBlorpCola();
        money = new ItemMoney();
        armSword = new ItemArmSword();
        zebraBody = new ItemArmorZebraBody();
        zebraBoots = new ItemArmorZebraBoots();
        zebraHelmet = new ItemArmorZebraHelmet();
        zebraLegs = new ItemArmorZebraLegs();
        armyGem = new ItemArmyGem();
        atom = new ItemAtom();
        babyJarEmpty = new ItemBabyJarEmpty();
        babyJarFull = new ItemBabyJarFull();
        lolly = new ItemLolly();
        gooDonut = new ItemGooDonut();
        raygun = new ItemRaygun();
        dev_raygun = new ItemDevRaygun();
        evilEgg = new ItemEvilEgg();
        earthGem = new ItemEarthGem();
        miningGem = new ItemMiningGem();
        skyGem = new ItemSkyGem();
        healingGem = new ItemHealingGem();
        fireGem = new ItemFireGem();
        donut = new ItemDonut();
        gemSword = new ItemGemSword();
        frisbee = new ItemFrisbee();
        shrinkRay = new ItemShrinkRay();
        horseHeadGem = new ItemHorseHeadGem();
        sundae = new ItemSundae();
        shrinkShrink = new CreepsItem("shrinkshrink", true);
        gun = new ItemGun();
        extinguisher = new ItemExtinguisher();
        rayRay = new CreepsItem("rayray", true);
        dev_rayRay = new CreepsItem("dev_rayray", true);
        rocket = new ItemRocket();
        floobRaygun = new ItemRaygun("floob_raygun", true);
        limbs = new ItemLimbs();
        bulletBullet = new CreepsItem("bulletbullet", true);
        smoke = new CreepsItem("smoke", true);
        growRay = new ItemGrowRay();
        zebraHide = new ItemZebraHide();
        cavemanClub = new ItemCavemanClub();
        growbotGrowRay = new ItemGrowRay("growbot_growray", true);
        salGun = new ItemGun("sal_gun", true);
        battery = new ItemBattery();
        ram16k = new ItemRam16K();
        mobilePhone = new ItemMobilePhone();
        medicine = new ItemMedicine();
        luckyDress = new ItemLuckyDress();
        peeBucket = new ItemPeeBuck();
        //vhsTape = new ItemVHSTape();
        //vhsTape2 = new ItemTameVHSTape();
        //vHammer = new ItemVhammer();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                peeBucket,
                guineaPigRadio,
                lifeGem,
                bandaid,
                popsicle,
                blorpCola,
                money,
                armSword,
                floobAchievement,
                goToHellAchievement,
                pigWhispererAchievement,
                zebraBody,
                zebraBoots,
                zebraHelmet,
                zebraLegs,
                armyGem,
                atom,
                babyJarEmpty,
                babyJarFull,
                lolly,
                gooDonut,
                raygun,
                dev_raygun,
                evilEgg,
                earthGem,
                miningGem,
                skyGem,
                healingGem,
                fireGem,
                donut,
                gemSword,
                frisbee,
                shrinkRay,
                horseHeadGem,
                sundae,
                shrinkShrink,
                gun,
                extinguisher,
                rayRay,
                dev_rayRay,
                rocket,
                smoke,
                floobRaygun,
                limbs,
                bulletBullet,
                growRay,
                zebraHide,
                cavemanClub,
                growbotGrowRay,
                salGun,
                battery,
                ram16k,
                mobilePhone,
                medicine,
                luckyDress
                //vhsTape,
                //vhsTape2,
                //vHammer
        );
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : Item.REGISTRY) {
            if (item instanceof CreepsItem || item instanceof CreepsItemArmor || item instanceof CreepsItemFood || item instanceof CreepsItemSword) {
                ResourceLocation resourceLocation = item.getRegistryName();

                if (resourceLocation != null) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(resourceLocation, "inventory"));
                }
            }
        }
    }
}
