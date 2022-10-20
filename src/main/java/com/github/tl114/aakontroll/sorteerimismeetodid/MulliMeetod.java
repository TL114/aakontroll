package com.github.tl114.aakontroll.sorteerimismeetodid;

public class MulliMeetod extends SorteerimisMeetod {

    public MulliMeetod(int[] järjend) {
        super(järjend);
    }

    @Override
    public void järgmineSamm() {
        for (int i = getJärjend().length - 1; i > 0; i--) {
            if (getJärjend()[i] < getJärjend()[i - 1]) {
                int temp = getJärjend()[i - 1];
                getJärjend()[i - 1] = getJärjend()[i];
                getJärjend()[i] = temp;
            }
        }
    }
}
