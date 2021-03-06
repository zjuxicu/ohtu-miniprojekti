package miniprojekti;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miniprojekti.data_access.ReadingTipDao;
import miniprojekti.database.Database;
import miniprojekti.domain.Logic;
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
    
    private final static Logic appLogic = new Logic();
    
    public static void main(String[] args) {
        Spark.port(portSelection());
        
        getIndexPage();
        postReadingTip();
        getReadingTipsPage();
    }

    private static void getIndexPage() {
        get("/", (req, res) -> {  
            HashMap<String, Object> model = new HashMap<>();
            
            model.put("tips", appLogic.retrieveAllTips());
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
    }

    private static void postReadingTip() {
        post("/", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();

            appLogic.saveNewTip(req.queryParams("author"), req.queryParams("title"), req.queryParams("url"));
            
            model.put("tipAdded", "New tip added succesfully");
            model.put("tips", appLogic.retrieveAllTips());
            
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
        
    }

    private static void getReadingTipsPage() {
        get("/tips", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            
            model.put("tips", appLogic.retrieveAllTips());
            
            return new ModelAndView(model, "templates/tips.html");
        }, new VelocityTemplateEngine());
    }
    
    private static Integer portSelection() {
        ProcessBuilder process = new ProcessBuilder();
        if (process.environment().get("PORT") != null) {
            return Integer.parseInt(process.environment().get("PORT"));
        } 
        return 4567;
    }
}