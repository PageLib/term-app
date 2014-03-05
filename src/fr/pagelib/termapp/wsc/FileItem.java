package fr.pagelib.termapp.wsc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileItem {
    private String fullPath;
    private String name;

    public FileItem(String fullPath) {
        this.fullPath = fullPath;
    }

    private boolean isDirectory;
    private boolean isPDF;

    public FileItem(Path file){
        fullPath = file.toString();

        // test if this is a directory
        if(Files.isDirectory(file)){
            isDirectory = true;
        }
        else{
            isDirectory = false;
            int lastIndex = fullPath.lastIndexOf(".");
            String extension = fullPath.substring(lastIndex);
                if(extension.equals(".pdf")){
                    isPDF = true;
                }
                else{
                    isPDF = false;
                }
        }

        //set the value
        if(!fullPath.endsWith(File.separator)){
            //Set the value which is displayed
            String value = file.toString();
            int indexOf = value.lastIndexOf(File.separator);
            if(indexOf>0){
                name = value.substring(indexOf+1);
            }
            else{
                name = value;
            }
        }
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isPDF() {
        return isPDF;
    }

    public String getName() {
        return name;
    }
}
