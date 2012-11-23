package imageshow;


import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.io.OutputStream;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.Image.Format;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class StoreImageByUploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
 
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
 
    	
    	Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");
    	 
        if (blobKey == null) {
            res.sendRedirect("/");
        } else {
        	//res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
        	
        	BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
            byte[] bytes = blobstoreService.fetchData(blobKey, 0, blobInfo.getSize());
        	
            Image oldImage = ImagesServiceFactory.makeImage(bytes);
        	//Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
        	
        	Myimage myimage = new Myimage();
        	Date date=new Date();
            String downKey=String.valueOf(date.getTime());
            myimage.setDownKey(downKey);
            myimage.setDate(date);
        	myimage.setImageType("image/"+oldImage.getFormat().toString());
        	myimage.setExtName(oldImage.getFormat().toString());
        	myimage.setImage(bytes);
        	myimage.setTitle(req.getParameter("title"));
        	myimage.setDescription(req.getParameter("desc"));
        	myimage.setPath("/image?s=&downkey="+downKey);
        	myimage.setSmallImagePath("/image?s=1&downkey="+downKey);
        	int newWidth=Integer.parseInt(req.getParameter("width"));

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