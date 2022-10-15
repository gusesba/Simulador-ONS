/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ons.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
            
            //Abre os arquivos csv
            FileWriter csvWriter = new FileWriter("simulations/simulation100.csv");
            FileWriter csvWriter2 = new FileWriter("simulations/teste2.csv");
            FileWriter csvWriter3 = new FileWriter("simulations/teste3.csv");
            
            //Cabeçalho dos arquivos
            csvWriter.append("Slots Demandados, Tempo Transmitido, Tempo Faltante, Classe, Degradacao Tolerada(%), Degradaco Tolerada, Degradacao Realizada %, Degradacao Realizada, Atraso (%), Atraso, Custo, Carga na Rede, Status, Tamanho(Path 1), Tamanho(Path 2), Tamanho(Path 3), Path 1, Path 2, Path 3, NumWaveInUse, NumWave, Trafego caminho 1(%),Trafego caminho 1, Trafego caminho 2(%),Trafego caminho 2, Trafego caminho 3(%),Trafego caminho 3\n");
            csvWriter2.append("getSource,getDestination,getNumWavesInUse,getNumWave,State,%1,%2,%3\n");
            csvWriter3.append("Transmitted Bw, Bw Req, Arrival time, Departure Time, Hold, Dep - Ar\n");
            
            for (Flow f : flows) {
                
               csvWriter3.append(f.getTransmittedBw()+",");
               csvWriter3.append(f.getBwReq()+",");
               csvWriter3.append(f.getArrivalEvent().getTime()+",");
               csvWriter3.append(f.getDepartureEvent().getTime()+",");
               csvWriter3.append(f.getHoldTime()+",");
               csvWriter3.append(f.getDepartureEvent().getTime()-f.getArrivalEvent().getTime()+",");
               csvWriter3.append("\n");
                
                ArrayList<Integer>[] paths = f.getPaths();
                r = 0;
                csvWriter.append(f.getRequiredSlotsRestauration() + ","); //slots demandados
                csvWriter.append(f.getMissingTime()*f.getTransmittedBw()/(f.getBwReq()-f.getTransmittedBw())+","); //Tempo Transmitido
                csvWriter.append(f.getMissingTime()+",");//Tempo Faltante
                csvWriter.append(f.getCOS()+","); //Classe
                csvWriter.append(f.getServiceInfo().getDegradationTolerance()+",");//Degradação Tolerada%
                csvWriter.append(f.getBwPerWave()*f.getNumWave()*f.getServiceInfo().getDegradationTolerance() + ","); // Degradação Tolerada
                csvWriter.append(1-f.calcDegradation()+","); //Degradação Realizada %
                csvWriter.append(f.getBwPerWave()*(f.getNumWave()-f.getNumWavesInUse())+",");//Degradação Realizada
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
                
                
                r = 0;
                if(f.isDropped()) r += 4;
                if(f.isIsDelayed()) r += 2;
                if(f.getServiceInfo().getDegradationTolerance() > 0) r += 1;
                //if(f.calcDegradation()!=1 && f.calcDegradation()!=0) r +=1;
                
                
                
                csvWriter.append(r+","); // Status
                
               for(ArrayList<Integer> path : paths){
                    csvWriter.append(path.size()+",");
                }
               
               for(ArrayList<Integer> path : paths){
                    csvWriter.append("[");
                    for(int i = 0;i<path.size()-1;i++){
                        csvWriter.append(path.get(i)+"-");
                    }
                    csvWriter.append(path.get(path.size()-1)+"],");
                    
                }
               
               csvWriter.append(f.getNumWavesInUse()+",");
               csvWriter.append(f.getNumWave()+",");
               
               if(f.getPercentage()!=null){
                    float totalValue = 0;
                    for(Map.Entry<int[],Integer>entry:f.getPercentage().entrySet()){
                        totalValue+= entry.getValue();
                    }

                    for(Map.Entry<int[],Integer>entry:f.getPercentage().entrySet()){

                        csvWriter.append(entry.getValue()/totalValue + ","); // Tráfego por caminho %
                        csvWriter.append(entry.getValue() + ","); // Tráfego por caminho
                    }
                }
               
                //CsvWritter 2
                   
                csvWriter2.append(f.getSource()+",") ;
                csvWriter2.append(f.getDestination()+",");
                csvWriter2.append(f.getNumWavesInUse() + ",");
                csvWriter2.append(f.getNumWave() + ",");   
                csvWriter2.append(r+",");
               // for(ArrayList<Integer> path : paths){
                 //   csvWriter2.append(path+",");
                    
                //}
                
                
                
                
                
               
                
                csvWriter.append("\n");
                csvWriter2.append("\n");
               
    
            }
            csvWriter.flush();
            csvWriter.close();
            csvWriter2.flush();
            csvWriter2.close();
            csvWriter3.flush();
            csvWriter3.close();
            
        } catch (IOException ex) {
            Logger.getLogger(EON_QFDDM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
