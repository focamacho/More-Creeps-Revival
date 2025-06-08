package com.morecreepsrevival.morecreeps.common.capabilities;

public class GuineaPigPickedUp implements IGuineaPigPickedUp {
    private boolean pickedUp = false;

    public boolean getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUpIn) {
        pickedUp = pickedUpIn;
    }
}
