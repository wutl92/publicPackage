package sample.file;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-23 17:32
 */
public class FileLog {
    private int packindex;
    private String packpath;
    private String fileName;
    private int compIndex;
    private String compSvn;
    private String compLocal;

    public int getCompIndex() {
        return compIndex;
    }

    public void setCompIndex(int compIndex) {
        this.compIndex = compIndex;
    }

    public String getCompSvn() {
        return compSvn;
    }

    public void setCompSvn(String compSvn) {
        this.compSvn = compSvn;
    }

    public String getCompLocal() {
        return compLocal;
    }

    public void setCompLocal(String compLocal) {
        this.compLocal = compLocal;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getPackindex() {
        return packindex;
    }

    public void setPackindex(int packindex) {
        this.packindex = packindex;
    }

    public String getPackpath() {
        return packpath;
    }

    public void setPackpath(String packpath) {
        this.packpath = packpath;
    }
}
