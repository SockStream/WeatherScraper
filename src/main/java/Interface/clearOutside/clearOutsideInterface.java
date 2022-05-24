package Interface.clearOutside;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Interface.Coordinate;
import Interface.InterfaceRecuperation;
import datas.DayData;
import datas.DoubleColorData;
import datas.IssDatas;
import datas.LieuData;
import utils.ColorsEnum;
import utils.CustomUtils;

public class clearOutsideInterface extends InterfaceRecuperation {

	@Override
	public void ScraperDonneesLieu(Coordinate coord) throws IOException {
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
		getListeLieuxData().add(lieu);
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
		dayData.setAstroDarkBeginHour(ScraperDebutAstroDarkJour(fc_day));
		dayData.setAstroDarkEndHour(ScraperFinAstroDarkJour(fc_day));
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
		dayData.setISSPassOver(ScraperISS(fc_day));
		dayData.setRelativeHumidity(ScraperHumidity(fc_day));
		dayData.setDewPoint(ScraperDewPoint(fc_day));
		dayData.setWindSpeed(ScrapperWindSpeed(fc_day));
		return dayData;
	}
	
	public static List<DoubleColorData> ScraperHumidity(Element fc_day)
	{
		List<DoubleColorData> listeHumidityData = new ArrayList<DoubleColorData>();
		
		Element fc_detail_row = fc_day.select(".fc_detail_row").get(15);
		
		for(Element rating : fc_detail_row.select("li"))
		{
			DoubleColorData humidityData = new DoubleColorData();
			ColorsEnum color = CustomUtils.ConvertRatingToColors(rating);
			humidityData.setCouleur(color);
			humidityData.setValeur(Double.parseDouble(rating.text()));
			listeHumidityData.add(humidityData);
		}
		
		return listeHumidityData;
	}
	
	private static List<ColorsEnum> ScraperDewPoint(Element fc_day)
	{
		List<ColorsEnum> listeDewPoint = new ArrayList<ColorsEnum>();
		
		Element fc_detail_row = fc_day.select(".fc_detail_row").get(14);
		
		for(Element rating : fc_detail_row.select("li"))
		{
			ColorsEnum color = CustomUtils.ConvertRatingToColors(rating);
			listeDewPoint.add(color);
		}
		
		return listeDewPoint;
	}
	
	private static List<DoubleColorData> ScrapperWindSpeed(Element fc_day)
	{
		List<DoubleColorData> listeWindSpeed = new ArrayList<DoubleColorData>();
		Element fc_detail_row = fc_day.select(".fc_detail_row").get(10);
		for(Element rating : fc_detail_row.select("li"))
		{
			DoubleColorData windData = new DoubleColorData();
			ColorsEnum color = CustomUtils.ConvertRatingToColors(rating);
			windData.setCouleur(color);
			windData.setValeur(Double.parseDouble(rating.text()));
			listeWindSpeed.add(windData);
		}
		return listeWindSpeed;
		
	}
	
	private static List<IssDatas> ScraperISS(Element fc_day) {
		List<IssDatas> listeIssDatas = new ArrayList<IssDatas>();
		Element fc_detail_row = fc_day.select(".fc_detail_row").get(4);
		for(Element hour : fc_detail_row.select("li"))
		{
			IssDatas datas = new IssDatas();
			String content = hour.attr("data-content");
			if (!content.trim().isEmpty())
			{
				datas.parse(content);
			}
			listeIssDatas.add(datas);
		}
		return listeIssDatas;
		
	}

	private static List<ColorsEnum> ScraperQualiteCiel(Element fc_day) {
		List<ColorsEnum> listeQualiteCiel = new ArrayList<ColorsEnum>();
		
		Element fc_hour_ratings = fc_day.selectFirst(".fc_hours.fc_hour_ratings");
		for(Element rating : fc_hour_ratings.select("li"))
		{
			ColorsEnum color = CustomUtils.ConvertRatingToColors(rating);
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
	
	private static int ScraperDebutAstroDarkJour(Element fc_day)
	{
		int heure = -1;
		Element fc_day_astro_dark = fc_day.selectFirst(".fc_daylight");
		String data_content = fc_day_astro_dark.attr("data-content");
		String astro_dark_content = data_content.split("Astro Dark:</strong>")[1];
		String content_begin = astro_dark_content.split("-")[0];
		int content_begin_hour = -1;
		try
		{
			content_begin_hour = Integer.parseInt(content_begin.split(":")[0].replaceAll(" ",""));
		}
		catch (Exception e)
		{
		}
		int content_begin_minute = -1;
		try
		{
			content_begin_minute = Integer.parseInt(content_begin.split(":")[1].replaceAll(" ",""));
		}
		catch (Exception e)
		{
		}
		if (content_begin_minute > 0)
		{
			content_begin_hour += 1;
		}
		heure = content_begin_hour;
		return heure;
	}
	
	private static int ScraperFinAstroDarkJour(Element fc_day)
	{
		int heure = -1;
		Element fc_day_astro_dark = fc_day.selectFirst(".fc_daylight");
		String data_content = fc_day_astro_dark.attr("data-content");
		String astro_dark_content = data_content.split("Astro Dark:</strong>")[1];
		String content_end = astro_dark_content.split("-")[1];
		int content_end_hour = -1;
		try
		{
			content_end_hour = Integer.parseInt(content_end.split(":")[0].replaceAll(" ",""));
		}
		catch (NumberFormatException e)
		{
		}
		heure = content_end_hour;
		return heure;
	}

}
