package com.LTTBDD.ecommerce_app.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {

	public static Date convertStringToDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = formatter.parse(dateInString);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String dateToStringStartWithDDMMYYY(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = dateFormat.format(date);
		return strDate;
	}
	
}	
