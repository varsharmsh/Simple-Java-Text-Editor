package simplejavatexteditor;

import java.util.concurrent.*;
import java.util.ArrayList;

//class that implements searching in parallel
public class SearchParallel implements Callable <ArrayList<Integer>> 
{
	String myString;
	String searchString;
	int start;
	//construct object with target and search strings and specify start index
	public SearchParallel(int start, String str, String searchStr) {
		this.start = start;
		myString = str;
		searchString = searchStr;

	}

	//returns an arraylist of occurrences
	@Override
	public ArrayList<Integer> call() 
	{
		ArrayList<Integer> occurrences=new ArrayList<Integer>();
		int index = myString.indexOf(searchString);
		while (index >= 0) 
		{
		    occurrences.add(start+index);
		    index = myString.indexOf(searchString, index + 1);
		   
		}
		return occurrences;
	}

}
