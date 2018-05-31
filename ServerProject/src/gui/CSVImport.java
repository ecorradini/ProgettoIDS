package gui;

import connessioni.Server;
import entita.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class CSVImport extends JFrame {

    public CSVImport(String tabella) {
        JFileChooser csvOpen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files (*csv)", "csv");
        csvOpen.setFileFilter(filter);
        int returnVal = csvOpen.showOpenDialog(this);
        if (returnVal == 0) {
            File file = csvOpen.getSelectedFile();
            String path = file.getAbsolutePath();
            try {
                BufferedReader br = new BufferedReader( new FileReader(path));
                String strLine = "";
                StringTokenizer st = null;

                while( (strLine = br.readLine()) != null) {
                    //break comma separated line using ","
                    st = new StringTokenizer(strLine, ";");
                    String[] dataToInsert = new String[st.countTokens()];
                    for(int i=0; i<dataToInsert.length; i++) {
                        dataToInsert[i] = st.nextToken();
                    }

                    try {
                        if (tabella.equals("AULA")) {
                            if(dataToInsert.length==5) {
                                DAOAula.insertAula(dataToInsert[0], dataToInsert[1], dataToInsert[2], dataToInsert[3], dataToInsert[4]);
                            }
                            else {
                                DAOAula.insertAula(dataToInsert[0], dataToInsert[1], dataToInsert[2], dataToInsert[3], null);
                            }
                        }
                        else if(tabella.equals("BEACON")) {
                            DAOBeacon.insertBeacon(dataToInsert[0], dataToInsert[1], dataToInsert[2], dataToInsert[3], dataToInsert[4]);
                        }
                        else if(tabella.equals("EDIFICIO")) {
                            DAOEdificio.insertEdificio(dataToInsert[0]);
                        }
                        else if(tabella.equals("PIANO")) {
                            DAOPiano.insertPiano(dataToInsert[0],dataToInsert[1]);
                        }
                        else if(tabella.equals("TRONCO")) {
                            DAOTronco.insertTronco(dataToInsert[0], dataToInsert[1], dataToInsert[2], dataToInsert[3], dataToInsert[4],dataToInsert[5],dataToInsert[6]);
                            DAOParametri.insertParametri(dataToInsert[0]);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
