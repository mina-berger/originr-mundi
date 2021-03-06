/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package la.clamor.forma;

import la.clamor.referibile.OscillatioPulse;
import java.io.File;
import la.clamor.Envelope;
import la.clamor.Positio;
import la.clamor.Punctum;
import la.clamor.Res;
import la.clamor.io.IOUtil;
import la.clamor.io.ScriptorWav;
import la.clamor.referibile.ModEnv;
import la.clamor.referibile.Referibile;

/**
 *
 * @author mina
 */
public class VCF implements Forma {

    private final ModEnv filters;
    private final IIRFilter[] iirs;
    private int index;

    public VCF(Envelope<Punctum> filters) {
        this(new ModEnv(filters));
    }

    public VCF(ModEnv filters) {
        this.filters = filters;
        this.iirs = new IIRFilter[Res.publica.channel()];
        Punctum primo_filter = filters.capio(0);
        for (int i = 0; i < Res.publica.channel(); i++) {
            iirs[i] = new IIRFilter(primo_filter.capioAestima(i).doubleValue(), true);
        }
        index = 0;
    }

    @Override
    public Punctum formo(Punctum lectum) {
        Punctum reditum = new Punctum();
        for (int i = 0; i < Res.publica.channel(); i++) {
            double freq = filters.capio(index).capioAestima(i).doubleValue();
            iirs[i].rescribo(freq, true);
            reditum.ponoAestimatio(i, iirs[i].formo(new Punctum(lectum)).capioAestima(i));
        }
        index++;
        return reditum;
    }

    @Override
    public int resto() {
        return 0;
    }

    public static void _main(String[] args) {
        Res.publica.ponoChannel(4);
        File out_file = new File(IOUtil.getDirectory("opus"), "iir_osc.wav");
        ScriptorWav sw = new ScriptorWav(out_file);
        sw.scribo(CadentesFormae.capioLegibilis(new Referibile(new OscillatioPulse(false), new Envelope<>(new Punctum(500)), 5000),
                new VCF(new Envelope<>(new Punctum(500))),
                new VCA(new Envelope<>(new Punctum(),
                        new Positio(50, new Punctum(1, 0, 0, 0)),
                        new Positio(1000, new Punctum(0, 1, 0, 0)),
                        new Positio(2000, new Punctum(0, 0, 0, 1)),
                        new Positio(3000, new Punctum(0, 0, 1, 0)),
                        new Positio(4000, new Punctum(1, 0, 0, 0)),
                        new Positio(5000, new Punctum(0))))
        ), false);

    }

    public static void main(String[] args) {
        double value = 10000;
        Envelope<Punctum> env = new Envelope<>(new Punctum(value));
        for (int i = 0; i < 40; i++) {
            value *= 0.9;
            env.ponoPositiones(new Positio<>(i * 100, new Punctum(value)));
        }
        File out_file = new File(IOUtil.getDirectory("opus"), "iir_osc1.wav");
        ScriptorWav sw = new ScriptorWav(out_file);
        sw.scribo(CadentesFormae.capioLegibilis(new Referibile(new OscillatioPulse(false), new Envelope<>(new Punctum(500)), 5000),
                new VCF(new Envelope<>(true, new Punctum(50),
                        new Positio<>(1000, new Punctum(10000)),
                        new Positio<>(4000, new Punctum(50))
                )),
                new VCA(new Envelope<>(true, new Punctum(),
                        new Positio(50, new Punctum(1))/*, 
                new Positio(3000, new Punctum(1)), 
                new Positio(4000, new Punctum(0))*/))), false);
    }

    @Override
    public void ponoPunctum(int index, double tempus, Punctum punctum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
    }

}
