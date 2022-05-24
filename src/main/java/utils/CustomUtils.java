package utils;

import java.util.List;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.jsoup.nodes.Element;

import datas.DayData;
import datas.LieuData;

public class CustomUtils {

	public static ColorsEnum ConvertRatingToColors(Element rating) {
		String className = rating.className();
		if (className.contains("bad"))
		{
			return ColorsEnum.RED;
		}
		
		if (className.contains("ok"))
		{
			return ColorsEnum.ORANGE;
		}
		
		if (className.contains("good"))
		{
			return ColorsEnum.GREEN;
		}
		
		return ColorsEnum.WHITE;
	}
	
	public static Double ConvertMphToKmh(double mph)
	{
		double kmh = mph * 1.60934;
		double roundOff = Math.round(kmh * 100.0) / 100.0;
		return roundOff;
	}

	public static void identifierOpportunites(List<LieuData> listeLieuxData) {
		//pour chaque emplacement,
		for(LieuData lieu : listeLieuxData)
		{
			//pour chaque jour jusqu'au n-1eme jour
			for (DayData day : lieu.getDayData())
			{
				//à partir du coucher du soleil jusqu'au lever
				//si on a plus de 4 heures de pas nuages + pas vent + pas humidité
					//On envoie un mail pour prévenir
						//- on donne les infos sur la Lune
						//- lieu (nom + Bortle)
						//- heure début + heure de fin
			}
		}
		
		
		throw new NotImplementedException(null);
	}

}
