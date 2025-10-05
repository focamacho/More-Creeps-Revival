package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.helpers.EffectHelper;
import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class EntityGuineaPig extends EntityCreepBaseOwnable implements IEntityCanChangeSize {
    private static final DataParameter<Boolean> hotelBuilt = EntityDataManager.<Boolean>createKey(EntityGuineaPig.class, DataSerializers.BOOLEAN);

    private static final String[] textures = {
            "textures/entity/ggpig1",
            "textures/entity/ggpig2",
            "textures/entity/ggpig3",
            "textures/entity/ggpig4",
            "textures/entity/ggpig5",
            "textures/entity/ggpig6",
            "textures/entity/ggpig7",
            "textures/entity/ggpig8",
            "textures/entity/ggpig9",
            "textures/entity/ggpiga"
    };

    private static final int[] levelDamages = {
            0, 200, 600, 1000, 1500, 2000, 2700, 3500, 4400, 5400,
            6600, 7900, 9300, 10800, 12400, 14100, 15800, 17600, 19500, 21500,
            25000, 30000
    };

    public EntityGuineaPig(World worldIn) {
        super(worldIn);

        setSize(0.6f, 0.6f);


        baseSpeed = 0.325d;

        baseHealth = (float) rand.nextInt(5) + 5.0f;

        setWanderState(1);

        updateAttributes();
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        dataManager.register(hotelBuilt, Boolean.valueOf(false));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsGuineaPig");
        props.setBoolean("HotelBuilt", getHotelBuilt());
        compound.setTag("MoreCreepsGuineaPig", props);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsGuineaPig");

        if (props.hasKey("HotelBuilt")) {
            setHotelBuilt(props.getBoolean("HotelBuilt"));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!isRiding() && rand.nextInt(5) == 0) {
            return CreepsSoundHandler.guineaPigSound;
        }

        return null;
    }

    @Override
    protected String[] getTamedNames() {
        return MoreCreepsConfig.TamedNames.guineaPigNames;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CreepsSoundHandler.guineaPigAngrySound;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.guineaPigDeathSound;
    }

    @Override
    protected SoundEvent getMountSound() {
        return CreepsSoundHandler.guineaPigMountSound;
    }

    @Override
    protected SoundEvent getUnmountSound() {
        return CreepsSoundHandler.guineaPigUnmountSound;
    }

    @Override
    protected SoundEvent getEatSound() {
        return CreepsSoundHandler.guineaPigEatSound;
    }

    @Override
    protected SoundEvent getFullSound() {
        return CreepsSoundHandler.guineaPigFullSound;
    }

    @Override
    protected SoundEvent getLevelUpSound() {
        return CreepsSoundHandler.guineaPigLevelUpSound;
    }

    @Override
    protected SoundEvent getSpeedUpSound() {
        return CreepsSoundHandler.guineaPigSpeedUpSound;
    }

    @Override
    protected SoundEvent getSpeedDownSound() {
        return CreepsSoundHandler.guineaPigSpeedDownSound;
    }

    @Override
    protected SoundEvent getCriticalHitSound() {
        return CreepsSoundHandler.guineaPigCriticalHitSound;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return CreepsSoundHandler.guineaPigAngrySound;
    }

    @Override
    protected SoundEvent getKillSound() {
        return CreepsSoundHandler.guineaPigAngrySound;
    }

    @Override
    protected SoundEvent getTamedSound() {
        return CreepsSoundHandler.guineaPigFullSound;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            return super.processInteract(player, hand);
        }

        ItemStack itemStack = player.getHeldItem(hand);

        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();

            if (isTamed() && isPlayerOwner(player)) {
                if (item == Items.DIAMOND) {
                    if (isRiding()) {
                        if (!world.isRemote) {
                            player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.hotel.down"));
                        }
                    } else if (!getHotelBuilt()) {
                        if (getLevel() >= 20) {
                            if (createHotel(player, MathHelper.floor(player.posX) + 2, MathHelper.floor(player.getEntityBoundingBox().minY), MathHelper.floor(player.posZ) + 2)) {
                                //player.playSound(MoreCreepsAndWeirdos.achievementSound, 1.0f, 1.0f);
                                // TODO: add achievements bro

                                playSound(SoundEvents.ENTITY_TNT_PRIMED, 1.0f, 0.5f);

                                itemStack.shrink(1);
                            }
                        } else if (!world.isRemote) {
                            player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.hotel.level"));

                            player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.currentlevel", getName(), getLevel()));
                        }
                    } else if (!world.isRemote) {
                        player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.hotel.already"));
                    }

                    return true;
                } else if (item == Item.getItemFromBlock(Blocks.RED_FLOWER) || item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) {
                    EffectHelper.smoke(world, this, rand, true);

                    switch (getWanderState()) {
                        case 0:
                            if (!world.isRemote) {
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.1", getName()));
                            }

                            setWanderState(1);

                            break;
                        case 1:
                            if (!world.isRemote) {
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.2", getName()));
                            }

                            setWanderState(2);

                            break;
                        case 2:
                            if (!world.isRemote) {
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.0", getName()));
                            }

                            setWanderState(0);

                            break;
                        default:
                            break;
                    }

                    itemStack.shrink(1);

                    return true;
                } else if (item == Items.REEDS) {
                    giveSpeedBoost(13000);

                    itemStack.shrink(1);

                    return true;
                } else if (item == Items.LEATHER_BOOTS || item == Items.LEATHER_CHESTPLATE || item == Items.LEATHER_HELMET || item == Items.LEATHER_LEGGINGS) {
                    setArmor(1);

                    setHealth(getMaxHealth());

                    EffectHelper.smoke(world, this, rand, false);

                    playSound(CreepsSoundHandler.guineaPigArmorSound, 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);

                    itemStack.shrink(1);

                    return true;
                } else if (item == Items.GOLDEN_BOOTS || item == Items.GOLDEN_CHESTPLATE || item == Items.GOLDEN_HELMET || item == Items.GOLDEN_LEGGINGS) {
                    setArmor(2);

                    setHealth(getMaxHealth());

                    EffectHelper.smoke(world, this, rand, false);

                    playSound(CreepsSoundHandler.guineaPigArmorSound, 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);

                    itemStack.shrink(1);

                    return true;
                } else if (item == Items.IRON_BOOTS || item == Items.IRON_CHESTPLATE || item == Items.IRON_HELMET || item == Items.IRON_LEGGINGS) {
                    setArmor(3);

                    setHealth(getMaxHealth());

                    EffectHelper.smoke(world, this, rand, false);

                    playSound(CreepsSoundHandler.guineaPigArmorSound, 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);

                    itemStack.shrink(1);

                    return true;
                } else if (item == Items.DIAMOND_BOOTS || item == Items.DIAMOND_CHESTPLATE || item == Items.DIAMOND_HELMET || item == Items.DIAMOND_LEGGINGS) {
                    setArmor(4);

                    setHealth(getMaxHealth());

                    EffectHelper.smoke(world, this, rand, false);

                    playSound(CreepsSoundHandler.guineaPigArmorSound, 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);

                    itemStack.shrink(1);

                    return true;
                }
            }

            if (item == Items.EGG) {
                playSound(SoundEvents.ENTITY_TNT_PRIMED, 1.0f, 0.5f);

                setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);

                motionX = -MathHelper.sin((rotationYaw / 180F) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);

                motionZ = MathHelper.cos((rotationYaw / 180F) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);

                double d = motionX / 100.0d;

                double d1 = motionZ / 100.0d;

                for (int i = 0; i < 2000; i++) {
                    move(MoverType.SELF, d, 0.0d, d1);

                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, posY + (double) (rand.nextFloat() * height), (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d);
                }

                world.createExplosion(null, posX, posY, posZ, 1.1f, true);

                //interest = 0;

                setDead();

                itemStack.shrink(1);

                return true;
            } else if (item == Items.WHEAT || item == Items.MELON) {
                feed(player, 10, 15);

                EffectHelper.smoke(world, this, rand, false);

                itemStack.shrink(1);

                return true;
            } else if (item == Items.COOKIE) {
                feed(player, 15, 30);

                EffectHelper.smoke(world, this, rand, false);

                itemStack.shrink(1);

                return true;
            } else if (item == Items.APPLE) {
                feed(player, 25, 55);

                EffectHelper.smoke(world, this, rand, false);

                itemStack.shrink(1);

                return true;
            } else if (item == Items.GOLDEN_APPLE) {
                feed(player, 75, 111);

                EffectHelper.smoke(world, this, rand, false);

                itemStack.shrink(1);

                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    private boolean createHotel(EntityPlayer player, int x, int y, int z) {
        int width = 16;

        int height = 6;

        int length = 16;

        int alt = 1;

        int area = 0;

        for (int h = 0; h < (height + 4); h++) {
            for (int i = -2; i < (width + 2); i++) {
                for (int j = -2; j < (length + 2); j++) {
                    if (world.getBlockState(new BlockPos(x + i, y + h, z + j)).getBlock() != Blocks.AIR) {
                        area++;
                    }
                }
            }
        }

        if (area < 900) {
            setHotelBuilt(true);

            playSound(CreepsSoundHandler.guineaPigHotelSound, getSoundVolume(), getSoundPitch());

            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.hotel.built"));
            }

            for (int h = 0; h < (height + 4); h++) {
                for (int i = -2; i < (width + 2); i++) {
                    for (int j = -2; j < (length + 2); j++) {
                        world.setBlockToAir(new BlockPos(x + i, y + h, z + j));
                    }
                }
            }

            for (int h = 0; h < height; h++) {
                for (int i = 0; i < length; i++) {
                    alt *= -1;

                    for (int j = 0; j < width; j++) {
                        world.setBlockState(new BlockPos(x + i, y + h, z), Blocks.WOOL.getDefaultState());

                        world.setBlockState(new BlockPos(x + i, y + h, z + width - 1), Blocks.WOOL.getDefaultState());

                        world.setBlockState(new BlockPos(x, y + h, z + j), Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));

                        world.setBlockState(new BlockPos(x + length, y + h, z + j), Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));

                        alt *= -1;

                        if (alt > 0) {
                            world.setBlockState(new BlockPos(x + i, y, z + j), Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE));
                        } else {
                            world.setBlockState(new BlockPos(x + i, y, z + j), Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE));
                        }

                        world.setBlockState(new BlockPos(x + i, y + height, z + j), Blocks.GLASS.getDefaultState());
                    }
                }
            }

            world.setBlockState(new BlockPos(x + 7, y, z - 1), Blocks.DOUBLE_STONE_SLAB.getDefaultState());
            world.setBlockState(new BlockPos(x + 10, y, z - 1), Blocks.DOUBLE_STONE_SLAB.getDefaultState());

            world.setBlockState(new BlockPos(x + 7, y + 1, z - 1), Blocks.TORCH.getDefaultState());
            world.setBlockState(new BlockPos(x + 10, y + 1, z - 1), Blocks.TORCH.getDefaultState());

            world.setBlockState(new BlockPos(x + 8, y, z - 1), Blocks.STONE_SLAB.getDefaultState());
            world.setBlockState(new BlockPos(x + 9, y, z - 1), Blocks.STONE_SLAB.getDefaultState());

            world.setBlockState(new BlockPos(x + 8, y + 1, z), Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT).withProperty(BlockDoor.FACING, EnumFacing.SOUTH));
            world.setBlockState(new BlockPos(x + 8, y + 2, z), Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT).withProperty(BlockDoor.FACING, EnumFacing.SOUTH));

            world.setBlockState(new BlockPos(x + 9, y + 1, z), Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.FACING, EnumFacing.SOUTH));
            world.setBlockState(new BlockPos(x + 9, y + 2, z), Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.FACING, EnumFacing.SOUTH));

            world.setBlockState(new BlockPos(x + 8, y + 1, z + 5), Blocks.SANDSTONE.getDefaultState());
            world.setBlockState(new BlockPos(x + 9, y + 1, z + 5), Blocks.SANDSTONE.getDefaultState());

            world.setBlockState(new BlockPos(x + 8, y + 2, z + 5), Blocks.TORCH.getDefaultState());
            world.setBlockState(new BlockPos(x + 9, y + 2, z + 5), Blocks.TORCH.getDefaultState());

            for (int i = 4; i < (length - 4); i += 3) {
                world.setBlockState(new BlockPos(x + 1, y + 4, z + i), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));

                world.setBlockState(new BlockPos(x + length - 1, y + 4, z + i), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));

                world.setBlockState(new BlockPos(x + i + 2, y + 4, z + width - 2), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 1; j < length; j++) {
                    world.setBlockState(new BlockPos(x + j, y + 1, z + i + 6), Blocks.DIRT.getDefaultState());
                }
            }

            for (int i = 0; i < 5; i++) {
                for (int j = 1; j < length; j++) {
                    world.setBlockState(new BlockPos(x + j, y + 2, z + i + 10), Blocks.DIRT.getDefaultState());
                }
            }

            for (int j = 3; j < (length - 3); j++) {
                world.setBlockToAir(new BlockPos(x + j, y + 1, z + 6));
            }

            for (int j = 7; j < (length - 4); j++) {
                world.setBlockToAir(new BlockPos(x + j, y + 1, z + 7));
            }

            for (int j = 7; j < 12; j++) {
                world.setBlockState(new BlockPos(x + 1, y + 2, z + j), Blocks.YELLOW_FLOWER.getDefaultState());

                world.setBlockState(new BlockPos(x + 2, y + 2, z + j), Blocks.YELLOW_FLOWER.getDefaultState());

                world.setBlockState(new BlockPos(x + 14, y + 2, z + j), Blocks.RED_FLOWER.getDefaultState());

                world.setBlockState(new BlockPos(x + 15, y + 2, z + j), Blocks.RED_FLOWER.getDefaultState());
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 6; j < (length - 3); j++) {
                    world.setBlockState(new BlockPos(x + j, y + 2, z + i + 11), Blocks.FLOWING_WATER.getDefaultState());

                    world.setBlockState(new BlockPos(x + j, y + 1, z + i + 11), Blocks.FLOWING_WATER.getDefaultState());
                }
            }

            world.setBlockState(new BlockPos(x + 5, y + 2, z + 12), Blocks.FLOWING_WATER.getDefaultState());
            world.setBlockState(new BlockPos(x + 5, y + 2, z + 13), Blocks.FLOWING_WATER.getDefaultState());

            world.setBlockState(new BlockPos(x + 9, y + 1, z + 8), Blocks.GRASS.getDefaultState());

            world.setBlockState(new BlockPos(x + 5, y + 3, z), Blocks.GLASS.getDefaultState());
            world.setBlockState(new BlockPos(x + 5, y + 2, z), Blocks.GLASS.getDefaultState());

            world.setBlockState(new BlockPos(x + 4, y + 3, z), Blocks.GLASS.getDefaultState());
            world.setBlockState(new BlockPos(x + 4, y + 2, z), Blocks.GLASS.getDefaultState());

            world.setBlockState(new BlockPos(x + 13, y + 3, z), Blocks.GLASS.getDefaultState());
            world.setBlockState(new BlockPos(x + 13, y + 2, z), Blocks.GLASS.getDefaultState());

            world.setBlockState(new BlockPos(x + 12, y + 3, z), Blocks.GLASS.getDefaultState());
            world.setBlockState(new BlockPos(x + 12, y + 2, z), Blocks.GLASS.getDefaultState());

            world.setBlockState(new BlockPos(x + 1, y + 1, z + 3), Blocks.CHEST.getDefaultState());

            TileEntityChest chest = new TileEntityChest();

            world.setTileEntity(new BlockPos(x + 1, y + 1, z + 3), chest);

            world.setBlockState(new BlockPos(x + 1, y + 1, z + 4), Blocks.CHEST.getDefaultState());

            TileEntityChest chest1 = new TileEntityChest();

            world.setTileEntity(new BlockPos(x + 1, y + 1, z + 4), chest1);

            for (int i = 0; i < chest.getSizeInventory(); i++) {
                if (rand.nextInt(10) == 0) {
                    chest.setInventorySlotContents(i, new ItemStack(Items.GOLDEN_APPLE, 1));

                    chest1.setInventorySlotContents(i, new ItemStack(Items.GOLDEN_APPLE, 1));
                } else {
                    chest.setInventorySlotContents(i, new ItemStack(Items.APPLE, 1));

                    chest1.setInventorySlotContents(i, new ItemStack(Items.WHEAT, rand.nextInt(16)));
                }
            }

            world.setBlockState(new BlockPos(x + length - 1, y + 1, z + 3), Blocks.CHEST.getDefaultState());

            TileEntityChest chest2 = new TileEntityChest();

            world.setTileEntity(new BlockPos(x + length - 1, y + 1, z + 3), chest2);

            world.setBlockState(new BlockPos(x + length - 1, y + 1, z + 4), Blocks.CHEST.getDefaultState());

            TileEntityChest chest3 = new TileEntityChest();

            world.setTileEntity(new BlockPos(x + length - 1, y + 1, z + 4), chest3);

            for (int i = 0; i < chest1.getSizeInventory(); i++) {
                if (rand.nextInt(15) == 0) {
                    chest2.setInventorySlotContents(i, new ItemStack(Items.GOLDEN_APPLE, 1));

                    chest3.setInventorySlotContents(i, new ItemStack(Items.APPLE, 1));
                } else {
                    chest2.setInventorySlotContents(i, new ItemStack(Items.APPLE, 1));

                    chest3.setInventorySlotContents(i, new ItemStack(Items.WHEAT, rand.nextInt(16)));
                }
            }

            return true;
        } else if (!world.isRemote) {
            player.sendMessage(new TextComponentTranslation("entity.morecreeps.guinea_pig.heaven.obstructions"));
        }

        return false;
    }

    @Override
    protected void dropItemsOnDeath() {
        dropItem(Items.PORKCHOP, 1);
    }

    @Override
    protected String[] getAvailableTextures() {
        return textures;
    }

    @Override
    public boolean isTamable() {
        return true;
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }

    @Override
    protected boolean shouldOpenTamableMenu(Item item) {
        if (item == CreepsItemHandler.guineaPigRadio) {
            return true;
        }

        return super.shouldOpenTamableMenu(item);
    }

    @Override
    protected boolean canUseTamableMenu() {
        return true;
    }

    @Override
    public String getLevelName() {
        return I18n.format("other.morecreeps.guineapig.level." + getLevel());
    }

    @Override
    public int getLevelDamage() {
        return levelDamages[getLevel()];
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public boolean getHotelBuilt() {
        return dataManager.get(hotelBuilt);
    }

    protected void setHotelBuilt(boolean b) {
        dataManager.set(hotelBuilt, b);
    }

    @Override
    protected boolean shouldJumpWhileAttacking(Entity entity) {
        return true;
    }

    @Override
    protected double getLevelSpeedMultiplier() {
        return 0.0025d;
    }

    @Override
    public void onRevive(NBTTagCompound compound) {
        super.onRevive(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsGuineaPig");

        setHotelBuilt(props.getBoolean("HotelBuilt"));
    }

    @Override
    public void onTombstoneCreate(NBTTagCompound compound) {
        super.onTombstoneCreate(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsGuineaPig");

        props.setBoolean("HotelBuilt", getHotelBuilt());

        compound.setTag("MoreCreepsGuineaPig", props);
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public boolean canLevelUp() {
        return true;
    }

    @Override
    public boolean canBeRevived() {
        return true;
    }

    @Override
    public float maxShrink() {
        return 0.5f;
    }

    @Override
    public float getRayAmount() {
        return 0.15f;
    }

    @Override
    public float maxGrowth() {
        return 5.0f;
    }

}
