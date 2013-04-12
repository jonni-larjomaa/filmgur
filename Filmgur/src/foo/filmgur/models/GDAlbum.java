package foo.filmgur.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Google Drive Album model.
 * @author Jonni
 *
 */
public class GDAlbum implements Parcelable{
	
	private String title;
	private String id;
	
	public GDAlbum(){
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		return title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(id);
		
	}
	
	public static final Parcelable.Creator<GDAlbum> CREATOR = new Parcelable.Creator<GDAlbum>() {
		public GDAlbum createFromParcel(Parcel in) {
			return new GDAlbum(in);
		}

		public GDAlbum[] newArray(int size) {
			return new GDAlbum[size];
		}
	};
	
	private GDAlbum(Parcel in){
		title = in.readString();
		id = in.readString();
	}
}
