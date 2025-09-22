package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.entity.ai.EntityCreepAIFollowOwner;
import com.morecreepsrevival.morecreeps.common.entity.ai.EntityCreepAIFollowOwnerTarget;
import com.morecreepsrevival.morecreeps.common.entity.ai.EntityCreepAIOwnerHurtByTarget;
import com.morecreepsrevival.morecreeps.common.entity.ai.EntityCreepAIOwnerHurtTarget;
import com.morecreepsrevival.morecreeps.common.helpers.EffectHelper;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageOpenGuiTamableEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class EntityCreepBaseOwnable extends EntityCreepBase implements IEntityOwnable {

    private static final DataParameter<Integer> level = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<String> owner = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.STRING);
    private static final DataParameter<Integer> experience = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> totalDamage = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<String> creepTypeName = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.STRING);
    private static final DataParameter<Integer> speedBoost = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> healthBoost = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> skillHealing = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> skillAttack = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> skillDefend = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> skillSpeed = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> interest = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> armor = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> healTimer = EntityDataManager.createKey(EntityCreepBaseOwnable.class, DataSerializers.VARINT);

    private int internalWanderState = 0;

    public EntityCreepBaseOwnable(World worldIn) {
        super(worldIn);
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
    protected void entityInit() {
        super.entityInit();

        dataManager.register(level, 1);
        dataManager.register(owner, "");
        dataManager.register(experience, 0);
        dataManager.register(totalDamage, 0);
        dataManager.register(creepTypeName, "creep");
        dataManager.register(skillHealing, 0);
        dataManager.register(skillAttack, 0);
        dataManager.register(skillDefend, 0);
        dataManager.register(skillSpeed, 0);
        dataManager.register(interest, 0);
        dataManager.register(healTimer, 0);
        dataManager.register(armor, 0);
        dataManager.register(speedBoost, 0);
        dataManager.register(healthBoost, 0);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        switch (getWanderState()) {
            case 0:
            case 1:
                if (isTamed()) {
                    targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
                }

                break;

            case 2:
                tasks.addTask(3, new EntityCreepAIFollowOwner(this, 1.0d, 6.0f, 2.0f));
                if (isTamed()) {
                    targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
                    targetTasks.addTask(2, new EntityCreepAIOwnerHurtByTarget(this));
                    targetTasks.addTask(3, new EntityCreepAIOwnerHurtTarget(this));
                    targetTasks.addTask(4, new EntityCreepAIFollowOwnerTarget(this));
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        NBTTagCompound props = compound.getCompoundTag("MoreCreepsEntity");
        props.setInteger("Level", getLevel());
        props.setInteger("Interest", dataManager.get(interest));
        props.setInteger("TotalDamage", getTotalDamage());
        props.setInteger("Experience", getExperience());
        props.setInteger("HealthBoost", dataManager.get(healthBoost));
        props.setInteger("SpeedBoost", getSpeedBoost());
        props.setInteger("Armor", dataManager.get(armor));
        props.setInteger("SkillHealing", getSkillHealing());
        props.setInteger("SkillAttack", getSkillAttack());
        props.setInteger("SkillDefend", getSkillDefend());
        props.setInteger("SkillSpeed", getSkillSpeed());

        UUID owner = getOwnerId();

        if (owner != null) {
            props.setString("Owner", owner.toString());
        }

        compound.setTag("MoreCreepsEntity", props);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        NBTTagCompound props = compound.getCompoundTag("MoreCreepsEntity");

        if (props.hasKey("ModelSize")) {
            setModelSize(props.getFloat("ModelSize"));
        }

        if (props.hasKey("HealthBoost")) {
            setHealthBoost(props.getInteger("HealthBoost"));
        }

        if (props.hasKey("Level")) {
            setLevel(props.getInteger("Level"));
        }

        if (props.hasKey("Interest")) {
            setInterest(props.getInteger("Interest"));
        }

        if (props.hasKey("TotalDamage")) {
            setTotalDamage(props.getInteger("TotalDamage"));
        }

        if (props.hasKey("Experience")) {
            setExperience(props.getInteger("Experience"));
        }

        if (props.hasKey("Armor")) {
            setArmor(props.getInteger("Armor"));
        }

        if (props.hasKey("SkillHealing")) {
            setSkillHealing(props.getInteger("SkillHealing"));
        }

        if (props.hasKey("SkillAttack")) {
            setSkillAttack(props.getInteger("SkillAttack"));
        }

        if (props.hasKey("SkillDefend")) {
            setSkillDefend(props.getInteger("SkillDefend"));
        }

        if (props.hasKey("SkillSpeed")) {
            setSkillSpeed(props.getInteger("SkillSpeed"));
        }

        if (props.hasKey("Owner")) {
            setOwner(UUID.fromString(props.getString("Owner")));
        }

        updateAttributes();

        super.readEntityFromNBT(compound);
    }

    @Override
    protected void updateAttackStrength() {
        double attackDamage = getAttackDamage() + (getLevelDamageMultiplier() * (getLevel() - 1));

        switch (getArmor()) {
            case 1:
                attackDamage++;

                break;
            case 2:
                attackDamage += 3;

                break;
            case 3:
                attackDamage += 2;

                break;
            case 4:
                attackDamage += 6;

                break;
            default:
                break;
        }

        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(attackDamage);
    }

    @Override
    protected void updateMoveSpeed() {
        double speed = getMoveSpeed() + (getLevelSpeedMultiplier() * (getLevel() - 1));

        if (getSpeedBoost() > 0) {
            speed += 0.75d;
        }

        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
    }

    public int getInterest() {
        return dataManager.get(interest);
    }

    protected void setInterest(int i) {
        dataManager.set(interest, Math.max(0, Math.min(100, i)));
    }

    protected void addInterest(int i, EntityPlayer player) {
        if (!isTamable() || isTamed()) {
            return;
        }

        setInterest(getInterest() + i);

        if (getInterest() >= 100) {
            tame(player);
        }
    }

    public void feed(EntityPlayer player, float healthToAdd, int interestToAdd) {
        addHealth(healthToAdd);
        addInterest(interestToAdd, player);

        SoundEvent fullSound = getFullSound();

        SoundEvent eatSound;

        if (getHealth() >= getMaxHealth() && fullSound != null) {
            eatSound = fullSound;
        } else {
            eatSound = getEatSound();
        }

        if (eatSound != null) {
            playSound(eatSound, getSoundVolume(), getSoundPitch());
        }
    }

    public int getArmor() {
        return dataManager.get(armor);
    }

    protected void setArmor(int i) {
        dataManager.set(armor, i);
        updateHealth();
        updateAttackStrength();
        updateTexture();
    }

    @Override
    protected void updateHealth() {
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBaseHealth() + (getLevelHealthMultiplier() * (getLevel() - 1)) + getHealthBoost() + getArmorHealthBonus(getArmor()));
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
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            return super.processInteract(player, hand);
        }

        ItemStack itemStack = player.getHeldItem(hand);

        if (isEntityAlive()) {
            if (itemStack.isEmpty()) {
                if (player.isSneaking() && isTamed() && isPlayerOwner(player) && canUseTamableMenu()) {
                    if (!world.isRemote) {
                        CreepsPacketHandler.INSTANCE.sendTo(new MessageOpenGuiTamableEntity(getEntityId()), (EntityPlayerMP) player);
                    }

                    return true;
                } else if (canRidePlayer() && canRidePlayer(player)) {
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
                } else if (isRideable() && canPlayerRide(player) && !player.equals(getFirstPassenger()) && player.startRiding(this)) {
                    return true;
                }
            } else {
                Item item = itemStack.getItem();

                if (isTamed() && isPlayerOwner(player) && canUseTamableMenu() && (item == Items.BOOK || item == Items.PAPER || shouldOpenTamableMenu(item))) {
                    if (!world.isRemote) {
                        CreepsPacketHandler.INSTANCE.sendTo(new MessageOpenGuiTamableEntity(getEntityId()), (EntityPlayerMP) player);
                    }

                    return true;
                }
            }
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        if (!dead && !world.isRemote) {
            if (isTamed() && canBeRevived()) {
                if (!(this instanceof EntityTombstone)) {
                    EntityTombstone tombstone = new EntityTombstone(world, this);

                    tombstone.determineBaseTexture();
                    tombstone.setInitialHealth();

                    world.spawnEntity(tombstone);
                }
            } else {
                dropItemsOnDeath();
            }
        }

        super.onDeath(cause);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        int iSkillHealing = getSkillHealing();
        if (dataManager.get(healTimer) > 0) {
            dataManager.set(healTimer, dataManager.get(healTimer) - 1);
        }

        if (iSkillHealing > 0 && dataManager.get(healTimer) < 1 && getHealth() < getMaxHealth()) {
            dataManager.set(healTimer, (6 - iSkillHealing) * 200);

            addHealth(iSkillHealing);

            for (int i = 0; i < iSkillHealing; i++) {
                world.spawnParticle(EnumParticleTypes.HEART, (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (rand.nextFloat() * height), (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d);
            }

            updateEntityActionState();
        }

        int speedBoost = getSpeedBoost();

        if (speedBoost > 0) {
            speedBoost--;

            setSpeedBoost(speedBoost);

            if (speedBoost < 1) {
                if (!world.isRemote) {
                    EntityPlayer owner = getOwner();

                    if (owner != null) {
                        owner.sendMessage(new TextComponentString("\247b" + getName() + "\2476 has run out of speedboost."));
                    }
                }

                SoundEvent speedDownSound = getSpeedDownSound();

                if (speedDownSound != null) {
                    playSound(speedDownSound, getSoundVolume(), getSoundPitch());
                }

                updateMoveSpeed();
            }
        }
    }

    protected void giveSpeedBoost(int speedBoost) {
        EffectHelper.smoke(world, this, rand, true);
        setSpeedBoost(Math.max(0, getSpeedBoost()) + speedBoost);

        updateMoveSpeed();

        SoundEvent speedUpSound = getSpeedUpSound();

        if (speedUpSound != null) {
            playSound(speedUpSound, getSoundVolume(), getSoundPitch());
        }

        int speedBoostLeft = Math.max((getSpeedBoost() / 21) / 60, 0);

        if (!world.isRemote) {
            EntityPlayer owner = getOwner();

            if (owner != null) {
                owner.sendMessage(new TextComponentString("\2473" + getName() + "\2476 has\247f " + speedBoostLeft + "\2476 minute" + ((speedBoostLeft > 1 ? "s" : "")) + " of speedboost left."));
            }
        }
    }

    protected String[] getTamedNames() {
        return new String[0];
    }

    @Override
    protected boolean canDespawn() {
        if (isTamed()) return false;
        return super.canDespawn();
    }

    protected void clearOwner() {
        dataManager.set(owner, "");
    }

    @Nullable
    public UUID getOwnerId() {
        String uuid = dataManager.get(owner);

        if (uuid.isEmpty()) {
            return null;
        }

        return UUID.fromString(uuid);
    }

    public boolean isPlayerOwner(EntityPlayer player) {
        if (player == null || getOwnerId() == null) {
            return false;
        }

        return player.getUniqueID().equals(getOwnerId());
    }

    @Nullable
    public EntityPlayer getOwner() {
        UUID owner = getOwnerId();

        if (owner != null) {
            return world.getPlayerEntityByUUID(owner);
        }

        return null;
    }

    public void setOwner(UUID uuid) {
        dataManager.set(owner, uuid.toString());
    }

    public void setOwner(EntityPlayer player) {
        dataManager.set(owner, player.getUniqueID().toString());
    }

    @Override
    protected void updateTexture() {
        if (baseTexture == null || baseTexture.isEmpty()) {
            return;
        }

        StringBuilder builder = (new StringBuilder()).append(baseTexture);

        switch (getArmor()) {
            case 1:
                builder.append("l");

                break;
            case 2:
                builder.append("g");

                break;
            case 3:
                builder.append("i");

                break;
            case 4:
                builder.append("d");

                break;
            default:
                break;
        }

        builder.append(".png");

        setTexture(builder.toString());
    }

    public int getLevel() {
        return dataManager.get(level);
    }

    protected void setLevel(int i) {
        dataManager.set(level, i);
    }

    public void tame(EntityPlayer player) {
        setInterest(0);
        setOwner(player);
        
        updateAttributes();
        setHealth(getMaxHealth());
        setWanderState(2);

        if(!hasCustomName())
            setCustomNameTag(getTamedNames()[ThreadLocalRandom.current().nextInt(getTamedNames().length)]);

        SoundEvent tamedSound = getTamedSound();
        if (tamedSound != null) {
            playSound(tamedSound, getSoundVolume(), getSoundPitch());
        }

        if (!world.isRemote) {
            player.sendMessage(new TextComponentString("\2476" + getName() + " \247fhas been tamed!"));
        }
    }

    public void untame() {
        clearOwner();
        setCustomNameTag("");
        setInterest(0);
    }

    public boolean isTamed() {
        return (getOwnerId() != null);
    }

    public boolean isTamable() {
        return false;
    }

    public boolean canRidePlayer(EntityPlayer player) {
        return (!isTamable() || (isTamed() && isPlayerOwner(player)));
    }

    public boolean isRideable() {
        return false;
    }

    public boolean canPlayerRide(EntityPlayer player) {
        return (!isTamable() || (isTamed() && isPlayerOwner(player)));
    }

    protected boolean shouldOpenTamableMenu(Item item) {
        return false;
    }

    protected boolean canUseTamableMenu() {
        return false;
    }

    public String getLevelName() {
        return "";
    }

    public int getLevelDamage() {
        return 0;
    }

    public int getMaxLevel() {
        return 1;
    }

    protected void addExperience(int i) {
        setExperience(getExperience() + i);
    }

    public int getExperience() {
        return dataManager.get(experience);
    }

    protected void setExperience(int i) {
        dataManager.set(experience, i);
    }

    public void addTotalDamage(int i) {
        addExperience(i);

        i += getTotalDamage();

        if (i >= getLevelDamage() && getLevel() < getMaxLevel()) {
            int lvl = getLevel() + 1;

            setLevel(lvl);

            setTotalDamage(0);

            int healthBoostNew = rand.nextInt(4);

            setHealthBoost(healthBoostNew);

            updateAttributes();

            addHealth(healthBoostNew + getLevelHealthMultiplier());

            if (!world.isRemote) {
                EntityPlayer player = getOwner();

                if (player != null) {
                    player.sendMessage(new TextComponentString("\247b" + getName() + " \247fincreased to level \2476" + lvl + "!"));
                }
            }

            SoundEvent levelUpSound = getLevelUpSound();

            if (levelUpSound != null) {
                playSound(levelUpSound, getSoundVolume(), getSoundPitch());
            }

            return;
        }

        setTotalDamage(i);
    }

    public int getTotalDamage() {
        return dataManager.get(totalDamage);
    }

    protected void setTotalDamage(int i) {
        dataManager.set(totalDamage, i);
    }

    protected float getLevelHealthMultiplier() {
        return 1.0f;
    }

    protected double getLevelDamageMultiplier() {
        return 1.0d;
    }

    protected double getLevelSpeedMultiplier() {
        return 0.0d;
    }

    public boolean shouldAttackEntity(EntityLivingBase target) {
        if (isTamed() && target instanceof EntityCreepBaseOwnable && ((EntityCreepBaseOwnable) target).isPlayerOwner(getOwner())) {
            return false;
        }

        return true;
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

            if (isTamed() && canLevelUp()) {
                int iSkillAttack = getSkillAttack();

                addTotalDamage((int) (f * (1.85d + iSkillAttack)));

                double hitChance = 1.0d + (getLevel() * 5) + (iSkillAttack * 4);

                if (hitChance < 5.0d) {
                    hitChance = 5.0d;
                }

                if ((double) rand.nextInt(100) > (100.0d - hitChance)) {
                    /*if (MoreCreepsConfig.blood && !world.isRemote)
                    {
                        CreepsPacketHandler.INSTANCE.sendToAllTracking(new MessageSendBloodEffect(entity.getEntityId()), entity);
                    }*/

                    float damageDealt = f * 0.75f;

                    if (damageDealt < 1.0f) {
                        damageDealt = 1.0f;
                    }

                    if (dataManager.get(EntityCreepBase.criticalHitCooldown) > 0) {
                        dataManager.set(EntityCreepBase.criticalHitCooldown, dataManager.get(EntityCreepBase.criticalHitCooldown) - 1);
                    }

                    if (iSkillAttack > 1 && rand.nextInt(100) > (100 - (iSkillAttack * 2)) && dataManager.get(EntityCreepBase.criticalHitCooldown) < 1) {
                        float hp = ((EntityLivingBase) entity).getHealth();

                        if (damageDealt < hp) {
                            damageDealt = hp;
                        }

                        dataManager.set(EntityCreepBase.criticalHitCooldown, 50 - (iSkillAttack * 8));

                        SoundEvent criticalHitSound = getCriticalHitSound();

                        if (criticalHitSound != null) {
                            entity.playSound(criticalHitSound, 1.0f, 1.0f);
                        }

                        addTotalDamage(25);
                    }

                    if ((((EntityLivingBase) entity).getHealth() - damageDealt) <= 0.0f) {
                        SoundEvent killSound = getKillSound();

                        if (killSound != null) {
                            playSound(killSound, getSoundVolume(), getSoundPitch());
                        }
                    }

                    addTotalDamage((int) (damageDealt * (1.85d + iSkillAttack)));

                    return entity.attackEntityFrom(DamageSource.causeThrownDamage(this, entity), damageDealt);
                }
            }
        }

        return flag;
    }

    @Override
    public Team getTeam() {
        if (isTamed()) {
            EntityPlayer owner = getOwner();

            if (owner != null) {
                return owner.getTeam();
            }
        }

        return super.getTeam();
    }

    @Override
    public boolean isOnSameTeam(Entity entity) {
        if (isTamed()) {
            EntityPlayer owner = getOwner();

            if (owner != null) {
                if (owner.equals(entity)) {
                    return true;
                }

                return owner.isOnSameTeam(entity);
            }
        }

        return super.isOnSameTeam(entity);
    }

    public int getSpeedBoost() {
        return dataManager.get(speedBoost);
    }

    protected void setSpeedBoost(int i) {
        dataManager.set(speedBoost, i);
    }

    public int getHealthBoost() {
        return dataManager.get(healthBoost);
    }

    protected void setHealthBoost(int healthBoostIn) {
        dataManager.set(healthBoost, healthBoostIn);
    }

    public int getSkillAttack() {
        return dataManager.get(skillAttack);
    }

    protected void setSkillAttack(int i) {
        dataManager.set(skillAttack, i);
    }

    public int getSkillHealing() {
        return dataManager.get(skillHealing);
    }

    protected void setSkillHealing(int i) {
        dataManager.set(skillHealing, i);
    }

    public int getSkillDefend() {
        return dataManager.get(skillDefend);
    }

    protected void setSkillDefend(int i) {
        dataManager.set(skillDefend, i);
    }

    public int getSkillSpeed() {
        return dataManager.get(skillSpeed);
    }

    protected void setSkillSpeed(int i) {
        dataManager.set(skillSpeed, i);
    }

    public int getSkillLevel(String skill) {
        switch (skill) {
            case "attack":
                return getSkillAttack();
            case "defend":
                return getSkillDefend();
            case "healing":
                return getSkillHealing();
            case "speed":
                return getSkillSpeed();
            default:
                break;
        }

        return 0;
    }

    public int getRequiredLevelForSkill(String skill) {
        return getSkillLevel(skill) * 5;
    }

    public boolean canLevelSkill(String skill) {
        return (getSkillLevel(skill) < 5 && getLevel() >= getRequiredLevelForSkill(skill));
    }

    public void levelUpSkill(String skill) {
        if (getSkillLevel(skill) >= 5) {
            return;
        }

        switch (skill) {
            case "attack":
                setSkillAttack(getSkillAttack() + 1);

                updateAttackStrength();

                break;
            case "defend":
                setSkillDefend(getSkillDefend() + 1);

                break;
            case "healing":
                setSkillHealing(getSkillHealing() + 1);

                break;
            case "speed":
                setSkillSpeed(getSkillSpeed() + 1);

                updateMoveSpeed();

                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdate() {
        EntityLivingBase target = getAttackTarget();
        if (target != null && target.equals(getOwner())) {
            setAttackTarget(null);
        }

        super.onUpdate();
    }

    public void onRevive(NBTTagCompound compound) {
    }

    public void onTombstoneCreate(NBTTagCompound compound) {
    }

    protected SoundEvent getTamedSound() {
        return null;
    }

    public boolean canLevelUp() {
        return false;
    }

    public boolean canBeRevived() {
        return false;
    }

    public void cloneEntity() {
        if (world.isRemote) {
            return;
        }

        try {
            Constructor<? extends EntityCreepBaseOwnable> constructor = getClass().getConstructor(World.class);

            EntityCreepBaseOwnable newEntity = constructor.newInstance(world);

            newEntity.copyLocationAndAnglesFrom(this);

            NBTTagCompound compound = new NBTTagCompound();

            writeEntityToNBT(compound);

            newEntity.readEntityFromNBT(compound);

            newEntity.setHealth(getHealth());

            world.spawnEntity(newEntity);

            setDead();
        } catch (Exception ignored) {}
    }

}
