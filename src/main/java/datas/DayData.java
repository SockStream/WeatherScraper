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
	private List<HumidityData> _RelativeHumidity;
	private List<ColorsEnum> _DewPoint;
	
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

	public void setRelativeHumidity(List<HumidityData> relativeHumidity) {
		_RelativeHumidity = relativeHumidity;
	}
	
	public List<HumidityData> getRelativeHumidity() {
		return _RelativeHumidity;
	}

	public void setDewPoint(List<ColorsEnum> dewPoint) {
		_DewPoint = dewPoint;
	}
	
	public List<ColorsEnum> getDewPoint()
	{
		return _DewPoint;
	}

}
