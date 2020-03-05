package core;

public class CheckDC {

	private static Integer dc = null;
	private static String failure = null;

	private CheckDC(){

	}

	public static boolean hasDC() {
		return dc != null;
	}

	public static Integer peek() {
		return dc;
	}

	public static void setDC(int checkDC) {
		dc = checkDC;
	}

	public static void setFailureMessage(String failureMessage ) {
		failure = failureMessage;
	}

	public static int getDC() {
		Integer checkDC = dc;
		dc = null;
		return checkDC;
	}

	public static String getFailureMessage() {
		String failureMessage = failure;
		failure = null;
		return failureMessage;
	}
}
