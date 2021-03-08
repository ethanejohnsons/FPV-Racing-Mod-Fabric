package dev.lazurite.quadz.common.mixin.player;

import com.mojang.authlib.GameProfile;
import dev.lazurite.quadz.common.entity.QuadcopterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.SetCameraEntityS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public ServerPlayNetworkHandler networkHandler;
    @Shadow private Entity cameraEntity;
    @Shadow public abstract Entity getCameraEntity();

    /* Ughhhh */
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    /**
     * This mixin prevents the server log from getting spammed with messages
     * about the player moving too quickly.
     * @param info required by every mixin injection
     */
    @Inject(at = @At("HEAD"), method = "isInTeleportationState()Z", cancellable = true)
    public void isInTeleportationState(CallbackInfoReturnable<Boolean> info) {
        if (getCameraEntity() instanceof QuadcopterEntity) {
            info.setReturnValue(true);
        }
    }

    /**
     * Changes the behavior of sneaking as the player so that when the player
     * is flying a drone, they cannot exit from the goggles at this point in the code.
     * @param player the player on which this method was originally called
     * @return whether or not the player should dismount
     */
    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldDismount()Z")
    )
    public boolean shouldDismount(ServerPlayerEntity player) {
        if (player.getCameraEntity() instanceof QuadcopterEntity) {
            return false;
        } else {
            return player.isSneaking();
        }
    }

//    @Override
//    public void setView(Entity entity) {
//        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
//        ShouldRenderPlayerS2C.send(player, !player.equals(entity));
//        player.setCameraEntity(entity);
//
//        if (entity instanceof QuadcopterEntity) {
//            QuadcopterEntity quadcopter = (QuadcopterEntity) entity;
//
//            for (int i = 0; i < 9; i++) {
//                ItemStack stack = player.inventory.getStack(i);
//
//                if (stack.getItem() instanceof TransmitterItem) {
//                    if (quadcopter.isBound(FPVRacing.TRANSMITTER_CONTAINER.get(stack))) {
//                        player.inventory.selectedSlot = i;
//                        SelectedSlotS2C.send(player, i);
//                    }
//                }
//            }
//        }
//    }
}
