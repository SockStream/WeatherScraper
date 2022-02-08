import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;

import GoogleDriveUtils.GoogleMailManager;
import Interface.Coordinate;
import Interface.InterfaceRecuperation;
import Interface.clearOutside.clearOutsideInterface;
import input.GoogleDriveInput;
import output.ExcelFile;
import output.GoogleDrivePusher;
import output.MailSender;
import utils.CustomComparator;

public class Launcher {

	public static void main(String[] args) throws IOException, MessagingException {
		
		GoogleMailManager.getGMailService();
		List<Coordinate> listeLieux = getCoordinatesFromConfigFile();
		InterfaceRecuperation interfaceRecuperation = new clearOutsideInterface();
			
		for (Coordinate lieu : listeLieux)
		{
			System.out.println("Traitement du lieu : " + lieu.getLatitude() + "/" + lieu.getLongitude());
			try
			{
				interfaceRecuperation.ScraperDonneesLieu(lieu);
			}
			catch(IOException e)
			{
				System.err.println("Erreur en traitant le lieu : " + lieu.getLatitude() + "/" + lieu.getLongitude());
			}
		}
		//on va trier les lieu par Bortle
		Collections.sort(interfaceRecuperation.getListeLieuxData(),new CustomComparator());
		
		ExcelFile excelFile = new ExcelFile("Previsions_clearoutside");
		excelFile.Generate(interfaceRecuperation.getListeLieuxData());
		//GoogleDrivePusher.push("Previsions_clearoutside");
		FileInputStream fis = new FileInputStream("Previsions_clearoutside.xls");
		GoogleDrivePusher.createGoogleFile("Previsions_ClearOutside", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Previsions_clearoutside.xls",fis);
		fis.close();
		File file = new File("Previsions_clearoutside.xls");
		file.delete();
		System.out.println("Fin du Traitement");
	}
	
	private static List<Coordinate> getCoordinatesFromConfigFile() throws IOException {
		List<Coordinate> listeLieux = new ArrayList<Coordinate>();
		
		String configFileName = GoogleDriveInput.getFile("Coordinates.txt");
		
		List<String> content = Files.readAllLines(Paths.get(configFileName));
		Files.delete(Paths.get("Coordinates.txt"));
		
		for(String line: content)
		{
			if (line.contains("#"))
			{
				continue;
			}
			try
			{
				String coord = line.split(" ")[0];
				double latitude = Double.parseDouble(coord.split(",")[0]);
				double longitude = Double.parseDouble(coord.split(",")[1]);
				listeLieux.add(new Coordinate(latitude,longitude));
			}
			catch(Exception e)
			{
				
			}
		}
		return listeLieux;
	}


}
