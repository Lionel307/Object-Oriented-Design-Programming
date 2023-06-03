package unsw.blackout;

public class File {
    private String fileName;
    private String content;
    private int fileSize;

    public File(String fileName, String content, int fileSize) {
        this.fileName = fileName;
        this.content = content;
        this.fileSize = fileSize;  
    }
    /**
     * 
     * @return name of the file
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * 
     * @return contents of the file
     */
    public String getContent() {
        return content;
    }
    /**
     * 
     * @return size of the file in bytes
     */
    public int getFileSize() {
        return fileSize;
    }
}
