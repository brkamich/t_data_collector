/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;
import java.util.HashMap;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 *
 * @author Michal
 */
public class Alliance extends GameObject{
    private HashMap<String,Player> players;
    private String name;
    private int allyOffPoints,allyDefPoints,allyPopPoints,allyVillageCount;
    public Alliance(){
        super();
        players = new HashMap<String,Player>();
        allyOffPoints = 0;
        allyDefPoints = 0;
        allyPopPoints = 0;
        allyVillageCount = 0;
    }

    public HashMap<String,Player> getPlayers() {
        return players;
    }

    public int getAllyVillageCount() {
        return allyVillageCount;
    }

    public void setAllyVillageCount(int allyVillageCount) {
        this.allyVillageCount = allyVillageCount;
    }
    

    public int getAllyOffPoints() {
        return allyOffPoints;
    }

    public void setAllyOffPoints(int allyOffPoints) {
        this.allyOffPoints = allyOffPoints;
    }

    public int getAllyDefPoints() {
        return allyDefPoints;
    }

    public void setAllyDefPoints(int allyDefPoints) {
        this.allyDefPoints = allyDefPoints;
    }

    public int getAllyPopPoints() {
        return allyPopPoints;
    }

    public void setAllyPopPoints(int allyPopPoints) {
        this.allyPopPoints = allyPopPoints;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void print(){
        System.out.println("Alliance: "+name+", players: "+players.size());
    }
    
    @Override
    public IndexRequest getIndexRequest(String timeID) throws IOException{
        IndexRequest request = new IndexRequest("alliances","_doc",this.getName()+"."+timeID);
        XContentBuilder object = XContentFactory.jsonBuilder().startObject();
        {
            object.timeField("timestamp", timeID);
            object.field("allianceName", this.getName());
            object.field("playersCount", this.getPlayers().size());
            object.field("alliancePop", this.getAllyPopPoints());
            object.field("allianceDef", this.getAllyDefPoints());
            object.field("allianceOff", this.getAllyOffPoints());
            object.field("allianceVillCount", this.getAllyVillageCount());
            object.startArray("players");
            for(Player p : this.getPlayers().values()){
                object.startObject();
                object.field("accountName", p.getPlayerName());
                object.field("accountPop", p.getPopPoints());
                object.endObject();
            }
            object.endArray();
        }
        object.endObject();
        request.source(object);
        return request;
    }
    
}
