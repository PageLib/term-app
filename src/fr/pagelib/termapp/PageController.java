package fr.pagelib.termapp;

/**
 * Base controller class, holds a reference to the application's MainController
 */
public abstract class PageController {

    protected MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
