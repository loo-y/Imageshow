package imageshow;

import imageshow.Myimage;
import imageshow.PMF;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class DownloadImage extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String downkey = request.getParameter("downkey");
        String extName = request.getParameter("en");
        Myimage myimage = getMyimageByDownKey(downkey);
        
        response.setContentType("application/octet-stream");  
        response.setHeader("Content-Disposition","attachment;filename ="+downkey+"."+ extName);
        response.getOutputStream().write(myimage.getImage());
    }
    
    /**
     * Queries the datastore for the Movie object with the passed-in title. If
     * found, returns the Movie object; otherwise, returns null.
     *
     * @param title movie title to look up
     */
    private Myimage getMyimageByDownKey(String downkey) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Search for any Myimage object with the passed-in title; limit the number
        // of results returned to 1 since there should be at most one Myimage with
        // a given title
        Query query = pm.newQuery(Myimage.class, "downKey == downkeyParam");
        query.declareParameters("String downkeyParam");
        query.setRange(0,1);

        try {
            List<Myimage> results = (List<Myimage>) query.execute(downkey);
            if (results.iterator().hasNext()) {
                // If the results list is non-empty, return the first (and only)
                // result
                return results.get(0);
            }
        } finally {
            query.closeAll();
            pm.close();
        }

        return null;
    }
       
}