package la.clamor.io;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import la.clamor.Aestima;
import la.clamor.Constantia;
import la.clamor.Functiones;
import la.clamor.Legibilis;
import la.clamor.Punctum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import la.clamor.Res;
import static la.clamor.Constantia.getAudioFormat;

public class ScriptorWav implements Constantia {

  public static Log log = LogFactory.getLog(ScriptorWav.class);
  private final File file;
  private Long index_ab;
  private Long index_ad;
  private AudioFormat format;

  public ScriptorWav(File file) {
    this(file, getAudioFormat());
  }

  public ScriptorWav(File file, AudioFormat format) {
    this.file = file;
    index_ab = null;
    index_ad = null;
    this.format = format;
  }

  public void ponoIndexAb(double tempus_ab) {
    System.out.println("tempus_ab=" + tempus_ab);
    this.index_ab = Functiones.adPositio(tempus_ab);
  }

  public void ponoIndexAd(double tempus_ad) {
    System.out.println("tempus_ad=" + tempus_ad);
    this.index_ad = Functiones.adPositio(tempus_ad);
  }

  public File getFile() {
    return file;
  }

  public void scribo(Legibilis legibilis, boolean pono_locus) {
    scribo(legibilis, pono_locus, REGULA_MAGISTRI);
  }

  public void scribo(Legibilis legibilis, boolean pono_locus, Aestima master_volume) {
    ObjectOutputStream o_out;
    //FileOutputStream   f_out;
    ObjectInputStream o_in;
    Aestima ratio;
    int longitudo = 0;
    File tmp_file;

    int channel = format.getChannels();
    int sample_rate = (int) format.getSampleRate();
    try {
      tmp_file = IOUtil.createTempFile("s_lima");
      //tmp_file.deleteOnExit();
      o_out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tmp_file)));

      long locus = pono_locus ? Functiones.adPositio(LOCUS_TERMINATO) : 0;
      Aestima max = new Aestima();
      Aestima min = new Aestima();
      log.info("mixdown start:" + file.getAbsolutePath());
      for (long i = 0; i < locus * channel; i++) {
        o_out.writeObject(new Aestima(0));
        longitudo++;
        if (longitudo % (sample_rate * channel * 5) == 0) {
          log.info("lecti   : " + (longitudo / sample_rate / channel) + " sec.(locus)");
        }
      }
      long index = 0;
      //log.info("ante lego:index_ab=" + index_ab + ":index_ad=" + index_ad);
      while (legibilis.paratusSum()) {
        Punctum punctum = legibilis.lego();
        if ((index_ab != null && index_ab > index)) {
          index++;
          continue;
        } else if (index_ad != null && index_ad < index) {
          break;
        }
        index++;
        for (int i = 0; i < channel; i++) {
          Aestima aestimatio = punctum.capioAestima(i);
          o_out.writeObject(aestimatio);
          max = max.max(aestimatio);
          min = min.min(aestimatio);
          longitudo++;
          if (longitudo % (sample_rate * channel * 5) == 0) {
            log.info("lecti   : " + (longitudo / sample_rate / channel) + " sec.");
            //log.info("index_ab=" + index_ab + ":index_ad=" + index_ad + ":index" + index);
          }
        }
        if (index % 1000 == 0) {
          o_out.reset();
        }

      }
      legibilis.close();
      for (long i = 0; i < locus * channel; i++) {
        o_out.writeObject(new Aestima(0));
        longitudo++;
        if (longitudo % (sample_rate * channel) == 0) {
          log.info("lecti   : " + (longitudo / sample_rate / channel) + " sec.(locus)");
        }
      }
      o_out.flush();
      o_out.close();
      log.info("longitudo=" + longitudo);
      log.info("maximum  =" + max);
      log.info("minimum  =" + min);
      ratio = master_volume.multiplico(Res.publica.maxAmplitudo()).partior(max).abs().min(
        master_volume.multiplico(Res.publica.minAmplitudo()).partior(min).abs());
      longitudo += locus * 2;
      log.info("ratio   =" + ratio);
      log.info("tmp_file=" + tmp_file.getAbsolutePath());
      o_in = new ObjectInputStream(new FileInputStream(tmp_file));
    } catch (IOException ex) {
      Logger.getLogger(ScriptorWav.class.getName()).log(Level.SEVERE, null, ex);
      throw new IllegalArgumentException(ex);
    }

    try {
      scriboSub(o_in, file, ratio, longitudo);
    } catch (IOException ex) {
      Logger.getLogger(ScriptorWav.class.getName()).log(Level.WARNING, file.getAbsolutePath(), ex);
      try {
        File new_file = new File(file.getParentFile(), System.currentTimeMillis() + "_" + file.getName());
        Logger.getLogger(ScriptorWav.class.getName()).log(Level.WARNING, "scribo {0}", new_file.getAbsolutePath());
        scriboSub(o_in, new_file, ratio, longitudo);
      } catch (IOException ex2) {
        Logger.getLogger(ScriptorWav.class.getName()).log(Level.SEVERE, null, ex2);
        throw new IllegalArgumentException(ex2);
      }
    } finally {
      tmp_file.delete();
    }
  }

  private void scriboSub(ObjectInputStream o_in, File file, Aestima ratio, int longitudo) throws IOException {
    AudioFormat af = new AudioFormat(
      AudioFormat.Encoding.PCM_SIGNED,
      format.getSampleRate(),
      format.getSampleSizeInBits(),
      format.getChannels(),
      format.getFrameSize(),
      format.getFrameRate(),
      true);
    /*AudioFormat af = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, 
                (float)REGULA_EXAMPLI, 
                8 * BYTE_PER_EXAMPLUM, 
                Res.publica.channel(),
                Res.publica.channel() * BYTE_PER_EXAMPLUM, 
                (float)REGULA_EXAMPLI, 
                true);*/
    file.getParentFile().mkdirs();
    AudioSystem.write(
      new AudioInputStream(new LegibilisInputStream(o_in, ratio), af, (longitudo) / format.getChannels()),
      AudioFileFormat.Type.WAVE, file);
  }

  /*private void scriboSub(ObjectInputStream o_in, File file, Aestimatio ratio, int longitudo, int locus) throws FileNotFoundException, IOException{
        try (FileOutputStream f_out = new FileOutputStream(file)) {
            f_out.write(new FilumOctorum(Lima.RIFF).capioBytes());
            f_out.write(new FilumOctorum(longitudo + 8, 4).capioBytes());
            f_out.write(new FilumOctorum(Lima.WAVE).capioBytes());

            //int channel = 1;
            FilumOctorum octets = new FilumOctorum();
            octets.addo(new FilumOctorum(1, 2));// formatID PCM
            octets.addo(new FilumOctorum(Res.publica.channel(), 2));// channel
            octets.addo(new FilumOctorum(REGULA_EXAMPLI, 4));// sampling rate
            octets.addo(new FilumOctorum(REGULA_EXAMPLI * Res.publica.channel() * BYTE_PER_EXAMPLUM, 4));// byte
            // per
            // second
            octets.addo(new FilumOctorum(Res.publica.channel() * BYTE_PER_EXAMPLUM, 2));// block size
            octets.addo(new FilumOctorum(8 * BYTE_PER_EXAMPLUM, 2));// bit
            RiffData data = new RiffData("fmt ", octets);
            f_out.write(new FilumOctorum(data.getTag()).capioBytes());
            f_out.write(new FilumOctorum(data.longitudoDatorum(), 4).capioBytes());
            FilumOctorum data_data = data.capioData();
            f_out.write(data_data.capioBytes());

            f_out.write(new FilumOctorum(Lima.DATA_TAG).capioBytes());
            f_out.write(new FilumOctorum(longitudo * BYTE_PER_EXAMPLUM, 4).capioBytes());

            for (int i = 0; i < locus; i++) {
                f_out.write(new FilumOctorum(convertoBytes(0, BYTE_PER_EXAMPLUM)).capioBytes());
            }
            int written = 0;
            //FileWriter data_csv = new FileWriter(new File(file.getParentFile(), file.getName() + ".csv"));
            while (true) {
                try{
                    if(written % (REGULA_EXAMPLI * Res.publica.channel())  == 0){
                        log.info("written : " + (written / REGULA_EXAMPLI / Res.publica.channel()) + " sec.");
                    }
                    Aestimatio value = (Aestimatio)o_in.readObject();
                    f_out.write(new FilumOctorum(convertoBytes(value.multiplico(ratio).intValue(), BYTE_PER_EXAMPLUM)).capioBytes());
                    //Aestimatio value = new Aestimatio(o_in.readDouble(), true);
                    //f_out.write(new FilumOctorum(convertoBytes(value.multiplico(ratio).intValue(), BYTE_PER_EXAMPLUM)).capioBytes());

                    //if(written % Res.publica.channel() == 0){
                    //    data_csv.append(Double.toString(value) + "\n");
                    //}
                    written++;
                }catch(EOFException | ClassNotFoundException ex) {
                    //Logger.getLogger(ScriptorLimam.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
            log.info("written=" + written);

            //data_csv.flush();
            //data_csv.close();
            o_in.close();
            f_out.flush();
        }
    }*/

 /*FilumOctorum logo(InputStream in, int length) {
        byte[] bytes = new byte[length];
        try {
            if (in.read(bytes) != length) {
                return null;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return new FilumOctorum(bytes);
    }*/

 /*private static byte[] convertoBytes(int aestimatio, int longitudo) {
        if (longitudo < 1 && longitudo > 3) {
            throw new IllegalArgumentException("so far length(" + longitudo + ") must be 1, 2, or 3.");
        }
        boolean positive = aestimatio >= 0;

        byte[] bytes = new byte[longitudo];
        if (longitudo == 1) {
            int byte_value_1 = (aestimatio + 128) % 256;
            bytes[0] = new Integer(byte_value_1).byteValue();
        } else if (longitudo == 2) {
            int byte_value_1 = aestimatio % 256;
            int byte_value_2 = (aestimatio / 256) % 256;
            if (byte_value_2 <= 0 && !positive) {
                byte_value_2 -= 1;
            }
            bytes[0] = new Integer(byte_value_1).byteValue();
            bytes[1] = new Integer(byte_value_2).byteValue();
        } else {
            int byte_value_1 = aestimatio % 256;
            int byte_value_2 = (aestimatio / 256) % 256;
            int byte_value_3 = (aestimatio / 256 / 256) % 256;
            if (byte_value_3 <= 0 && byte_value_2 < 0 && !positive) {
                byte_value_3 -= 1;
            }
            bytes[0] = new Integer(byte_value_1).byteValue();
            bytes[1] = new Integer(byte_value_2).byteValue();
            bytes[2] = new Integer(byte_value_3).byteValue();
        }
        return bytes;
    }*/
  public static void main(String[] args) {
    LectorLimam ll = new LectorLimam(new File(IOUtil.getDirectory("sample"), "ba_1_h_40.lima"));
    ScriptorWav sl = new ScriptorWav(new File(IOUtil.getDirectory("sample"), "sample011.wav"));
    sl.scribo(ll, false);
  }

  public static class LegibilisInputStream extends InputStream {

    ObjectInputStream o_in;
    ArrayList<Byte> list;
    Aestima ratio;
    Integer read;
    long count = 0;

    public LegibilisInputStream(ObjectInputStream o_in, Aestima ratio) {
      this.o_in = o_in;
      this.ratio = ratio;
      list = new ArrayList<>();
      read = null;
    }

    @Override
    public int read() throws IOException {
      if (read == null) {
        read = lego();
      }
      //System.out.println(count + ";" + read);
      //count++;
      int ret = read;
      read = null;
      return ret;
    }

    public int lego() throws IOException {
      if (list.isEmpty()) {
        try {
          Aestima value = (Aestima) o_in.readObject();

          ByteBuffer buffer = ByteBuffer.allocate(8);
          buffer.putLong(value.multiplico(ratio).longValue());
          byte[] array = buffer.array();
          for (int i = 8 - Res.publica.sampleSize(); i < 8; i++) {
            list.add(array[i]);
          }

          /*ByteBuffer buffer = ByteBuffer.allocate(2 * BYTE_PER_EXAMPLUM);
                    buffer.putInt(value.multiplico(ratio).intValue());
                    for (int i = BYTE_PER_EXAMPLUM;i < buffer.array().length;i++) {
                        list.add(buffer.array()[i]);
                    }*/
          count++;
          if (count % (Res.publica.sampleRate() * Res.publica.channel() * 5) == 0) {
            log.info("scripti : " + (count / Res.publica.sampleRate() / Res.publica.channel()) + " sec.");
          }
        } catch (EOFException ex) {
          return -1;
        } catch (IOException | ClassNotFoundException | RuntimeException ex) {
          Logger.getLogger(ScriptorWav.class.getName()).log(Level.SEVERE, "count=" + count, ex);
          throw new IOException(ex);
        }
      }
      int ret = list.remove(0).intValue();
      if (ret < 0) {
        ret += 256;
      }
      return ret;

    }

    @Override
    public int available() throws IOException {
      if (read == null) {
        read = lego();
      }
      if (read == -1) {
        return 0;
      }
      return 1;
    }

    @Override
    public void close() throws IOException {
      o_in.close();
    }
  }
  /*public static byte[] toBytes(long value, int byte){
        String str = Long.toBinaryString(value);
        while()
    }*/
 /*public static String toString(byte[] bytes){
        String str = "";
        for(byte _byte:bytes){
            str += str.isEmpty()?"":",";
            str += Byte.toString(_byte);
        }
        return str;
    }*/
}
