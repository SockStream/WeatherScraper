import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.api.services.drive.Drive;

import datas.DayData;
import datas.LieuData;
import input.GoogleDriveInput;
import output.ExcelFile;
import output.GoogleDrivePusher;
import utils.ColorsEnum;
import utils.CustomComparator;
import utils.Utils;

public class Launcher {

	public static void main(String[] args) throws IOException {
		
		List<Coordinate> listeLieux = getCoordinatesFromConfigFile();
		List<LieuData> listeLieuxData = new ArrayList<LieuData>();		
		for (Coordinate lieu : listeLieux)
		{
			System.out.println("Traitement du lieu : " + lieu.getLatitude() + "/" + lieu.getLongitude());
			try
			{
				listeLieuxData.add(ScraperDonneesLieu(lieu));
			}
			catch(IOException e)
			{
				System.err.println("Erreur en traitant le lieu : " + lieu.getLatitude() + "/" + lieu.getLongitude());
			}
		}
		//on va trier les lieu par nom
		Collections.sort(listeLieuxData,new CustomComparator());
		
		ExcelFile excelFile = new ExcelFile("Previsions_clearoutside");
		excelFile.Generate(listeLieuxData);
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
		
		String configFileName = GoogleDriveInput.getConfigFile("Coordinates.txt");
		
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

	public static LieuData ScraperDonneesLieu(Coordinate coord) throws IOException
	{
		Document doc = Jsoup.connect("http://clearoutside.com/forecast/" + coord.getLatitude() + "/" + coord.getLongitude() + "?view=midnight").get();
		//System.out.println(doc.title());
		LieuData lieu = new LieuData();
		lieu.SetNom(ScraperNomLieu(doc));
		lieu.setSkyQualityColor(ScraperCouleurQualiteCiel(doc));
		lieu.setSkyMagnitude(ScraperMagnitudeCiel(doc));
		lieu.setBortle(ScraperBortle(doc));
		List<DayData> listeDayData = new ArrayList<DayData>();
		Elements DayElements = doc.select(".fc_day");
		for (Element fc_day : DayElements) {
		  //System.out.println(fc_day.attr("id"));
		  listeDayData.add(ScraperDonneesJour(fc_day));
		}
		lieu.setDayDatas(listeDayData);
		return lieu;
	}

	private static int ScraperBortle(Document doc) 
	{
		int bortle = -1;
		
		Elements container_content = doc.select(".container.content");
		if (container_content.size() > 0)
		{
			Element containerContent = container_content.first();
			Element skySpan = containerContent.select(".btn.btn-primary").first();
			if (skySpan != null)
			{
				String skySpanContent = skySpan.toString();
				String splitContent = skySpanContent.split("Class")[1];
				splitContent = splitContent.split("Bortle")[0];
				splitContent = splitContent.replaceAll("<strong>", "");
				splitContent = splitContent.replaceAll("</strong>","");
				splitContent = splitContent.replaceAll("&nbsp;","");
				splitContent = splitContent.replaceAll(" ", "");
				bortle = Integer.parseInt(splitContent);
			}
		}
		
		return bortle;
	}
	
	private static double ScraperMagnitudeCiel(Document doc) {
		double magnitude = -1;
		
		Elements container_content = doc.select(".container.content");
		if (container_content.size() > 0)
		{
			Element containerContent = container_content.first();
			Element skySpan = containerContent.select(".btn.btn-primary").first();
			if (skySpan != null)
			{
				String skySpanContent = skySpan.toString();
				String splitContent = skySpanContent.split("Sky Quality:")[1];
				splitContent = splitContent.split("Magnitude")[0];
				splitContent = splitContent.replaceAll("<strong>", "");
				splitContent = splitContent.replaceAll("</strong>","");
				splitContent = splitContent.replaceAll("&nbsp;","");
				magnitude = Double.parseDouble(splitContent);
			}
		}
		
		return magnitude;
	}

	private static String ScraperNomLieu(Document doc) {
		String nomLieu = "";
		Elements container_content = doc.select(".container.content");
		if (container_content.size() > 0)
		{
			Element content = container_content.first();
			nomLieu = content.select("h1").first().toString();
			nomLieu = nomLieu.replaceAll("<h1>","");
			nomLieu = nomLieu.replaceAll("</h1>","");
			nomLieu = nomLieu.replaceAll("Forecast for ","");
		}
		
		return nomLieu;
	}

	private static ColorsEnum ScraperCouleurQualiteCiel(Document doc) {
		ColorsEnum couleur = ColorsEnum.WHITE;
		
		/*Elements container_content = doc.select(".container.content");
		if (container_content.size() > 0)
		{
			Element content = container_content.first();
		}*/
		
		return couleur;
	}

	private static DayData ScraperDonneesJour(Element fc_day) {
		//ici on va scraper les donnees du jour courant pour r�cup�rer :
		DayData dayData = new DayData();
		//	- la date
		dayData.setDate(ScraperDateJour(fc_day));
		//	- la phase de la lune
		dayData.setMoonPhase(ScraperLuneJour(fc_day));
		// -le remplissage de la lune
		dayData.setMoonPercentage(ScraperLunePourcent(fc_day));
		//	- les heures de lever et coucher
		dayData.setMoonRise(ScraperLeverLuneJour(fc_day));
		dayData.setMoonSet(ScraperCoucherLuneJour(fc_day));
		//	- les nuages hauts pour chaque heure
		dayData.setHighClouds(ScraperNuagesHauts(fc_day));
		//	- les nuages moyens pour chaque heure
		dayData.setMediumClouds(ScraperNuagesMoyens(fc_day));
		//	- les nuages bas pour chaque heure
		dayData.setLowClouds(ScraperNuagesBas(fc_day));
		//	-les nuages totaux pour chaque heure
		dayData.setTotalClouds(ScraperNuagesTotaux(fc_day));
		//	- l'�valuation de qualit� pour chaque heure
		dayData.setSkyQuality(ScraperQualiteCiel(fc_day));
		
		return dayData;
	}

	private static List<ColorsEnum> ScraperQualiteCiel(Element fc_day) {
		List<ColorsEnum> listeQualiteCiel = new ArrayList<ColorsEnum>();
		
		Element fc_hour_ratings = fc_day.selectFirst(".fc_hours.fc_hour_ratings");
		for(Element rating : fc_hour_ratings.select("li"))
		{
			ColorsEnum color = Utils.ConvertRatingToColors(rating);
			listeQualiteCiel.add(color);
		}
		
		return listeQualiteCiel;
	}

	private static List<Integer> ScraperNuagesTotaux(Element fc_day) {
		return ScraperNuages(fc_day,0);
	}

	private static List<Integer> ScraperNuagesBas(Element fc_day) {
		return ScraperNuages(fc_day,1);
	}

	private static List<Integer> ScraperNuagesMoyens(Element fc_day) {
		return ScraperNuages(fc_day,2);
	}

	private static List<Integer> ScraperNuagesHauts(Element fc_day) {
		return ScraperNuages(fc_day,3);
	}
	
	private static List<Integer> ScraperNuages(Element fc_day, int index)
	{
		List<Integer> nuages = new ArrayList<Integer>();
		
		Element fc_detail_row = fc_day.select(".fc_detail_row").get(index);
		for(Element hour : fc_detail_row.select("li"))
		{
			int valeur = -1;

			try
			{
				valeur = Integer.parseInt(hour.text());
			}
			catch(Exception e)
			{
				
			}
			nuages.add(valeur);
		}
		return nuages;
	}

	private static String ScraperCoucherLuneJour(Element fc_day) {
		String heureLeverLune= "";
		
		Element fc_moon_riseset = fc_day.selectFirst(".fc_moon_riseset");
		
		heureLeverLune = fc_moon_riseset.text();
		
		return heureLeverLune.split(" ")[1];
	}

	private static int ScraperLunePourcent(Element fc_day) {
		int pourcent = -1;
		
		Element fc_moon_percentage = fc_day.selectFirst(".fc_moon_percentage");
		String pourcentString = fc_moon_percentage.text().replaceAll("%", "");
		pourcent = Integer.parseInt(pourcentString);
		
		return pourcent;
	}

	private static String ScraperLeverLuneJour(Element fc_day) {
		String heureLeverLune= "";
		
		Element fc_moon_riseset = fc_day.selectFirst(".fc_moon_riseset");
		
		heureLeverLune = fc_moon_riseset.text();
		
		return heureLeverLune.split(" ")[0];
	}

	private static String ScraperLuneJour(Element fc_day) {
		String phaseLune = "";
		Element fc_moon_phase_image = fc_day.selectFirst(".fc_moon_phase_image");
		phaseLune = fc_moon_phase_image.attr("title");
		return phaseLune;
	}

	private static String ScraperDateJour(Element fc_day) {
		String date = "";
		
		Element fc_day_date = fc_day.selectFirst(".fc_day_date");
		
		date = fc_day_date.text();
		
		return date;
	}

}
