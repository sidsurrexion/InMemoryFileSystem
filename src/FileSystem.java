import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
// Driver class to run File System Operation
public class FileSystem {
	
	static Map<String, Drive> driveMap = new HashMap<String, Drive>(); // HashMap to store the drives
	
	public static void main(String[] args) {
		// Main  function to perform file operations
		
		Scanner scanner = new Scanner(System.in);
		fileOperation(scanner);
		scanner.close();
	}
	
	// Method to perform file operation
	public static void fileOperation(Scanner scanner){
		
		boolean toOperate = true;
		while (toOperate){
			System.out.println("Select one of the following file operations: ");
			System.out.println("1. Create");
			System.out.println("2. Delete");
			System.out.println("3. Move");
			System.out.println("4. Write To File");
			System.out.println("5. Exit");

			int operation = scanner.nextInt();
			
			switch (operation){
			case 1:
				System.out.print("Please enter the path: ");
				String path = scanner.next();
				System.out.println();
				
				System.out.println("Please enter the name of the file/folder: ");
				String name = scanner.next();
				System.out.println();
				create(fileType(scanner), name, path, scanner);
				continue;
			case 2:
				System.out.print("Please enter the path: ");
				path = scanner.next();
				System.out.println();
				operationUpdate(path, operation, null);
				continue;
			case 3:
				System.out.print("Please enter the source path: ");
				path = scanner.next();
				System.out.println();
				
				System.out.print("Please enter the destination path: ");
				String destination = scanner.next();
				System.out.println();
				
				moveOperation(path, destination,  operation);
				continue;
			case 4:
				System.out.print("Please enter the path: ");
				path = scanner.next();
				System.out.println();
				
				System.out.print("Please enter the content: ");
				String content = scanner.next();
				System.out.println();
				
				operationUpdate(path, operation, content);
				continue;
			case 5:
				toOperate = false;
			}
		}		
	}
	
	// Method to determine which file type to be selected by the user
	public static String fileType(Scanner scanner){
		System.out.println("Please select one of the file types");
		System.out.println("1. Drive");
		System.out.println("2. Folder");
		System.out.println("3. Text");
		System.out.println("4. Zip");
		int fileOption = scanner.nextInt();
		
		switch (fileOption){
		case 1: 
			return "Drive";
		case 2:
			return "Folder";
		case 3:
			return "Text";
		case 4: 
			return "Zip";
		default:
			System.out.println("Incorrect File Option");
			return (fileType(scanner));
		}
		
	}
	
