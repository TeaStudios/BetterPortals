package de.johni0702.minecraft.view.impl.mixin;

import de.johni0702.minecraft.view.impl.client.render.ViewCameraEntity;
import de.johni0702.minecraft.view.impl.client.render.ViewChunkRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
    @Shadow @Final private Minecraft mc;

    @Redirect(method = "loadRenderers", at = @At(value = "NEW", target = "net/minecraft/client/renderer/chunk/ChunkRenderDispatcher"))
    private ChunkRenderDispatcher createChunkRenderDispatcher() {
        return new ViewChunkRenderDispatcher();
    }

    //
    // For rendering views, we use a special ViewCameraEntity which may be positioned anywhere nearby but which
    // will keep the view frustum centered on the player entity (the ViewEntity synced with the server).
    // This allows views to be rendered from arbitrary locations without causing chunks to be unloaded.
    //
    // To accomplish that, the following redirectors will query the player for posX/Y/Z and chunkCoordX/Y/Z if the
    // view entity is a ViewCameraEntity, otherwise they'll behave as normal.
    //

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;posX:D", opcode = Opcodes.GETFIELD)
    )
    private double getCameraPosX(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.posX;
        } else {
            return entity.posX;
        }
    }

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;posY:D", opcode = Opcodes.GETFIELD)
    )
    private double getCameraPosY(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.posY;
        } else {
            return entity.posY;
        }
    }

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;posZ:D", opcode = Opcodes.GETFIELD)
    )
    private double getCameraPosZ(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.posZ;
        } else {
            return entity.posZ;
        }
    }

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;chunkCoordX:I", opcode = Opcodes.GETFIELD)
    )
    private int getCameraChunkCoordX(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.chunkCoordX;
        } else {
            return entity.chunkCoordX;
        }
    }

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;chunkCoordY:I", opcode = Opcodes.GETFIELD)
    )
    private int getCameraChunkCoordY(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.chunkCoordY;
        } else {
            return entity.chunkCoordY;
        }
    }

    @Redirect(
            method = "setupTerrain",
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ViewFrustum;updateChunkPositions(DD)V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;chunkCoordZ:I", opcode = Opcodes.GETFIELD)
    )
    private int getCameraChunkCoordZ(Entity entity) {
        if (entity instanceof ViewCameraEntity) {
            return mc.player.chunkCoordZ;
        } else {
            return entity.chunkCoordZ;
        }
    }
}