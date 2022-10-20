package com.github.tl114.aakontroll.sorteerimismeetodid;

import java.util.LinkedList;

public abstract class SorteerimisMeetod {
    private int[] järjend;

    public SorteerimisMeetod(int[] järjend) {
        this.järjend = järjend;
    }

    public int[] getJärjend() {
        return järjend;
    }

    public void setJärjend(int[] järjend) {
        this.järjend = järjend;
    }

    public void järgmineSamm() {}

    protected void salvestaSeis() {
    }

    public void sammTagasi() {
    }

}