	// Method to create a file or a directory
	public static void create(String Type, String Name, String parentPath, Scanner scanner){		
		try{
			if (isValidPath(parentPath, Type, Name, scanner)){
				System.out.println("File Operation performed successfully");
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		 
	}
	
	// Method in create method to validate if the file operation is valid
	public static boolean isValidPath(String parentPath, String Type, String Name, Scanner scanner) throws FileAlreadyExistsException, FileNotFoundException, IllegalFileOperation{
		
		String[] concatenatedPath = parentPath.trim().split("/");
		Drive getDrive = null;
		Text getText = null;
		Zip getZip = null;
		Folder getFolder = null;
		String typeString = null;
		Set<Drive> driveHolder = new HashSet<>();
		Set<Folder> folderHolder = new HashSet<>();
		
		if (Type.equals("Drive")){
			if (concatenatedPath.length >= 1){
				throw new IllegalFileOperation();
			} else {
				if (driveMap.isEmpty()){
					Drive drive = new Drive(Name, "/" +  Name, 0, Type);
					driveMap.put(Name, drive);
					return true;
				} else {
					if (driveMap.containsKey(Name)){
						throw new FileAlreadyExistsException(parentPath);
					} else {
						Drive drive = new Drive(Name, "/" +  Name, 0, Type);
						driveMap.put(Name, drive);
						return true;
					}
				}				
			}
		} else if (Type.equals("Folder")){
			
			return pathTraversal(Type, null, driveHolder, folderHolder, concatenatedPath, getDrive, getZip,
					getFolder, getText, Name, typeString);
			
		} else if (Type.equals("Text")){
			System.out.print("Please enter the text content: ");
			String Content = scanner.next();
			System.out.println();
			
			return pathTraversal(Type, Content, driveHolder, folderHolder, concatenatedPath, getDrive, getZip,
					getFolder, getText, Name, typeString);			
			
		} else {
			
			return pathTraversal(Type, null, driveHolder, folderHolder, concatenatedPath, getDrive, getZip,
					getFolder, getText, Name, typeString);
		}
	}
	
	// Method to update size during file operation
	private static void updateSize(Set<Drive> driveHolder, Set<Folder> folderHolder, int size){
		
		for (Drive drive : driveHolder){
			drive.putSize(size);
		}
		
		for (Folder folder : folderHolder){
			folder.putSize(size);
		}
	}
	
	// Method to be used during delete and write to file operation
	public static void operationUpdate(String path, int operation, String content){
		try {
			String[] concatenatedPath = path.split("/");
			if (checkPath(concatenatedPath, operation, content).isTrue){
				System.out.println("Operation successful");
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Method to perform move operation
	public static void moveOperation(String source, String destination, int operation){
		try {
			String[] concatenatedPath = source.split("/");
			OperationFolder of1 = checkPath(concatenatedPath, operation, null);
			if (of1.isTrue){
				concatenatedPath = destination.split("/");
				OperationFolder of2 = checkPath(concatenatedPath, operation, null);
				if (of2.isTrue){
					if (source.equals(destination)){
						throw new FileAlreadyExistsException(source);
					} else {
						operateMove(of1, of2);
						System.out.println("File Operation Performed Successfully");
					}				
				} 
			} 
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Path traversal method used to validate any file operation exceptions
	private static boolean pathTraversal (String Type, String Content, Set<Drive> driveHolder,
			Set<Folder> folderHolder, String[] concatenatedPath, Drive getDrive, Zip getZip,
			Folder getFolder, Text getText, String Name, String typeString) throws FileNotFoundException, IllegalFileOperation{
		if (driveMap.isEmpty()){
			throw new FileNotFoundException();
		} else {				
			for (int i = 1; i < concatenatedPath.length; i++){
				if (i == 1){
					if (driveMap.containsKey(concatenatedPath[i])){
						getDrive =  driveMap.get(concatenatedPath[i]);
						typeString = getDrive.getType();
						if (Type.equals("Text") || Type.equals("Zip")){
							driveHolder.add(getDrive);
						}						
					} else {
						throw new FileNotFoundException();
					}
				} else {
					if (typeString.equals("Drive")){
						if (!getDrive.folderMap.isEmpty()){
							if (getDrive.folderMap.containsKey(concatenatedPath[i])){
								getFolder = getDrive.folderMap.get(concatenatedPath[i]);
								typeString = getFolder.getType();
								if (Type.equals("Text")|| Type.equals("Zip")){
									folderHolder.add(getFolder);
								}								
								continue;
							}
						}
						if (!getDrive.textMap.isEmpty()){
							if (getDrive.textMap.containsKey(concatenatedPath[i])){
								getText = getDrive.textMap.get(concatenatedPath[i]);
								typeString = getText.getType();
								continue;
							}								
						}
						if (!getDrive.zipMap.isEmpty()){
							if (getDrive.zipMap.containsKey(concatenatedPath[i])){
								getZip = getDrive.zipMap.get(concatenatedPath[i]);
								typeString = getZip.getType();
								continue;	
							}								
						}
						throw new FileNotFoundException();
					} else if (typeString.equals("Folder")){
						if (!getFolder.folderMap.isEmpty()){
							if (getFolder.folderMap.containsKey(concatenatedPath[i])){
								getFolder = getFolder.folderMap.get(concatenatedPath[i]);
								typeString = getFolder.getType();
								if (Type.equals("Text") || Type.equals("Zip")){
									folderHolder.add(getFolder);
								}
								continue;
							}
						}
						if (!getFolder.textMap.isEmpty()){
							if (getFolder.textMap.containsKey(concatenatedPath[i])){
								getText = getFolder.textMap.get(concatenatedPath[i]);
								typeString = getText.getType();
								continue;
							}								
						}
						if (!getFolder.zipMap.isEmpty()){
							if (getFolder.zipMap.containsKey(concatenatedPath[i])){
								getZip = getFolder.zipMap.get(concatenatedPath[i]);
								typeString = getZip.getType();
								continue;	
							}								
						}
						throw new FileNotFoundException();
					} else if (typeString.equals("Text")
							|| typeString.equals("Zip")){
						throw new FileNotFoundException();
					}
				}
			}
			if (typeString.equals("Text")
					|| typeString.equals("Zip")){
				throw new IllegalFileOperation();
			} else {
				if (Type.equals("Text")){
					if (typeString.equals("Drive")){
						Text text = new Text(Name, getDrive.getPath() + "/" +  Name, 0, Type, Content);
						text.putSize(Content.length());
						getDrive.textMap.put(Name, text);
						updateSize(driveHolder, folderHolder, Content.length());
						return true;
					} else {
						Text text = new Text(Name, getFolder.getPath() + "/" +  Name, 0, Type, Content);
						text.putSize(Content.length());
						getFolder.textMap.put(Name, text);
						updateSize(driveHolder, folderHolder, Content.length());
						return true;
					}
				} else if (Type.equals("Folder")){
					if (typeString.equals("Drive")){
						Folder folder = new Folder(Name, getDrive.getPath() + "/" +  Name, 0, Type);
						getDrive.folderMap.put(Name, folder);
						return true;
					} else {
						Folder folder = new Folder(Name, getFolder.getPath() + "/" +  Name, 0, Type);
						getFolder.folderMap.put(Name, folder);
						return true;
					}
				} else {
					Zip zip;
					if (typeString.equals("Drive")){
						if (!getDrive.folderMap.isEmpty()){
							if (getDrive.folderMap.containsKey(Name)){
								zip = new Zip(Name, getDrive.getPath()+ "/" +Name + ".zip", 
										getDrive.folderMap.get(Name).getSize(), Type, 
										getDrive.folderMap.get(Name), null);
								zip.putSize();
								getDrive.zipMap.put(Name + ".zip", zip);
								updateSize(driveHolder, folderHolder, zip.getSize());
								return true;
							}							
						}
						if (!getDrive.textMap.isEmpty()){
							if (getDrive.textMap.containsKey(Name)){
								zip = new Zip(Name, getDrive.getPath()+ "/" + Name + ".zip",
										getDrive.textMap.get(Name).getSize(), Type,
										null, getDrive.textMap.get(Name));
								zip.putSize();
								getDrive.zipMap.put(Name + ".zip", zip);
								updateSize(driveHolder, folderHolder, zip.getSize());
								return true;
							}
						}
						throw new FileNotFoundException();
					} else {					
						if (!getFolder.folderMap.isEmpty()){
							if (getFolder.folderMap.containsKey(Name)){
								zip = new Zip(Name, getFolder.getPath()+ "/" + Name + ".zip", 
										getFolder.folderMap.get(Name).getSize(), Type, 
										getFolder.folderMap.get(Name), null);
								zip.putSize();
								getFolder.zipMap.put(Name + ".zip", zip);
								updateSize(driveHolder, folderHolder, zip.getSize());
								return true;
							}							
						}
						if (!getFolder.textMap.isEmpty()){
							if (getFolder.textMap.containsKey(Name)){
								zip = new Zip(Name, getFolder.getPath()+ "/" + Name + ".zip",
										getFolder.textMap.get(Name).getSize(), Type,
										null, getFolder.textMap.get(Name));
								zip.putSize();
								getFolder.zipMap.put(Name + ".zip", zip);
								updateSize(driveHolder, folderHolder, zip.getSize());
								return true;
							}
						}
						throw new IllegalFileOperation();
					}
				}
				
			}
		}
	}
	
	// Path Validation for Delete, Write To File and Move Operations
	private static OperationFolder checkPath(String[] concatenatedPath, int operation, String Content) throws FileNotFoundException, TextNotFound{
			
		OperationFolder of = new OperationFolder();
		
		if (driveMap.isEmpty()){
			throw new FileNotFoundException();
		} else {
			for (int i = 1; i < concatenatedPath.length; i++){
				if (i == 1){
					if (driveMap.containsKey(concatenatedPath[i])){
						of.getDrive =  driveMap.get(concatenatedPath[i]);
						of.typeString = of.getDrive.getType();
						of.driveHolder.add(of.getDrive);
						if (i == concatenatedPath.length - 2){
							of.parentDrive = of.getDrive;
						}
					} else {
						throw new FileNotFoundException();
					}
				} else {
					if (of.typeString.equals("Drive")){
						if (!of.getDrive.folderMap.isEmpty()){
							if (of.getDrive.folderMap.containsKey(concatenatedPath[i])){
								of.getFolder = of.getDrive.folderMap.get(concatenatedPath[i]);
								of.typeString = of.getFolder.getType();
								of.folderHolder.add(of.getFolder);
								if (i == concatenatedPath.length - 2){
									of.parentFolder = of.getFolder;
								}
								continue;
							}
						}
						if (!of.getDrive.textMap.isEmpty()){
							if (of.getDrive.textMap.containsKey(concatenatedPath[i])){
								of.getText = of.getDrive.textMap.get(concatenatedPath[i]);
								of.typeString = of.getText.getType();
								continue;
							}								
						}
						if (!of.getDrive.zipMap.isEmpty()){
							if (of.getDrive.zipMap.containsKey(concatenatedPath[i])){
								of.getZip = of.getDrive.zipMap.get(concatenatedPath[i]);
								of.typeString = of.getZip.getType();
								continue;	
							}								
						}
						throw new FileNotFoundException();
					} else if (of.typeString.equals("Folder")){
						if (!of.getFolder.folderMap.isEmpty()){
							if (of.getFolder.folderMap.containsKey(concatenatedPath[i])){
								of.getFolder = of.getFolder.folderMap.get(concatenatedPath[i]);
								of.typeString = of.getFolder.getType();
								of.folderHolder.add(of.getFolder);
								if (i == concatenatedPath.length - 2){
									of.parentFolder = of.getFolder;
								}
								continue;
							}
						}
						if (!of.getFolder.textMap.isEmpty()){
							if (of.getFolder.textMap.containsKey(concatenatedPath[i])){
								of.getText = of.getFolder.textMap.get(concatenatedPath[i]);
								of.typeString = of.getText.getType();
								continue;
							}								
						}
						if (!of.getFolder.zipMap.isEmpty()){
							if (of.getFolder.zipMap.containsKey(concatenatedPath[i])){
								of.getZip = of.getFolder.zipMap.get(concatenatedPath[i]);
								of.typeString = of.getZip.getType();
								continue;	
							}								
						}
						throw new FileNotFoundException();
					} else if (of.typeString.equals("Text")
							|| of.typeString.equals("Zip")){
						throw new FileNotFoundException();
					}
				}
			}
			
			if (operation == 2){
				
				int size = 0;
				
				if (of.typeString.equals("Text")){
					size = -1 * of.getText.getSize();				
					if (of.getFolder != null){
						of.getFolder.textMap.remove(of.getText.getName());
					} else {
						of.getDrive.textMap.remove(of.getText.getName());
					}
					of.getText = null;
					updateSize(of.driveHolder, of.folderHolder, size);
					of.isTrue = true;
					return of;
				} else if (of.typeString.equals("Zip")){
					size = -1 * of.getZip.getSize();				
					if (of.getFolder != null){
						of.getFolder.zipMap.remove(of.getZip.getName());
					} else {
						of.getDrive.zipMap.remove(of.getZip.getName());
					}
					of.getZip = null;
					updateSize(of.driveHolder, of.folderHolder, size);
					of.isTrue = true;
					return of;
				} else if (of.typeString.equals("Drive")){
					driveMap.remove(of.getDrive.getName());
					of.getDrive = null;
					of.isTrue = true;
					return of;
				} else {
					size = -1 * of.getFolder.getSize();
					if (of.parentFolder != null){
						of.parentFolder.folderMap.remove(of.getFolder.getName());
					} else {
						of.parentDrive.folderMap.remove(of.getFolder.getName());
					}
					of.folderHolder.remove(of.getFolder);
					of.getFolder = null;
					updateSize(of.driveHolder, of.folderHolder, size);
					of.isTrue = true;
					return of;
				}
				
			} else if (operation == 4){
				
				if (!of.typeString.equals("Text")){
					throw new TextNotFound();
				} else {
					int size = of.getText.Size - Content.length();
					of.getText.Size = Content.length();
					if (size < 0){
						updateSize(of.driveHolder, of.folderHolder, Math.abs(size));
					} else {
						updateSize(of.driveHolder, of.folderHolder, -1 * size);
					}
					
					of.isTrue = true;
					return of;
				}
			
			} else {
				of.isTrue = true;
				return of;
			}
			
			
		}
		
	}
	
	// Method that validates the move method and performs actual move operation
	private static void operateMove(OperationFolder of1, OperationFolder of2) throws IllegalFileOperation{
		if (of1.typeString.equals("Drive")
				|| (of2.typeString.equals("Text"))|| of2.typeString.equals("Zip")){
			throw new IllegalFileOperation();
		} else {
			if (of1.typeString.equals("Folder")){
				if (of2.getFolder != null){
					if (of2.folderHolder.contains(of1.getFolder)){
						throw new IllegalFileOperation();
					} else {
						if (of1.parentFolder != null){
							of1.parentFolder.folderMap.remove(of1.getFolder.getName());
							of1.folderHolder.remove(of1.getFolder);
							updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getFolder.getSize());							
						} else {
							of1.parentDrive.folderMap.remove(of1.getFolder.getName());
							of1.folderHolder.remove(of1.getFolder);
							updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getFolder.getSize());
						}
						if (of2.getFolder.folderMap.containsKey(of1.getFolder.getName())){
								int size = of1.getFolder.getSize() - of2.getFolder.folderMap.get(of1.getFolder.getName()).getSize();
								updateSize(of2.driveHolder, of2.folderHolder, size);
						} else {
							of2.getFolder.folderMap.put(of1.getFolder.getName(), of1.getFolder);
							updateSize(of2.driveHolder, of2.folderHolder, of1.getFolder.getSize());
						}
					}
				} else {
					if (of1.parentFolder != null){
						of1.parentFolder.folderMap.remove(of1.getFolder.getName());
						of1.folderHolder.remove(of1.getFolder);
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getFolder.getSize());
					} else {
						of1.parentDrive.folderMap.remove(of1.getFolder.getName());
						of1.folderHolder.remove(of1.getFolder);
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getFolder.getSize());						
					}
					if (of2.getDrive.folderMap.containsKey(of1.getFolder.getName())){
						int size = of1.getFolder.getSize() - of2.getDrive.folderMap.get(of1.getFolder.getName()).getSize();
						updateSize(of2.driveHolder, of2.folderHolder, size);
					} else {
						of2.getDrive.folderMap.put(of1.getFolder.getName(), of1.getFolder);
						updateSize(of2.driveHolder, of2.folderHolder, of1.getFolder.getSize());
					}
				}
				
			} else if (of1.typeString.equals("Text")){
				if (of2.getFolder != null){
						if (of1.parentFolder != null){
							of1.parentFolder.textMap.remove(of1.getText.getName());
							updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getText.getSize());							
						} else {
							of1.parentDrive.textMap.remove(of1.getText.getName());
							updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getText.getSize());
						}
						if (of2.getFolder.textMap.containsKey(of1.getText.getName())){
								int size = of1.getText.getSize() - of2.getFolder.textMap.get(of1.getText.getName()).getSize();
								updateSize(of2.driveHolder, of2.folderHolder, size);
						} else {
							of2.getFolder.textMap.put(of1.getText.getName(), of1.getText);
							updateSize(of2.driveHolder, of2.folderHolder, of1.getText.getSize());
						}
				} else {
					if (of1.parentFolder != null){
						of1.parentFolder.textMap.remove(of1.getText.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getText.getSize());
					} else {
						of1.parentDrive.textMap.remove(of1.getText.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getText.getSize());						
					}
					if (of2.getDrive.textMap.containsKey(of1.getText.getName())){
						int size = of1.getText.getSize() - of2.getDrive.textMap.get(of1.getText.getName()).getSize();
						updateSize(of2.driveHolder, of2.folderHolder, size);
					} else {
						of2.getDrive.textMap.put(of1.getText.getName(), of1.getText);
						updateSize(of2.driveHolder, of2.folderHolder, of1.getText.getSize());
					}
				}
			} else {
				if (of2.getFolder != null){
					if (of1.parentFolder != null){
						of1.parentFolder.zipMap.remove(of1.getZip.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getZip.getSize());							
					} else {
						of1.parentDrive.zipMap.remove(of1.getZip.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getZip.getSize());
					}
					if (of2.getFolder.zipMap.containsKey(of1.getZip.getName())){
							int size = of1.getZip.getSize() - of2.getFolder.zipMap.get(of1.getZip.getName()).getSize();
							updateSize(of2.driveHolder, of2.folderHolder, size);
					} else {
						of2.getFolder.zipMap.put(of1.getZip.getName(), of1.getZip);
						updateSize(of2.driveHolder, of2.folderHolder, of1.getZip.getSize());
					}
				} else {
					if (of1.parentFolder != null){
						of1.parentFolder.zipMap.remove(of1.getZip.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getZip.getSize());
					} else {
						of1.parentDrive.zipMap.remove(of1.getZip.getName());
						updateSize(of1.driveHolder, of1.folderHolder, -1 * of1.getZip.getSize());						
					}
					if (of2.getDrive.zipMap.containsKey(of1.getZip.getName())){
						int size = of1.getZip.getSize() - of2.getDrive.zipMap.get(of1.getZip.getName()).getSize();
						updateSize(of2.driveHolder, of2.folderHolder, size);
					} else {
						of2.getDrive.zipMap.put(of1.getZip.getName(), of1.getZip);
						updateSize(of2.driveHolder, of2.folderHolder, of1.getZip.getSize());
					}
				}
			}
				
		}
	}
}
