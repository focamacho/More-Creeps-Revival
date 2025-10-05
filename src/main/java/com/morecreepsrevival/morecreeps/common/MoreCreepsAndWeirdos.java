package com.morecreepsrevival.morecreeps.common;

import com.morecreepsrevival.morecreeps.common.capabilities.CreepsCapabilityHandler;
import com.morecreepsrevival.morecreeps.common.capabilities.IPlayerJumping;
import com.morecreepsrevival.morecreeps.common.capabilities.PlayerJumpingProvider;
import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.entity.EntityBubbleScum;
import com.morecreepsrevival.morecreeps.common.entity.EntityCreepBase;
import com.morecreepsrevival.morecreeps.common.entity.EntityTrophy;
import com.morecreepsrevival.morecreeps.common.handlers.CreepsEntityHandler;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;

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

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CreepsEntityHandler());
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

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void loadWorld(WorldEvent.Load event) {
        JailManager.tryCasheStructure(event.getWorld());
        WorldGenCastle.tryCacheStructure(event.getWorld());
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

    public static MoreCreepsAndWeirdos getInstance() {
        return INSTANCE;
    }

}