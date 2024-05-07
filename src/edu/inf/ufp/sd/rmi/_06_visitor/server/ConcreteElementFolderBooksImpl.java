package edu.inf.ufp.sd.rmi._06_visitor.server;

public class ConcreteElementFolderBooksImpl implements ElementFolderRI {

    private static SingletonFolderOperationsBooks stateBooksFolder;

    public ConcreteElementFolderBooksImpl() {
    }

    public ConcreteElementFolderBooksImpl(String booksFolder) {
        super();
    }

    @Override
    public Object acceptVisitor(VisitorFolderOperationI visitor) throws Exception {
        return visitor.visitConcreteElementStateBooks(this);
    }

    public SingletonFolderOperationsBooks getStateBooksFolder(){
        return stateBooksFolder;
    }
}
