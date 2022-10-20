package com.github.tl114.aakontroll.sorteerimismeetodid;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class KuhjaMeetod extends SorteerimisMeetod {
    private final boolean kuhjasta;
    private int viimaneTipp;
    private int töödeldudOsa;

    public KuhjaMeetod(boolean kuhjasta, int[] järjend) {
        super(järjend);
        this.kuhjasta = kuhjasta;
        viimaneTipp = Math.floorDiv(getJärjend().length, 2) - 1;
    }

    public void järgmineSamm() {
        if (kuhjasta) {
            kuhjastaSamm();
        } else {
            kuhjaMeetodilJärjestamiseSamm();
        }
    }

    private void kuhjastaSamm() {
        if (viimaneTipp >= 0) {
            kuhjastaTipp(viimaneTipp);
            viimaneTipp--;
        }
    }

    private void kuhjaMeetodilJärjestamiseSamm() {
        if (töödeldudOsa == getJärjend().length - 1) {
            return;
        }
        int viimaneIndeks = getJärjend().length - (1 + töödeldudOsa);

        int temp = getJärjend()[viimaneIndeks];
        getJärjend()[viimaneIndeks] = getJärjend()[0];
        getJärjend()[0] = temp;


        int[] sorteeritud = Arrays.copyOfRange(getJärjend(), viimaneIndeks, getJärjend().length);
        setJärjend(Arrays.copyOfRange(getJärjend(), 0, viimaneIndeks));
        kuhjastaTipp(0);
        setJärjend(ArrayUtils.addAll(getJärjend(), sorteeritud));
        töödeldudOsa++;
    }

    void kuhjastaTipp(int tipuIndeks) {
        int tipp = getJärjend()[tipuIndeks];
        int viimane = getJärjend().length - 1;
        if (vasak(tipuIndeks) > viimane) return;
        if (vasak(tipuIndeks) == viimane) {
            if (getJärjend()[vasak(tipuIndeks)] > getJärjend()[tipuIndeks]) {
                vaheta(tipuIndeks, vasak(tipuIndeks));
            }
            return;
        }
        int vasak = getJärjend()[vasak(tipuIndeks)];
        int parem = getJärjend()[parem(tipuIndeks)];

        if (vasak < tipp && parem < tipp) return;

        if (parem > tipp && vasak > tipp) {
            if (parem == vasak) vaheta(tipuIndeks, vasak(tipuIndeks));
            else {
                if (parem > vasak) {
                    vaheta(tipuIndeks, parem(tipuIndeks));
                } else {
                    vaheta(tipuIndeks, vasak(tipuIndeks));
                }
            }
            return;
        }
        if (parem > tipp) {
            vaheta(tipuIndeks, parem(tipuIndeks));
        }
        if (vasak > tipp) {
            vaheta(tipuIndeks, vasak(tipuIndeks));
        }
    }

    private void vaheta(int indeks, int alluvIndeks) {
        int temp = getJärjend()[indeks];
        getJärjend()[indeks] = getJärjend()[alluvIndeks];
        getJärjend()[alluvIndeks] = temp;
        kuhjastaTipp(alluvIndeks);
    }

    public void sammTagasi() {
        if (kuhjasta) {
            if (viimaneTipp < Math.floorDiv(getJärjend().length, 2)) {
                viimaneTipp++;
            }
        } else {
            if (töödeldudOsa > 0) {
                töödeldudOsa--;
            }
        }
    }

    private int vasak(int indeks) {
        return 2 * indeks + 1;
    }

    private int parem(int indeks) {
        return 2 * indeks + 2;
    }
}
