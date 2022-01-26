package main.hr.java.covidportal.niti;

import main.java.sample.BazaPodataka;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * dohvaca sve bolesti spremljene u bazu podataka
 */
public class DohvacanjeSvihBolestiNit implements Runnable{
    private static Connection veza = null;

    @Override
    public void run(){
        try{
            otvoriVezuSBazom();
            BazaPodataka.dohvatiSveBolesti(veza);
            zatvoriVezuSBazom();
        } catch(SQLException | IOException | InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public synchronized void otvoriVezuSBazom() throws SQLException, IOException, InterruptedException{
        if(BazaPodataka.aktivnaVezaSBazomPodataka){
            wait();
        }
        else {
            veza = BazaPodataka.vezaSaBazom();
            BazaPodataka.aktivnaVezaSBazomPodataka = true;
        }
    }

    public synchronized void zatvoriVezuSBazom() throws SQLException {
        BazaPodataka.zatvoriVezuSaBazom(veza);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
