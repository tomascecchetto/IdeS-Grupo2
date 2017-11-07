package ids.androidsong.object;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ALAN on 07/11/2017.
 * Clase para modelar/manipular el contenido de un DriveFile
 */

public class cancionDrive extends cancionXml {

    DriveFolder driveFolder;
    DriveFile driveFile;
    DriveId driveId;

    public cancionDrive() {
    }

    @Override
    protected Document getDocument()
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(new File(path));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }
}
