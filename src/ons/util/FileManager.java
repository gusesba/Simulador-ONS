/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ons.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ons.EONLightPath;
import ons.Flow;
import ons.TrafficGenerator;
import ons.ra.EON_QFDDM;

/**
 *
 * @author gusta
 */
public class FileManager {
    
    public static void writeCSV(ArrayList<Flow> flows) {
        int r;
        double cost = 0;
        try {
            FileWriter csvWriter = new FileWriter("simulations/simulation100.csv");

            for (Flow f : flows) {
                ArrayList<Integer>[] paths = f.getPaths();
                
                
                
                r = 0;
                
                csvWriter.append(f.getRequiredSlotsRestauration() + ","); //slots demandados
                csvWriter.append(f.getTransmittedTime()+","); //Momento atual(disaster arrival) - arrival time
                csvWriter.append((f.getHoldTime()-f.getTransmittedTime())+",");//Duration - Trasmited
                csvWriter.append(f.getCOS()+","); //Classe
                csvWriter.append(f.getServiceInfo().getDegradationTolerance()+",");//Degradação %
                csvWriter.append(f.getRate()*f.getServiceInfo().getDegradationTolerance() + ","); // Degradação banda
                csvWriter.append(f.getServiceInfo().getDelayTolerance()+",");//Atraso %
                csvWriter.append(f.getHoldTime()*f.getServiceInfo().getDelayTolerance()+",");//Tempo de atraso
                switch(f.getCOS()) {
                    case 0: 
                        cost = 0.00000375;
                        break;
                    case 1:
                        cost = 0.000003;
                        break;
                    case 2:
                        cost = 0.0000015;
                        break;
                    case 3:
                        cost = 0;
                        break;
                }
                csvWriter.append(cost + ","); // Custo
                csvWriter.append(TrafficGenerator.getLoad()+","); // Carga na rede
      
                ArrayList<EONLightPath> usedLps = f.getUsedLps();
                int i = 0;
                
               for(ArrayList<Integer> path : paths){
                    csvWriter.append(path.size()+",");
                }
                if(f.isDropped()) r = 3;
                else if(f.isIsDelayed()) r = 2;
                else if(f.IsDegraded()) r = 1;
                csvWriter.append(r+"\n");
               
    
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(EON_QFDDM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
