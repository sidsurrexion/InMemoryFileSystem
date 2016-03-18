
public class TextNotFound extends Exception{

	/** Customized exception for text not found
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String message = "Text File Not Found";
	
	TextNotFound(){
		super(message);
	}

}
