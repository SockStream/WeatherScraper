package utils;

import java.util.Comparator;

import datas.LieuData;

public class CustomComparator implements Comparator<LieuData> {

	@Override
	public int compare(LieuData o1, LieuData o2) {
		// TODO Auto-generated method stub
		return o1.getNom().compareTo(o2.getNom());
	}
}
