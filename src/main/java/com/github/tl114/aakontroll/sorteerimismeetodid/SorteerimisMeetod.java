package com.github.tl114.aakontroll.sorteerimismeetodid;

import java.util.LinkedList;

public abstract class SorteerimisMeetod {
    private final LinkedList<int[]> järjendiSalvestus = new LinkedList<>();
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
        järjendiSalvestus.push(järjend.clone());
    }

    public void sammTagasi() {
        if (järjendiSalvestus.size() > 1) {
            järjendiSalvestus.pop();
        }
        setJärjend(järjendiSalvestus.getFirst());
    }

}
