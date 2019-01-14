package beans;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import configs.AppConfigs;
import entities.SongFile;
import helpers.FileHelpers;
import helpers.TransactionsHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.UUID;

@ApplicationScoped
public class SongFileBean {
    private static Logger LOG = LogManager.getLogger(SongFileBean.class.getName());

    @PersistenceContext(unitName = "uploader-jpa")
    private EntityManager entityManager;

    @Inject
    private AppConfigs appConfig;

    public SongFile saveFile(InputStream inputStream, Integer songId) {
        OutputStream outputStream;
        SongFile songFile;
        String dir;
        try {
            if (appConfig.getOs().equals("linux")) {
                dir = FileHelpers.getCurrentDir();
            }
            else {
                dir = appConfig.getFilesPath();
            }
            String fileName = String.format("song-%d-%s.mp3", songId, UUID.randomUUID().toString());
            File dirF = new File(dir);
            File file = new File(dir + fileName);
            if (!dirF.exists() && !(dirF.mkdirs() && file.createNewFile())) {
                LOG.error("Could not create directory or file at specified path");
                return null;
            }
            outputStream = new FileOutputStream(file);

            songFile = new SongFile();
            songFile.setSongId(songId);
            songFile.setFileName(fileName);
            songFile.setFilePath(dir);

            if (!saveToDB(songFile)) {
                removeFromDB(songFile);
                LOG.error("Could not save file data to DB.");
                return null;
            }

            int readBytesNum;
            byte[] bytes = new byte[1024];
            while ((readBytesNum = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readBytesNum);
            }
            outputStream.close();
            FileHelpers.increaseNumOfFiles();
        } catch (IOException e) {
            LOG.error("Writing to file failed.");
            e.printStackTrace();
            return null;
        }
        return songFile;
    }

    public boolean removeSong(SongFile songFile) {
        File file = new File(songFile.getFilePath() + songFile.getFileName());
        removeFromDB(songFile);
        if (!file.delete()) {
            saveToDB(songFile);
            LOG.error("Could not remove song file.");
            return false;
        }
        return true;
    }

    public SongFile getSong(Integer songId) {
        return entityManager.find(SongFile.class, songId);
    }

    private boolean saveToDB(SongFile songFile) {
        if (songFile != null) {
            try{
                TransactionsHandler.beginTx(entityManager);
                entityManager.persist(songFile);
                TransactionsHandler.commitTx(entityManager);
            } catch (Exception e) {
                TransactionsHandler.rollbackTx(entityManager);
                return false;
            }
        }
        return true;
    }

    private void removeFromDB(SongFile songFile) {
        if (songFile == null) {
            return;
        }
        try{
            TransactionsHandler.beginTx(entityManager);
            entityManager.remove(songFile);
            TransactionsHandler.commitTx(entityManager);
        } catch (Exception e) {
            TransactionsHandler.rollbackTx(entityManager);
        }
    }
}
