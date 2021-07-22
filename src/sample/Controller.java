package sample;

import com.browniebytes.javafx.control.DateTimePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.data.MysqlJdbcUtil;
import sample.data.SvnPro;
import sample.data.SvnPub;
import sample.file.FileLog;
import sample.file.FileObj;
import sample.file.FileUtil;
import sample.svn.SvnLog;
import sample.svn.SvnUtil;
import sample.sys.SysUtil;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
    private PasswordField password;
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
    private TextField fileTotalText;
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

    private SvnPro svnPro;
    /**
     * 本地导出的文件集合
     */
    public static List<FileLog> fileLogList = new ArrayList<>();

    /**
     * SVN导出的文件集合
     */
    public static List<SvnLog> svnLogList = new ArrayList<>();

    public void showSvnPath(SvnPro svnPro) {
        this.svnPro = svnPro;
        this.svnUrl.setText(svnPro.getSvnpath());
        this.srcPath.setText(svnPro.getOut());
    }

    public void selectFilePath() {
        DirectoryChooser file = new DirectoryChooser();
        file.setTitle("请选择输出文件夹");
        Window window = this.destPath.getScene().getWindow();
        File newFolder = file.showDialog(window);//这个file就是选择的文件夹了
        this.destPath.setText(newFolder.getAbsolutePath());
       /* FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage  mainStage = (Stage) StageManger.manager.get("mainStage");
        File file = fileChooser.showOpenDialog(mainStage);*/
    }

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
                //FileUtil.showCopyFiles(fileObjList, new File(srcPathText), beginDate, endTDate);
                // 根据SVN提交文件路径查找本地文件。
                FileObj.setTopDir(srcPathText, destPathText);
                FileUtil.findSvnToLocalFile(fileObjList);
                this.packindex.setCellValueFactory(new PropertyValueFactory("packindex"));//映射
                this.packpath.setCellValueFactory(new PropertyValueFactory("packpath"));
                this.packTable.setEditable(true);//表格设置为可编辑
                this.packpath.setCellFactory(TextFieldTableCell.forTableColumn());
                ObservableList<FileLog> list = FXCollections.observableArrayList();
                int index = 1;
                for (int i = 0; i < fileObjList.size(); i++) {
                    FileObj fileObj = fileObjList.get(i);
                    List<File> newfileList = fileObj.copyFile();
                    if (newfileList.size() > 0) {
                        for (int j = 0; j < newfileList.size(); j++) {
                            FileLog tableInfo = new FileLog();
                            File newFile = newfileList.get(j);
                            tableInfo.setPackindex(index);
                            tableInfo.setPackpath(newFile.getAbsolutePath());
                            tableInfo.setFileName(newFile.getName());
                            list.add(tableInfo);
                            index++;
                        }

                    }
                }
                this.packTable.setItems(list);
                this.packTable.refresh();
                fileLogList.addAll(list);
                packProgress.setProgress(1.0);
                int total = FileUtil.getFileTotal(destPathText);
                fileTotalText.setText("" + total);
                SvnPub svnPub = new SvnPub();
                if (this.svnPro != null) {
                    svnPub.setKsbbh(this.beginVersion.getText());
                    svnPub.setJsbbh(this.endVersion.getText());
                    String xmdm = this.svnPro.getXmdm();
                    String xmmc = this.svnPro.getXmmc();
                    svnPub.setXmdm(xmdm);
                    svnPub.setFbxm(xmmc);
                    MysqlJdbcUtil.insertSvnPub(svnPub);
                }
                this.alert("导出成功！");
            } catch (ParseException e) {
                e.printStackTrace();
                this.alert("导出失败：" + e.getMessage());
            }
        }).start();
    }

    public void alert(String msg) {
        //允许在其他线程中弹出UI提示
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText("项目导出");
                alert.setContentText(msg);
                alert.show();
            }
        });

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
                List<String> svnFilePathList = new ArrayList<>();
                for (int i = stringList.size() - 1; i > -1; i--) {
                    SvnLog svnLog = new SvnLog();
                    String filePath = stringList.get(i);
                    String[] split = filePath.split("/");
                    String fileName = split[split.length - 1];
                    int i1 = fileName.lastIndexOf(".");
                    if (i1 > -1) {
                        try {
                            if (!svnFilePathList.contains(filePath)) {
                                fileNameList.add(fileName);
                                svnFilePathList.add(filePath);
                                svnLog.setSvnpath(filePath);
                                svnLog.setSvnindex(index);
                                svnLog.setFileName(fileName);
                                index++;
                                list.add(svnLog);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    csize++;
                }
                svnlogTable.setItems(list);
                svnlogTable.refresh();
                svnLogList.addAll(list);
                FileObj.setFilterNameList(fileNameList);
                FileObj.setSvnFilePathList(svnFilePathList);
                csize = stringList.size() + 100;
                svnProgress.setProgress(1.0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 打开对比窗口
     */
    public void openCompWin() {
        Compwin open = new Compwin();
        try {
            Stage stage = new Stage();
            open.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showpro() {
        Datawin datawin = new Datawin();
        try {
            Stage stage = new Stage();
            datawin.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StageManger.manager.put("main", this);
    }
}
