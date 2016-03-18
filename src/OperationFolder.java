import java.util.HashSet;
import java.util.Set;

// Class to hold operation related objects
public class OperationFolder {

		Drive getDrive = null;
		Drive parentDrive = null;
		String typeString = null;
		Folder getFolder = null;
		Folder parentFolder = null;
		Text getText = null;
		Zip getZip = null;
		Set<Drive> driveHolder = new HashSet<>();
		Set<Folder> folderHolder = new HashSet<>();
		boolean isTrue;

}
