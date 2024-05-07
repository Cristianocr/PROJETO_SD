    package edu.ufp.inf.sd.rmi._06_visitor.server;

public class ConcreteElementFolderMagazinesImpl implements ElementFolderRI {

    private static SingletonFolderOperationsBooks stateBooksFolder;

    public ConcreteElementFolderMagazinesImpl() {
    }

    public ConcreteElementFolderMagazinesImpl(String booksFolder) {
        super();
    }

    @Override
    public Object acceptVisitor(VisitorFolderOperationI visitor) throws Exception{
        return visitor.visitConcreteElementStateMagazines(this);
    }

    public SingletonFolderOperationsBooks getStateBooksFolder(){
        return stateBooksFolder;
    }
}
