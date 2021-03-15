package dev.imabad.ndi;

import net.minecraft.client.Camera;

public interface CameraExt {

    void setCameraY(float cameraY);

    float getCameraY();

    static CameraExt from(Camera self){
        return (CameraExt) self;
    }

}
