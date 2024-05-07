package edu.ufp.inf.sd.rmi._06_visitor.server;

public class VisitorsFoldersOperationCreateFile implements VisitorFolderOperationI {

    private String fileToCreate;

    private String fileToCreateWithPrefix;

    public VisitorsFoldersOperationCreateFile() {

    }

    public VisitorsFoldersOperationCreateFile(String newFolder) {
        super();
    }

    @Override
    public Object visitConcreteElementStateBooks(ElementFolderRI element) {
        return null;
    }

    @Override
    public Object visitConcreteElementStateMagazines(ElementFolderRI element) {
        return element;
    }

    public String getFileToCreate() {
        return fileToCreate;
    }

    public void setFileToCreate(String fileToCreate) {
        this.fileToCreate = fileToCreate;
    }
}
