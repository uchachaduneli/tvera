package ge.tvera.service;


//import org.apache.commons.codec.binary.Base64;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ucha
 */
@Service
public class FileService {

    private String rootDir;

    public byte[] readFile(String identifier) throws IOException {
        return fileTOBytesArray(getFilePath(identifier));
    }

    private byte[] fileTOBytesArray(String fileName) throws FileNotFoundException, IOException {
        if (fileName != null) {
            File f = new File(fileName);
            if (f.exists()) {
                byte[] fileAsBytes = IOUtils.toByteArray(new FileInputStream(new File(fileName)));
                return fileAsBytes;
            }
        }
        return new byte[0];
    }

    private String getFilePath(String identifier) {
        File f = new File(rootDir);
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File _f : files) {
                String a = FilenameUtils.removeExtension(_f.getName());
                if (FilenameUtils.removeExtension(_f.getName()).equals(identifier)) {
                    return _f.getPath();
                }
            }
        }
        return null;
    }

    public void deleteFile(String identifier) {
        File f = new File(rootDir + "/" + identifier);
        if (f.exists()) {
            try {
                f.delete();
            } catch (Exception ex) {

            }
        }
    }

    public String getImageBase64(String fileName) {
        String imageString = null;
        try {
            File f = new File(rootDir + "/" + fileName);
            FileInputStream fis = new FileInputStream(f);
            byte byteArray[] = new byte[(int) f.length()];
            fis.read(byteArray);
            imageString = Base64.getEncoder().encodeToString(byteArray);
        } catch (Exception ex) {
            Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, ex.getMessage().toString());
        }
        return imageString;
    }

    public String addFile(MultipartFile file) throws IOException {
        String[] fileParts = file.getOriginalFilename().split("\\.");
        String fileExtention = fileParts.length > 1 ? fileParts[fileParts.length - 1] : "";
//        String fileName = "" + UUID.randomUUID() + (fileExtention.length() > 0 ? ("." + fileExtention) : "");
        String fileName = fileParts[0] + "_" + new Date().getTime() + (fileExtention.length() > 0 ? ("." + fileExtention) : "");
        File f = new File(rootDir + "/" + fileName);
        try {
            file.transferTo(f);
        } catch (Exception ex) {
            Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, ex.getMessage().toString());
        }
        return fileName;
    }

    public String getFileFullPath(String name) {
        return rootDir + "/" + name;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

}
