import java.util.*;

public class SensId {
	/*
	 * i created this class for senseID part in the train set.
	 * every sensId has its own context.
	 */
	public int wordCount;/* total word number in the context */
	/*
	 * A HashMap for feature list.
	 * key---> feature name such as "IN-1"
	 * value----> frequency of the name.
	 */
	HashMap<String, Integer> featMap = new HashMap<String, Integer>();
}
