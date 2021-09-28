package datas;

import java.util.List;

import utils.ColorsEnum;
import java.text.Normalizer;
import java.text.Normalizer.Form;

public class LieuData {

	private String _Nom;
	private ColorsEnum _SkyQualityColor;
	private double _SkyMagnitude;
	private int _Bortle;
	List<DayData> _DayDatasList;
	
	public void SetNom(String nom)
	{
		_Nom = Normalizer.normalize(nom, Form.NFD);
	}

	public void setSkyQualityColor(ColorsEnum couleurQualiteCiel) {
		_SkyQualityColor = couleurQualiteCiel;
	}

	public void setSkyMagnitude(double magnitudeCiel) {
		_SkyMagnitude = magnitudeCiel;
	}

	public void setBortle(int bortle) {
		_Bortle = bortle;
	}

	public void setDayDatas(List<DayData> listeDayData) {
		_DayDatasList = listeDayData;
	}

	public String getNom() {
		return _Nom;
	}

	public int getBortle() {
		return _Bortle;
	}
	
	public double getMagnitude()
	{
		return _SkyMagnitude;
	}

	public List<DayData> getDayData() {
		return _DayDatasList;
	}

}
