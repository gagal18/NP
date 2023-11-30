package kolokvium_1.k_03;

import java.util.ArrayList;
import java.util.Scanner;

class FileNameExistsException extends Exception{
    FileNameExistsException(String file, String folder) {
        super(String.format("There is already a file named "+ file +" in the folder "+folder));
    }
}
interface IFile extends Comparable<IFile> {
    String getFileName();
    long getFileSize();
    void sortBySize();
    String getFileInfo(int f);
    long findLargestFile();

    @Override
    default int compareTo(IFile o) {
        return Long.compare(getFileSize(), o.getFileSize());
    }
}
class File implements IFile{
    private String file_name;
    private long size;

    public File(String file_name, long size) {
        this.file_name = file_name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return file_name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public void sortBySize() {}

    @Override
    public String getFileInfo(int f) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < f; i++) {
            s.append(String.format("%4s", ""));
        }
        s.append(String.format("File name: %10s File size: %10d\n", file_name,size));
        return s.toString();
    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class Folder implements IFile{
    private String folder_name;
    private long size;
    private ArrayList<IFile> files;
    public Folder(String folder_name) {
        this.folder_name = folder_name;
        this.size = 0;
        this.files = new ArrayList<>();
    }
    public Folder(String folder_name, long size) {
        this.folder_name = folder_name;
        this.size = size;
        this.files = new ArrayList<>();
    }
    public void addFile(IFile file) throws FileNameExistsException{
        String filename = file.getFileName();
        for (IFile f: files) {
            if(f.getFileName().equals(filename)){
                throw new FileNameExistsException(filename, folder_name);
            }
        }
        files.add(file);
        size += file.getFileSize();
    }
    @Override
    public String getFileName() {
        return folder_name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public void sortBySize() {
        files.sort(IFile::compareTo);
        for (IFile f: files) {
            f.sortBySize();
        }
    }

    @Override
    public String getFileInfo(int f) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < f; i++) {
            s.append(String.format("%4s", ""));
        }
        s.append(String.format("Folder name: %10s Folder size: %10d\n", folder_name,size));
        files.forEach(file -> s.append(file.getFileInfo(f+1)));
        return s.toString();
    }

    @Override
    public long findLargestFile() {
        return files.stream().mapToLong(i -> i.findLargestFile()).max().orElse(0);
    }
}

class FileSystem{
    private Folder root;

    public FileSystem() {
        this.root = new Folder("root");
    }
    public void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }
    public void sortBySize(){
        root.sortBySize();
    }
    public long findLargestFile() {
        return root.findLargestFile();
    }

    @Override
    public String toString() {
        return root.getFileInfo(0);
    }
}
public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}