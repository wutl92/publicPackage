package sample.data;

import java.util.Date;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description: 项目发布
 * @date 2021-5-11 14:55
 */
public class SvnPub {
    private String id;
    private String ksbbh;
    private String jsbbh;
    private Date fbsj;
    private String fbxm;
    private String xmdm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKsbbh() {
        return ksbbh;
    }

    public void setKsbbh(String ksbbh) {
        this.ksbbh = ksbbh;
    }

    public String getJsbbh() {
        return jsbbh;
    }

    public void setJsbbh(String jsbbh) {
        this.jsbbh = jsbbh;
    }

    public Date getFbsj() {
        return fbsj;
    }

    public void setFbsj(Date fbsj) {
        this.fbsj = fbsj;
    }

    public String getFbxm() {
        return fbxm;
    }

    public void setFbxm(String fbxm) {
        this.fbxm = fbxm;
    }

    public String getXmdm() {
        return xmdm;
    }

    public void setXmdm(String xmdm) {
        this.xmdm = xmdm;
    }
}
