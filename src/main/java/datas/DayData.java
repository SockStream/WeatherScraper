package datas;

import java.util.List;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import utils.ColorsEnum;

public class DayData {

	private String _DateJour;
	private String _MoonPhase;
	private String _MoonRise;
	private String _MoonSet;
	private int _MoonPercentage;
	private List<Integer> _HighClouds;
	private List<Integer> _MediumClouds;
	private List<Integer> _LowClouds;
	private List<Integer> _TotalClouds;
	private List<ColorsEnum> _QualiteCiel;
	private List<IssDatas> _PassagesISS;
	private List<DoubleColorData> _RelativeHumidity;
	private List<ColorsEnum> _DewPoint;
	private List<DoubleColorData> _WindSpeed;
	private int _AstroDarkBeginHour;
	private int _AstroDarkEndHour;
	private List<Integer> _ListeHeures;
	
	public void setDate(String dateJour) {
		_DateJour = dateJour;
	}

	public void setMoonPhase(String moonPhase) {
		_MoonPhase = moonPhase;
	}

	public void setMoonRise(String leverLune) {
		_MoonRise = leverLune;
	}

	public void setMoonSet(String coucherLune) {
		_MoonSet = coucherLune;
	}
	
	public void setMoonPercentage(int pourcentLune ) {
		_MoonPercentage = pourcentLune;
	}

	public void setHighClouds(List<Integer> nuagesHauts) {
		_HighClouds = nuagesHauts;
	}

	public void setMediumClouds(List<Integer> nuagesMoyens) {
		_MediumClouds = nuagesMoyens;
	}

	public void setLowClouds(List<Integer> nuagesBas) {
		_LowClouds = nuagesBas;
	}

	public void setTotalClouds(List<Integer> nuagesTotaux) {
		_TotalClouds = nuagesTotaux;
	}

	public void setSkyQuality(List<ColorsEnum> qualiteCiel) {
		_QualiteCiel = qualiteCiel;
	}
	
	public void setISSPassOver(List<IssDatas> passagesISS) {
		_PassagesISS = passagesISS;
	}
	
	public void setHeures(List<Integer> listeHeures) {
		_ListeHeures = listeHeures;
	}

	public List<IssDatas> getISSPassOver() {
		return _PassagesISS;
	}

	public String getDate() {
		return _DateJour;
	}

	public String getMoonPhase() {
		return _MoonPhase;
	}

	public String getMoonPercent() {
		return String.valueOf(_MoonPercentage);
	}

	public String getMoonRise() {
		return _MoonRise;
	}

	public String getMoonSet() {
		return _MoonSet;
	}
	
	public List<Integer> getTotalClouds()
	{
		return _TotalClouds;
	}

	public List<ColorsEnum> getSkyQuality() {
		return _QualiteCiel;
	}

	public List<Integer> getHighClouds() {
		return _HighClouds;
	}
	
	public List<Integer> getMediumClouds()
	{
		return _MediumClouds;
	}
	
	public List<Integer> getLowClouds()
	{
		return _LowClouds;
	}

	public void setRelativeHumidity(List<DoubleColorData> relativeHumidity) {
		_RelativeHumidity = relativeHumidity;
	}
	
	public List<DoubleColorData> getRelativeHumidity() {
		return _RelativeHumidity;
	}

	public void setDewPoint(List<ColorsEnum> dewPoint) {
		_DewPoint = dewPoint;
	}
	
	public List<ColorsEnum> getDewPoint()
	{
		return _DewPoint;
	}
	
	public void setWindSpeed(List<DoubleColorData> windSpeed)
	{
		_WindSpeed = windSpeed;
	}
	
	public List<DoubleColorData> getWindSpeed()
	{
		return _WindSpeed;
	}

	public void setAstroDarkBeginHour(int astroDarkBeginHour) {
		_AstroDarkBeginHour = astroDarkBeginHour;
	}
	
	public int getAstroDarkBeginHour()
	{
		return _AstroDarkBeginHour;
	}

	public void setAstroDarkEndHour(int astroDarkEndHour) {
		_AstroDarkEndHour = astroDarkEndHour;
	}
	
	public int getAstroDarkEndHour()
	{
		return _AstroDarkEndHour;
	}
	
	public List<Integer> getHeures() {
		return _ListeHeures;
	}

}
