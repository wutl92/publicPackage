package sample.data;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description: mysql数据库连接
 * @date 2021-5-11 14:54
 */
public class MysqlJdbcUtil {

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connections = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/svnpub?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
            String user = "root";
            String password = "123456";
            //2.建立连接
            connections = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回连接对象
        return connections;
    }

    /**
     * 查询项目
     *
     * @return
     */
    public static List<SvnPro> selectSvnProList() {
        List<SvnPro> svnProList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            String query = " select id , xmmc,svnpath,lrsj,pxh,xmdm from svn_pro order by pxh asc ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String xmmc = resultSet.getString(2);
                String svnpath = resultSet.getString(3);
                Date lrsj = resultSet.getTimestamp(4);
                Integer pxh = resultSet.getInt(5);
                String xmdm = resultSet.getString(6);
                SvnPro svnPro = new SvnPro();
                svnPro.setId(id);
                svnPro.setXmmc(xmmc);
                svnPro.setXmdm(xmdm);
                svnPro.setSvnpath(svnpath);
                svnPro.setLrsj(lrsj);
                svnPro.setPxh(pxh);
                svnProList.add(svnPro);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception throwables) {
                }
            }
        }
        return svnProList;
    }


    /**
     * 查询项目
     *
     * @return
     */
    public static List<SvnPub> selectSvnPubList(String xmdmSelect) {
        List<SvnPub> svnPubList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            String query = " select id , ksbbh,jsbbh,fbxm,fbsj,xmdm from svn_pub where xmdm = '" + xmdmSelect + "' order by fbsj desc ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String fbxm = resultSet.getString("fbxm");
                String jsbbh = resultSet.getString("jsbbh");
                Date fbsj = resultSet.getTimestamp("fbsj");
                String ksbbh = resultSet.getString("ksbbh");
                String xmdm = resultSet.getString("xmdm");
                SvnPub svnPub = new SvnPub();
                svnPub.setId(id);
                svnPub.setFbxm(fbxm);
                svnPub.setXmdm(xmdm);
                svnPub.setFbsj(fbsj);
                svnPub.setJsbbh(jsbbh);
                svnPub.setKsbbh(ksbbh);
                svnPubList.add(svnPub);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception throwables) {
                }
            }
        }
        return svnPubList;
    }

    public static void insertSvnPub(SvnPub svnPub) {
        svnPub.setId(System.currentTimeMillis()+"");
        Connection connection = null;
        try {
            String fbsjTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            System.out.println(fbsjTime);
            connection = getConnection();
            Statement statement = connection.createStatement();
            String insetSql = "INSERT INTO `svnpub`.`svn_pub`(`id`, `ksbbh`, `jsbbh`, `fbsj`, `fbxm`, `xmdm`) VALUES " +
                    "   ('"+svnPub.getId()+"', '"+svnPub.getKsbbh()+"', '"+svnPub.getJsbbh()+"', '"+fbsjTime+"', '"+svnPub.getFbxm()+"', '"+svnPub.getXmdm()+"');";
            boolean execute = statement.execute(insetSql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception throwables) {
                }
            }
        }
    }
}
