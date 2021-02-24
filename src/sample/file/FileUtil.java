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
        if(!file.isFile()){
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
        Date begin = simpleDateFormat.parse("2021-02-11 00:00:00");
        Date end = simpleDateFormat.parse("2021-02-23 12:00:00");
        List<FileObj> fileObjList = new ArrayList<>();
        FileUtil.showCopyFiles(fileObjList,new File("E:\\ideaout\\chasstage\\out\\artifacts\\chasstage_war_exploded"), begin, end);
        FileObj.setTopDir("E:\\ideaout\\chasstage\\out\\artifacts\\chasstage_war_exploded","d:\\Users\\htk\\Desktop\\logs(7)\\chasstage");
        for (int i = 0; i < fileObjList.size(); i++) {
            FileObj fileObj = fileObjList.get(i);
            fileObj.copyFile();
        }
        System.out.println("copy "+ fileObjList.size());
    }
}
