package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.file.FileLog;
import sample.file.FileObj;
import sample.svn.SvnLog;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-24 13:41
 */
public class CompController implements Initializable {
    @FXML
    public Pane root;
    private Stage stage;
    @FXML
    private TableColumn compIndex;
    @FXML
    private TableColumn compLocal;
    @FXML
    private ProgressBar compProgress;
    @FXML
    private TableView compTable;
    /**
     * 总数
     */
    private int total = 1;
    /**
     * 当前个数
     */
    private int index = 1;


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
        new Thread(() -> {
            try {
                index = 1;
                Thread.sleep(1000);
                compProgress.setProgress(0.10f);
                compProgressThread();
                List<SvnLog> svnLogList = Controller.svnLogList;
                List<FileLog> fileLogList = Controller.fileLogList;
                total = svnLogList.size();
                this.compLocal.setCellValueFactory(new PropertyValueFactory("compLocal"));
                this.compIndex.setCellValueFactory(new PropertyValueFactory("compIndex"));
                this.compTable.setEditable(true);//表格设置为可编辑
                this.compLocal.setCellFactory(TextFieldTableCell.forTableColumn());
                ObservableList<FileLog> list = FXCollections.observableArrayList();
                for (int i = 0; i < fileLogList.size(); i++) {
                    FileLog fileLog = new FileLog();
                    FileLog localFileLog = fileLogList.get(i);
                    String fileName = localFileLog.getFileName();
                    String svnFileName = "";
                    for (int j = 0; j < svnLogList.size(); j++) {
                        SvnLog svnLog = svnLogList.get(j);
                        String svnLogFileName = svnLog.getFileName();
                        if (fileName.equals(svnLogFileName)) {
                            svnFileName = svnLog.getSvnpath();
                        }
                    }
                    fileLog.setCompIndex(i + 1);
                    fileLog.setCompLocal(localFileLog.getPackpath() + "\n" + svnFileName);
                    list.add(fileLog);
                }
                compTable.setItems(list);
                index = -1;
                compProgress.setProgress(1.0);
            } catch (Exception e) {
            }
        }).start();
    }

    /**
     * 获取窗口对象
     *
     * @return
     */
    private Stage getStage() {
        if (stage == null) {
            stage = (Stage) root.getScene().getWindow();
        }
        return stage;
    }


    /**
     * svn进度条 进程线程
     */
    private void compProgressThread() {
        new Thread(() -> {
            while (index > -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                double pro = index / total;
                if (pro > 0.1) {
                    compProgress.setProgress(pro);
                }
            }
        }).start();
    }
}
