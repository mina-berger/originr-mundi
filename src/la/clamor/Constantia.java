package la.clamor;

import javax.sound.sampled.AudioFormat;

public interface Constantia {

    public enum Rebus {
        FREQ, QUANT, PAN, VCO_FREQ, VCO_QUANT, VCA_FREQ, VCA_QUANT, FB_QUANT
    };
    //public static int CHANNEL = 2;

    //public static final int REGULA_EXAMPLI = 48000;
    //public static final double REGULA_EXAMPLI_D = REGULA_EXAMPLI;
    public static final Aestima REGULA_MAGISTRI = new Aestima(1.0);
    /**
     * 1=8bit, 2=16bit, 3=24bit
     */
    //public static final int BYTE_PER_EXAMPLUM = 2;
    //public static final Aestima MAX_AMPLITUDO = new Aestima(Math.pow(2, BYTE_PER_EXAMPLUM * 8 - 1) - 1);
    //public static final Aestima MIN_AMPLITUDO = new Aestima(Math.pow(2, BYTE_PER_EXAMPLUM * 8 - 1) * -1);
    /**
     * locus terminato (ms)
     */
    public static final int LOCUS_TERMINATO = 1000;
    //public static final int LOCUS_TERMINATO = 200;

    public enum Fons {
        IN, EX
    }

    public enum Partes {
        PRIMO, VCO, VCF, VCA, VCP, FB
    };

    public enum Parma {
        FCO, FCA, QCO, QCA, PCO, PCA
    };

    public enum Unda {
        SINE, QUAD, TRIA, DENT, FRAG
    }

    public enum Effector {
        DIST, CHOR, MORA, COMP
    };

    public static AudioFormat getAudioFormat() {
        return getAudioFormat((float) Res.publica.sampleRate(), Res.publica.sampleSize(), Res.publica.channel());
    }

    public static AudioFormat getAudioFormat(float sample_rate, int sample_size_byte, int channels) {
        boolean signed = true;
        boolean big_endian = false;
        AudioFormat format = new AudioFormat(sample_rate, sample_size_byte * 8,
                channels, signed, big_endian);
        return format;
    }

}
