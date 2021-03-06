/*
 * CLAMOR project
 * by MINA BERGER
 */
package la.clamor.forma;

import java.io.File;
import la.clamor.Aestima;
import la.clamor.Functiones;
import la.clamor.Legibilis;
import la.clamor.Punctum;
import la.clamor.Res;
import la.clamor.io.IOUtil;
import la.clamor.io.ScriptorWav;
import la.clamor.referibile.OscillatioSine;

/**
 *
 * @author minae.hiyamae
 */
public class Limitter implements Forma {

    Punctum terminus;
    Punctum compendium_ante;
    Punctum compendium_post;

    public Limitter(Punctum terminus) {
        this(terminus, new Punctum(1), new Punctum(1).partior(terminus));
    }

    public Limitter(Punctum terminus, Punctum compendium_ante, Punctum compendium_post) {
        this.terminus = terminus;
        this.compendium_ante = compendium_ante;
        this.compendium_post = compendium_post;
    }

    @Override
    public int resto() {
        return 0;
    }

    @Override
    public Punctum formo(Punctum lectum) {
        final Aestima zero = new Aestima();
        Punctum multiplicatum = lectum.multiplico(compendium_ante);
        for (int i = 0; i < Res.publica.channel(); i++) {
            Aestima aestimatio = multiplicatum.capioAestima(i);
            if (aestimatio.compareTo(zero) < 0) {
                aestimatio = aestimatio.max(terminus.capioAestima(i).nego());
            } else {
                aestimatio = aestimatio.min(terminus.capioAestima(i));
            }
            lectum.ponoAestimatio(i, aestimatio);
        }
        return lectum.multiplico(compendium_post);
    }

    public static void main(String[] args) {
        File out_file = new File(IOUtil.getDirectory("opus"), "distorquetor.wav");
        ScriptorWav sl = new ScriptorWav(out_file);
        sl.scribo(new FormaLegibilis(new Legibilis() {
            OscillatioSine o = new OscillatioSine();
            int count = 0;

            @Override
            public Punctum lego() {
                count++;
                return o.lego(new Punctum(440));
            }

            @Override
            public boolean paratusSum() {
                return count < 144000;
            }

            @Override
            public void close() {
            }
        }, new Limitter(new Punctum(0.1), new Punctum(1.5), new Punctum(1))), false);
        Functiones.ludoLimam(out_file);

    }

    @Override
    public void ponoPunctum(int index, double tempus, Punctum punctum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
    }

}
