/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package la.clamor.voix;

import la.clamor.Mergibilis;

/**
 *
 * @author mina
 */
public class Vowel implements Mergibilis<Vowel>{
    public static final Vowel A = new Vowel(800, 1200, 2500, 3500);
    public static final Vowel I = new Vowel(300, 2300, 2900, 3500);
    public static final Vowel U = new Vowel(300, 1200, 2500, 3500);
    public static final Vowel E = new Vowel(500, 1900, 2500, 3500);
    public static final Vowel O = new Vowel(500, 800, 2500, 3500);
    double[] freqs;
    public Vowel(double freq0, double freq1, double freq2, double freq3){
        freqs = new double[]{freq0, freq1, freq2, freq3};
    }
    public double[] getFrequentia(){
        return freqs;
    }

    @Override
    public Vowel mergo(long diff, long index, Vowel tectum) {
        return new Vowel(
            getValue(diff, index, freqs[0], tectum.freqs[0]),
            getValue(diff, index, freqs[1], tectum.freqs[1]),
            getValue(diff, index, freqs[2], tectum.freqs[2]),
            getValue(diff, index, freqs[3], tectum.freqs[3])
        );
    }
    private double getValue(double diff, double index, double solum, double tectum){
        return (solum * (diff - index) + tectum * index) / diff;
    }

    @Override
    public Vowel multiplico(Vowel factor) {
        return new Vowel(freqs[0] * factor.freqs[0], freqs[1] * factor.freqs[1], freqs[2] * factor.freqs[2], freqs[3] * factor.freqs[3]);
    }

    @Override
    public Vowel partior(Vowel partitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vowel addo(Vowel multiplicator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vowel subtraho(Vowel multiplicator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vowel nego() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
