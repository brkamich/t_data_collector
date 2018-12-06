/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 *
 * @author Michal
 */
public class Player extends GameObject{
    private String playerName;
    private int popPoints;
    private int villageCount;
    private int offPoints;
    private int defPoints;
    private int raidPoints;
    private Alliance alliance;
    private boolean isInAlliance;
    private HashMap<String,Village> villages;
    private int heroPoints;
    
    public Player(){
        super();
        villages = new HashMap<String,Village>();
        alliance = null;
        isInAlliance = false;
        popPoints = 0;
    }

    public int getHeroPoints() {
        return heroPoints;
    }

    public void setHeroPoints(int heroPoints) {
        this.heroPoints = heroPoints;
    }

    public HashMap<String,Village> getVillages() {
        return villages;
    }
   
    public int getVillageCount() {
        return villageCount;
    }

    public void setVillageCount(int villageCount) {
        this.villageCount = villageCount;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPopPoints() {
        return popPoints;
    }

    public void setPopPoints(int popPoints) {
        this.popPoints = popPoints;
    }


    public int getOffPoints() {
        return offPoints;
    }

    public void setOffPoints(int offPoints) {
        this.offPoints = offPoints;
    }

    public int getDefPoints() {
        return defPoints;
    }

    public void setDefPoints(int defPoints) {
        this.defPoints = defPoints;
    }

    public int getRaidPoints() {
        return raidPoints;
    }

    public void setRaidPoints(int raidPoints) {
        this.raidPoints = raidPoints;
    }

    public String getAllianceName() {
        if(alliance!=null){
            return alliance.getName();
        }
        return "";
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public boolean isIsInAlliance() {
        return isInAlliance;
    }

    public void setIsInAlliance(boolean isInAlliance) {
        this.isInAlliance = isInAlliance;
    }

    public void print(){
        System.out.println("Player: "+playerName+",  villageCount: "+villages.size());
    }
    
    @Override
    public IndexRequest getIndexRequest(String timeID) throws IOException{
        IndexRequest request = new IndexRequest("players","_doc",this.getPlayerName()+"."+timeID);
        XContentBuilder object = XContentFactory.jsonBuilder().startObject();
        {
            object.timeField("timestamp", timeID);
            object.field("accountName", this.getPlayerName());
            object.field("isInAlly", this.isIsInAlliance());
            object.field("allianceName", this.getAllianceName());
            object.field("accountPop", this.getPopPoints());
            object.field("heroExp", this.getHeroPoints());
            object.field("offPoints", this.getOffPoints());
            object.field("defPoints", this.getDefPoints());
            object.field("villCount", this.getVillageCount());
            object.startArray("villages");
            for(Village v : this.getVillages().values()){
                object.startObject();
                object.field("villageName", v.getVillageName());
                object.field("coordX", v.getCoordX());
                object.field("coordY", v.getCoordY());
                object.field("villagePop", v.getPopulation());
                object.endObject();
            }
            object.endArray();
        }
        object.endObject();
        request.source(object);
        return request;
    }
    
    
}
