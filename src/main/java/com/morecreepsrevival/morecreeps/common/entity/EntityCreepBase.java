package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageDismountEntity;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;

public class EntityCreepBase extends EntityCreature {

    private static final DataParameter<String> texture = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> wanderState = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.VARINT);
    private static final DataParameter<Float> modelSize = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> criticalHitCooldown = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> unmountTimer = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.VARINT);
    private static final DataParameter<Float> hammerSwing = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> size = EntityDataManager.createKey(EntityCreepBase.class, DataSerializers.FLOAT);

    protected String baseTexture = "";

    protected float baseHealth = 100.0f;

    protected double baseSpeed = 1.0d;

    protected double baseAttackDamage = 1.0d;
    protected float currentSize = 1f;
    protected float widthActual, heightActual;

    protected EnumCreatureType creatureType = EnumCreatureType.CREATURE;

    protected boolean spawnOnlyAtNight = false;

    private int internalWanderState = 0;

    public EntityCreepBase(World worldIn) {
        super(worldIn);
        fallDistance = -25.0f;
        experienceValue = 5;
        updateAttributes();
    }

    protected static float getArmorHealthBonus(int armorLevel) {
        switch (armorLevel) {
            case 1:
                return 5.0f;
            case 2:
                return 15.0f;
            case 3:
                return 9.0f;
            case 4:
                return 22.0f;
            default:
                break;
        }

        return 0.0f;
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width * currentSize, height * currentSize);
    }

    @Override
    @Nonnull
    public SoundCategory getSoundCategory() {
        if (getCreatureType() == EnumCreatureType.MONSTER) {
            return SoundCategory.HOSTILE;
        }

        return SoundCategory.NEUTRAL;
    }

    protected void onDismount(Entity entity) {
    }

    @Override
    public void dismountRidingEntity() {
        if (!world.isRemote) {
            CreepsPacketHandler.INSTANCE.sendToAll(new MessageDismountEntity(getEntityId()));
        }

        fallDistance = -25.0f;

        dataManager.set(unmountTimer, 20);

        SoundEvent unmountSound = getUnmountSound();

        if (unmountSound != null) {
            playSound(unmountSound, getSoundVolume(), getSoundPitch());
        }

        Entity entity = getRidingEntity();

        super.dismountRidingEntity();

        onDismount(entity);
    }

    @Override
    public boolean isEntityInvulnerable(@Nonnull DamageSource damageSource) {
        if (isRiding()) {
            return true;
        }

        return super.isEntityInvulnerable(damageSource);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(texture, "");
        dataManager.register(size, 1f);
        dataManager.register(wanderState, 0);
        dataManager.register(modelSize, 1.0f);
        dataManager.register(criticalHitCooldown, 5);
        dataManager.register(unmountTimer, 0);
        dataManager.register(hammerSwing, 0.0f);
    }

    protected void updateAttributes() {
        updateHealth();
        updateMoveSpeed();
        updateTexture();
        updateAttackStrength();
        updateModelSize();
    }

    protected void updateModelSize() {
        widthActual = width;
        heightActual = height;
    }

    @Override
    protected void initEntityAI() {
        clearAITasks();

        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);

        switch (getWanderState()) {
            case 0:
                tasks.addTask(1, new EntityAISwimming(this));
                tasks.addTask(2, new EntityAIAttackMelee(this, 1.0d, true));
                tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
                tasks.addTask(3, new EntityAILookIdle(this));
                break;
            case 1:
                tasks.addTask(1, new EntityAISwimming(this));
                tasks.addTask(2, new EntityAIAttackMelee(this, 1.0d, true));
                tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0d));
                tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
                tasks.addTask(4, new EntityAILookIdle(this));
                break;
            case 2:
                tasks.addTask(1, new EntityAISwimming(this));
                tasks.addTask(2, new EntityAIAttackMelee(this, 1.0d, true));
                tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0d));
                tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
                tasks.addTask(5, new EntityAILookIdle(this));
                break;
            default:
                break;
        }
    }

    protected void clearAITasks() {
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        updateAttributes();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsEntity");

        props.setFloat("ModelSize", getModelSize());
        props.setString("BaseTexture", baseTexture);
        props.setFloat("SizeCreep", dataManager.get(size));
        props.setInteger("WanderState", getWanderState());

        compound.setTag("MoreCreepsEntity", props);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        NBTTagCompound props = compound.getCompoundTag("MoreCreepsEntity");

        if (props.hasKey("ModelSize")) {
            setModelSize(props.getFloat("ModelSize"));
        }

        if (props.hasKey("BaseTexture")) {
            baseTexture = props.getString("BaseTexture");
        } else {
            String[] availableTextures = getAvailableTextures();

            if (availableTextures.length > 0) {
                baseTexture = availableTextures[rand.nextInt(availableTextures.length)];
            }
        }

        if (props.hasKey("SizeCreep")) {
            putSizeNBT(props.getFloat("SizeCreep"));
        }

        if (props.hasKey("WanderState")) {
            setWanderState(props.getInteger("WanderState"));
        }

        updateAttributes();

        super.readEntityFromNBT(compound);
    }

    public void determineBaseTexture() {
        if (!baseTexture.isEmpty()) {
            return;
        }

        String[] availableTextures = getAvailableTextures();

        if (availableTextures.length > 0) {
            baseTexture = availableTextures[rand.nextInt(availableTextures.length)];
        }

        updateTexture();
    }

    public void setInitialHealth() {
        setHealth(getMaxHealth());
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData) {
        super.onInitialSpawn(difficulty, livingData);

        determineBaseTexture();

        setInitialHealth();

        return livingData;
    }

    protected double getMoveSpeed() {
        return baseSpeed;
    }

    protected double getAttackDamage() {
        return baseAttackDamage;
    }

    protected void updateMoveSpeed() {
        double speed = getMoveSpeed();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
    }

    @Override
    public boolean isCreatureType(@Nullable EnumCreatureType type, boolean forSpawnCount) {
        if (forSpawnCount && isNoDespawnRequired()) {
            return false;
        }

        return (getCreatureType() == type);
    }

    public EnumCreatureType getCreatureType() {
        return creatureType;
    }

    protected float getBaseHealth() {
        return baseHealth;
    }

    protected void updateHealth() {
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBaseHealth());
    }

    protected void updateAttackStrength() {
        double attackDamage = getAttackDamage();
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(attackDamage);
    }

    public void addHealth(float amt) {
        setHealth(Math.max(0, Math.min(getMaxHealth(), getHealth() + amt)));
    }

    protected void updateTexture() {
        if (baseTexture == null || baseTexture.isEmpty()) {
            return;
        }

        String builder = baseTexture + ".png";
        setTexture(builder);
    }

    public int getWanderState() {
        return dataManager.get(wanderState);
    }

    public void setWanderState(int i) {
        dataManager.set(wanderState, i);
    }

    public boolean canRidePlayer() {
        return false;
    }

    protected double getRidingYOffset() {
        return 0.5;
    }

    @Override
    public double getYOffset() {
        Entity entity = getRidingEntity();

        if (entity != null) {
            return (getRidingYOffset() * (entity.getPassengers().indexOf(this) + 1));
        }

        return 0.0d;
    }

    protected boolean shouldJumpWhileAttacking(Entity entity) {
        return false;
    }

    protected void doAttackJump(Entity entity) {
        rotationYaw = ((float) Math.toDegrees(Math.atan2(entity.posZ - posZ, entity.posX - posX))) - 90.0f;

        double d0 = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        double f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        motionX = (d0 / f) * 0.5d * 0.800000011920929d + motionX * 0.20000000298023224d;
        motionZ = (d1 / f) * 0.5d * 0.800000011920929d + motionZ * 0.20000000298023224d;
        motionY = 0.40000000596046448f;
        fallDistance = -25.0f;
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        if (onGround && shouldJumpWhileAttacking(entity)) {
            doAttackJump(entity);
        }

        if (rand.nextInt(5) == 0) {
            SoundEvent angrySound = getAngrySound();

            if (angrySound != null) {
                playSound(angrySound, getSoundVolume(), getSoundPitch());
            }
        }

        float f = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                entity.setFire(j * 4);
            }

            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                ItemStack itemStack = getHeldItemMainhand();
                ItemStack itemStack2 = (player.isHandActive() ? player.getActiveItemStack() : ItemStack.EMPTY);

                if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem().canDisableShield(itemStack, itemStack2, player, this) && itemStack2.getItem().isShield(itemStack2, player)) {
                    float f1 = 0.25f + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05f;

                    if (rand.nextFloat() < f1) {
                        player.getCooldownTracker().setCooldown(itemStack2.getItem(), 100);

                        world.setEntityState(player, (byte) 30);
                    }
                }
            }

            applyEnchantments(this, entity);
        }

        return flag;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    public boolean canMount(Entity entity) {
        return true;
    }

    @Override
    public boolean startRiding(@Nonnull Entity entity, boolean force) {
        if (!force && !canMount(entity)) {
            return false;
        }

        boolean flag = super.startRiding(entity, force);

        if (flag) {
            rotationYaw = entity.rotationYaw;

            SoundEvent mountSound = getMountSound();

            if (mountSound != null) {
                playSound(mountSound, getSoundVolume(), getSoundPitch());
            }

            dataManager.set(unmountTimer, 20);
        }

        return flag;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            return super.processInteract(player, hand);
        }

        ItemStack itemStack = player.getHeldItem(hand);

        if (isEntityAlive()) {
            if (itemStack.isEmpty()) {
                if (canRidePlayer() && canRidePlayer(player)) {
                    if (!player.equals(getRidingEntity())) {
                        if (isStackable()) {
                            copyLocationAndAnglesFrom(player);

                            startRiding(player, true);
                        } else {
                            startRiding(player);
                        }
                    } else {
                        dismountRidingEntity();
                    }

                    return true;
                }
            }
        }

        return super.processInteract(player, hand);
    }

    public boolean canRidePlayer(EntityPlayer player) {
        return !isTamable();
    }

    public boolean isTamable() {
        return false;
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        if (!dead && !world.isRemote) {
            dropItemsOnDeath();
        }

        super.onDeath(cause);
    }

    protected void dropItemsOnDeath() {
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (isRiding() || dataManager.get(unmountTimer) > 0) {
            return false;
        }

        return super.isEntityInsideOpaqueBlock();
    }

    @Override
    public boolean canBreatheUnderwater() {
        if (isRiding() || dataManager.get(unmountTimer) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> parameter) {
        super.notifyDataManagerChange(parameter);

        if (parameter.getId() == size.getId()) {
            if (currentSize != dataManager.get(size)) {
                currentSize = dataManager.get(size);
                setSize(widthActual, heightActual);
            }
        }
    }

    @Override
    public boolean canBeSteered() {
        return (isRideable() && getControllingPassenger() instanceof EntityLivingBase);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        updateArmSwingProgress();

        /*Entity ridingEntity = getRidingEntity();

        if (isInsideOfMaterial(Material.WATER) && ridingEntity != null && ridingEntity.isInsideOfMaterial(Material.WATER) && world.isRemote)
        {
            dismountRidingEntity();

            CreepsPacketHandler.INSTANCE.sendToServer(new MessageDismountEntity(getEntityId()));
        }*/

        if (getBrightness() > 0.5f) {
            idleTime += 2;
        }

        if (dataManager.get(unmountTimer) > 0) {
            dataManager.set(unmountTimer, dataManager.get(unmountTimer) - 1);
        }

        if (shouldBurnInDay() && world.isDaytime() && !world.isRemote && !isChild()) {
            float f = getBrightness();

            if (f > 0.5f && (rand.nextFloat() * 30) < ((f - 0.4f) * 2.0f) && world.canSeeSky(new BlockPos(posX, posY + (double) getEyeHeight(), posZ))) {
                setFire(getBurnInDayTime());
            }
        }
    }

    protected int getBurnInDayTime() {
        return 20;
    }

    protected boolean shouldBurnInDay() {
        return false;
    }

    protected SoundEvent getMountSound() {
        return null;
    }

    protected SoundEvent getUnmountSound() {
        return null;
    }

    protected SoundEvent getEatSound() {
        return null;
    }

    protected SoundEvent getFullSound() {
        return null;
    }

    protected SoundEvent getSpeedUpSound() {
        return null;
    }

    protected SoundEvent getSpeedDownSound() {
        return null;
    }

    protected SoundEvent getLevelUpSound() {
        return null;
    }

    protected SoundEvent getCriticalHitSound() {
        return null;
    }

    protected SoundEvent getAngrySound() {
        return null;
    }

    protected String[] getTamedNames() {
        return new String[0];
    }

    protected String[] getAvailableTextures() {
        return new String[0];
    }

    @Override
    protected boolean canDespawn() {
        if (isNoDespawnRequired()) return false;
        return super.canDespawn();
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    public void resetTarget() {
        setAttackTarget(null);
        setRevengeTarget(null);
    }

    public void resetModelSize() {
        setModelSize(1.0f);
    }

    public float getModelSize() {
        return dataManager.get(modelSize);
    }

    protected void setModelSize(float f) {
        dataManager.set(modelSize, f);
    }

    public void shrinkModelSize(float f, float maxShrink) {
        setModelSize(Math.max(maxShrink, getModelSize() - f));
    }

    public void growModelSize(float f, float maxGrowth) {
        setModelSize(Math.max(0.0F, Math.min(maxGrowth, getModelSize() + f)));
    }

    public void growHitboxSize(float f) {
        dataManager.set(size, dataManager.get(size) + f);
    }

    public void shrinkHitboxSize(float f) {
        dataManager.set(size, dataManager.get(size) - f);
    }

    public void decreaseMoveSpeed(float f) {
        baseSpeed -= f;

        updateMoveSpeed();
    }

    public void increaseMoveSpeed(float f) {
        baseSpeed += f;

        updateMoveSpeed();
    }

    public String getTexture() {
        return dataManager.get(texture);
    }

    protected void setTexture(String textureIn) {
        dataManager.set(texture, textureIn);
    }

    protected void putSizeNBT(float f) {
        dataManager.set(size, f);
    }

    public boolean isRideable() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    public boolean isStackable() {
        return false;
    }

    public String getBaseTexture() {
        return baseTexture;
    }

    protected void setBaseTexture(String baseTextureIn) {
        baseTexture = baseTextureIn;
    }

    @Override
    public void onUpdate() {
        if (internalWanderState != getWanderState()) {
            initEntityAI();

            internalWanderState = getWanderState();
        }

        super.onUpdate();

        if (getHammerSwing() < 0.0f) {
            addHammerSwing(0.45f);
        } else {
            setHammerSwing(0.0f);
        }

        if (getCreatureType() == EnumCreatureType.MONSTER && !world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            setDead();
        }
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        if (getCreatureType() == EnumCreatureType.MONSTER && spawnOnlyAtNight) {
            return (0.5f - world.getLightBrightness(blockPos));
        }

        return (world.getLightBrightness(blockPos) - 0.5f);
    }

    protected boolean isValidLightLevel() {
        if (!spawnOnlyAtNight) {
            return true;
        }

        BlockPos blockPos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);

        if (world.getLightFor(EnumSkyBlock.SKY, blockPos) > rand.nextInt(32)) {
            return false;
        }

        int i = world.getLightFromNeighbors(blockPos);

        if (world.isThundering()) {
            int j = world.getSkylightSubtracted();

            world.setSkylightSubtracted(10);

            i = world.getLightFromNeighbors(blockPos);

            world.setSkylightSubtracted(j);
        }

        return (i <= rand.nextInt(8));
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public float getHammerSwing() {
        return dataManager.get(hammerSwing);
    }

    public void setHammerSwing(float f) {
        dataManager.set(hammerSwing, f);
    }

    public void addHammerSwing(float f) {
        setHammerSwing(getHammerSwing() + f);
    }

    public void takeHammerSwing(float f) {
        setHammerSwing(getHammerSwing() - f);
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    public boolean canBleed() {
        return true;
    }

    protected Entity getFirstPassenger() {
        for (Entity entity : getPassengers()) {
            return entity;
        }

        return null;
    }

    protected SoundEvent getKillSound() {
        return null;
    }

    protected SoundEvent getMissSound() {
        return null;
    }

    public int getUnmountTimer() {
        return dataManager.get(unmountTimer);
    }

    public void cloneEntity() {
        if (world.isRemote) {
            return;
        }

        try {
            Constructor<? extends EntityCreepBase> constructor = getClass().getConstructor(World.class);

            EntityCreepBase newEntity = constructor.newInstance(world);

            newEntity.copyLocationAndAnglesFrom(this);

            NBTTagCompound compound = new NBTTagCompound();

            writeEntityToNBT(compound);

            newEntity.readEntityFromNBT(compound);

            newEntity.setHealth(getHealth());

            world.spawnEntity(newEntity);

            setDead();
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean isInsideOfMaterial(@Nonnull Material material) {
        if (material == Material.WATER && isRiding() && dataManager.get(unmountTimer) > 0) {
            return false;
        }

        return super.isInsideOfMaterial(material);
    }
}
