package miniprojekti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miniprojekti.data_access.ReadingTipDao;
import miniprojekti.database.Database;
import miniprojekti.domain.ReadingTip;
import spark.ModelAndView;
import spark.Redirect;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import static spark.Spark.get;
import static spark.Spark.post;
import spark.template.velocity.VelocityTemplateEngine;

// Main tulee toimimaan Controllerina
public class Main {
    @SuppressWarnings("MethodLength")
    public static void main(String[] args) {
        System.out.println("Test");  
        
        Database database = new Database("jdbc:sqlite:readingtips.db");
        ReadingTipDao readingtipdao = new ReadingTipDao(database);
        
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        
        if (process.environment().get("PORT") != null) {
        	port = Integer.parseInt(process.environment().get("PORT"));
        } else {
        	port = 4567;
        }
        
        Spark.port(port);
        
        Spark.get("/helloworld", (req, res) -> "Hello World");
        
        // Etusivu
        get("/", (req, res) -> {  
            HashMap<String, String> model = new HashMap<>();
            
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());

        // Uuden lisäys
        post("/", (req, res) -> {
            HashMap<String, String> model = new HashMap<>();
            //ReadingTip tip = new ReadingTip(req.queryParams("author"), req.queryParams("title"), req.queryParams("url"));
            readingtipdao.save(req.queryParams("author"), req.queryParams("title"), req.queryParams("url"));

            model.put("tipAdded", "New tip added succesfully");
            
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
        
        // Tipsien nayttaminen, aluksi kaikki
        get("/tips", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            
            List<ReadingTip> tips = readingtipdao.findAll();
            ArrayList<HashMap<String, String>> modelTips = new ArrayList<>();
            
            for (ReadingTip tip : tips) {
                HashMap<String, String> tipMap = new HashMap<>();
                
                tipMap.put("author", tip.getAuthor());
                tipMap.put("title", tip.getTitle());
                tipMap.put("url", tip.getUrl());
                
                modelTips.add(tipMap);
            }
            
            model.put("tips", modelTips);
            return new ModelAndView(model, "templates/tips.html");
        }, new VelocityTemplateEngine());
    }
}