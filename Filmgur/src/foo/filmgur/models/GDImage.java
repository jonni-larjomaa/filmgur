package foo.filmgur.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Google Drive Image model.
 * 
 * @author Jonni
 *
 */
public class GDImage implements Parcelable {

	private String name;
	private String id;
	private String srcUrl;
	private String albumid;
	
	
	
	public GDImage() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(albumid);
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(srcUrl);
	}
	
	public static final Parcelable.Creator<GDImage> CREATOR = new Parcelable.Creator<GDImage>() {
		public GDImage createFromParcel(Parcel in) {
			return new GDImage(in);
		}

		public GDImage[] newArray(int size) {
			return new GDImage[size];
		}
	};
	
	private GDImage(Parcel in){
	}

}
