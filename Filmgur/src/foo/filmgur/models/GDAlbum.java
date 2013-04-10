package foo.filmgur.models;

public class GDAlbum {
	
	final static String MIME = "application/vnd.google-apps.folder";
	
	private String title;
	private String id;
	
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
}
