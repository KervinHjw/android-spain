package com.ludees.scian.util;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 运算
 * 
 * @author OutTime
 *
 */
public class Operation {
	// 势
	public static double add(double a, double b) {
		DecimalFormat df = new DecimalFormat("#.00");

		return Double.parseDouble(df.format(a + b));

	}

	// 凿
	public static double sub(double a, double b) {
		DecimalFormat df = new DecimalFormat("#.00");

		return Double.parseDouble(df.format(a - b));

	}

	// 乿
	public static double mul(String a, String b) {
		try {
			double c = Double.parseDouble(a);
			double d = Double.parseDouble(b);
			DecimalFormat df = new DecimalFormat("#.00");

			return Double.parseDouble(df.format(c * d));
		} catch (Exception e) {
		}
		return 0D;

	}

	// 乿
	public static String mul2Str(String a, String b) {
		try {
			double c = Double.parseDouble(a);
			double d = Double.parseDouble(b);
			DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(0);
			return df.format(c * d);
		} catch (Exception e) {
		}
		return "";

	}

	// ÷
	public static double div(double a, double b) {
		DecimalFormat df = new DecimalFormat("#.00");

		return Double.parseDouble(df.format(a / b));

	}

	// dou
	public static double getDistance(double dis) {
		DecimalFormat df = new DecimalFormat("#.0");

		return Double.parseDouble(df.format(dis / 1000));

	}

	public static String doubleToStr(double dis) {
		if (dis >= 1) {
			DecimalFormat df = new DecimalFormat("#.00");
			return df.format(dis);
		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(dis);
		}

	}

	// 时间比较
	public static boolean twoDateDistance(Date startDate, Date endDate) {
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 0) {
			return false;
		} else {
			return true;
		}
	}

}
