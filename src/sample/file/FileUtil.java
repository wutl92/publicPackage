package sample.file;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-22 17:22
 */
public class FileUtil {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * src源代码路径
     */
    public static String SRCPATH = "src/main/java";

    /**
     * resources源代码路径
     */
    public static String RESOURCESPATH = "src/main/resources";

    /**
     * webapp源代码路径
     */
    public static String WEBAPPPATH = "src/main/webapp";


    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public static void copyFileUsingFileStreams(String source, String dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        File destFile = new File(dest);
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    /**
     * 创建文件目录
     *
     * @param path
     */
    public static void mkDir(String path) {
        File file = new File(path);
        //如果文件不存在
        if (!file.isFile()) {
            file = file.getParentFile();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void showCopyFiles(List<FileObj> fileObjList, File file, Date begin, Date end) {
        if (file.isFile()) {
            long l = file.lastModified();
            if (begin != null && end != null) {
                long beginTime = begin.getTime();
                long endTime = end.getTime();
                if (beginTime <= l && l <= endTime) {
                    fileObjList.add(new FileObj(file));
                }
            } else if (begin != null) {
                long beginTime = begin.getTime();
                if (beginTime <= l) {
                    fileObjList.add(new FileObj(file));
                }
            } else if (end != null) {
                long endTime = end.getTime();
                if (l <= endTime) {
                    fileObjList.add(new FileObj(file));
                }
            } else {
                fileObjList.add(new FileObj(file));
            }
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                showCopyFiles(fileObjList, files[i], begin, end);
            }
        }
    }


    public static void main(String[] args) throws Exception {
//        copyFileUsingFileStreams("D:\\Users\\htk\\Desktop\\logs(6)\\mylog\\2021022215(0)face.log","d:\\Users\\htk\\Desktop\\log7\\mylog\\hesss\\2021022215(0)face.log");
//        mkDir("d:\\Users\\htk\\Desktop\\log7\\mylog\\hesss");
     /*   List<FileObj> fileObjList = new ArrayList<>();
        showFiles(fileObjList,new File("d:\\Users\\htk\\Desktop\\logs(6)"), null, null);
        System.out.println(fileObjList);*/
//        mkDir("D:\\Users\\htk\\Desktop\\log7\\mylog\\hesss\\ssss\\sss.txt");
       /* Date begin = simpleDateFormat.parse("2021-02-11 00:00:00");
        Date end = simpleDateFormat.parse("2021-02-23 12:00:00");
        List<FileObj> fileObjList = new ArrayList<>();
        FileUtil.showCopyFiles(fileObjList,new File("E:\\ideaout\\chasstage\\out\\artifacts\\chasstage_war_exploded"), begin, end);
        FileObj.setTopDir("E:\\ideaout\\chasstage\\out\\artifacts\\chasstage_war_exploded","d:\\Users\\htk\\Desktop\\logs(7)\\chasstage");
        for (int i = 0; i < fileObjList.size(); i++) {
            FileObj fileObj = fileObjList.get(i);
            fileObj.copyFile();
        }
        System.out.println("copy "+ fileObjList.size());*/
        List<String> list = new ArrayList<>();
        list.add("/dev/java/baq/baq_4/chasstage/code/trunk/chasstage/src/main/java/com/wckj/chasstage/modules/rygj/entity/ChasRygjSnap.java");
        list.add("/dev/java/baq/baq_4/chasstage/code/trunk/chasstage/src/main/resources/mappings/chasstage/rygj/ChasRygjSnapMapper.xml");
        list.add("/dev/java/baq/baq_4/chasstage/code/trunk/chasstage/src/main/webapp/static/chas/bigscreen/bjfj/images/blue.png");
        FileObj.setSvnFilePathList(list);
        FileObj.TOPDIR = "E:\\project\\shengnei\\chasstage\\out1\\artifacts\\chasstage_war_exploded";
        findSvnToLocalFile(new ArrayList<>());
    }

    public static void findSvnToLocalFile(List<FileObj> fileObjList) {
        List<String> svnFilePathList = FileObj.getSvnFilePathList();
        String file = "";
        for (int i = 0; i < svnFilePathList.size(); i++) {
            String svnFilePath = svnFilePathList.get(i);
            if (svnFilePath.contains(SRCPATH)) {
                String[] srcArr = svnFilePath.split(SRCPATH);
                file = FileObj.TOPDIR + "\\WEB-INF\\classes" + getClassFileName(srcArr[1]);
            }
            if (svnFilePath.contains(RESOURCESPATH)) {
                String[] resArr = svnFilePath.split(RESOURCESPATH);
                file = FileObj.TOPDIR + "\\WEB-INF\\classes" + getClassFileName(resArr[1]);
            }
            if (svnFilePath.contains(WEBAPPPATH)) {
                String[] webArr = svnFilePath.split(WEBAPPPATH);
                file = FileObj.TOPDIR + "\\" + getClassFileName(webArr[1]);

            }
            System.out.println("============================================");
            System.out.println(file);
            if(file.length()>0){
                FileObj fileObj = new FileObj(new File(file));
                fileObjList.add(fileObj);
            }
        }
    }

    public static String getClassFileName(String fileName) {
        String newFileName = fileName.replace(".java", ".class");
        return newFileName;
    }
}
