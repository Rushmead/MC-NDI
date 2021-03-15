package dev.imabad.ndi.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.imabad.ndi.MinecraftClientExt;
import dev.imabad.ndi.NDIMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.storage.LevelStorageSource;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin implements MinecraftClientExt {

    @Mutable
    @Shadow
    @Final
    private RenderTarget mainRenderTarget;
    @Shadow @Final private Window window;

    @Shadow public abstract float getDeltaFrameTime();

    @Shadow public LocalPlayer player;

    @Shadow @Final public File gameDirectory;

    @Shadow private IntegratedServer singleplayerServer;

    @Shadow @Final private LevelStorageSource levelSource;

    @Shadow public abstract ClientPacketListener getConnection();

    @Shadow private boolean pause;

    @Shadow public abstract boolean hasSingleplayerServer();

    @Inject(method = "runTick(Z)V", at=@At("RETURN"))
    public void runTick(boolean tick, CallbackInfo info) {
        NDIMod.getGameRenderHook().render(mainRenderTarget, window, player, getDeltaFrameTime(), pause);
    }

    @Override
    public void setFramebuffer(RenderTarget fb) {
        mainRenderTarget = fb;
    }

    @Inject(method= "setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V", at=@At("RETURN"))
    public void joinWorld(ClientLevel clientWorld, CallbackInfo callbackInfo){
        NDIMod.getCameraManager().load(gameDirectory, hasSingleplayerServer(), singleplayerServer, levelSource, clientWorld, this.getConnection());
    }

    @Inject(method= "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at=@At("HEAD"))
    public void disconnect(Screen screen, CallbackInfo ci){
        NDIMod.getCameraManager().save(gameDirectory, player, hasSingleplayerServer(), levelSource, singleplayerServer);
    }
}
