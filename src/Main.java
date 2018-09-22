import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

	
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		int totalWordCount = 0;
		PrintWriter out = new PrintWriter(new FileWriter(args[2]));/* creating object for out.txt third argument*/ 
		/*
		 *  A HashMap that holds Lexelt objects.
		 */
		HashMap<String, Lexelt> lexMap = new HashMap<String, Lexelt>();
		/*
		 * An ArrayList that holds stopWords.
		 */	
		ArrayList<String> stopWords = new ArrayList<String>();
		String lexName = ""; // instance for lexelt item's name in train set. holds "accident-n"
		String sensId = "";	// instance for lexelt item's sense Id in train set. holds "532675"
		
		try (BufferedReader stopWord = new BufferedReader(new FileReader("stopwords.txt"))) /* reading stopWrods.*/
		{
			/*
			 * this try block reads the stopwords and add this words to the ArrayList
			 */
			String line;
			while((line = stopWord.readLine()) != null)
			{
				stopWords.add(line);
			}
		}
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) 
		{
			/*
			 * this try block reads the first command line argument which is trainSet.
			 */
		    String line;//instance for every line in the train set
		    while ((line = br.readLine()) != null)
		    {
		    	/*
		    	 * this loops reads the line in the train set until its done.
		    	 */
		    	if(!line.equals(""))
		    	{
		    		/*
		    		 * if line is not empty line we have a context or lexelt item's information line. 
		    		 */
		    		if(line.contains("<answer"))
		    		{
		    			/*
		    			 * this line contains information of lexelt item. Line is in this format
		    			 * <answer instance="accident-n.800001" senseid="532675"/>
		    			 */
		    			String [] tokens = line.split("\""); /* splitting respected to " char.*/
		    			lexName = tokens[1].split("\\.")[0]; /* this holds "accident-n"*/
		    			sensId = tokens[3];					 /* this holds "532675"*/
		    			if(!(lexMap.containsKey(lexName)))
		    			{
		    				/*
		    				 * if lexMap not have a item whose name is lexName
		    				 * then i create new object.
		    				 */
		    				lexMap.put(lexName, new Lexelt());
		    				
		    			}
		    			if(!(lexMap.get(lexName).sensMap.containsKey(sensId)))
		    			{
		    				/*
		    				 * if lexelt object's sensMap no have a item whose name is sesId
		    				 * then i create new object.
		    				 */
		    				lexMap.get(lexName).sensMap.put(sensId, new SensId());
		    			}
		    		}
		    		if(line.contains("<p="))
		    		{
		    			/*
		    			 * that means line is a context line.
		    			 */
		    			String [] tokens = line.split("<p=\"|\"/>");
		    			/*
		    			 * i used regex to get tokens for this line:----->  late <p="RB"/> on <p="IN"/> thursday <p="NNP"/>
		    			 * i get tokens like this :----> late RB on IN thursday NNP
		    			 */
		    	    	for (int i = 0; i < tokens.length; i++) 
		    	    	{
		    	    		if(!tokens[i].equals(""))
		    	    		{
		    	    			lexMap.get(lexName).sensMap.get(sensId).wordCount++;
		    	    			/*
		    	    			 * incrementing the sensId object's word count.
		    	    			 * which is the total number of words in given sense ID
		    	    			 */
				    			totalWordCount++;/* incrementing total wordCount*/
				    			
			    	    		if(tokens[i].contains("<head>"))
			    	    		{
			    	    			/*
			    	    			 * that means i have a token like this:----> <head>accident
			    	    			 * and this is my reference point in current line 
			    	    			 * i look 3 pos and words from left and right for create feature list
			    	    			 */
			    	    			int tempL = i - 1; /* this index walks to the left*/
			    	    			int tempR = i + 1; /* this index walks to the right*/
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
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempL].trim() +"-1" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN-1" 
			    				    			 * -1 means first left pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-1" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempL].trim() +"-1" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-1" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempL--;/* move to next left token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempL].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(tokens[tempL].trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(tokens[tempL].trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()) , tempValue);
				    				    		
				    				    	}
			    				    	}
			    	    				tempL--; /* to look to next pos.*/
			    	    			}
			    	    			
			    	    			if(tempL>=0)
			    	    			{
			    	    				/*
			    	    				 * if block for second left pos and word
			    	    				 */
			    	    				if(!stopWords.contains(tokens[tempL].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a pos, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempL].trim() +"-2" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN-2" 
			    				    			 * -2 means second left pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-2" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempL].trim() +"-2" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-2" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempL--;/* move to next left token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempL].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(tokens[tempL].trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(tokens[tempL].trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()), tempValue);
				    				    		
				    				    	}
			    				    	}
			    	    				tempL--;/* to look to next pos.*/
			    	    			}
			    	    			if(tempL>=0)
			    	    			{
			    	    				/*
			    	    				 * if block for third left pos and word
			    	    				 */
			    	    				if(!stopWords.contains(tokens[tempL].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a pos, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempL].trim() +"-3" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN-3" 
			    				    			 * -3 means third left pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-3" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempL].trim() +"-3" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempL].trim() +"-3" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempL--;/* move to next left token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempL].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(tokens[tempL].trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(tokens[tempL].trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempL].trim()) , tempValue);
				    				    		
				    				    	}
			    				    	}
			    	    			}
			    	    			if(tempR<tokens.length)
			    	    			{
			    	    				/*
			    	    				 * if block for first right pos and word
			    	    				 */
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a pos, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempR].trim() +"+1" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN+1" 
			    				    			 * +1 means first right pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+1" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempR].trim() +"+1" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+1" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempR++; /* move to next right token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    	    					String temp ="";
			    	    					if(tokens[tempR].contains("</head>"))
			    	    					{
			    	    						/*
			    	    						 * if token comes like this:----> "</head> appeared "
			    	    						 * i hold the appeared string with splitting.
			    	    						 */
			    	    						temp = tokens[tempR].split("</head>")[1];
			    	    					}
			    	    					else
			    	    					{
			    	    						/*
			    	    						 * otherwise there is no problem.
			    	    						 */
			    	    						temp = tokens[tempR];
			    	    					}
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(temp.trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(temp.trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(temp.trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(temp.trim())  , tempValue);
				    				    		
				    				    	}
			    				    	}
			    	    				tempR++;	/* to look to next pos.	*/    	    				
			    	    			}
			    	    			if(tempR<tokens.length)
			    	    			{
			    	    				/*
			    	    				 * if block for second right pos and word
			    	    				 */
			    	    				
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a pos, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempR].trim() +"+2" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN+2" 
			    				    			 * +2 means second right pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+2" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempR].trim() +"+2" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+2" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempR++; /* move to next right token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    	    					String temp ="";
			    	    					if(tokens[tempR].contains("</head>"))
			    	    					{
			    	    						/*
			    	    						 * if token comes like this:----> "</head> appeared "
			    	    						 * i hold the appeared string with splitting.
			    	    						 */
			    	    						temp = tokens[tempR].split("</head>")[1];
			    	    					}
			    	    					else
			    	    					{
			    	    						/*
			    	    						 * otherwise there is no problem.
			    	    						 */
			    	    						temp = tokens[tempR];
			    	    					}
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(temp.trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(temp.trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(temp.trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(temp.trim())  , tempValue);
				    				    		
				    				    	}
			    				    	}
			    	    				tempR++;	/* to look to next pos	*/    	    				
			    	    			}
			    	    			if(tempR<tokens.length)
			    	    			{
			    	    				/*
			    	    				 * if block for third right pos and word
			    	    				 */
			    	    				
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a pos, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(tokens[tempR].trim() +"+3" )))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token "IN" like this "IN+3" 
			    				    			 * +3 means third right pos
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+3" , 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given pos.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(tokens[tempR].trim() +"+3" );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(tokens[tempR].trim() +"+3" , tempValue);
				    				    		
				    				    		
				    				    	}
			    				    	}
			    	    				tempR++;/* move to next right token which is a word*/
			    	    				if(!stopWords.contains(tokens[tempR].trim()))
			    				    	{
			    	    					/*
			    	    					 * if token, which is a word, is not in the stopWordList
			    	    					 */
			    				    		if(!(lexMap.get(lexName).sensMap.get(sensId).featMap.containsKey(stemmer(tokens[tempR].trim()))))
				    				    	{
			    				    			/*
			    				    			 * if tokens is not in the feature list add a new one
			    				    			 * but modify token with stemmer method 
			    				    			 */
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempR].trim()), 1);
				    				    		
				    				    	}
				    				    	else
				    				    	{
				    				    		/*
				    				    		 * increment the frequency of given word.
				    				    		 */
				    				    		int tempValue = lexMap.get(lexName).sensMap.get(sensId).featMap.get(stemmer(tokens[tempR].trim()) );
				    				    		tempValue ++;
				    				    		lexMap.get(lexName).sensMap.get(sensId).featMap.put(stemmer(tokens[tempR].trim())  , tempValue);
				    				    		
				    				    	}
			    				    	}		    	    				
			    	    			}
			    	    		}
		    	    		}
		    	    		
		    			}
		    	    	
		    		}
		    	}
            }
		    br.close();/*closing the first argument*/
		}
		
		try (BufferedReader br1 = new BufferedReader(new FileReader(args[1]))) /* reading second command line argument*/
		{
			/*
			 * this try block reads the second command line argument which is testSet.
			 */
			String line;/*instance for every line in the train set*/
		    while ((line = br1.readLine()) != null)
		    {
		    	/*
		    	 * this loops reads the line in the train set until its done.
		    	 */
		    	
		    	/*
		    	 * calling Test class's test method
		    	 * parameters:
		    	 * String line: 						current line
		    	 * HashMap<String, Lexelt> lexMap  		HashMap of lexelt objects
		    	 * ArrayList<String> stopWords			ArrayList of stop words
		    	 * int totalWordCount					Total word number in the train set
		    	 * PrintWriter out						output file to write output
		    	 */
		    	Test.test(line, lexMap, stopWords, totalWordCount, out);
			
			}
		 }
		out.close();/* closing the third argument*/
	}
		
	
	
	public static String stemmer(String word)
	{
		/*
		 * stemmer method which is shared by TA.
		 * I changed the paramater it takes word instead of inputfile as an argument
		 */
		 char[] w = new char[501];
	     Stemmer s = new Stemmer();
	     InputStream in = new ByteArrayInputStream(word.getBytes(StandardCharsets.UTF_8));

		 try
		 { while(true)

		   {  int ch = in.read();
		      if (Character.isLetter((char) ch))
		      {
		         int j = 0;
		         while(true)
		         {  ch = Character.toLowerCase((char) ch);
		            w[j] = (char) ch;
		            if (j < 500) j++;
		            ch = in.read();
		            if (!Character.isLetter((char) ch))
		            {
		               /* to test add(char ch) */
		               for (int c = 0; c < j; c++) s.add(w[c]);

		               /* or, to test add(char[] w, int j) */
		               /* s.add(w, j); */

		               s.stem();
		               {  String u;

		                  /* and now, to test toString() : */
		                  u = s.toString();

		                  /* to test getResultBuffer(), getResultLength() : */
		                  /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */

		                  return u;
		               }
		            }
		         }
		      }
		      if (ch < 0) break;
		      
		   }
		 }
		 catch (IOException e)
		 {  System.out.println("error reading " + word);
		 }
		 return "";
	}
	
}

