package GoogleDriveUtils;

import java.io.IOException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveManager extends GoogleManager{
 

    private static Drive _driveService;
    
    public static Drive getDriveService() throws IOException {
        if (_driveService != null) {
            return _driveService;
        }
        Credential credential = GoogleManager.getCredentials();
        //
        _driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build();
        return _driveService;
    }

	public static String findFileInFolder(String folderId, String customFileName) throws IOException {
		String query =  "name = '" + customFileName + "' ";
		Drive driveService = GoogleDriveManager.getDriveService();
		FileList result = driveService.files().list().setQ(query).execute();
		String fileId = null;
		for (File file : result.getFiles())
		{
			//System.out.println(file.getName()  + " " + file.getId() + " " + file.getParents());
			fileId = file.getId();
			break;
		}
		return fileId;
	}
 
}
