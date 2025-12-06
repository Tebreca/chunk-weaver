package nl.ggentertainment.chunkweaver.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.blacksmith.BlacksmithAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.KeyItemHandler;
import nl.ggentertainment.chunkweaver.common.core.classes.farmer.FarmerAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.fighter.FighterAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.miner.MinerAttributes;
import nl.ggentertainment.chunkweaver.common.core.classes.wizard.WizardAttributes;
import nl.ggentertainment.chunkweaver.common.network.KeyVaultPackets;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;

@Mixin(value = Player.class, priority = 1200)
public abstract class PlayerEntityMixin extends Entity implements DynamicVaultHolder {


    public PlayerEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;createLivingAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;"))
    private static AttributeSupplier.Builder modifyExpressionValueAtCreateLivingAttributes(AttributeSupplier.Builder builder) {
        return builder
                // Blacksmith: masterworks
                .add(BlacksmithAttributes.FORGE_CHANCE).add(BlacksmithAttributes.FORGE_MODIFIER).add(BlacksmithAttributes.FORGE_ROLLS).add(BlacksmithAttributes.MAX_SLOT_COUNT).add(BlacksmithAttributes.MIN_SLOT_COUNT) // Extra special tablets
                // Blacksmith: Anvil
                .add(BlacksmithAttributes.REPAIR_COST_MODFIER).add(BlacksmithAttributes.XP_COST_MODFIER)
                // Farmer: Green fingers
                .add(FarmerAttributes.HARVEST_DROP_CHANCE).add(FarmerAttributes.HARVEST_MUTATION_CHANCE).add(FarmerAttributes.HARVEST_MUTATION_QUALITY).add(FarmerAttributes.HARVEST_REGROW_CHANCE)
                // Miner: Pickage proficient
                .add(MinerAttributes.DROP_MODIFIER)
                // Miner: Underwater minng
                .add(MinerAttributes.ORE_BREATHING)
                // Miner: Supermine
                .add(MinerAttributes.DURABILITY_NEGATION)
                .add(MinerAttributes.SUPERMINE_RANGE)
                // Fighter: General
                .add(FighterAttributes.ABILITY_LENGTH).add(FighterAttributes.ABILITY_POWER)
                // Figher: Brawler
                .add(FighterAttributes.BLOODLUST_RADIUS).add(FighterAttributes.LIFE_STEAL)
                // Fighter: Ranged
                .add(FighterAttributes.ARROW_POWER_MODIFIER).add(FighterAttributes.ACCURACY)
                // Fighter: Tank
                .add(FighterAttributes.NEGATE_CHANCE).add(FighterAttributes.NEGATE_AMOUNT)
                // Wizard
                .add(WizardAttributes.MAGE_LEVEL);
    }

    /* Implement DynamicVaultHolder */

    @Nullable
    @Unique
    private KeyItemHandler chunkweaver$keyVault = null;
    @Unique
    private int chunkweaver$slotCount = 0;

    @Intrinsic
    @Override
    public boolean hasVault() {
        if (getData(CLASS_ATTACHMENT) == PlayerClass.ENGINEER) {
            refresh();
            return chunkweaver$slotCount > 0;
        }
        return false;
    }

    @Intrinsic
    @Override
    public Optional<KeyItemHandler> getVault() {
        return Optional.ofNullable(chunkweaver$keyVault);
    }

    @Intrinsic
    @Override
    public boolean refresh() {
        if (chunkweaver$keyVault == null) {
            chunkweaver$keyVault = new KeyItemHandler(chunkweaver$slotCount);
            return true;
        } else if (chunkweaver$keyVault.getSlots() != chunkweaver$slotCount) {
            Deque<ItemStack> stacks = new LinkedList<>();
            for (int i = 0; i < chunkweaver$keyVault.getSlots(); i++) {
                stacks.add(chunkweaver$keyVault.getStackInSlot(i).copy());
            }
            int i = chunkweaver$slotCount - 1;
            chunkweaver$keyVault = new KeyItemHandler(chunkweaver$slotCount);
            while (!stacks.isEmpty() && i >= 0) {
                chunkweaver$keyVault.insertItem(i--, stacks.pop(), false);
            }
            return true;
        }
        return false;
    }

    @Intrinsic
    @Override
    public int vaultSize() {
        return chunkweaver$slotCount;
    }

    @Intrinsic
    @Override
    public int grow(int count) {
        chunkweaver$slotCount = Math.clamp(chunkweaver$slotCount + count, 0, 5);
        refresh();
        if (((Object) this) instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new KeyVaultPackets.SlotCount(chunkweaver$slotCount));
        }
        return chunkweaver$slotCount;
    }

    @Intrinsic
    @Override
    public DynamicVaultHolder empty() {
        chunkweaver$keyVault = new KeyItemHandler(chunkweaver$slotCount);
        return this;
    }

    @Intrinsic
    @Override
    public void copy(DynamicVaultHolder other) {
        chunkweaver$slotCount = other.vaultSize();
        chunkweaver$keyVault = other.getVault().orElseGet(() -> new KeyItemHandler(chunkweaver$slotCount));
        if (((Object) this) instanceof ServerPlayer player)
            PacketDistributor.sendToPlayer(player, new KeyVaultPackets.SlotCount(chunkweaver$slotCount));
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    public void chunkweaver$read(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("key_vault")) {
            chunkweaver$keyVault = KeyItemHandler.read((CompoundTag) compound.get("key_vault"));
            chunkweaver$slotCount = chunkweaver$keyVault.getSlots();
            if (((Object) this) instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new KeyVaultPackets.SlotCount(chunkweaver$slotCount));
            }
        }
    }

    @SuppressWarnings("nullable")
    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    public void chunkweaver$write(CompoundTag compound, CallbackInfo ci) {
        if (hasVault()) {
            chunkweaver$keyVault.write(compound);
        }
    }

    @Override
    public CompoundTag getPersistentData() {
        return super.getPersistentData();
    }


}
