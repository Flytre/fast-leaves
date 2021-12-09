package net.flytre.fast_leaves.mixin;


import net.flytre.fast_leaves.Variables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    private static final Random RANDOM = new Random();

    @Shadow
    public abstract ServerWorld toServerWorld();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {

        ServerWorld world = toServerWorld();

        List<BlockPos> cached_check = new ArrayList<>(Variables.to_check);
        List<BlockPos> to_check = new ArrayList<>(cached_check);
        List<BlockPos> remove = new ArrayList<>();

        for (BlockPos pos : to_check) {
            if (RANDOM.nextInt(10) < 2) {
                BlockState blockState = world.getBlockState(pos);
                Block b = blockState.getBlock();
                if (b.hasRandomTicks(blockState)) {
                    b.randomTick(blockState, world, pos, RANDOM);
                    addNeighbors(cached_check, pos,world);

                }
                remove.add(pos);
            }
        }
        for (BlockPos pos : remove)
            cached_check.remove(pos);

        Variables.to_check.clear();
        Variables.to_check = cached_check;
    }

    private boolean valid(BlockPos pos, ServerWorld world) {
        if (world == null || !world.isChunkLoaded(pos))
            return false;

        BlockState state = world.getBlockState(pos);

        return state != null && state.getBlock() instanceof LeavesBlock;
    }

    private void addNeighbors(List<BlockPos> cached_check, BlockPos pos, ServerWorld world) {

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    if(x == 0 && y == 0 && z == 0)
                        continue;
                    BlockPos nPos = pos.north(x).up(y).east(z);

                    if(valid(nPos,world))
                    cached_check.add(nPos);
                }
            }
        }

    }
}
