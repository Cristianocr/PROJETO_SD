package edu.ufp.inf.sd.rmi._06_visitor.server;

public class VisitorsFoldersOperationDeleteFile implements VisitorFolderOperationI{

    private String fileToDelete;

    private String fileToDeleteWithPrefix;

    public VisitorsFoldersOperationDeleteFile() {

    }

    public VisitorsFoldersOperationDeleteFile(String newFolder) {
        super();
    }

    @Override
    public Object visitConcreteElementStateBooks(ElementFolderRI element) {
        return element;
    }

    @Override
    public Object visitConcreteElementStateMagazines(ElementFolderRI element) {
        return element;
    }

    public String getFileToDelete() {
        return fileToDelete;
    }

    public void setFileToDelete(String fileToDelete) {
        this.fileToDelete = fileToDelete;
    }
}
