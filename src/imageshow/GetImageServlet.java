package imageshow;

import imageshow.Myimage;
import imageshow.PMF;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GET requests return the promotional image associated with the movie with the
 * title specified by the title query string parameter.
 */
public class GetImageServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //String title = request.getParameter("title");
        //Myimage myimage = getMyimage(title);
        Myimage myimage = getMyimageByDownKey(request.getParameter("downkey"));
        
        String smallImage = request.getParameter("s");
        Boolean isSmall = false;
        if(smallImage!=null && smallImage.equals("1")){isSmall=true;}
        
        if (myimage != null && myimage.getImageType() != null &&
        		myimage.getImage() != null) {
            // Set the appropriate Content-Type header and write the raw bytes
            // to the response's output stream
            response.setContentType(myimage.getImageType());
            if(isSmall){response.getOutputStream().write(myimage.getSImage());}
            else{response.getOutputStream().write(myimage.getImage());}
        } else {
            // If no image is found with the given title, redirect the user to
            // a static image
            response.sendRedirect("/static/noimage.jpg");
        }
    }
    
    /**
     * Queries the datastore for the Movie object with the passed-in title. If
     * found, returns the Movie object; otherwise, returns null.
     *
     * @param title movie title to look up
     */
    private Myimage getMyimage(String title) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Search for any Myimage object with the passed-in title; limit the number
        // of results returned to 1 since there should be at most one image with
        // a given title
        Query query = pm.newQuery(Myimage.class, "title == titleParam");
        query.declareParameters("String titleParam");
        query.setRange(0,5);

        try {
            List<Myimage> results = (List<Myimage>) query.execute(title);
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
    
    private Myimage getMyimageByDownKey(String downKey) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Search for any Myimage object with the passed-in title; limit the number
        // of results returned to 1 since there should be at most one image with
        // a given title
        Query query = pm.newQuery(Myimage.class, "downKey == downKeyParam");
        query.declareParameters("String downKeyParam");
        //query.setRange(0,5);
        
        
        try {
        	//query.deletePersistentAll(downKey);
        	
            List<Myimage> results = (List<Myimage>) query.execute(downKey);
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