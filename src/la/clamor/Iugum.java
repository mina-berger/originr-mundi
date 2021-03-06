/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package la.clamor;

/**
 *
 * @author minae.hiyamae
 */
public class Iugum {

    Integer index;
    //Integer channel;
    Punctum altum;
    Punctum humile;
    double tempus;

    public Iugum(double tempus, Punctum altum) {
        //this(tempus, null, null, altum, altum);
        this(tempus, null, altum, null);
    }

    public Iugum(double tempus, Punctum altum, Punctum humile) {
        //this(tempus, null, null, altum, humile);
        this(tempus, null, altum, humile);
    }

    public Iugum(double tempus, Integer index, /*Integer channel, */ Punctum altum, Punctum humile) {
        if (altum == null) {
            throw new IllegalArgumentException("altum should not be null");
        }
        this.index = index;
        //this.channel = channel;
        this.altum = altum;
        this.humile = humile == null ? altum : humile;
        this.tempus = tempus;
    }

    public void print() {
        System.out.println(String.format("tempus=%s, index=%s, altum=%s, humile=%s", tempus, index == null ? "null" : index.toString(), altum, humile));
    }

    public Punctum capioPunctum(Vel velocitas) {
        //return capioPunctumStaticus(altum, humile, velocitas);
        //hv + l(1 - v)
        Punctum punctum = new Punctum();
        Punctum p_velocitas = velocitas.capio(index);
        for (int i = 0; i < Res.publica.channel(); i++) {
            Aestima a_velocitas = p_velocitas.capioAestima(i);
            punctum.ponoAestimatio(i,
                altum.capioAestima(i).multiplico(a_velocitas).addo(
                humile.capioAestima(i).multiplico(new Aestima(1).subtraho(a_velocitas))));
        }
        return punctum;
    }

    public int capioIndicem() {
        return index;
    }

    //public Integer capioChannel(){
    //    return channel;
    //}
    public double capioTempus() {
        return tempus;
    }

    public long capioPositio() {
        return Functiones.adPositio(tempus);
    }
}
