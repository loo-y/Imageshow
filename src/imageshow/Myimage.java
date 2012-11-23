package imageshow;

import java.util.Date;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * JDO-annotated model class for storing Bimage properties; Bimage's promotional
 * image is stored as a Blob (large byte array) in the image field.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Myimage {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String title;

    @Persistent
    @Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    private String imageType;

    @Persistent
    private Blob image;

    @Persistent
    private Blob simage;

    @Persistent
    private String description;
    
    @Persistent
    private String path;
    
    @Persistent
    private String spath;
    
    @Persistent
    private String sheight;

    @Persistent
    private Date date;

    @Persistent
    private String downKey;
    
    @Persistent
    private String extName;
    
    public Long getId() {
        return key.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getImageType() {
        return imageType;
    }

    public String getExtName() {
        return extName;
    }
    
    public byte[] getImage() {
        if (image == null) {
            return null;
        }

        return image.getBytes();
    }
    
    public Date getDate() {
        return date;
    }
    public String getPath() {
        return path;
    }
    
    public String getSmallImagePath() {
        return spath;
    }
    
    public String getSmallImageHeight() {
        return sheight;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getDownKey() {
        return downKey;
    }

    public byte[] getSImage() {
        if (simage == null) {
            return null;
        }

        return simage.getBytes();
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImage(byte[] bytes) {
        this.image = new Blob(bytes);
    }

    public void setSImage(byte[] bytes) {
        this.simage = new Blob(bytes);
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setExtName(String extName) {
        this.extName = extName;
    }
    
    public void setDownKey(String downKey) {
        this.downKey = downKey;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setSmallImagePath(String spath) {
    	this.spath = spath;
    }
    
    public void setSmallImageHeight(String sheight) {
    	this.sheight = sheight;
    }
}
