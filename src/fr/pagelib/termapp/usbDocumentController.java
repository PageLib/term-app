package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.FileItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class UsbDocumentController extends PageController {

    @FXML TableView<FileItem> table;
    @FXML TableColumn<FileItem, String> nameColumn;
    @FXML Label pathLabel;
    @FXML Button parentButton;

    private String currentDirectory;

    private ObservableList<FileItem> fileList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<FileItem, String>("name"));
        table.setItems(fileList);
        showDirectory(Paths.get("D:\\"));
    }

    public void folderClicked(){
        FileItem selectedFile = table.getSelectionModel().getSelectedItem();
        // newValue may be null when the list is cleared (in that case do not try to download the document)
        if (selectedFile != null) {
            if(table.getSelectionModel().getSelectedIndex() != -1) {
                table.getSelectionModel().select(null);
                Path path = Paths.get(selectedFile.getFullPath());
                if (selectedFile.isDirectory()) {
                    showDirectory(path);
                } else if (selectedFile.isPDF()) {
                    mainController.setCurrentDocumentPath(selectedFile.getFullPath());
                    mainController.showPage(MainController.Page.JOB_SETTINGS);
                }
            }
        }
    }
    public void showDirectory(Path path){
        try{
            DirectoryStream<Path> dir = Files.newDirectoryStream(path);
            table.getSelectionModel().clearSelection();
            if(path.getRoot().equals(path)){
                parentButton.setVisible(false);
            }
            else{
                parentButton.setVisible(true);
            }
            fileList.clear();
            for(Path file:dir){
                FileItem fileItem = new FileItem(file);
                fileList.add(fileItem);
            }
            currentDirectory = path.toString();
            pathLabel.setText(currentDirectory);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public void parentFolder(){
        //TODO securite pour pas plus remonter que la racine de la clé USB
        Path path = Paths.get(currentDirectory);
        Path parent = path.getParent();
        showDirectory(parent);
    }
     public void reset(){
        showDirectory(Paths.get("D:\\"));
    }
}