package com.mygdx.pathfindergui;

/**
 * PFTimer. Frontend's logic timer, implemented as a singleton to ensure runtime remains constant
 * and accessible from anywhere.
 */
public final class PFTimer {
    private static PFTimer INSTANCE;
    private int pfRuntime = 0;

    private PFTimer() {}

    public static PFTimer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PFTimer();
        }

        return INSTANCE;
    }

    public int getPfRuntime() {
        return this.pfRuntime;
    }

    public void increasePfRuntime() {
        if (this.pfRuntime >= (Integer.MAX_VALUE - 60)) {
            this.pfRuntime = 0;
        } else {
            this.pfRuntime++;
        }
    }
}
