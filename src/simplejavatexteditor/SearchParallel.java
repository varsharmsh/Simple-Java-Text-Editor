package simplejavatexteditor;

import java.util.concurrent.*;
import java.util.ArrayList;


public class SearchParallel implements Callable <ArrayList<Integer>> 
{
	String myString;
	String searchString;
	int start;

	public SearchParallel(int start, String str, String searchStr) {
		this.start = start;
		myString = str;
		searchString = searchStr;

	}

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
