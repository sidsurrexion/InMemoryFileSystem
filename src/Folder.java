import java.util.*;
// Class extended from File Attribute
public class Folder extends FileAttribute {
	Map<String, Folder> folderMap;
	Map<String, Text> textMap;
	Map<String, Zip> zipMap;
	
	Folder(String Name, String Path, int Size, String Type) {
		super(Name, Path, Size, Type);
		folderMap = new HashMap<>();
		textMap = new HashMap<>();
		zipMap = new HashMap<>();
	}
	
}
