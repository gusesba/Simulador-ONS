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
        System.out.println("******Flows " + flows.size() + "*******");
        int r;
        double cost = 0;
        try {
            FileWriter csvWriter = new FileWriter("simulations/teste.csv");
            FileWriter csvWriter2 = new FileWriter("simulations/teste2.csv");
            csvWriter.append("slots demandados, arrival time, tempo faltante, class, degradação%, degradação, atraso%, atraso, custo, carga,path size, path size, path size, status,\n");

            for (Flow f : flows) {
                ArrayList<Integer>[] paths = f.getPaths();
       
                r = 0;
                csvWriter.append(f.getRequiredSlotsRestauration() + ","); //slots demandados
                csvWriter.append(f.getTransmittedTime()+","); //Momento atual(disaster arrival) - arrival time
                csvWriter.append((((f.getTransmittedTime()/f.getTransmittedBw())*f.getBwReq())-f.getTransmittedTime())+",");//Duration - Trasmited
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
               
                r = 0;
                if(f.isDropped()) r += 4;
                if(f.isIsDelayed()) r += 2;
                if(f.IsDegraded()) r += 1;
                
                
                
                csvWriter.append(r+"\n");
                
                
                
                   csvWriter2.append(paths[0].get(0)+",");
                   csvWriter2.append(paths[0].get(paths[0].size()-1)+",");
                   
                   if(f.isDropped()){
                       csvWriter2.append("0,");
                   }else{
                       csvWriter2.append("1,");
                   }
                   
                   for(EONLightPath lps : usedLps){
                       csvWriter2.append(lps.getFirstSlot() + ",");
                       csvWriter2.append(lps.getLastSlot() + ",");
                   }
                   
                   csvWriter2.append(f.getRequiredSlots() + ",");
                  
                   
                
                csvWriter2.append("\n");
               
    
            }
            csvWriter.flush();
            csvWriter.close();
            csvWriter2.flush();
            csvWriter2.close();
        } catch (IOException ex) {
            Logger.getLogger(EON_QFDDM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
