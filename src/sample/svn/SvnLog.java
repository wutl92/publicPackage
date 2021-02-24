package sample.svn;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-23 11:55
 */
public class SvnLog {
    private int svnindex;
    private String svnpath;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSvnindex() {
        return svnindex;
    }

    public void setSvnindex(int svnindex) {
        this.svnindex = svnindex;
    }

    public String getSvnpath() {
        return svnpath;
    }

    public void setSvnpath(String svnpath) {
        this.svnpath = svnpath;
    }
}
