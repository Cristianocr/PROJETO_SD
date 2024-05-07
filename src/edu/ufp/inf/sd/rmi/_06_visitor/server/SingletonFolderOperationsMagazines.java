/**
 * <p>Title: Projecto SD</p>
 *
 * <p>Description: Projecto apoio aulas SD</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: UFP & INESC Porto</p>
 *
 * @author Rui Moreira
 * @version 1.0
 */
package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingletonFolderOperationsMagazines implements SingletonFoldersOperationsI {

    private static SingletonFolderOperationsMagazines singletonFolderOperationsMagazines;
    private final File folderMagazines;

    /**
     * private - Avoid direct instantiation
     */
    private SingletonFolderOperationsMagazines(String folder) {
        folderMagazines = new File(folder);
    }

    public synchronized static SingletonFolderOperationsMagazines createSingletonFolderOperationsBooks(String folder) {
        if (singletonFolderOperationsMagazines == null) {
            singletonFolderOperationsMagazines = new SingletonFolderOperationsMagazines(folder);
        }
        return singletonFolderOperationsMagazines;
    }

    @Override
    public Boolean createFile(String fname) {
        try {
            File newFile = new File(this.folderMagazines.getAbsolutePath() + "/" + fname);
            return newFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SingletonFolderOperationsMagazines.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Boolean deleteFile(String fname) {
        File existingFile = new File(this.folderMagazines.getAbsolutePath() + "/" + fname);
        return existingFile.delete();
    }


}

