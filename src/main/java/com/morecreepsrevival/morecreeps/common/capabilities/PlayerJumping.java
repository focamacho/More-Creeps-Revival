package com.morecreepsrevival.morecreeps.common.capabilities;

public class PlayerJumping implements IPlayerJumping {
    private boolean jumping = false;

    public boolean getJumping() {
        return jumping;
    }

    public void setJumping(boolean jumpingIn) {
        jumping = jumpingIn;
    }
}
