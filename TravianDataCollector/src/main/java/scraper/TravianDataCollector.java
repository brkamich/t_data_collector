/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import elasticsearch.ElasticManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Model;

/**
 *
 * @author Michal
 */
public class TravianDataCollector {

    private static Model model;   
    private static ElasticManager els;
    public static void main(String[] args) {
        model = new Model();
        els = new ElasticManager();
        BotScraper bot = new BotScraper("ts4.travian.com.au","tttt","heslo1234",model);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        long timeBefore,timeAfter,sleepTime;
        while(true){
        try {
            timeBefore = System.currentTimeMillis();
            bot.scrape();
            els.sendDataToElastic(model);
            //model.print();
            timeAfter = System.currentTimeMillis();
            sleepTime = (1000*60*60) - (timeAfter - timeBefore);
            if(sleepTime>0){
                System.out.println("sleeping (sec): "+sleepTime/1000);
                Thread.sleep(sleepTime);
            }
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(TravianDataCollector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex:"+ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(TravianDataCollector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex:"+ex.toString());
        } catch (InterruptedException ex) {
            Logger.getLogger(TravianDataCollector.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ex:"+ex.toString());
        }
        }
    }
    
}
