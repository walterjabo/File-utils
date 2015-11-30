package com.wjb.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class StringParamUtils {

	public static String formatTime(String patronFormat) {
		return formatTime(patronFormat, null);
	}

	public static String formatTime(Date date, String patronFormat,
			String timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(patronFormat);
		if (timeZone != null) {
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return sdf.format(date);
	}

	public static String formatTime(String patronFormat, String timeZone) {
		Calendar cal = GregorianCalendar.getInstance();
		return formatTime(cal.getTime(), patronFormat, timeZone);
	}

}
