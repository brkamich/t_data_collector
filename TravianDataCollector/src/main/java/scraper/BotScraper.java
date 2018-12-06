/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import data.Alliance;
import data.Player;
import data.Village;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Model;

/**
 *
 * @author Michal
 */
public class BotScraper {

    private String server;
    private String account;
    private String password;
    private WebClient webClient;
    private int maxPageNumber;
    private static Model model;

    public BotScraper(String server, String account, String password, Model model) {
        this.server = server;
        this.account = account;
        this.password = password;
        this.webClient = new WebClient();
        this.model = model;
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setTimeout(5000);
    }

    public void scrape() throws FailingHttpStatusCodeException, IOException, InterruptedException {
        model.clearModel();
        login();
        extractPlayerData();
        extractVillageData();
        model.updateModelInfo();
        //HtmlPage page2 = webClient.getPage("https://"+server+"/statistiken.php?id=2&page=317");
        //System.out.println("page2:"+page2.getElementById("villages").getElementsByTagName("tbody").get(0).getElementsByTagName("tr").get(0).getElementsByTagName("td").get(1).getTextContent());

    }

    private void login() throws IOException {
        HtmlPage page1 = webClient.getPage("https://" + server);
        HtmlForm form = page1.getFormByName("login");
        form.getInputByName("name").type(account);
        form.getInputByName("password").type(password);
        form.getButtonByName("s1").click();
    }

    private void extractPlayerData() throws IOException {
        HtmlPage page = webClient.getPage("https://" + server + "/statistiken.php?id=0&page=1");
        DomElement dE = (DomElement) page.getByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/div[1]/div[3]/div/a[2]").get(0);
        maxPageNumber = Integer.parseInt(dE.getTextContent());
        extractBasicInfo();
        extractOffInfo();
        extractDefInfo();
        extractRaidInfo();
        extractHeroInfo();
    }

    private void extractBasicInfo() throws IOException {
        HtmlPage page;
        Player player;
        String playerName;
        Alliance alliance;
        DomNodeList<HtmlElement> list;
        for (int i = 1; i <= maxPageNumber; i++) {
            page = webClient.getPage("https://" + server + "/statistiken.php?id=0&idSub=0&page=" + i);
            list = page.getElementById("player").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
            for (int p = 0; p < list.size(); p++) {
                playerName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
                if (playerName.length() > 2) {
                    if (!model.getPlayers().containsKey(playerName)) {
                        player = new Player();
                        player.setPlayerName(playerName);
                        if (list.get(p).getElementsByTagName("td").get(2).getElementsByTagName("a").size() > 0) {
                            alliance = new Alliance();
                            alliance.setName(list.get(p).getElementsByTagName("td").get(2).getElementsByTagName("a").get(0).getTextContent());
                            if (!model.getAlliances().containsKey(alliance.getName())) {
                                model.getAlliances().put(alliance.getName(), alliance);
                            }
                            player.setAlliance(alliance);
                            player.setIsInAlliance(true);
                        }
                        model.getPlayers().put(player.getPlayerName(), player);
                    }
                }
            }
        }
    }

    private void extractOffInfo() throws IOException {
        HtmlPage page;
        Player player;
        String playerName;
        int offPoints;
        DomNodeList<HtmlElement> list;
        for (int i = 1; i <= maxPageNumber; i++) {
            page = webClient.getPage("https://" + server + "/statistiken.php?id=0&idSub=1&page=" + i);
            list = page.getElementById("player").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
            for (int p = 0; p < list.size(); p++) {
                playerName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
                if (playerName.length() > 2) {
                    offPoints = Integer.parseInt(list.get(p).getElementsByTagName("td").get(4).getTextContent());
                    if (model.getPlayers().containsKey(playerName)) {
                        model.getPlayers().get(playerName).setOffPoints(offPoints);
                    } else {
                        player = new Player();
                        player.setPlayerName(playerName);
                        player.setOffPoints(offPoints);
                        model.getPlayers().put(playerName, player);
                    }
                }
            }
        }
    }

    private void extractDefInfo() throws IOException {
        HtmlPage page;
        Player player;
        String playerName;
        int defPoints;
        DomNodeList<HtmlElement> list;
        for (int i = 1; i <= maxPageNumber; i++) {
            page = webClient.getPage("https://" + server + "/statistiken.php?id=0&idSub=2&page=" + i);
            list = page.getElementById("player").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
            for (int p = 0; p < list.size(); p++) {
                playerName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
                if (playerName.length() > 2) {
                    defPoints = Integer.parseInt(list.get(p).getElementsByTagName("td").get(4).getTextContent());
                    if (model.getPlayers().containsKey(playerName)) {
                        model.getPlayers().get(playerName).setDefPoints(defPoints);
                    } else {
                        player = new Player();
                        player.setPlayerName(playerName);
                        player.setDefPoints(defPoints);
                        model.getPlayers().put(playerName, player);
                    }
                }
            }
        }
    }

