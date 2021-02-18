package dev.lazurite.fpvracing.common.util.access;

import com.jme3.math.Vector3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public interface Matrix4fAccess {
    Vector3f matrixToVector();
    void fromQuaternion(Quaternion q);

    @SuppressWarnings("ConstantConditions")
    static Matrix4fAccess from(Matrix4f self) {
        return (Matrix4fAccess) (Object) self;
    }
}
