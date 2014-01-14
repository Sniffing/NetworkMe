package example.networkme.Handler;


/**
 * Created by JOSE on 27/10/13.
 */
public class FacebookData {
    private String location;
    private String name = null;
    private String description;
    private String start_time;
    private FacebookData next;
    private int counter;



    public FacebookData(String name, String location,String description, String start_time, int counter){
        this.location = location;
        this.name = name;
        this.description = description;
        this.start_time = start_time;
        this.counter = counter;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNext(FacebookData data){
        this.next = data;
    }

    public FacebookData getNext(){
        return next;
    }

    public boolean hasNext(){
        if(next != null){
            return true;
        }else
            return false;

    }

    @Override
    public String toString(){
        String s = "";
        s += "\nName: "+name+
//                "\nDescription: "+description+
                "\nLocation: "+location+
                "\nstart Time: "+start_time
//                "\n Counter: "+counter+"\n"
        ;
        return s;
    }

}

