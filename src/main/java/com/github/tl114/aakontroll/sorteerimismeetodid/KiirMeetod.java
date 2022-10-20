package com.github.tl114.aakontroll.sorteerimismeetodid;

import javafx.util.Pair;

import java.util.LinkedList;

public class KiirMeetod extends SorteerimisMeetod {
    private final Salvestus salvestus = new Salvestus();
    private final boolean valikuKiirmeetod;
    private LinkedList<Pair<Integer, Integer>> vasakOsad = new LinkedList<>();
    private LinkedList<Pair<Integer, Integer>> paremOsad = new LinkedList<>();
    private int valikuArv;

    private int algus;
    private int lõpp;
    private int lahe;

    public KiirMeetod(boolean valikuKiirmeetod, int valikuArv, int[] järjend) {
        super(järjend);
        this.valikuKiirmeetod = valikuKiirmeetod;
        this.valikuArv = valikuArv - 1;
        initialize();
    }

    private void initialize() {
        if (valikuKiirmeetod) {
            algus = 0;
            lõpp = getJärjend().length - 1;
            lahe = 0;
        } else {
            vasakOsad.push(new Pair<>(0, getJärjend().length - 1));
        }
        salvestaSeis();
    }

    @Override
    public void järgmineSamm() {
        if (valikuKiirmeetod) {
            kiirValikJärgmineSamm();
        } else {
            kiirMeetodJärgmineSamm();
        }
        salvestaSeis();
    }

    private void kiirValikJärgmineSamm() {
        if (valikuArv == 0) {
            return;
        }
        jaotaOsaLahkmeJärgi(algus, lõpp, getJärjend()[algus]);
        int indeks = lahkmeIndeks(lahe, algus, lõpp) + 1;
        int a = indeks - algus;

        if (indeks == valikuArv) {
            return;
        }
        if (a > valikuArv) {
            lõpp = indeks;
        } else {
            valikuArv = valikuArv - a;
            algus = indeks;
            lahe = getJärjend()[indeks];
        }
    }

    public void kiirMeetodJärgmineSamm() {
        if (vasakOsad.isEmpty() && paremOsad.isEmpty()) {
            return;
        }
        Pair<Integer, Integer> osa;
        if (vasakOsad.isEmpty()) {
            osa = paremOsad.pop();
        } else {
            osa = vasakOsad.pop();
        }
        algus = osa.getKey();
        lõpp = osa.getValue();
        lahe = getJärjend()[algus];

        jaotaOsaLahkmeJärgi(algus, lõpp, lahe);
        int lahkmeIndeks = lahkmeIndeks(lahe, algus, lõpp);
        if (algus < lahkmeIndeks) {
            vasakOsad.push(new Pair<>(algus, lahkmeIndeks));
        }
        if (lahkmeIndeks + 1 < lõpp) {
            paremOsad.push(new Pair<>(lahkmeIndeks + 1, lõpp));
        }
    }

    private void jaotaOsaLahkmeJärgi(int algus, int lõpp, int lahe) {
        if (algus >= lõpp) return;
        int suurIndeks = algus;
        int väikeIndeks = lõpp;
        boolean leitiSuurem = false;
        boolean leitiVäiksem = false;
        for (int i = algus; i < väikeIndeks; i++) {
            if (getJärjend()[i] >= lahe) {
                suurIndeks = i;
                leitiSuurem = true;
                break;
            }
        }
        for (int i = lõpp; i > suurIndeks; i--) {
            if (getJärjend()[i] < lahe) {
                väikeIndeks = i;
                leitiVäiksem = true;
                break;
            }
        }
        if (!(leitiSuurem && leitiVäiksem)) {
            return;
        }
        int temp = getJärjend()[suurIndeks];
        getJärjend()[suurIndeks] = getJärjend()[väikeIndeks];
        getJärjend()[väikeIndeks] = temp;
        suurIndeks++;
        väikeIndeks--;

        jaotaOsaLahkmeJärgi(suurIndeks, väikeIndeks, lahe);
    }

    private int lahkmeIndeks(int arv, int algus, int lõpp) {
        int index = 0;
        if (getJärjend()[algus] == arv) {
            index = algus;
        } else {
            for (int i = algus; i <= lõpp; i++) {
                if (getJärjend()[i] >= arv) {
                    index = i - 1;
                    break;
                }
            }
        }
        return index;
    }

    protected void salvestaSeis() {
        salvestus.salvesta();
    }

    public void sammTagasi() {
        salvestus.sammTagasi();
    }

    private class Salvestus {
        //Kiirmeetod
        private final LinkedList<LinkedList<Pair<Integer, Integer>>> vasakOsadSalvestus = new LinkedList<>();
        private final LinkedList<LinkedList<Pair<Integer, Integer>>> paremOsadSalvestus = new LinkedList<>();

        //Valiku kiir meetod
        private final LinkedList<Integer> algusSalvestus = new LinkedList<>();
        private final LinkedList<Integer> lõppSalvestus = new LinkedList<>();
        private final LinkedList<Integer> laheSalvestus = new LinkedList<>();

        private void salvesta() {
            if (valikuKiirmeetod) {
                valikuKiirMeetodSalvesta();
            } else {
                kiirMeetodSalvesta();
            }
        }

        private void sammTagasi() {
            if (valikuKiirmeetod) {
                valikuKiirMeetodSammTagasi();
            } else {
                kiirMeetodSammTagasi();
            }
        }


        private void valikuKiirMeetodSalvesta() {
            algusSalvestus.push(algus);
            lõppSalvestus.push(lõpp);
            laheSalvestus.push(lahe);
        }

        private void valikuKiirMeetodSammTagasi() {
            if (algusSalvestus.size() > 1) {
                algusSalvestus.pop();
                lõppSalvestus.pop();
                laheSalvestus.pop();
            }
            algus = algusSalvestus.getFirst();
            lõpp = lõppSalvestus.getFirst();
            lahe = laheSalvestus.getFirst();
        }

        private void kiirMeetodSalvesta() {
            vasakOsadSalvestus.push((LinkedList<Pair<Integer, Integer>>) vasakOsad.clone());
            paremOsadSalvestus.push((LinkedList<Pair<Integer, Integer>>) paremOsad.clone());
        }

        private void kiirMeetodSammTagasi() {
            if (vasakOsadSalvestus.size() > 1) {
                vasakOsadSalvestus.pop();
                paremOsadSalvestus.pop();

            }
            vasakOsad = vasakOsadSalvestus.getFirst();
            paremOsad = paremOsadSalvestus.getFirst();
        }
    }
}
