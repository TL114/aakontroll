package com.github.tl114.aakontroll.sorteerimismeetodid;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class PisteMeetod extends SorteerimisMeetod {

    private int sorteeritud = 0;

    public PisteMeetod(int[] järjend) {
        super(järjend);
        super.salvestaSeis();
    }

    @Override
    public void järgmineSamm() {
        if (sorteeritud < getJärjend().length) {
            int[] töödeldavOsa = Arrays.copyOfRange(getJärjend(), 0, sorteeritud + 1);
            Arrays.sort(töödeldavOsa);
            setJärjend(ArrayUtils.addAll(töödeldavOsa, Arrays.copyOfRange(getJärjend(), sorteeritud + 1, getJärjend().length)));
            sorteeritud++;
        }
    }

    @Override
    public void sammTagasi() {
        if (sorteeritud > 0) {
            sorteeritud--;
        }
    }
}
