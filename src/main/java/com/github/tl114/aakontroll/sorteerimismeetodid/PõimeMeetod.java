package com.github.tl114.aakontroll.sorteerimismeetodid;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class PõimeMeetod extends SorteerimisMeetod {

    private final Salvestus salvestus = new Salvestus();

    private LinkedList<Pair<Integer, Integer>> vasakOsad = new LinkedList<>();
    private LinkedList<Pair<Integer, Integer>> paremOsad = new LinkedList<>();
    private LinkedList<Pair<Integer, Integer>> põimiOsad = new LinkedList<>();

    private boolean põimi;
    private boolean altÜlesse;

    private int töötlemataOsa = 0;
    private int algus;
    private int lõpp;
    private int iteratsioon;

    public PõimeMeetod(boolean altÜlesse, int[] järjend) {
        super(järjend);
        this.altÜlesse = altÜlesse;
        initialize();
    }

    private void initialize() {
        if (altÜlesse) {
            iteratsioon = 1;
            töötlemataOsa = getJärjend().length;
        } else {
            vasakOsad.push(new Pair<>(0, getJärjend().length - 1));
        }
        salvestus.salvesta();
    }

    @Override
    public void järgmineSamm() {
        if (altÜlesse) {
            altÜlesseJärgmineSamm();
        } else {
            põimeMeetodJärgmineSamm();
        }
        salvestaSeis();
    }

    private void altÜlesseJärgmineSamm() {
        int iteratsiooniSuurus = (int) Math.pow(2, iteratsioon);
        boolean töödeldud = false;
        int osaSuurus = iteratsiooniSuurus / 2;
        if (osaSuurus >= getJärjend().length) {
            return;
        }
        if (töötlemataOsa == getJärjend().length) {
            algus = 0;
            lõpp = 2 * osaSuurus - 1;
        }
        if (töötlemataOsa != 0) {
            if (töötlemataOsa < iteratsiooniSuurus) {
                if (iteratsiooniSuurus - töötlemataOsa < osaSuurus) {
                    lõpp = getJärjend().length - 1;
                    põimiMassiiviOsa(algus, lõpp);
                }
                töötlemataOsa = 0;
                töödeldud = true;
            } else {
                põimiMassiiviOsa(algus, lõpp);
                töötlemataOsa -= iteratsiooniSuurus;
                if (töötlemataOsa == 0) {
                    töödeldud = true;
                }
            }
            algus += iteratsiooniSuurus;
            lõpp += iteratsiooniSuurus;
        }
        if (töödeldud) {
            töötlemataOsa = getJärjend().length;
            iteratsioon++;
        }
    }

    private void põimeMeetodJärgmineSamm() {
        if (põimi) {
            põimi();
        } else {
            poolitaJärjendiOsa();
        }
    }

    private void põimi() {
        if (põimiOsad.isEmpty()) {
            return;
        }
        Pair<Integer, Integer> põimitavOsa = põimiOsad.pop();
        põimiMassiiviOsa(põimitavOsa.getKey(), põimitavOsa.getValue());
        if (!paremOsad.isEmpty()) {
            Pair<Integer, Integer> järgmineOsa = paremOsad.getFirst();
            Pair<Integer, Integer> järgminePõimitav = põimiOsad.getFirst();
            if (järgmineOsa.getValue() - järgmineOsa.getKey() < järgminePõimitav.getValue() - järgminePõimitav.getKey()) {
                põimi = false;
            }
        }
    }

    private void poolitaJärjendiOsa() {
        boolean töödeldakseParemat = false;
        if (vasakOsad.isEmpty() && paremOsad.isEmpty()) {
            return;
        }
        Pair<Integer, Integer> järjendiOsa;
        if (vasakOsad.isEmpty()) {
            järjendiOsa = paremOsad.pop();
            töödeldakseParemat = true;
        } else {
            järjendiOsa = vasakOsad.pop();
        }
        algus = järjendiOsa.getKey();
        lõpp = järjendiOsa.getValue();
        int osaPikkus = lõpp - algus + 1;
        int vasakLõpp = algus + (osaPikkus / 2 - 1);
        int paremAlgus = algus + (osaPikkus / 2);
        if (algus != vasakLõpp) {
            vasakOsad.push(new Pair<>(algus, vasakLõpp));
        }
        if (paremAlgus != lõpp) {
            paremOsad.push(new Pair<>(paremAlgus, lõpp));
        }
        if (algus != lõpp) {
            põimiOsad.push(new Pair<>(algus, lõpp));
        }
        if ((algus == vasakLõpp && paremAlgus == lõpp && algus != lõpp)) {
            põimi = true;
        }
    }

    public void põimiMassiiviOsa(int algus, int lõpp) {
        if (algus == lõpp) {
            return;
        }
        int osaPikkus = lõpp - algus + 1;
        int vasakAlgus = algus;
        int vasakLõpp = vasakAlgus + (osaPikkus / 2 - 1);
        if (osaPikkus % 2 != 0 && altÜlesse) {
            vasakLõpp += 1;
        }
        int paremAlgus = vasakLõpp + 1;
        int paremLõpp = lõpp;
        int jooksev1 = 0;
        int jooksev2 = 0;
        int[] vasak = Arrays.copyOfRange(getJärjend(), vasakAlgus, vasakLõpp + 1);
        int[] parem = Arrays.copyOfRange(getJärjend(), paremAlgus, paremLõpp + 1);

        for (int i = 0; i < osaPikkus; i++) {
            boolean esimeneOtsas = jooksev1 == vasak.length;
            boolean teineOtsas = jooksev2 == parem.length;
            if (vasakAlgus + i >= getJärjend().length) {
                break;
            }
            if (esimeneOtsas) {
                getJärjend()[algus + i] = parem[jooksev2];
                jooksev2++;
            } else if (teineOtsas) {
                getJärjend()[algus + i] = vasak[jooksev1];
                jooksev1++;
            } else {
                if (vasak[jooksev1] < parem[jooksev2]) {
                    getJärjend()[algus + i] = vasak[jooksev1];
                    jooksev1++;
                } else {
                    getJärjend()[algus + i] = parem[jooksev2];
                    jooksev2++;
                }
            }
        }
    }

    protected void salvestaSeis() {
        salvestus.salvesta();
    }

    @Override
    public void sammTagasi() {
        salvestus.sammTagasi();
    }

    private class Salvestus {
        //Alt-Ülesse
        private final LinkedList<Integer> iteratsioonSalvestus = new LinkedList<>();
        private final LinkedList<Integer> töötlemataOsaSalvestus = new LinkedList<>();
        private final LinkedList<Integer> algusSalvestus = new LinkedList<>();
        private final LinkedList<Integer> lõppSalvestus = new LinkedList<>();

        //Tavaline põime meetod
        private final LinkedList<LinkedList<Pair<Integer, Integer>>> vasakOsadSalvestus = new LinkedList<>();
        private final LinkedList<LinkedList<Pair<Integer, Integer>>> paremOsadSalvestus = new LinkedList<>();
        private final LinkedList<LinkedList<Pair<Integer, Integer>>> põimiOsadSalvestus = new LinkedList<>();
        private final LinkedList<Boolean> põimiSalvestus = new LinkedList<>();

        private void salvesta() {
            if (altÜlesse) {
                salvestaAltÜlesse();
            } else {
                salvestaPõimeMeetod();
            }
        }

        private void salvestaAltÜlesse() {
            iteratsioonSalvestus.push(iteratsioon);
            töötlemataOsaSalvestus.push(töötlemataOsa);
            algusSalvestus.push(algus);
            lõppSalvestus.push(lõpp);
        }

        private void salvestaPõimeMeetod() {
            vasakOsadSalvestus.push((LinkedList<Pair<Integer, Integer>>) vasakOsad.clone());
            paremOsadSalvestus.push((LinkedList<Pair<Integer, Integer>>) paremOsad.clone());
            põimiOsadSalvestus.push((LinkedList<Pair<Integer, Integer>>) põimiOsad.clone());
            põimiSalvestus.push(põimi);
        }

        private void sammTagasi() {
            if (altÜlesse) {
                sammTagasiAltÜlesse();
            } else {
                sammTagasiPõimeMeetod();
            }
        }

        private void sammTagasiPõimeMeetod() {
            if (vasakOsadSalvestus.size() > 1) {
                vasakOsadSalvestus.pop();
                paremOsadSalvestus.pop();
                põimiOsadSalvestus.pop();
                põimiSalvestus.pop();
            }
            vasakOsad = vasakOsadSalvestus.getFirst();
            paremOsad = paremOsadSalvestus.getFirst();
            põimiOsad = põimiOsadSalvestus.getFirst();
            põimi = põimiSalvestus.getFirst();
        }

        private void sammTagasiAltÜlesse() {
            if (iteratsioonSalvestus.size() > 1) {
                iteratsioonSalvestus.pop();
                töötlemataOsaSalvestus.pop();
                algusSalvestus.pop();
                lõppSalvestus.pop();
            }
            iteratsioon = iteratsioonSalvestus.getFirst();
            töötlemataOsa = töötlemataOsaSalvestus.getFirst();
            algus = algusSalvestus.getFirst();
            lõpp = lõppSalvestus.getFirst();
        }
    }
}
