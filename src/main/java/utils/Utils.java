package utils;

import org.jsoup.nodes.Element;

public class Utils {

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

}
