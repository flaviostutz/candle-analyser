package br.com.candleanalyser.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Helper {

	public static String formatNumber(double number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		return nf.format(number);
	}
	
	/**
	 * list Classes inside a given package
	 * 
	 * @author Jon Peck http://jonpeck.com (adapted from
	 *         http://www.javaworld.com/javaworld/javatips/jw-javatip113.html)
	 * @param pckgname String name of a Package, EG "java.lang"
	 * @return Class[] classes inside the root of the given package
	 * @throws ClassNotFoundException if the Package is invalid
	 */
	public static Class[] getClasses(String pckgname) throws ClassNotFoundException {
		ArrayList classes = new ArrayList();
		// Get a File object for the package
		File directory = null;
		try {
			directory = new File(Thread.currentThread().getContextClassLoader()
					.getResource(pckgname.replace('.', '/')).getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6)));
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}
		Class[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	public static double clamp(double value, double minValue, double maxValue) {
		if(value<minValue) {
			value = minValue;
		} else if(value>maxValue) {
			value = maxValue;
		}
		return value;
	}
	
	public static Date createDate(int day, int month, int year) {
		Calendar d = GregorianCalendar.getInstance();
		d.set(year, month-1, day, 0, 0, 0);
		return d.getTime();
	}

	public static boolean dayAfterEquals(Date date, Date reference) {
		Calendar ref = GregorianCalendar.getInstance();
		ref.setTime(reference);
		ref.set(Calendar.HOUR_OF_DAY, 0);
		ref.set(Calendar.MINUTE, 0);
		ref.set(Calendar.SECOND, 0);
		
		Calendar d = GregorianCalendar.getInstance();
		d.setTime(date);
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		
		return d.after(ref) || d.equals(ref);
	}
	
	public static boolean dayBeforeEquals(Date date, Date reference) {
		Calendar ref = GregorianCalendar.getInstance();
		ref.setTime(reference);
		ref.set(Calendar.HOUR_OF_DAY, 0);
		ref.set(Calendar.MINUTE, 0);
		ref.set(Calendar.SECOND, 0);
		
		Calendar d = GregorianCalendar.getInstance();
		d.setTime(date);
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		
		return d.before(ref) || d.equals(ref);
	}
	
	public static boolean dayBetweenDates(Date date, Date fromDate, Date toDate) {
		return dayAfterEquals(date, fromDate) && dayBeforeEquals(date, toDate);
	}

	public static Object loadObject(File file) throws IOException, ClassNotFoundException {
		if(!file.exists()) return null;
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream is = new ObjectInputStream(fis);
		Object result = is.readObject();
		fis.close();
		return result;
	}

	public static void saveObject(File file, Object object) throws IOException, ClassNotFoundException {
		if(file.exists()) {
			file.delete();
		}
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(object);
		fos.close();
	}

}
