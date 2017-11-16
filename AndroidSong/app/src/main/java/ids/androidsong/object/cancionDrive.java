package ids.androidsong.object;


import com.google.api.services.drive.Drive;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ALAN on 07/11/2017.
 * Clase para modelar/manipular el contenido de un DriveFile
 */

public class cancionDrive extends cancionXml {

    com.google.api.services.drive.Drive driveService = null;
    private String driveId;

    public cancionDrive(String t, Drive s, String c, String d) {
        this.driveService = s;
        this.driveId = d;
        this.titulo = t;
        this.carpeta = c;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    @Override
    protected Document getDocument()
            throws ParserConfigurationException, SAXException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(getDriveId())
                .executeMediaAndDownloadTo(outputStream);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(new ByteArrayInputStream(outputStream.toByteArray()));
    }


}
