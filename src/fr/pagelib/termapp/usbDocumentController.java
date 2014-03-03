package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.FileItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UsbDocumentController extends PageController {

    @FXML TableView<FileItem> table;
    @FXML TableColumn<FileItem, String> nameColumn;

    private String currentDirectory;

    private ObservableList<FileItem> fileList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<FileItem, String>("name"));
        table.setItems(fileList);
        showDirectory(Paths.get("D:\\"));
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FileItem>() {

            @Override
            public void changed(ObservableValue<? extends FileItem> observable,
                                FileItem oldValue, FileItem newValue) {

                // newValue may be null when the list is cleared (in that case do not try to download the document)
                if(newValue != null){
                    Path path = Paths.get(newValue.getFullPath());
                    if(newValue.isDirectory()){
                        showDirectory(path);
                    }
                    else if(newValue.isPDF()){
                        mainController.setCurrentDocumentPath(newValue.getFullPath());
                        mainController.showPage(MainController.Page.JOB_SETTINGS);
                    }
                }
            }
        });
    }

    public void showDirectory(Path path){
        try{
            DirectoryStream<Path> dir = Files.newDirectoryStream(path);
            fileList.clear();
            for(Path file:dir){
                FileItem fileItem = new FileItem(file);
                fileList.add(fileItem);
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}