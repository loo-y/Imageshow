package imageshow;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.urlfetch.FetchOptions;
import static com.google.appengine.api.urlfetch.FetchOptions.*;
import static com.google.appengine.api.urlfetch.FetchOptions.Builder.*;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class StoreImageServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        URLFetchService fetchService =
            URLFetchServiceFactory.getURLFetchService();
        
        URL furl=new URL(request.getParameter("url"));
        Double d=10000.0;
        FetchOptions options = FetchOptions.Builder.withDeadline(d);
        HTTPRequest hrequest = new HTTPRequest(furl, HTTPMethod.GET, options);
        hrequest.getFetchOptions().setDeadline(d);
        
        HTTPResponse fetchResponse = fetchService.fetch(hrequest);
                 
        String fetchResponseContentType = null;
        for (HTTPHeader header : fetchResponse.getHeaders()) {
            if (header.getName().equalsIgnoreCase("content-type")) {
                fetchResponseContentType = header.getValue();
                break;
            }
        }

        if (fetchResponseContentType != null) {
            // Create a new Myimage instance
            Myimage myimage = new Myimage();
            Date date=new Date();
            String downKey=String.valueOf(date.getTime());
            myimage.setDownKey(downKey);
            myimage.setDate(date);
            myimage.setTitle(request.getParameter("title"));
            myimage.setDescription(request.getParameter("desc"));
            myimage.setImageType(fetchResponseContentType);
        	myimage.setExtName(fetchResponseContentType.split("/")[1].toLowerCase());
        	myimage.setPath("/image?s=&downkey="+downKey);
        	myimage.setSmallImagePath("/image?s=1&downkey="+downKey);
        	int newWidth=Integer.parseInt(request.getParameter("width"));
            
            byte[] newImageByte=fetchResponse.getContent();
            Image oldImage = ImagesServiceFactory.makeImage(newImageByte);
            
            // Set the movie's promotional image by passing in the bytes pulled
            // from the image fetched via the URL Fetch service
            myimage.setImage(newImageByte);
            myimage.setImageType(oldImage.getFormat().toString());
            
            //resize the image to be smaller.
            int newHeight = oldImage.getHeight() * newWidth / oldImage.getWidth();
            Transform resize = ImagesServiceFactory.makeResize(newWidth, newHeight);
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            Image resizedImage = imagesService.applyTransform(resize, oldImage);
            byte[] resizedImageData = resizedImage.getImageData();
            myimage.setSImage(resizedImageData);
            myimage.setSmallImageHeight(String.valueOf(newHeight));

            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // Store the image in App Engine's datastore
                pm.makePersistent(myimage);
            } finally {
                pm.close();
            }
        }
    }
}