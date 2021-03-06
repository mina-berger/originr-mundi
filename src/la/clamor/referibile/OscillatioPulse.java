package la.clamor.referibile;

import java.io.File;
import la.clamor.Aestima;
import la.clamor.Constantia;
import la.clamor.Envelope;
import la.clamor.Functiones;
import la.clamor.Punctum;
import la.clamor.Res;
import la.clamor.io.IOUtil;
import la.clamor.io.ScriptorWav;
import org.apache.commons.math3.util.FastMath;

/**
 * Oscillation with duration specified, but frequency nor volume unspecified
 *
 * @author minae.hiyamae
 */
public class OscillatioPulse implements Referibilis, Constantia {

    Punctum x;
    Punctum last;
    Aestima ratio;
    Aestima threshold;
    boolean con_negatif;

    /**
     * setting for this oscillatio
     *
     * @param con_negatif
     */
    public OscillatioPulse(boolean con_negatif) {
        this.con_negatif = con_negatif;
        ratio = new Aestima(2d * FastMath.PI / Res.publica.sampleRateDouble());
        threshold = new Aestima(2d * FastMath.PI);
        x = new Punctum();
        last = new Punctum();
        //System.out.println("th:" + threshold);
    }

    @Override
    public Punctum lego(Punctum frequentia) {
        //f//requentia = (frequentia == null) ? new Punctum() : frequentia;
        //quantitas = (quantitas == null) ? new Punctum() : quantitas;
        Punctum punctum = new Punctum();
        for (int i = 0; i < Res.publica.channel(); i++) {
            Aestima omega_t = frequentia.capioAestima(i).multiplico(ratio);
            Aestima current_x = x.capioAestima(i).addo(omega_t);
            //System.out.println(omega_t + ":" + current_x + ":" + current_x.resto(threshold));
            if (current_x.compareTo(threshold) >= 0) {
                punctum.ponoAestimatio(i, new Aestima(1));
            } else if (con_negatif && last.capioAestima(i).equals(new Aestima(1))) {
                punctum.ponoAestimatio(i, new Aestima(-1));
                //punctum.ponoAestimatio(i, new Aestimatio(0));
            }
            x.ponoAestimatio(i, current_x.resto(threshold));
        }
        last = punctum;
        return punctum;
    }

    public static void main(String[] args) {

        File out_file = new File(IOUtil.getDirectory("opus"), "pulse_train1.wav");
        ScriptorWav sw = new ScriptorWav(out_file);
        sw.scribo(new Referibile(new OscillatioPulse(false),
            new Envelope<>(new Punctum(100)),
            3000), false);

        Functiones.ludoLimam(out_file);
    }

    @Override
    public Referibilis duplicate() {
        return new OscillatioPulse(con_negatif);
    }
}
