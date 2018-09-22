Word Sense Disambiguation using Naive Bayes

For the train data:
	I design like in that order
	i creat object for every lexelt item
	Acording to train data every lexelt item has several senseId
	i creat object for every senseId
	So every lexelt object holds a HashMap which holds senseID object
	Every senseId object has a word count which it has and feature list.
	In senseId object i hold a HashMap with key as a word or pos and value its frequency.

	Note: you declare window size as a 3 therefore i build if control structures for every word and pos
	3 for the left and 3 for the right. I might iterate it but i thought 3 is a small value than i thought it is better to build with if structure. ## it might be don in dynmicly but in given short time i choose to done like this way.

For the test data:
	I've done the same spliting operations same as I've done in the train data
	and i found the most possible senseId for given context by using naive bayes.

For stopwords:
	I read this text file staticly due to given command line format. It has not an argument such as stopWords.txt.
	i read the file like this.
	BufferedReader stopWord = new BufferedReader(new FileReader("stopwords.txt"))
	There for stopwords.txt file must be in the same direcory with the Main.java file.

Command Line:
	java Main train.pos test.pos out.txt
