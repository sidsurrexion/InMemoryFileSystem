// Text Class inherited from File Attribute 
public class Text extends FileAttribute{
	String content;
	Text(String Name, String Path, int Size, String Type, String content) {
		super(Name, Path, Size, Type);
		this.content = content;
	}	
}
