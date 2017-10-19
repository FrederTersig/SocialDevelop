package Util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 
 */
public class FreeMarker {
   
    public static void process(String path_template, Map data, HttpServletResponse response, ServletContext servlet_context) throws IOException{
        
         
        
        response.setContentType("text/html;charset=UTF-8");        
        
        // Configurazione freemarker
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        cfg.setServletContextForTemplateLoading(servlet_context, "/template");

        Template template = cfg.getTemplate(path_template);
        
        PrintWriter out = response.getWriter();
        
        try{
            template.process(data, out);
            
        } catch (TemplateException ex) {     
            
        } finally{
            out.flush();
            out.close(); 
        }
    }
}
