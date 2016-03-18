
public class IllegalFileOperation extends Exception{

	/** Customized class to implement Illegal File Operation
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String message = "Illegal File Operation Performed";
	IllegalFileOperation(){
		super(message);
	}

}
