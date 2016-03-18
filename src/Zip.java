import java.util.*;
public class Zip extends FileAttribute{
	// Zip Class inherited from File Attribute
	public final Map<String, Folder> folderMap;
	public final Map<String, Text> textMap;
	public final Map<String, Zip> zipMap;
	
	Zip(String Name, String Path, int Size, String Type,
			Folder folder, Text text) {
		super(Name, Path, Size, Type);
		if (folder != null){
			folderMap = folder.folderMap;
			textMap = folder.textMap;
			zipMap = folder.zipMap;
		} else {
			folderMap = null;
			zipMap = null;
			textMap = new HashMap<>();
			textMap.put(text.Name, text);
		}
	}
	
	//Method overriding to modify size specific to zip operation
	public void putSize(){
		Size = super.Size / 2;
	}
	
}
