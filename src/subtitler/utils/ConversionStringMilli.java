package subtitler.utils;

public class ConversionStringMilli {

	
	public static long StringToMillisecond(String time) {
		try {
			String stringMilli = time.replaceAll("[^0123456789:.]", "");
			
			String[] splitedTime = stringMilli.split(":");
			
			int hours = splitedTime.length<3?00:Integer.parseInt(splitedTime[0]);
			
			int minutes = splitedTime.length<2?00:Integer.parseInt(splitedTime[splitedTime.length-2]);
			
			String[] secAndMilliSec = splitedTime[splitedTime.length-1].split("[.]");
			
			int seconds = Integer.parseInt( secAndMilliSec.length<2?splitedTime[splitedTime.length-1]:secAndMilliSec[0]);
			
			int milliseconds = Integer.parseInt(secAndMilliSec.length<2 ?"00":secAndMilliSec[1]);
			
			return hours*1000*60*60 + minutes*1000*60 + seconds*1000 + milliseconds;
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public static String MillisecondsToString(long time) {
        
        long milliseconds=time%1000; 
        time=time/1000; 
        long seconds=time%60; 
         time=time/60; 
        long minutes=time%60; 
         time=time/60; 
        long hours=time;
        return ((hours<10?"0"+hours:hours)+":"+(minutes<10?"0"+minutes:minutes)+":"+(seconds<10?"0"+seconds:seconds)+"."+milliseconds);
    }
	
}
