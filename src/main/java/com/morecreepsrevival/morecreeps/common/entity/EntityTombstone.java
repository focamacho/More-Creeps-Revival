package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.helpers.EffectHelper;
import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.util.function.Function;

public class EntityTombstone extends EntityCreepBaseOwnable {

    private NBTTagCompound additionalProps;
    private static final DataParameter<String> tombstoneType = EntityDataManager.createKey(EntityTombstone.class, DataSerializers.STRING);

    private TombstoneType type;

    public EntityTombstone(World worldIn) {
        super(worldIn);
        additionalProps = new NBTTagCompound();
    }

    public EntityTombstone(World worldIn, EntityCreepBaseOwnable deadEntity) {
        this(worldIn);

        creatureType = EnumCreatureType.AMBIENT;
        experienceValue = 0;
        setLocationAndAngles(deadEntity.posX, deadEntity.posY, deadEntity.posZ, deadEntity.rotationYaw, 0.0f);
        baseSpeed = 0.0d;
        setBaseTexture(deadEntity.getBaseTexture());
        setLevel(deadEntity.getLevel());
        setExperience(deadEntity.getExperience());
        setTotalDamage(deadEntity.getTotalDamage());
        setArmor(deadEntity.getArmor());
        setInterest(deadEntity.getInterest());
        setOwner(deadEntity.getOwnerId());
        setWanderState(deadEntity.getWanderState());
        setHealthBoost(deadEntity.getHealthBoost());
        setSkillAttack(deadEntity.getSkillAttack());
        setSkillDefend(deadEntity.getSkillDefend());
        setSkillHealing(deadEntity.getSkillHealing());
        setSkillSpeed(deadEntity.getSkillSpeed());
        if(deadEntity.hasCustomName()) setCustomNameTag(deadEntity.getCustomNameTag());
        setTombstoneType(TombstoneType.getTombstoneType(deadEntity.getClass()));

        deadEntity.onTombstoneCreate(additionalProps);

        updateAttributes();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> parameter) {
        super.notifyDataManagerChange(parameter);
        if(parameter.getId() == tombstoneType.getId()) {
            String tsType = dataManager.get(tombstoneType);
            setTombstoneType(tsType.isEmpty() ? null : TombstoneType.valueOf(tsType));
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(tombstoneType, type == null ? "" : type.name());
    }

    @Override
    public boolean getAlwaysRenderNameTag() {
        return false;
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    public String getDeadCreatureTypeName() {
        EntityEntry entry = this.type == null ? null : EntityRegistry.getEntry(this.type.getEntityClass());
        String s;

        if (entry == null || entry.getName() == null) {
            s = "generic";
        } else s = entry.getName();

        return I18n.translateToLocal("entity." + s + ".name");
    }

    @Override
    protected void initEntityAI() {
        clearAITasks();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (rand.nextInt(10) == 0) {
            return CreepsSoundHandler.tombstoneSound;
        }

        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (itemStack.isEmpty() || itemStack.getItem() != CreepsItemHandler.lifeGem) {
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("entity.morecreeps.tombstone.lifegem"));
            }

            return true;
        } else if (!isPlayerOwner(player)) {
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("entity.morecreeps.tombstone.notyour"));
            }

            return true;
        }

        itemStack.shrink(1);

        player.swingArm(hand);

        EffectHelper.smoke(world, this, rand, false);

        if (!world.isRemote && this.type != null) {
            EntityCreepBaseOwnable entity = this.type.getNewInstance().apply(getEntityWorld());

            if (entity != null) {
                entity.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                entity.setBaseTexture(getBaseTexture());
                entity.setLevel(Math.max(1, getLevel() - 1));
                if(hasCustomName()) entity.setCustomNameTag(getCustomNameTag());
                entity.setExperience(getExperience());
                entity.setTotalDamage(getTotalDamage());
                entity.setArmor(getArmor());
                entity.setInterest(getInterest());
                entity.setOwner(getOwnerId());
                entity.setWanderState(getWanderState());
                entity.setHealthBoost(getHealthBoost());
                entity.setSkillAttack(getSkillAttack());
                entity.setSkillDefend(getSkillDefend());
                entity.setSkillHealing(getSkillHealing());
                entity.setSkillSpeed(getSkillSpeed());
                entity.onRevive(additionalProps);
                entity.setInitialHealth();
                world.spawnEntity(entity);
                setDead();
            }
        }

        return true;
    }

    @Override
    protected void updateTexture() {
        setTexture("textures/entity/tombstone.png");
    }

    @Override
    public void onLivingUpdate() {
        motionX = 0.0d;
        motionY = 0.0d;
        motionZ = 0.0d;

        super.onLivingUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setTag("MoreCreepsTombstone", additionalProps);
        compound.setString("MoreCreepsTombstoneType", this.type.name());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        additionalProps = compound.getCompoundTag("MoreCreepsTombstone");
        setTombstoneType(TombstoneType.valueOf(compound.getString("MoreCreepsTombstoneType")));
    }

    @Override
    public boolean attackEntityFrom(@Nullable DamageSource damageSource, float amt) {
        return false;
    }

    private void setTombstoneType(TombstoneType type) {
        if(type == null) return;

        this.type = type;
        dataManager.set(tombstoneType, type.name());
    }

    private enum TombstoneType {

        GUINEA_PIG(EntityGuineaPig.class, EntityGuineaPig::new),
        HOTDOG(EntityHotdog.class, EntityHotdog::new);

        private final Class<? extends Entity> entityClass;
        private final Function<World, EntityCreepBaseOwnable> newInstance;

        TombstoneType(Class<? extends Entity> entityClass, Function<World, EntityCreepBaseOwnable> newInstance) {
            this.entityClass = entityClass;
            this.newInstance = newInstance;
        }

        public Class<? extends Entity> getEntityClass() {
            return entityClass;
        }

        public Function<World, EntityCreepBaseOwnable> getNewInstance() {
            return newInstance;
        }

        public static TombstoneType getTombstoneType(Class<? extends Entity> entityClass) {
            for (TombstoneType type : TombstoneType.values()) {
                if (type.getEntityClass() == entityClass) {
                    return type;
                }
            }

            return null;
        }

    }

}
