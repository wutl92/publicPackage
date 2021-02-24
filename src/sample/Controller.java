package sample;

import com.browniebytes.javafx.control.DateTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import sample.file.FileLog;
import sample.file.FileObj;
import sample.file.FileUtil;
import sample.svn.SvnLog;
import sample.svn.SvnUtil;
import sample.sys.SysUtil;

import java.io.File;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    @FXML
    private Button svnConnBtn;
    @FXML
    private DateTimePicker beginTime;
    @FXML
    private DateTimePicker endTime;
    @FXML
    private TextField svnUrl;
    @FXML
    private TextField beginVersion;
    @FXML
    private TextField endVersion;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TableView svnlogTable;
    @FXML
    private TableColumn svnindex;
    @FXML
    private TableColumn svnpath;
    @FXML
    private ProgressBar svnProgress;
    @FXML
    private ProgressBar packProgress;
    @FXML
    private TextField srcPath;
    @FXML
    private TextField destPath;
    @FXML
    private Button packBtn;

    @FXML
    private TableColumn packindex;
    @FXML
    private TableColumn packpath;

    @FXML
    private TableView packTable;

    private List<String> fileNameList = new ArrayList<>();

    private int svnLogSize = 1;

    private int csize = 1;

    private Thread svnLogThread = null;

    private int fileTotal = 1;

    private int cfileSize = 1;

    /**
     * 获取svn日志
     */
    public void svnLog() {
        svnLogThread();
    }

    public void packPublic() {
        packPublicThread();
    }

    public void packPublicThread() {
        new Thread(() -> {
            this.packTable.setItems(FXCollections.observableArrayList());
            this.packTable.refresh();
            packProgress.setProgress(0.1);
            fileTotal = 1;
            cfileSize = 1;
            packprogressThread();
            String beginTime = this.beginTime.dateTimeProperty().get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endTime = this.endTime.dateTimeProperty().get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            try {
                Date beginDate = FileUtil.simpleDateFormat.parse(beginTime);
                Date endTDate = FileUtil.simpleDateFormat.parse(endTime);
                String srcPathText = srcPath.getText();
                String destPathText = destPath.getText();
                List<FileObj> fileObjList = new ArrayList<>();
                FileUtil.showCopyFiles(fileObjList, new File(srcPathText), beginDate, endTDate);
                FileObj.setTopDir(srcPathText, destPathText);
                this.packindex.setCellValueFactory(new PropertyValueFactory("packindex"));//映射
                this.packpath.setCellValueFactory(new PropertyValueFactory("packpath"));
                this.packTable.setEditable(true);//表格设置为可编辑
                this.packpath.setCellFactory(TextFieldTableCell.forTableColumn());
                ObservableList<FileLog> list = FXCollections.observableArrayList();
                int index = 1;
                for (int i = fileObjList.size() - 1; i > -1; i--) {
                    FileObj fileObj = fileObjList.get(i);
                    boolean b = fileObj.copyFile();
                    if(b){
                        FileLog tableInfo = new FileLog();
                        tableInfo.setPackindex(index + "");
                        tableInfo.setPackpath(fileObj.getFileNewPath());
                        list.add(tableInfo);
                        index++;
                        this.packTable.setItems(list);
                        this.packTable.refresh();
                    }
                }
                packProgress.setProgress(1.0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void svnLogThread() {
        new Thread(() -> {
            this.svnlogTable.setItems(FXCollections.observableArrayList());
            this.svnlogTable.refresh();
            fileNameList = new ArrayList<>();
            svnProgress.setProgress(0.1);
            csize = 1;
            svnLogSize = 1;
            svnprogressThread();
            String svnUrlText = this.svnUrl.getText();
            String usernameText = this.username.getText();
            String passwordText = this.password.getText();
            SvnUtil.setSvnLogInfo(svnUrlText, usernameText, passwordText);
            String beginVersionText = this.beginVersion.getText();
            String versionText = this.endVersion.getText();
            long beginv = -1;
            long endv = -1;
            if (SysUtil.isNotEmpty(beginVersionText)) {
                beginv = Long.parseLong(beginVersionText);
            }
            if (SysUtil.isNotEmpty(versionText)) {
                endv = Long.parseLong(versionText);
            }
            try {
                List<String> stringList = SvnUtil.filterCommitHistory(beginv, endv);
                svnLogSize = stringList.size();
                this.svnindex.setCellValueFactory(new PropertyValueFactory("svnindex"));//映射
                this.svnpath.setCellValueFactory(new PropertyValueFactory("svnpath"));
                svnlogTable.setEditable(true);//表格设置为可编辑
                svnpath.setCellFactory(TextFieldTableCell.forTableColumn());
                csize = 1;
                int index = 1;
                ObservableList<SvnLog> list = FXCollections.observableArrayList();
                for (int i = stringList.size() - 1; i > -1; i--) {
                    SvnLog svnLog = new SvnLog();
                    String filePath = stringList.get(i);
                    String[] split = filePath.split("/");
                    String fileName = split[split.length - 1];
                    int i1 = fileName.lastIndexOf(".");
                    if (i1 > -1) {
                        try {
                            fileName = fileName.substring(0, i1);
                            if (!fileNameList.contains(fileName)) {
                                fileNameList.add(fileName);
                                svnLog.setSvnpath(filePath);
                                svnLog.setSvnindex(index + "");
                                index++;
                                list.add(svnLog);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    csize++;
                    svnlogTable.setItems(list);
                    svnlogTable.refresh();
                }
                FileObj.setFilterNameList(fileNameList);
                csize = stringList.size() + 100;
                svnProgress.setProgress(1.0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * svn进度条 进程线程
     */
    private void svnprogressThread() {
        this.svnLogThread = new Thread(() -> {
            while (csize != 1 && csize < svnLogSize) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                double pro = csize / svnLogSize;
                if (pro > 0.1) {
                    svnProgress.setProgress(pro);
                }
            }
        });
        svnLogThread.start();
    }


    /**
     * svn进度条 进程线程
     */
    private void packprogressThread() {
        new Thread(() -> {
            while (cfileSize != 1 && cfileSize < fileTotal) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                double pro = cfileSize / fileTotal;
                if (pro > 0.1) {
                    packProgress.setProgress(pro);
                }
            }
        }).start();

    }
}
