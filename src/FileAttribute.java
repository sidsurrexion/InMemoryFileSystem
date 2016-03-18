// Main File Attribute Class
public class FileAttribute{
		
	String Name;
	String Path;
	int Size;
	String Type;
	
	FileAttribute(String Name, String Path, int Size, String Type){
		this.Name = Name;
		this.Path = Path;
		this.Size = Size;
		this.Type = Type;
	}
	
	public String getName(){
		return Name;
	}
	
	public String getPath(){
		return Path;
	}
	
	public int getSize(){
		return Size;
	}
	
	public void putPath(String path){
		Path = path;
	}
	
	public void putSize(int size){
		Size = Math.abs(Size + size);
	}
	
	public void putType(String type){
		Type = type;
	}
	
	public String getType(){
		return Type;
	}
}
