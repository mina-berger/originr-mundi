/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package origine_mundi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import static origine_mundi.OmUtil.*;
import origine_mundi.OmUtil.Note;
import static origine_mundi.OmUtil.getMidiDevice;

/**
 *
 * @author Mina
 */
public class MidiInTester {
    public static class MidiReceiver implements Receiver {
        Receiver receiver;
        MidiReceiver(Receiver receiver) {
            this.receiver = receiver;
        }
        @Override
        public void send(MidiMessage message, long timeStamp) {
            if(message.getMessage()[0] == -2){
                return;
            }
            if(message instanceof ShortMessage){
                ShortMessage sm = (ShortMessage)message;
                String command = null;
                switch(sm.getCommand()){
                    case 0x90:
                        if(sm.getData2() == 0){
                        }else{
                            command = "note[" + sm.getChannel() + "] " + sm.getData1() + ":" + new Note(sm.getData1()) + " " + OmUtil.hex(sm.getData2());
                        }
                        break;
                    case 0xc0:
                        command = "pc[" + sm.getChannel() + "] " + sm.getData1() + ":" + sm.getData2();
                        break;
                    default :command = OmUtil.hex(sm.getCommand());
                }
                if(command != null){
                    System.out.println(command);
                }
                receiver.send(message, timeStamp);
            }else{
                String str = "";
                for(byte b:message.getMessage()){
                    int i = b < 0?((int)b) + 256:((int)b);
                    if(!str.isEmpty()){
                        str += " ";
                    }
                    str += OmUtil.fill(Integer.toHexString(i), 2);
                    //str += b;
                }
                System.out.println(str);
            }
        }

        @Override
        public void close() {
        }
    }
    public static void _main(String[] a) {
        OmUtil.printEnv(System.out);
    }
    public static void main(String[] a) {
        OmUtil.printEnv(System.out);
        MidiDevice ex_dev1 = null;
        MidiDevice in_dev1 = null;
        MidiDevice ex_dev2 = null;
        MidiDevice in_dev2 = null;
        MidiDevice ex_dev3 = null;
        MidiDevice in_dev3 = null;
        MidiDevice ex_dev4 = null;
        MidiDevice in_dev4 = null;
        try {
            
            //ex_dev = getMidiDevice("2- micro lite: Port 4", true);
            //ex_dev = getMidiDevice("2- micro lite: Port 2", true);
            
            //
            //ex_dev = getMidiDevice(MICRO_LITE_2, true); //U110
            //ex_dev = getMidiDevice(MICRO_LITE_3, true); //D110
            //ex_dev = getMidiDevice(MICRO_LITE_6, true); //01R/W
            //ex_dev = getMidiDevice(MICRO_LITE_7, true); //M3R
            //ex_dev = getMidiDevice(MU500[0], //true);//MU500
            ex_dev1 = getMidiDevice(MICRO_LITE_1, true); //TG77
            ex_dev2 = getMidiDevice(MICRO_LITE_2, true); //TG77
            ex_dev3 = getMidiDevice(MICRO_LITE_3, true); //TG77
            ex_dev4 = getMidiDevice(MICRO_LITE_4, true); //TG77
            
            //ex_dev = getMidiDevice(MU500[0], true);
            //ex_dev = getMidiDevice(MICRO_LITE_2, true);
            //in_dev = getMidiDevice("2- PC-50 MIDI OUT", false);
            in_dev1 = getMidiDevice(MICRO_LITE_5, false);
            in_dev2 = getMidiDevice(MICRO_LITE_5, false);
            in_dev3 = getMidiDevice(MICRO_LITE_5, false);
            in_dev4 = getMidiDevice(MICRO_LITE_5, false);
            //in_dev = getMidiDevice(US_122, false);
            MidiReceiver sr1 = new MidiReceiver(ex_dev1.getReceiver());
            in_dev1.getTransmitter().setReceiver(sr1);
            MidiReceiver sr2 = new MidiReceiver(ex_dev2.getReceiver());
            in_dev2.getTransmitter().setReceiver(sr2);
            MidiReceiver sr3 = new MidiReceiver(ex_dev3.getReceiver());
            in_dev3.getTransmitter().setReceiver(sr3);
            MidiReceiver sr4 = new MidiReceiver(ex_dev4.getReceiver());
            in_dev4.getTransmitter().setReceiver(sr4);
            while(true){
                Thread.sleep(100000);
            }
            /*byte[] data = sysex_ret.getData();
            ArrayList<Integer> ret = new ArrayList<>();
            ret.add(0xf0);
            for(int i = 0;i < data.length;i++){
                ret.add(data[i] < 0?data[i] + 0x100:data[i]);
            }
            return ret;*/
        } catch (Exception ex) {
            ex.printStackTrace();
            ex_dev1.close();
            in_dev1.close();
        }
    }
    
    
}
