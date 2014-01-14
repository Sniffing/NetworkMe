package example.networkme.Handler;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JOSE on 27/10/13.
 */
public class FacebookObject extends SocialMediaObject{
    private final String location;
    private final String name;
    private final String description;
    //private final String start_time; This is now the same as date in SocialMediaObject
    private String end_date;
    private String end_time;
    private final String host;
    private final int attending;
    private final String picCover;
    
    public FacebookObject(String name, String location,String description, String start_time, 
    		String end_dateFormat, String host, String picCover,int atten, double lat, double lon){
    	super(SocialMediaObject.TYPE_FB, start_time, lat, lon);
        this.location = location;
        this.name = name;
        this.description = description;
        this.host = host;
        setEnd_time(end_dateFormat);
        this.attending = atten;
        this.picCover = picCover;
    }
	
	public void setEnd_time(String end_dateFormat) {
		String end_date = SplitList.STANDARD_TIME_FORMATTER.format(give_me_the_D(end_dateFormat));
		String[] end_date_split = end_date.split("#");
		end_time = end_date_split[0];
		end_date = end_date_split[1];
	}

	public String getEndTime() {
		return end_time;
	}
	
	public String getEndDate() {
		return end_date;
	}
	
	public String getHost() {
		return host;
	}

	public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return this.getTime();
    }
    
    public String getStartDate(){
    	return this.getDate();
    }

    public String getLocation() {
        return location;
    }

    public URL getPicCover() throws MalformedURLException {
    	if(picCover == null) { return null; }
    	else { return new URL(picCover); }
    }
    
    public String getPicCoverString() {
    	return this.picCover;
    }
    
    public int getAttending() {
    	return attending;
    }
}

