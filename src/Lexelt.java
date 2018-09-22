import java.util.*;

public class Lexelt {
	/*
	 * i created this class for lexelt items
	 * every lexelt object has its own Sensid objects
	 * every lexelt objects has a MaxValue
	 */
	/*
	 * HashMap for sensId ---->  key is senseId name value is sensId objects.
	 */
	HashMap<String, SensId> sensMap = new HashMap<String, SensId>();
	/*
	 * It holds sensId's possibilities and sensId names
	 */
	ArrayList<MaxValue> maxList = new ArrayList<MaxValue>();
}
