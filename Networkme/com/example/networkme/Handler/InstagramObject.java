package example.networkme.Handler;

import java.util.Date;
import java.util.List;

public class InstagramObject extends SocialMediaPictureObject{
	
	private final String username;
	private final String location;
	private final List<String> tagsList;
	
	public InstagramObject(String url,String username, String location, String timeStamp,
								List<String> tagslist, double lat, double lon) {
		super(url,SocialMediaObject.TYPE_INSTAGRAM, timeStamp, lat, lon);
		this.username = username;
		this.location = location;
		this.tagsList = tagslist;
	}
		
	public String getUsername() {
		return this.username;
	}
		
	public String getLocation() {
		return this.location;
	}
	
	public List<String> getTagsList() {
		return this.tagsList;
	}
	
	public String getFlattenedTags() {
		String tags = "";
		
		if (tagsList.isEmpty()){
			return "No tags attached.";
		}
		
		for(String tag : tagsList) {
			tags += "#" + tag + " ";
		}
		return tags;
	}
	
	public String getTimeStamp() {
		return super.getTime();
	}

}
