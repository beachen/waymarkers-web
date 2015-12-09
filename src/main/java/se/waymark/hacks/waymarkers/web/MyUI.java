package se.waymark.hacks.waymarkers.web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 */
@Theme("mytheme")
@Widgetset("se.waymark.hacks.waymarkers.web.MyAppWidgetset")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        String profiles = "no profiles";
        try 
        {
            URL url = new URL("https://raw.githubusercontent.com/beachen/waymarkers/master/profiles.json");  
                    
            Document res = Jsoup.parse(url, 1000);
            profiles = res.select("body").text();
            
            System.out.println(profiles);
            Type listType = new TypeToken<List<Profiles>>() {}.getType();
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Profiles> consultants = gson.fromJson(profiles, listType);
            layout.addComponent(new Label("<h3>Waymark - Coolaste konsulterna i STHLM</h3>", ContentMode.HTML));
            layout.addComponent(new Label("<b>Antal coola konsulter:</b>" + consultants.size(), ContentMode.HTML));
            consultants.stream().forEach(p -> layout.addComponent(new Label(p.getName())));
            
        } catch (IOException ex) 
        {
            Logger.getLogger(MyUI.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}