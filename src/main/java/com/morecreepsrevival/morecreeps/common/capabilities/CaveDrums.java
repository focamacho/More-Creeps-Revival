package com.morecreepsrevival.morecreeps.common.capabilities;

public class CaveDrums implements ICaveDrums {
    private int drumsTime = 0;

    public int getDrumsTime() {
        return drumsTime;
    }

    public void setDrumsTime(int drumsTimeIn) {
        drumsTime = drumsTimeIn;
    }
}
