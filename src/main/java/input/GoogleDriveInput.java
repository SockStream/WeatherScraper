package input;

import java.io.FileOutputStream;
import java.io.IOException;

import com.google.api.services.drive.Drive;

import GoogleDriveUtils.GoogleDriveManager;

public class GoogleDriveInput {

	public static String getFile(String configFileName) throws IOException {
		String fileId = GoogleDriveManager.findFileInFolder(null, configFileName);
		if (fileId != null)
		{
			Drive service = GoogleDriveManager.getDriveService();
			FileOutputStream fos = new FileOutputStream(configFileName);
			service.files().get(fileId).executeMediaAndDownloadTo(fos);
			fos.flush();
			fos.close();
			return configFileName;
		
		}
		return null;
	}

}
