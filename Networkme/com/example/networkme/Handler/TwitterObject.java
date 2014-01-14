package example.networkme.Handler;

import java.net.MalformedURLException;
import java.net.URL;


public class TwitterObject extends SocialMediaPictureObject{
	
	/* However, this is a bad idea on Android. Virtual method calls are expensive, 
	 * much more so than instance field lookups. It's reasonable to follow common 
	 * object-oriented programming practices and have getters and setters in the public 
	 * interface, but within a class you should always access fields directly.
	 * Without a JIT, direct field access is about 3x faster than invoking a trivial 
	 * getter. With the JIT (where direct field access is as cheap as accessing a local), 
	 * direct field access is about 7x faster than invoking a trivial getter.
	 * 
	 * From official guidelines, always access fields directly whilst within classes.
	 */
	
	private final String status;
	private final String username;
	private final String profilePicUrl;
	private final String actualName;
	private boolean FLAG_CONTAINS_PIC;
	
	public TwitterObject(String status, String username, String time, String ppURL, String actualName,
			double lat, double lon) {
		super(null,SocialMediaObject.TYPE_TWITTER, time, lat, lon);
		this.status = status;
		this.username = username;
		this.profilePicUrl = ppURL;
		this.actualName = actualName;
		FLAG_CONTAINS_PIC = false;
	}
	
	public TwitterObject(String url, String status, String username, String time, String ppURL, String actualName,
			double lat, double lon) {
		super(url,SocialMediaObject.TYPE_TWITTER, time, lat, lon);
		this.status = status;
		this.username = username;
		this.profilePicUrl = ppURL;
		this.actualName = actualName;
		FLAG_CONTAINS_PIC = true;
	}

	public String getStatus() {
		return this.status;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getActualName() {
		return actualName;
	}
	
	public URL getProfilePicture() throws MalformedURLException {
		return new URL(profilePicUrl);
	}
	
	public String getPPUrlString() {
		return profilePicUrl;
	}
	
	public boolean containsPicture() {
		return FLAG_CONTAINS_PIC;
	}
		
}
