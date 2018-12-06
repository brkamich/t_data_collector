/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 *
 * @author Michal
 */
public class Village extends GameObject{
    private int coordX;
    private int coordY;
    private Player owner;
    private int population;
    private String villageName;

    public Village(){
        super();
    }
    
    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
    
    public void print(){
        System.out.println("Village: "+villageName+", pop: "+population+", coordinates: "+coordX+"|"+coordY+", owner: "+owner.getPlayerName());
    }
    
    @Override
    public IndexRequest getIndexRequest(String timeID) throws IOException{
        IndexRequest request = new IndexRequest("villages","_doc",this.getCoordX()+"|"+this.getCoordY()+"."+timeID);
        XContentBuilder object = XContentFactory.jsonBuilder().startObject();
        {
            object.timeField("timestamp", timeID);
            object.field("villageName", this.getVillageName());
            object.field("villageOwner", this.getOwner().getPlayerName());
            object.field("coordX", this.getCoordX());
            object.field("coordY", this.getCoordY());
            object.field("villagePop", this.getPopulation());
        }
        object.endObject();
        request.source(object);
        return request;
    }
    
}
