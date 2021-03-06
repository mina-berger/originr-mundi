/*
 * CLAMOR project
 * by MINA BERGER
 */
package la.clamor.forma;

import java.io.File;
import la.clamor.Functiones;
import la.clamor.Legibilis;
import la.clamor.OrbisPuncti;
import la.clamor.Punctum;
import la.clamor.Aestima;
import la.clamor.Res;
import la.clamor.io.IOUtil;
import la.clamor.io.ScriptorWav;
import la.clamor.referibile.OscillatioSine;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author minae.hiyamae
 */
public class Chorus implements Forma {

    Punctum frequentia;
    Punctum compendium_siccus;
    Punctum compendium_humens;
    int longitudo;
    OrbisPuncti oa;
    OscillatioSine osc;

    public Chorus(Punctum profundum, Punctum frequentia, Punctum compendium_siccus, Punctum compendium_humens) {
        this.frequentia = frequentia;
        this.compendium_siccus = compendium_siccus;
        this.compendium_humens = compendium_humens;
        long l_longitudo = Functiones.adPositio(profundum.maxAbs().doubleValue());
        if (l_longitudo * 2 > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("profundum is illegal.(smaller than "
                    + Functiones.adTempus(Integer.MAX_VALUE) + ")");
        }
        longitudo = new Long(l_longitudo).intValue();
        oa = new OrbisPuncti(longitudo * 2 + 1);
        osc = new OscillatioSine();
    }

    @Override
    public int resto() {
        return oa.longitudo();
    }

    @Override
    public Punctum formo(Punctum lectum) {
        Punctum oscillatio = osc.lego(frequentia);
        Punctum punctum = new Punctum();
        for (int i = 0; i < Res.publica.channel(); i++) {
            double index = oscillatio.capioAestima(i).addo(new Aestima(1)).multiplico(new Aestima(longitudo)).doubleValue();
            //System.out.println(index);
            Aestima floor = oa.capio((int) FastMath.floor(index)).capioAestima(i);
            Aestima ceil = oa.capio((int) FastMath.ceil(index)).capioAestima(i);
            Aestima aestimatio
                    = floor.multiplico(new Aestima(FastMath.ceil(index) - index))
                    .addo(ceil.multiplico(new Aestima(index - FastMath.floor(index))));
            punctum.ponoAestimatio(i, aestimatio);
        }
        oa.pono(lectum);
        return punctum.multiplico(compendium_humens).addo(lectum.multiplico(compendium_siccus));
    }

    public static void main(String[] args) {
        File out_file = new File(IOUtil.getDirectory("opus"), "chorus.wav");
        ScriptorWav sw = new ScriptorWav(out_file);
        sw.scribo(new FormaLegibilis(new Legibilis() {
            OscillatioSine o = new OscillatioSine();
            int count = 0;

            @Override
            public Punctum lego() {
                count++;
                return o.lego(new Punctum(880));
            }

            @Override
            public boolean paratusSum() {
                return count < 144000;
            }

            @Override
            public void close() {
            }
        }, new Chorus(new Punctum(0.3), new Punctum(9, 10), new Punctum(1), new Punctum(1, -1))), false);
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
