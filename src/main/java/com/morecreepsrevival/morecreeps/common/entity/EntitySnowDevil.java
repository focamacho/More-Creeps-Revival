package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageOpenGuiTamableEntityName;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntitySnowDevil extends EntityCreepBase implements IMob
{
    private static final String[] textures = {
            "textures/entity/snowdevil1",
            "textures/entity/snowdevil2"
    };

    private static final String[] names = {
            "Satan", "The Butcher", "Killer", "Tad", "Death Spanker", "Death Toll", "Bruiser", "Bones", "The Devil", "Little Devil",
            "Skinny", "Death to All", "I Will Hurt You", "Pierre", "Bonecruncher", "Bone Breaker", "Blood 'N Guts", "Kill Kill", "Murder", "The Juicer",
            "Scream", "Bloody Buddy", "Sawblade", "Ripper", "Razor", "Valley Strangler", "Choppy Joe", "Wiconsin Shredder", "Urinal", "Johnny Choke",
            "Annihilation", "Bloodshed", "Destructo", "Rub Out", "Massacre", "Felony", "The Mangler", "Destroyer", "The Marauder", "Wreck",
            "Vaporizer", "Wasteland", "Demolition Duo", "Two Knocks", "Double Trouble", "Thing One & Thing Two", "Wipeout", "Devil Duo", "Two Shot", "Misunderstood",
            "Twice As Nice"
    };

    public EntitySnowDevil(World worldIn)
    {
        super(worldIn);
        setCreepTypeName("Snow Devil");
        creatureType = EnumCreatureType.MONSTER;
        setSize(width * 1.6f, height * 1.6f);
        baseHealth = (float)rand.nextInt(50) + 15.0f;
        baseAttackDamage = 3.0d;
        baseSpeed = 0.3d;
        updateAttributes();
    }

    @Override
    protected String[] getAvailableTextures()
    {
        return textures;
    }

    @Override
    protected String[] getTamedNames()
    {
        return names;
    }

    @Override
    public boolean isTamable()
    {
        return true;
    }

    @Override
    public void initEntityAI()
    {
        clearAITasks();
        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(3, new EntityAIAttackMelee(this, 1.0d, true));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected boolean isDaylightMob() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos)
    {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block == Blocks.SNOW_LAYER)
        {
            return 10.0f;
        }
        return super.getBlockPathWeight(blockPos);
    }

    protected boolean isValidLightLevel() {
        return this.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) <= 15;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CreepsSoundHandler.snowDevilSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.snowDevilHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.snowDevilDeathSound;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.snowdevil;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (hand == EnumHand.OFF_HAND)
        {
            return super.processInteract(player, hand);
        }
        else if (isTamed())
        {
            if (player.isSneaking() && isPlayerOwner(player))
            {
                if (!world.isRemote)
                {
                    CreepsPacketHandler.INSTANCE.sendTo(new MessageOpenGuiTamableEntityName(getEntityId()), (EntityPlayerMP)player);
                }
                return true;
            }
        }
        else
        {
            ItemStack itemStack = player.getHeldItem(hand);
            if (!itemStack.isEmpty())
            {
                Item item = itemStack.getItem();
                if (item == Items.SNOWBALL)
                {
                    tame(player);
                    addHealth(2);
                    itemStack.shrink(1);
                    smoke();
                    return true;
                }
            }
        }
        return super.processInteract(player, hand);
    }

    @Override
    protected SoundEvent getTamedSound()
    {
        return CreepsSoundHandler.snowDevilTamedSound;
    }
}
