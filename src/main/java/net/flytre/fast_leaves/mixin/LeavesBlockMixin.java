package net.flytre.fast_leaves.mixin;


import net.flytre.fast_leaves.Variables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {



    @Shadow
    private static int getDistanceFromLog(BlockState state) {
        return 0;
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"))
    public void queue(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> cir) {
        int i = getDistanceFromLog(newState) + 1;
        if(i >= 4)
            Variables.to_check.add(pos);
    }

}
