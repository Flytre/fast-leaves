package net.flytre.fast_leaves.mixin;

import net.flytre.fast_leaves.Variables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlocksMixin {

    @Inject(method = "onBroken", at = @At("HEAD"))
    public void queue(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if((Object)this instanceof LeavesBlock) {
            Variables.to_check.add(pos);
        }
    }
}
