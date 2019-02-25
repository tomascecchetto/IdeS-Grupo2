package ids.androidsong.object;


import android.util.Log;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ParentReference;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ALAN on 07/11/2017.
 * Clase para modelar/manipular el contenido de un DriveFile
 */

public class cancionDrive extends cancionXml {

    private com.google.api.services.drive.Drive driveService = null;
    private String driveId;
    private com.google.api.services.drive.model.File driveFile;

    public cancionDrive(com.google.api.services.drive.model.File f, Drive s, String c) {
        super();
        this.driveService = s;
        this.driveFile = f;
        this.driveId = f.getId();
        this.titulo = f.getTitle();
        this.carpeta = c;
    }

    public cancionDrive(item item) {
        super();
        this.setId(item.getId());
        this.setCarpeta(item.getCarpeta());
        this.setTitulo(item.getTitulo());
        this.setSecciones(item.getSecciones());
        this.setAtributos(item.getAtributos());
    }

    public String getFechaDrive(){
        String fecha = getDriveFile().getModifiedDate().toString();
        if (fecha.equals("")) {
            fecha = getDriveFile().getCreatedDate().toString();
        }
        return fecha;
    }

    private String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public com.google.api.services.drive.model.File getDriveFile() {
        return driveFile;
    }

    private void setDriveFile(com.google.api.services.drive.model.File driveFile) {
        this.driveFile = driveFile;
    }
    public void setDriveService(Drive service){
        this.driveService = service;
    }

    public void copiarDatos(item item){
        this.setId(item.getId());
        this.setCarpeta(item.getCarpeta());
        this.setTitulo(item.getTitulo());
        this.setSecciones(item.getSecciones());
        this.setAtributos(item.getAtributos());
    }

    @Override
    Document getDocument()
            throws ParserConfigurationException, SAXException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(getDriveId())
                .executeMediaAndDownloadTo(outputStream);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    @Override
    File toXml(){
        Document dom = null;
        try {
            dom = getDocument();
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
        LoadCancion(dom);
        return grabar(dom,"tempXml.txt");
    }

    public void modificarDrive(String currentParent) throws IOException {
        com.google.api.services.drive.model.File file = getDriveFile();
        file.setTitle(getTitulo());
        StringBuilder previousParents = new StringBuilder();
        for (ParentReference parent : file.getParents()) {
            previousParents.append(parent.getId());
            previousParents.append(',');
        }
        FileContent mediaContent = new FileContent(null,toXml());
        file = driveService.files().update(getDriveId(), file, mediaContent)
                .setRemoveParents(previousParents.toString())
                .setAddParents(currentParent)
                .execute();
        setDriveFile(file);
    }

    public void bajaDrive() throws IOException {
        driveService.files().trash(getDriveId()).execute();
    }

    public void altaDrive(String currentParent) {
        com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
        body.setTitle(getTitulo());

        body.setParents(
                Collections.singletonList(new ParentReference().setId(currentParent)));

        java.io.File fileContent = super.toXml();
        FileContent mediaContent = new FileContent(null, fileContent);
        try {
            setDriveFile(driveService.files().insert(body, mediaContent).execute());
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }


}
