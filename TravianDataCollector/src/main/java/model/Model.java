/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import data.Alliance;
import data.GameObject;
import data.Player;
import data.Village;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Michal
 */
public class Model {
    private DateTimeFormatter formatter;
    private OffsetDateTime timestamp;
    private String strTimestamp;
    private HashMap<String,Player> players;
    private HashMap<String,Village> villages;
    private HashMap<String,Alliance> alliances;
    private ArrayList<GameObject> objects;
    public Model(){
        formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'HH':'mmXXX");
        players =  new HashMap<String,Player>();
        villages = new HashMap<String,Village>();
        alliances = new HashMap<String,Alliance>();
        objects = new ArrayList<GameObject>();
        timestamp = OffsetDateTime.now();
        strTimestamp = timestamp.format(formatter);
        //System.out.println("timestamp:"+strTimestamp);
    }

    public HashMap<String,Player> getPlayers() {
        return players;
    }

    public HashMap<String,Village> getVillages() {
        return villages;
    }

    public HashMap<String,Alliance> getAlliances() {
        return alliances;
    }
    public void clearModel(){
        players.clear();
        villages.clear();
        alliances.clear();
    }
    public void print(){
        for(Player p : players.values()){
            p.print();
        }
        for(Village v : villages.values()){
            v.print();
        }
        for(Alliance a : alliances.values()){
            a.print();
        }
    }

    public void updateModelInfo() {
        for(Player p : players.values()){
            objects.add(p);
            for(Village v : p.getVillages().values()){
                objects.add(v);
                p.setPopPoints(p.getPopPoints()+v.getPopulation());
            }
            p.setVillageCount(p.getVillages().size());
        }
        for(Alliance a : alliances.values()){
            objects.add(a);
            for(Player p : a.getPlayers().values()){
                a.setAllyDefPoints(a.getAllyDefPoints() + p.getDefPoints());
                a.setAllyOffPoints(a.getAllyOffPoints() + p.getOffPoints());
                a.setAllyPopPoints(a.getAllyPopPoints() + p.getPopPoints());
                a.setAllyVillageCount(a.getAllyVillageCount() + p.getVillageCount());
            }
        }
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getStrTimestamp() {
        return strTimestamp;
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    
    
}
