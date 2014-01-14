package example.networkme.Handler;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class SocialMediaPictureObject extends SocialMediaObject {

	private String url;
	
	public SocialMediaPictureObject(String url, int type, String time, double lat, double lon) {
		super(type, time, lat, lon);
		this.url = url;
	}
	
	public URL getUrl() throws MalformedURLException{
		URL url = new URL(this.url);
		return url;
	}
	
	public String getUrlString() {
		return url;
	}

}
