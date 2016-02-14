/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package la.clamor.deinde;

import java.util.ArrayList;
import static la.clamor.Constantia.CHANNEL;
import la.clamor.Legibilis;
import la.clamor.Puncta;
import la.clamor.Punctum;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author mina
 */
public class FourierSampler2 extends AbstractFourierSampler {
    FastFourierTransformer ftt;
    
    public FourierSampler2(Legibilis fons, int length, int interval){
        super(fons, length, interval);
        ftt = new FastFourierTransformer(DftNormalization.STANDARD);
    }
    @Override
    public Spectrum transform(Puncta values){
        Spectrum spectrum = new Spectrum(values.longitudo());
        Complex[][] complices = new Complex[CHANNEL][];
        for(int i = 0;i < CHANNEL;i++){
            complices[i] = ftt.transform(values.capioDoubleArray(i), TransformType.FORWARD);
        }
        for(int i = 0;i < values.longitudo();i++){
            double[] real = new double[CHANNEL];
            double[] imag = new double[CHANNEL];
            for(int j = 0;j < CHANNEL;j++){
                real[j] = complices[j][i].getReal();
                imag[j] = complices[j][i].getImaginary();
            }
            spectrum.add(i, true,  new Punctum(real));
            spectrum.add(i, false, new Punctum(imag));
        }
        return spectrum;
    }
    
}
