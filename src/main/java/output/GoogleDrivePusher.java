package output;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import GoogleDriveUtils.GoogleDriveManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;

public class GoogleDrivePusher {
	
	// PRIVATE!
    private static File _createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
 
        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
 
        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);
        //
        Drive driveService = GoogleDriveManager.getDriveService();
 
        File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id, webContentLink, webViewLink, parents").execute();
        return file;
    }
    
    public static final List<File> getGoogleSubFolderByName(String googleFolderIdParent, String subFolderName)
            throws IOException {
 
        Drive driveService = GoogleDriveManager.getDriveService();
 
        String pageToken = null;
        List<File> list = new ArrayList<File>();
 
        String query = null;
        if (googleFolderIdParent == null) {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }
 
        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

	private static File uploadGoogleFile(String fileId, String googleFolderIdParent, String contentType,
			AbstractInputStreamContent uploadStreamContent, String customFileName) throws IOException {
		Drive driveService = GoogleDriveManager.getDriveService();
		
		File fileMetadata = new File();
        fileMetadata.setName(customFileName);
		
		return driveService.files().update(fileId, fileMetadata, uploadStreamContent).execute();
	}
    
    public static final File createGoogleFolder(String folderIdParent, String folderName) throws IOException {
    	 
        File fileMetadata = new File();
 
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
 
        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);
 
            fileMetadata.setParents(parents);
        }
        Drive driveService = GoogleDriveManager.getDriveService();
 
        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name").execute();
 
        return file;
    }
    
    public static final void DeleteFolder(String folderIdParent) throws IOException
    {
    	Drive driveService = GoogleDriveManager.getDriveService();
    	driveService.files().delete(folderIdParent).execute();
    }
    
	/*
	 // Create Google File from byte[]
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, byte[] uploadData) throws IOException {
        //
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }
    
 // Create Google File from java.io.File
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, java.io.File uploadFile) throws IOException {
 
        //
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }*/
 
    // Create Google File from InputStream
    public static File createGoogleFile(String ParentDirectoryName, String contentType, //
            String customFileName, InputStream inputStream) throws IOException {
 
    	List<File> fileList = getGoogleSubFolderByName(null, ParentDirectoryName);
    	
    	if (fileList.size() > 0)
    	{
    		System.out.println("Le répertoire " + ParentDirectoryName + " existe déjà");
    		String folderId = fileList.get(0).getId();
    		String fileId = GoogleDriveManager.findFileInFolder(folderId, customFileName);
    		if (fileId == null)
    		{
    			System.out.println("On va créer un nouveau dossier dans le répertoire");
    			AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
    			return _createGoogleFile(folderId, contentType, customFileName, uploadStreamContent);
    		}
    		else
    		{
    			System.out.println("On va mettre à jour le fichier");
    			AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
    			return uploadGoogleFile(fileId, folderId , contentType, uploadStreamContent, customFileName);
    		}
    	}
    	else
    	{
    		System.out.println("On créé le répertoire " + ParentDirectoryName);
    		File folder = createGoogleFolder(null, ParentDirectoryName);
    	
	        //
	        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
	        //
    		System.out.println("Ainsi que le fichier " + customFileName);
	        return _createGoogleFile(folder.getId(), contentType, customFileName, uploadStreamContent);
    	}
    }
    
}
