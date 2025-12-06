package nl.ggentertainment.chunkweaver.common.core.rift.infusion;

import com.google.common.collect.Streams;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlockEntityTypes;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverBlocks;
import nl.ggentertainment.chunkweaver.common.ChunkWeaverItems;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftEntity;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;
import nl.ggentertainment.chunkweaver.common.mixin.accessor.ItemStackHandlerAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;

public class InfusionTableBlockEntity extends SyncedBlockEntity {

    ItemStackHandler itemHandler = new ItemStackHandler(3);

    public boolean isSummoning() {
        return isSummoning;
    }

    private boolean isSummoning = false;
    float progress = 0;
    int tick = 0;
    RiftTier summoning = null;

    public List<ItemStack> getItems() {
        return ((ItemStackHandlerAccessor) itemHandler).getStacks().stream().filter(s -> !s.isEmpty()).toList();
    }

    public void tryUpdate(Player player) {
        if (level == null || !(player instanceof ServerPlayer) || player.getData(CLASS_ATTACHMENT) != PlayerClass.WIZARD)
            return;
        RiftTier tier = getTierForSummon(level);
        player.displayClientMessage(Component.translatable("chunkweaver.infusion_table.ready", tier.getComponent()), true);
    }

    private RiftTier getTierForSummon(@NotNull Level level) {
        List<PylonBlock.PylonType> charged = new ArrayList<>(4);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            LinkedPylon pylon = getLinkedPylonOrFind(i);
            if (pylon != null && //
                    level.getBlockEntity(pylon.pos) instanceof PylonBlockEntity pylon_be //
                    && pylon_be.getcharge() == 1 //
                    && !charged.contains(pylon.type)) {
                charged.add(pylon.type);
                count++;
            }
        }
        return RiftTier.byPylonCount(count);
    }

    private @Nullable LinkedPylon getLinkedPylonOrFind(int i) {
        if (pylons[i] == null) {
            BlockPos pos = getBlockPos().offset(switch (i) {
                case 0 -> Direction.NORTH.getNormal().multiply(2);
                case 1 -> Direction.EAST.getNormal().multiply(2);
                case 2 -> Direction.SOUTH.getNormal().multiply(2);
                case 3 -> Direction.WEST.getNormal().multiply(2);
                default -> new Vec3i(0, 0, 0);
            });
            BlockState blockState = Objects.requireNonNull(level).getBlockState(pos);
            if (blockState.is(ChunkWeaverBlocks.PYLON)) {
                pylons[i] = new LinkedPylon(pos, blockState.getValue(PylonBlock.TYPE));
            }
            return null;
        }
        return pylons[i];
    }

    public void summon(int maxLevel) {
        if (isReadyForSummon() && level instanceof ServerLevel level) {
            RiftTier tierForSummon = getTierForSummon(level, maxLevel);
            this.isSummoning = true;
            this.summoning = tierForSummon;
            this.progress = 0;
            this.tick = 0;
            itemHandler = new ItemStackHandler(3);
            notifyUpdate();
        }
    }

    private RiftTier getTierForSummon(ServerLevel level, int maxLevel) {
        List<PylonBlock.PylonType> charged = new ArrayList<>(4);
        List<BlockPos> poses = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 4; i++) {
            LinkedPylon pylon = getLinkedPylonOrFind(i);
            if (pylon != null && //
                    level.getBlockEntity(pylon.pos) instanceof PylonBlockEntity pylon_be //
                    && pylon_be.getcharge() == 1 //
                    && !charged.contains(pylon.type)) {
                charged.add(pylon.type);
                count++;
                poses.add(pylon.pos);
            } else pylons[i] = null;
        }
        return RiftTier.byPylonCount(Math.min(maxLevel, count));
    }

    public static void tick(Level level, BlockPos pos, InfusionTableBlockEntity be) {
        if (be.isSummoning && ((++be.tick % 5) == 0)) {
            be.tick = 0;
            AtomicBoolean flag = new AtomicBoolean(false);
            Arrays.stream(be.pylons).filter(Objects::nonNull).map(LinkedPylon::pos).map(level::getBlockEntity).forEach(e -> {
                if (e instanceof PylonBlockEntity pylon) {
                    pylon.addCharge(-1);
                } else {
                    flag.set(true);
                }
            });
            if (flag.get()) {
                be.isSummoning = false;
                be.summoning = null;
                be.progress = 0;
                return;
            }
            be.progress++;
            if (be.progress >= 100 || be.summoning == RiftTier.TINY) {
                RiftTier tier = be.summoning;
                Streams.concat(Stream.of(pos), Arrays.stream(be.pylons).filter(Objects::nonNull).map(LinkedPylon::pos))//
                        .forEach(p -> level.destroyBlock(p, true));
                RiftEntity entity = new RiftEntity(level, tier);
                entity.setPos(pos.getBottomCenter().add(0, 0.05f, 0));
                level.addFreshEntity(entity);
            }
        }
    }

    public static <T extends BlockEntity> void absoluteTick(Level level, BlockPos pos, BlockState ignored, T t) {
        if (t instanceof InfusionTableBlockEntity be) tick(level, pos, be);
    }

    public record LinkedPylon(BlockPos pos, PylonBlock.PylonType type) {
    }

    private final LinkedPylon[] pylons = new LinkedPylon[4];

    public Optional<LinkedPylon> getLinked(Direction direction) {
        return switch (direction) {
            case NORTH -> Optional.ofNullable(pylons[0]);
            case EAST -> Optional.ofNullable(pylons[1]);
            case SOUTH -> Optional.ofNullable(pylons[2]);
            case WEST -> Optional.ofNullable(pylons[3]);
            case DOWN, UP -> Optional.empty();
        };
    }

    public void setLinked(Direction direction, LinkedPylon value) {
        switch (direction) {
            case NORTH:
                pylons[0] = value;
                break;
            case EAST:
                pylons[1] = value;
                break;
            case SOUTH:
                pylons[2] = value;
                break;
            case WEST:
                pylons[3] = value;
                break;
            default:
        }
    }

    public InfusionTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ChunkWeaverBlockEntityTypes.INFUSION_TABLE.get(), pos, blockState);
    }

    public @Nullable IItemHandler getCapability(@Nullable Direction direction) {
        if (direction != Direction.UP) {
            return itemHandler;
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.put("items", itemHandler.serializeNBT(registries));
        tag.putFloat("progress", progress);
        tag.putBoolean("summoning", isSummoning);
        if (isSummoning) {
            tag.putInt("type", summoning.ordinal());
        }
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        itemHandler.deserializeNBT(registries, (CompoundTag) tag.get("items"));
        progress = tag.getFloat("progress");
        isSummoning = tag.getBoolean("summoning");
        if (tag.contains("type")) summoning = RiftTier.values()[tag.getInt("type")];
        super.loadAdditional(tag, registries);
    }

    public boolean isReadyForSummon() {
        if (isSummoning) {
            return false;
        }
        NonNullList<ItemStack> stacks = ((ItemStackHandlerAccessor) itemHandler).getStacks();
        return stacks.stream().map(stack -> {
            if (stack.is(ChunkWeaverItems.RIFTSTONE)) {
                return stack.getCount();
            } else if (stack.is(Items.IRON_INGOT)) {
                return stack.getCount() / 16;
            } else if (stack.is(Items.ENDER_PEARL)) {
                return stack.getCount() / 4;
            }
            return 0;
        }).reduce(0, Integer::sum) >= 3;
    }
}
