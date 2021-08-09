package utils;

import java.util.Comparator;

import datas.LieuData;

public class CustomComparator implements Comparator<LieuData> {

	@Override
	public int compare(LieuData o1, LieuData o2) {
		// TODO Auto-generated method stub
		//return o1.getMagnitude().compareTo(o2.getMagnitude());
		return Double.compare(o1.getMagnitude(), o2.getMagnitude());
	}
}
