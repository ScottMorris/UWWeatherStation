package ca.scottibmorris.weather;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class UWWeatherSationActivity extends Activity {
    /* 
     * Resource Variables
     */
	public ImageView logo;
    
	/*
	 * Other Variables
	 */
    String tempSuffix = "°C";
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        logo = (ImageView)findViewById(R.id.uwLogo);
        logo.setImageResource(R.drawable.uwaterloo_logo);
    }
    
    private boolean getXMLData() {
    	boolean success = true;
    	String xmlDataAddress = "http://weather.uwaterloo.ca/waterloo_weather_station_data.xml";
    	URL xmlDataURL;
    	URLConnection urlConn;
    	String xmlDataFilename = "weather.xml", xmlDataString = "", temp = "";
    	
    	try {
    		xmlDataURL = new URL(xmlDataAddress);
    		urlConn = xmlDataURL.openConnection();
    		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
    		while ((temp = in.readLine()) != null) {
    			xmlDataString += temp + "/n";
    		}
    		FileOutputStream fos = openFileOutput(xmlDataFilename, MODE_PRIVATE);
    		fos.write(xmlDataString.getBytes());
    		fos.close();
    		
    	} catch (Exception e) {
    		success = false;
    	}
    	
    	
    	
    	return success;    	
    }
    
    
}