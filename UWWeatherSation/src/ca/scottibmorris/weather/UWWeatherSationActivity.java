package ca.scottibmorris.weather;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UWWeatherSationActivity extends Activity {
    /* 
     * Resource Variables
     */
	public ImageView logo;
	public Button testButton;
	public TextView tempValue, testView;
	HashMap<String, String> xmlContents;
    
	/*
	 * Other Variables
	 */
	boolean isCelsius = true;
    String tempSuffix = "°C";
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        logo = (ImageView)findViewById(R.id.uwLogo);
        logo.setImageResource(R.drawable.uwaterloo_logo);
        
       /* testView = (TextView)findViewById(R.id.textView1);
        tempValue = (TextView)findViewById(R.id.homeTemp);
        //Get XML Data from weather.uwaterloo.ca
        boolean test = getXMLData();
		testView.setText("Result: " + test);
		try {
			if (!test)
				testView.append("\nNo Internet Connection");
			//Write XML Data to a local file
			FileInputStream fis = openFileInput("weather.xml");
			testView.append("\nOpened File");
			//Create an Instance of the Parser
			XMLParser xmlParser = new XMLParser(fis, testView);
			testView.append("\nCreated Instance of Parser");
			//Parse XML Data
			xmlContents = xmlParser.parse();
			testView.append("\nParsed File");
			//Make sure to close the fis
			fis.close();
			testView.append("\nClosed File");
			testView.append("\n" + xmlContents.size());
		} catch (Exception e) {
			//TODO: write to application log
			testView.append("\nParser Error!\n" + e);
		}
		//Update temperature
		tempValue.setText(xmlContents.get("temperature_current_C") + tempSuffix);*/
        
        boolean weatherDataSuccess = updateWeatherData();
        
        
        testButton = (Button)findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*boolean test = getXMLData();
				testView.setText("Result: " + test);
				try {
				FileInputStream fis = openFileInput("weather.xml");
				testView.append("\nOpened File");
				//byte[] xmlData = new byte[(int) fis.getChannel().size()];			
				//fis.read(xmlData);
				//fis.close();
				XMLParser xmlParser = new XMLParser(fis, testView);
				testView.append("\nCreated Instance of Parser");
				xmlContents = xmlParser.parse();
				testView.append("\nParsed File");
				fis.close();
				testView.append("\nClosed File");
				testView.append("\n" + xmlContents.size());
				} catch (Exception e) {
					//TODO: write to application log
					testView.append("\nParser Error!\n" + e);
				}
				tempValue.setText(xmlContents.get("temperature_current_C") + tempSuffix);*/
			}
		});
        
    }
    
    /*
     * AlertDialog alertDialog;
alertDialog = new AlertDialog.Builder(this).create();
alertDialog.setTitle("Packing List");
alertDialog.setMessage("Could not find the file.");
alertDialog.show();
     */
    
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
    			xmlDataString += temp + "\n";
    		}
    		FileOutputStream fos = openFileOutput(xmlDataFilename, MODE_PRIVATE);
    		fos.write(xmlDataString.getBytes());
    		fos.close();
    		
    	} catch (Exception e) {
    		success = false;
    		//TODO: Wrote to application log
    		AlertDialog errorMessage = new AlertDialog.Builder(this).create();
    		errorMessage.setTitle("Parser Error");
    		errorMessage.setMessage(e.toString());
    		errorMessage.show();
    	}

    	return success;    	
    }
    
    private boolean updateWeatherData() {
    	boolean success = true;
    	TextView temp24, precip24;
    	float tempNum, temp24Num;
    	
    	//TextView Elements
    	testView = (TextView)findViewById(R.id.textView1);
        tempValue = (TextView)findViewById(R.id.homeTemp);
        temp24 = (TextView)findViewById(R.id.temp24);
        precip24 = (TextView)findViewById(R.id.precip24);
        
        //Get XML Data from weather.uwaterloo.ca
        boolean test = getXMLData();
		testView.setText("Result: " + test);
		try {
			if (!test)
				testView.append("\nNo Internet Connection");
			//Write XML Data to a local file
			FileInputStream fis = openFileInput("weather.xml");
			testView.append("\nOpened File");
			//Create an Instance of the Parser
			XMLParser xmlParser = new XMLParser(fis, testView);
			testView.append("\nCreated Instance of Parser");
			//Parse XML Data
			xmlContents = xmlParser.parse();
			testView.append("\nParsed File");
			//Make sure to close the fis
			fis.close();
			testView.append("\nClosed File");
			testView.append("\n" + xmlContents.size());
		} catch (Exception e) {
			//TODO: write to application log
			testView.append("\nParser Error!\n" + e);
			success = false;
		}
		if(isCelsius){
			//No Conversion Needed
			tempNum = Float.valueOf(xmlContents.get("temperature_current_C"));
			temp24Num = Float.valueOf(xmlContents.get("temperature_24hrmax_C"));
		} else {
			//Convert to Fahrenheit
			tempNum = ((Float.valueOf(xmlContents.get("temperature_current_C"))) * (9/5)) + 32;
			temp24Num = ((Float.valueOf(xmlContents.get("temperature_24hrmax_C"))) * (9/5)) + 32;
		}
		
		//Update temperature
		tempValue.setText(String.valueOf(tempNum) + tempSuffix);
		temp24.setText(String.valueOf(temp24Num) + tempSuffix);
		//precipitation_24hr_mm
		precip24.setText(xmlContents.get("precipitation_24hr_mm") + " mm");
		
		return success;
    }
    
}