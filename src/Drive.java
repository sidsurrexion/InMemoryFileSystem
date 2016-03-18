import java.util.*;
public class Drive extends FileAttribute{
		
	Map<String, Folder> folderMap;
	Map<String, Text> textMap;
	Map<String, Zip> zipMap;
	
	Drive(String Name, String Path, int Size, String Type) {
		super(Name, Path, Size, Type);
		folderMap = new HashMap<>();
		textMap = new HashMap<>();
		zipMap = new HashMap<>();
	}


	
}
