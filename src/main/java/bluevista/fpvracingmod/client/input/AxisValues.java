package bluevista.fpvracingmod.client.input;

public class AxisValues {
    public float currT;
    public float currX;
    public float currY;
    public float currZ;

    public AxisValues() {

    }

    public AxisValues(float currT, float currX, float currY, float currZ) {
        this.set(currT, currX, currY, currZ);
    }

    public void set(AxisValues axisValues) {
        this.set(axisValues.currT, axisValues.currX, axisValues.currY, axisValues.currZ);
    }

    public void set(float currT, float currX, float currY, float currZ) {
        this.currT = currT;
        this.currX = currX;
        this.currY = currY;
        this.currZ = currZ;
    }
}
