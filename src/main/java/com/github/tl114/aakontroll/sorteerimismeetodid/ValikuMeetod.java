package com.github.tl114.aakontroll.sorteerimismeetodid;

public class ValikuMeetod extends SorteerimisMeetod {

    private int töödeldud = 0;

    public ValikuMeetod(int[] järjend) {
        super(järjend);
        super.salvestaSeis();
    }

    @Override
    public void järgmineSamm() {
        if (töödeldud == getJärjend().length) {
            return;
        }

        int minimaalne = getJärjend()[töödeldud];
        int minIndeks = töödeldud;
        boolean leitiVäiksem = false;

        for (int i = töödeldud; i < getJärjend().length; i++) {
            if (getJärjend()[i] < minimaalne) {
                minimaalne = getJärjend()[i];
                minIndeks = i;
                leitiVäiksem = true;
            }
        }
        if (leitiVäiksem) {
            vahetaElemendid(minIndeks);
        }
        töödeldud++;
    }

    private void vahetaElemendid(int indeks) {
        int temp = getJärjend()[töödeldud];
        getJärjend()[töödeldud] = getJärjend()[indeks];
        getJärjend()[indeks] = temp;
    }

    @Override
    public void sammTagasi() {
        if (töödeldud > 0) {
            töödeldud--;
        }
    }
}