    private void extractRaidInfo() throws IOException {
        HtmlPage page;
        Player player;
        String playerName;
        int raidPoints;
        DomNodeList<HtmlElement> list;
        page = webClient.getPage("https://" + server + "/statistiken.php?id=0&idSub=3");
        list = page.getElementById("top10_raiders").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
        for (int p = 0; p < 10; p++) {
            playerName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
            if (playerName.length() > 2) {
                raidPoints = Integer.parseInt(list.get(p).getElementsByTagName("td").get(2).getTextContent());
                if (model.getPlayers().containsKey(playerName)) {
                    model.getPlayers().get(playerName).setRaidPoints(raidPoints);
                } else {
                    player = new Player();
                    player.setPlayerName(playerName);
                    player.setRaidPoints(raidPoints);
                    model.getPlayers().put(playerName, player);
                }
            }
        }
    }

    private void extractHeroInfo() throws IOException {
        HtmlPage page;
        Player player;
        String playerName;
        int heroPoints;
        DomNodeList<HtmlElement> list;
        for (int i = 1; i <= maxPageNumber; i++) {
            page = webClient.getPage("https://" + server + "/statistiken.php?id=3&page=" + i);
            list = page.getElementById("heroes").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
            for (int p = 0; p < list.size(); p++) {
                playerName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
                if (playerName.length() > 2) {
                    heroPoints = Integer.parseInt(list.get(p).getElementsByTagName("td").get(3).getTextContent());
                    if (model.getPlayers().containsKey(playerName)) {
                        model.getPlayers().get(playerName).setHeroPoints(heroPoints);
                    } else {
                        player = new Player();
                        player.setPlayerName(playerName);
                        player.setHeroPoints(heroPoints);
                        model.getPlayers().put(playerName, player);
                    }
                }
            }
        }
    }

    private void extractVillageData() throws IOException {
        Player player;
        String playerName, villageName, href;
        Village village;
        int villagePoints, coordX, coordY;
        DomNodeList<HtmlElement> list;
        HtmlPage page = webClient.getPage("https://" + server + "/statistiken.php?id=2&page=1");
        DomElement dE = (DomElement) page.getByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/div[1]/div[2]/div/a[2]").get(0);
        maxPageNumber = Integer.parseInt(dE.getTextContent());
        //long timeBef,timeAft;
        for (int i = 1; i <= maxPageNumber; i++) {
            //timeBef = System.currentTimeMillis();
            page = webClient.getPage("https://" + server + "/statistiken.php?id=2&page=" + i);
            //timeAft = System.currentTimeMillis();
            //System.out.println("Diff in time (page loading):"+(timeAft-timeBef));
            //timeBef = System.currentTimeMillis();
            list = page.getElementById("villages").getElementsByTagName("tbody").get(0).getElementsByTagName("tr");
            for (int p = 0; p < list.size(); p++) {
                playerName = list.get(p).getElementsByTagName("td").get(2).getElementsByTagName("a").get(0).getTextContent();
                if (playerName.length() > 2) {
                    villageName = list.get(p).getElementsByTagName("td").get(1).getElementsByTagName("a").get(0).getTextContent();
                    villagePoints = Integer.parseInt(list.get(p).getElementsByTagName("td").get(3).getTextContent());
                    href = list.get(p).getElementsByTagName("td").get(4).getElementsByTagName("a").get(0).getAttribute("href");
                    coordX = Integer.parseInt(href.substring(12, href.indexOf('&')));
                    coordY = Integer.parseInt(href.substring(href.lastIndexOf('=') + 1));
                    if (model.getPlayers().containsKey(playerName)) {
                        if (!( model.getPlayers().get(playerName).getVillages().containsKey(coordX + "|" + coordY))) {
                            village = new Village();
                            village.setCoordX(coordX);
                            village.setCoordY(coordY);
                            village.setOwner(model.getPlayers().get(playerName));
                            village.setPopulation(villagePoints);
                            village.setVillageName(villageName);
                            model.getPlayers().get(playerName).getVillages().put(coordX + "|" + coordY, village);
                            model.getVillages().put(coordX + "|" + coordY, village);
                        }
                    } else {
                        player = new Player();
                        player.setPlayerName(playerName);
                        village = new Village();
                        village.setCoordX(coordX);
                        village.setCoordY(coordY);
                        village.setOwner(player);
                        village.setPopulation(villagePoints);
                        village.setVillageName(villageName);
                        player.getVillages().put(coordX + "|" + coordY, village);
                        model.getPlayers().put(playerName, player);
                        model.getVillages().put(coordX + "|" + coordY, village);
                    }
                }
            }
            //timeAft = System.currentTimeMillis();
            //System.out.println("Time diff (cpu work):"+(timeAft-timeBef));
            //Thread.sleep(3000);
        }
    }

}
