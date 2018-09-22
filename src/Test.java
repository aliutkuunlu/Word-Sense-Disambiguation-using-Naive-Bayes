import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static String lexNameID;/*instance for lexnameID it holds: "accident-n.700001"*/
	public static String lexNameTest;/*instance for lexname it holds: "accident-n"*/ 
public static void test(String line, HashMap<String, Lexelt> lexMap, ArrayList<String> stopWords, int totalWordCount, 
		PrintWriter out)
	{
		/*
		 * test method in this method i calculated the test context possibilities for detecting sense of this context.
		 * Parameters:
		 * 
		 * String line								current line to process
		 * HashMap<String, Lexelt> lexMap			my map for lexelt items
		 * ArrayList<String> stopWords				list of stop words for eleminate the stopwords
		 * int totalWordCount						Total word count in the training set
		 * PrintWriter out							output stream for out.txt file
		 */
		
		if(!line.equals(""))
		{
			/*
			 * if line is not empty
			 */
			if(line.contains("<instance"))
			{
				/*
				 * if line contains that string which means i will have information of lexelt in the test set
				 */
				lexNameID = line.split("\"")[1];/*when line is splitted i get "accident-n.700001" i hold this for output file*/
				lexNameTest = lexNameID.split("\\.")[0];/* when lexNameID splitted i get "accident-n" i hold this for lexmap key*/
				
			}
			if(line.contains("<p="))
			{
				/*
				 * that means i am in the context
				 */
				if(lexMap.containsKey(lexNameTest))
				{
					/*
					 * if lexname is in the lexmap i start compute.
					 */
					String [] tokens = line.split("<p=\"|\"/>");/*Same tokenize in the train set*/
					for (int i = 0; i < tokens.length; i++) 
					{
						/*
    	    			 * that means i have a token like this:----> <head>accident
    	    			 * and this is my reference point in current line 
    	    			 * i look 3 pos and words from left and right for create feature list
    	    			 */
						if(tokens[i].contains("<head>"))
						{
							int tempL = i - 1; /* this index walks to the left*/
	    	    			int tempR = i + 1; /* this index walks to the right*/
	    	    				    	    			
	    	    			for(String name: lexMap.get(lexNameTest).sensMap.keySet())
	    					{
	    	    				/*
	    	    				 * iterating in the sens map
	    	    				 */
	    	    				Double tempMaxValue = Double.valueOf(1);/*initial possibility value*/
	    						String key = name.toString(); /* key  taht holds sensId*/
	    						SensId value = lexMap.get(lexNameTest).sensMap.get(key);/* value that holds sensId*/
	    						if(tempL>=0)
		    	    			{
	    							/*
		    	    				 * if block for first left pos and word if we have a token on the left
		    	    				 */
		    	    				if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
		    	    					/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(tokens[tempL].trim() +"-1"))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempL].trim() +"-1")) / 
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--;/* move to next left token which is a word*/
		    	    				if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
		    	    					/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempL].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempL].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--;/* move to next left token which is a pos*/
		    	    			}
	    						if(tempL>=0)
	    						{
	    							/*
		    	    				 * if block for second left pos and word if we have a token on the left
		    	    				 */
	    							if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(tokens[tempL].trim() +"-2"))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempL].trim() +"-2")) / 
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--;/* move to next left token which is a word*/
		    	    				if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
		    	    					/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempL].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempL].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--;/* move to next left token which is a pos*/
	    						}
	    						if(tempL>=0)
	    						{
	    							/*
		    	    				 * if block for third left pos and word if we have a token on the left
		    	    				 */
	    							if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(tokens[tempL].trim() +"-3"))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempL].trim() +"-3")) / 
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--; /* move to next left token which is a word*/
		    	    				if(!stopWords.contains(tokens[tempL].trim()))
		    	    				{
		    	    					/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempL].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempL].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
		    	    				tempL--;
	    						}
	    						if(tempR<tokens.length)
	    						{
	    							/*
		    	    				 * if block for first right pos and word if we have a token on the left
		    	    				 */
	    							if(!stopWords.contains(tokens[tempR].trim()))
	    							{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
	    								if(value.featMap.containsKey(tokens[tempR].trim() +"+1"))
	    								{
	    									/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
	    									tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempR].trim() +"+1")) / 
		    	    								Double.valueOf(value.wordCount);
	    								}
	    								else
	    								{
	    									/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
	    									tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
	    								}
	    							}
	    							tempR++; /* move to next right token which is a word*/
	    							if(!stopWords.contains(tokens[tempR].trim()))
		    	    				{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempR].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempR].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
	    							tempR++;/* move to next right token which is a pos*/
	    						}
	    						if(tempR<tokens.length)
	    						{
	    							/*
		    	    				 * if block for second right pos and word if we have a token on the left
		    	    				 */
	    							if(!stopWords.contains(tokens[tempR].trim()))
	    							{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
	    								if(value.featMap.containsKey(tokens[tempR].trim() +"+2"))
	    								{
	    									/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
	    									tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempR].trim() +"+2")) / 
		    	    								Double.valueOf(value.wordCount);
	    								}
	    								else
	    								{
	    									/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
	    									tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
	    								}
	    							}
	    							tempR++; /* move to next right token which is a word*/
	    							if(!stopWords.contains(tokens[tempR].trim()))
		    	    				{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempR].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempR].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
	    							tempR++;/* move to next right token which is a pos*/
	    						}
	    						if(tempR<tokens.length)
	    						{
	    							/*
		    	    				 * if block for third right pos and word if we have a token on the left
		    	    				 */
	    							if(!stopWords.contains(tokens[tempR].trim()))
	    							{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
	    								if(value.featMap.containsKey(tokens[tempR].trim() +"+3"))
	    								{
	    									/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
	    									tempMaxValue *=  Double.valueOf(value.featMap.get(tokens[tempR].trim() +"+3")) / 
		    	    								Double.valueOf(value.wordCount);
	    								}
	    								else
	    								{
	    									/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
	    									tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
	    								}
	    							}
	    							tempR++;/* move to next right token which is a word*/
	    							if(!stopWords.contains(tokens[tempR].trim()))
		    	    				{
	    								/*
		    	    					 * if token, which is a pos, is not in the stopWordList
			    	    				 */
		    	    					if(value.featMap.containsKey(Main.stemmer(tokens[tempR].trim())))
		    	    					{
		    	    						/*
		    	    						 * if tokens in the feature map than update the possibility with 
		    	    						 * frequency of current token / total number of word in current sensId
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(value.featMap.get(Main.stemmer(tokens[tempR].trim()))) /
		    	    								Double.valueOf(value.wordCount);
		    	    					}
		    	    					else
		    	    					{
		    	    						/*
		    	    						 * if it is not than update the possibility with
		    	    						 * 1/ total number of word in the train set
		    	    						 */
		    	    						tempMaxValue *= Double.valueOf(1) / Double.valueOf(totalWordCount);
		    	    					}
		    	    				}
	    							tempR++;
	    						}
	    							/*
	    							 * psi value is total number of word in sensId / total number of word in the training set
	    							 */
	    							Double psi =  Double.valueOf(value.wordCount) / Double.valueOf(totalWordCount);
		    						tempMaxValue *= psi; /*updating possibiity with psi value*/
		    						/*
		    						 * Add the maxVal list the tempMaxValue with its key which is sensId
		    						 */
		    						lexMap.get(lexNameTest).maxList.add(new MaxValue(key, tempMaxValue));
	    							
	    						
	    					}
	    	    			
	    	    			/*
	    	    			 * return value is the sensId which has the maximum possibility
	    	    			 */
	    	    			String sensId = getMax(lexMap.get(lexNameTest).maxList);
	    	    			lexMap.get(lexNameTest).maxList.clear();/*empty the list for new items.*/
	    	    			out.println(lexNameID+ " " + sensId);/*wirting outputs to the output file*/
						}
					}
					
				}
			}
		}
	
	
	}
	public static String getMax(ArrayList<MaxValue> maxList) 
	{
		/*
		 * this methods find the maximum value in the list with its sensId
		 * it returns the most possible sensID
		 */
		String returnSenseId = maxList.get(0).sensId; /*initial sensId*/
		Double max = maxList.get(0).possibility;/* initial possibility*/
		for (int i = 1; i < maxList.size(); i++) 
		{
			/*
			 * starting to compare with the index one
			 */
			if(maxList.get(i).possibility != Double.valueOf(1))
			{
				if(maxList.get(i).possibility > max)
				{
					/*
					 * if coming value is greater than max value update the max value and sensId
					 */
					max = maxList.get(i).possibility;
					returnSenseId = maxList.get(i).sensId;
				}
			}
		}
		return returnSenseId;
		
	}

}
