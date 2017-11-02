package Servlets;



import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Upload extends HttpServlet {

    Map<String, Object> data = new HashMap<String, Object>();
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
int id=0;
    

    public static String toString(int a, int b, int c) {
        return a + "-" + b + "-" + c;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 HttpSession s = SecurityLayer.checkSession(request);
 
 
 
  String action = request.getParameter("value");
        
        if("logout".equals(action)){ // Inizio del logout
                System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    response.sendRedirect("index");
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }else if("search".equals(action)){
                System.out.println("COMINCIA LA RICERCA!");
                String SearchStringa = request.getParameter("ricerca");
                System.out.println("RICERCA IN CORSO::::: >>>" + SearchStringa);           
                
                if(s != null){//condizione per vedere se la sessione esiste.                    
                    s.setAttribute("ricerca",SearchStringa);   
                }else{
                    HttpSession z = request.getSession(true);
                    z.setAttribute("ricerca",SearchStringa);         
                }   
                data.put("ricerca", SearchStringa);                
                response.sendRedirect("listaCerca");
            }
        // checks if the request actually contains upload file
        Map<String, Object> map = new HashMap<String, Object>();
        if (!ServletFileUpload.isMultipartContent(request)) {

            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }

        // Configurazione impostazioni di upload
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        System.out.println("UPLOAD: " + upload);

        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        //Costruisce il path della cartella dove fare l'upload del file
        String uploadPath = "C:\\Users\\user1\\Desktop\\SocialDevelopGitHub\\web\\template\\img" + UPLOAD_DIRECTORY;
        System.out.println("UPLOAD PATH: " + uploadPath);

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        System.out.println("UPLOAD DIR: " + uploadDir);

        if (!uploadDir.exists()) {
            System.out.println("DENTRO IF-EXIST");

            uploadDir.mkdir();
        }

        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            System.out.println("FORM ITEMS: " + formItems);
            System.out.println("ITER: " + iter);

            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                System.out.println("ITEM: " + item);

                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    String fileName2 = new File(item.getName()).getName();
                    System.out.println("FILENAME2: " + fileName2);

                    String fileName = item.getName();
                    System.out.println("FILENAME: " + fileName);

                    String filePath = uploadPath + File.separator + fileName;
                    System.out.println("FILEPATH: " + filePath);

                    File storeFile = new File(filePath);
                    System.out.println("STOREFILE" + storeFile);

                    map.put("immagine", "template/img/upload/" + fileName);
                    // saves the file on disk
                    item.write(storeFile);

                    

                    Databasee.connect();
                    
                    
                    Databasee.updateRecord("sviluppatore", map, "sviluppatore.id = " + s.getAttribute("id"));
                    Databasee.close();

                    map.clear();
                }
            }
            request.setAttribute("message", "Upload has been done successfully!");
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        response.sendRedirect("profilo");
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        HttpSession s = SecurityLayer.checkSession(request);

        if (s != null) {

           data.put("id", s.getAttribute("id"));

            FreeMarker.process("upload.html", data, response, getServletContext());
        } else {
            FreeMarker.process("index.html", data, response, getServletContext());
        }
    }
}
