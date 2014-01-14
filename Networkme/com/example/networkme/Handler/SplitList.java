package example.networkme.Handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SplitList {
	private List<SocialMediaObject> textList;
	private List<SocialMediaPictureObject> picsList;
	private List<SocialMediaObject> jointList;
	
	public static final SimpleDateFormat STANDARD_TIME_FORMATTER = new SimpleDateFormat("HH:mm#dd/MM/yy");
	
	public SplitList() {
		textList = new ArrayList<SocialMediaObject>();
		picsList = new ArrayList<SocialMediaPictureObject>();	
		jointList = new ArrayList<SocialMediaObject>();
	}
	
	public void addToTextList(SocialMediaObject o) {
		/* Since we still want O(1) access should keep ALL lists sorted as we go.
		 * If we have 25 objects, we can store them in O(25) time but unordered. 
		 * This will require a sort when we want to display them in order. With 
		 * merge sort this is done in O(25*4.6). However, the sort is delayed 
		 * until needed. With insertion sort, we store up to 25 objects, 
		 * considering worst case, we have to go in O(n*(n+1)/2) time which is
		 * O(325) this is more than delaying sort. However, consider the fact
		 * this is not usually the case since returned data is probably in 
		 * chronological order. In some cases insertion will be faster but
		 * still worst case scenario of packets coming in complete reverse order.
		 */
		
		 /* For the purposes of this project, we would stick to a limit of 25 results
		  * meaning we should merge sort. so whichever we use is not really a problem
		  * however, if we want to extend this into higher numbers of results,
		  * we should be using merge sort as it would do in nlogn while insertion sort is
		  * more or less n^2. In this project, we probably won't cache so many results
		  * therefore we will take the risk of insertion sort as it is likely the data will
		  * come back more or less ordered, so if perfect, will be o(n) time insertion. 
		  * If not, then the overhead is small as n remains around 25 per social platform.
		  */
		
		//placeInOrder(textList,o);
		//placeInOrder(jointList,o);
		textList.add(o);
		jointList.add(o);
	}
	
	public void addToPicsList(SocialMediaPictureObject o) {
		//placeInOrder(picsList,o);
		//placeInOrder(jointList,o);
		picsList.add(o);
		jointList.add(o);
	}
	
	public SocialMediaPictureObject getFromPicsAtPosition(int pos) {
		return picsList.get(pos);
	}
	
	public SocialMediaObject getFromTextAtPosition(int pos) {
		return textList.get(pos);
	}
	
	public void clearTextList() {
		textList.clear();
	}
	
	public void clearPicsList() {
		picsList.clear();
	}
	
	public void clearJointList() {
		jointList.clear();
	}
	
	public void clearAllLists() {
		clearJointList();
		clearPicsList();
		clearTextList();
	}
	
	public List<SocialMediaPictureObject> getPicsList() {
		return picsList;
	}
	
	public List<SocialMediaObject> getTextList() {
		return textList;
	}
	
	public List<SocialMediaObject> getJointList() {
		return jointList;
	}
		
	public static String convertDateToString(Date d){
		return STANDARD_TIME_FORMATTER.format(d);
	}
	
	
	//Leave out until we can implement the concurrent list.
	/*
	private void placeInOrder(List<SocialMediaObject> list ,SocialMediaObject o) {
		if (list.isEmpty()) {
			list.add(o);
		} else {
			for (int i = 0; i<list.size(); ++i) {
				if (list.get(i).happensBefore(o)) {
					list.add(i,o);
					return;
				}
			}
			list.add(o);
		}		
	}
	
	private void placeInOrder(List<SocialMediaPictureObject> list, SocialMediaPictureObject o) {
		if (list.isEmpty()) {
			list.add(o);
		} else {
			for (int i = 0; i<list.size(); ++i) {
				if (list.get(i).happensBefore(o)) {
					list.add(i,o);
					return;
				}
			}
			list.add(o);
		}	
	}

	 */
	
	
	//At the moment this is a very dirty way of doing it. Also
	//it requires the setting of flags to be done in the correct
	//order and made sure in the right order else it will not sort 
	//correctly.
	
	public void sortData() {
		Collections.sort(picsList);
		Collections.sort(textList);
		Collections.sort(jointList);
	}

}
