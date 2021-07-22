package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.data.MysqlJdbcUtil;
import sample.data.SvnPro;
import sample.data.SvnPub;
import sample.file.FileLog;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-5-11 17:09
 */
public class DataController implements Initializable {

    @FXML
    private TableView dataTable;
    @FXML
    private TableColumn pxh;
    @FXML
    private TableColumn xmmc;
    @FXML
    private TableColumn xmdm;
    @FXML
    private TableColumn svnpath;


    @FXML
    private TableView publogTable;

    @FXML
    private TableColumn id;
    @FXML
    private TableColumn fbxm;
    @FXML
    private TableColumn ksbbh;
    @FXML
    private TableColumn jsbbh;
    @FXML
    private TableColumn fbsj;

    public void dataTableClick(MouseEvent event) {
        //当前选中行数
        SvnPro selectedItem = (SvnPro) dataTable.getSelectionModel().getSelectedItem();
        //Checking double click
        if (event.getClickCount() == 2) {
            Controller controller = (Controller) StageManger.manager.get("main");
            controller.showSvnPath(selectedItem);
            Stage stage = (Stage) dataTable.getScene().getWindow();
            stage.close();
        } else {
            List<SvnPub> svnProList = MysqlJdbcUtil.selectSvnPubList(selectedItem.getXmdm());
            this.id.setCellValueFactory(new PropertyValueFactory("id"));
            this.fbxm.setCellValueFactory(new PropertyValueFactory("fbxm"));
            this.ksbbh.setCellValueFactory(new PropertyValueFactory("ksbbh"));
            this.jsbbh.setCellValueFactory(new PropertyValueFactory("jsbbh"));
            this.fbsj.setCellValueFactory(new PropertyValueFactory("fbsj"));
            ObservableList<SvnPub> list = FXCollections.observableArrayList();
            for (int i = 0; i < svnProList.size(); i++) {
                list.add(svnProList.get(i));
            }
            this.publogTable.setItems(list);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //加载表格行点击事件。
        List<SvnPro> svnProList = MysqlJdbcUtil.selectSvnProList();
        this.pxh.setCellValueFactory(new PropertyValueFactory("pxh"));
        this.xmmc.setCellValueFactory(new PropertyValueFactory("xmmc"));
        this.xmdm.setCellValueFactory(new PropertyValueFactory("xmdm"));
        this.svnpath.setCellValueFactory(new PropertyValueFactory("svnpath"));
        ObservableList<SvnPro> list = FXCollections.observableArrayList();
        for (int i = 0; i < svnProList.size(); i++) {
            list.add(svnProList.get(i));
        }
        this.dataTable.setItems(list);
    }
}
