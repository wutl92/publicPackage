package sample.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-22 18:00
 */
public class FileObj {
    public static String TOPDIR = "";
    public static String TOTOPDIR = "";
    private String fileName;
    private String filePath;
    private String fileNewPath;
    private Date updateTime;
    private File file;
    private static List<String> filterNameList;
    private static List<String> svnFilePathList;

    private String copyMessage;
    /**
     * 无后缀名字
     */
    private String fileNameWzh;

    public FileObj(File file) {
        this.file = file;
        long l = file.lastModified();
        this.updateTime = new Date(l);
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath().replace(File.separator + this.fileName, "");
    }

    public File getNewFile() {
        String localPath = this.filePath.replace(TOPDIR, TOTOPDIR);
        File file = new File(localPath + File.separator + fileName);
        return file;
    }

    public boolean copyFile() {
        String fileName = file.getName();
        int i1 = fileName.lastIndexOf(".");
        fileName = fileName.substring(0, i1);
        if (!filterNameList.contains(fileName)) {
            return false;
        }
        return localCopy(file, getNewFile());
    }

    private boolean localCopy(File file, File newFile) {
        FileUtil.mkDir(newFile.getAbsolutePath());
        try {
            FileUtil.copyFileUsingFileStreams(file.getAbsolutePath(), newFile.getAbsolutePath());
            this.setFileNewPath(newFile.getAbsolutePath());
            this.setCopyMessage("copy file[" + file.getAbsolutePath() + "] to [" + newFile.getAbsolutePath() + "]");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFileNameWzh() {
        String fileName = file.getName();
        int i1 = fileName.lastIndexOf(".");
        fileName = fileName.substring(0, i1);
        return fileName;
    }

    public void setFileNameWzh(String fileNameWzh) {
        this.fileNameWzh = fileNameWzh;
    }

    /**
     * 父类顶层结构
     *
     * @param topDir
     */
    public static void setTopDir(String topDir, String toTopDir) {
        FileObj.TOPDIR = topDir;
        FileObj.TOTOPDIR = toTopDir;
    }

    public String getFileNewPath() {
        return fileNewPath;
    }

    public void setFileNewPath(String fileNewPath) {
        this.fileNewPath = fileNewPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCopyMessage() {
        return copyMessage;
    }

    public void setCopyMessage(String copyMessage) {
        this.copyMessage = copyMessage;
    }

    public static List<String> getFilterNameList() {
        return filterNameList;
    }

    public static void setFilterNameList(List<String> filterNameList) {
        FileObj.filterNameList = filterNameList;
    }

    public static List<String> getSvnFilePathList() {
        return svnFilePathList;
    }

    public static void setSvnFilePathList(List<String> svnFilePathList) {
        FileObj.svnFilePathList = svnFilePathList;
    }
}
