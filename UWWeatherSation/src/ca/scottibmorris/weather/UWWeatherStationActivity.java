package ca.scottibmorris.weather;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UWWeatherStationActivity extends Activity {
    /* 
     * Resource Variables
     */
	public ImageView logo;
	public Button testButton, homeButton, graphsGoToButton;
	public TextView titleBarText, tempValue, testView;
    
	/*
	 * Other Variables
	 */
	boolean isCelsius = true;
    String tempSuffix = "°C";
    int counter = 0;
    HashMap<String, String> xmlContents;
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //University logo object
        logo = (ImageView)findViewById(R.id.uwLogo);
        logo.setImageResource(R.drawable.uwaterloo_logo);
        
        //Title Bar logo TextView Object
        titleBarText = (TextView)findViewById(R.id.weatherLogoText);
        
        //Set Title Bar colours to White
        logo.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        titleBarText.setTextColor(Color.WHITE);
        
        //Link action to Graph Button
        graphsGoToButton = (Button)findViewById(R.id.tempGraphGoToButton);
        graphsGoToButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent weatherGraphs = new Intent(v.getContext(), WeatherGraphs.class);
				startActivityForResult(weatherGraphs, -1);	
			}
		});
        
        /**TODO: Refactor this out so the app starts faster
        	Could also organise it to only update the XML every 15 min */
        
        //Initialize current weather data upon launch
        boolean weatherDataSuccess = updateWeatherData();
        
        //Test button Object - depreciated
        testButton = (Button)findViewById(R.id.testButton);
        testButton.setVisibility(View.INVISIBLE);
        testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO: Use for Testing New Features 
			}
		});
        
    }
    
    /*
     * Create the Options menu for this activity
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_screen_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
    /*
     * Handle option menu selection
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.updateWeatherOption:
			updateWeatherData();
			break;
		case R.id.CelsiusFahrenheitOption:
			switchCelsiusFahrenheit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * switchCelsiusFahrenheit
	 * Toggles the displayed temperatures between Celsius and Fahrenheit
	 */
	private void switchCelsiusFahrenheit() {
		isCelsius = !isCelsius;
		if(isCelsius)
			tempSuffix = "°C";
		else
			tempSuffix = "°F";
		updateWeatherData();
	}

	/*
	 * getXMLData
	 * Download the XML Data from weather.uwaterloo.ca and save it to a local XML file
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
    		//TODO: Write to application log
    		/*AlertDialog errorMessage = new AlertDialog.Builder(this).create();
    		errorMessage.setTitle("Network Error");
    		errorMessage.setMessage(e.toString());
    		errorMessage.show();*/
    	}

    	return success;    	
    }
    
	/*
	 * updateWeatherData
	 * Check for freshness of the data (if its older than 15 min)
	 * Parse the local copy of the weather data and update the information displayed
	 */
    private boolean updateWeatherData() {
    	boolean success = true;
    	TextView temp24, temp24Min, precip24, updateDateTime;
    	float tempNum, temp24Num, temp24MinNum;
    	Date currentFileDateTime = null;
    	
    	//TextView Elements
    	testView = (TextView)findViewById(R.id.textView1);
        tempValue = (TextView)findViewById(R.id.homeTemp);
        temp24 = (TextView)findViewById(R.id.temp24);
        temp24Min = (TextView)findViewById(R.id.temp24Min);
        precip24 = (TextView)findViewById(R.id.precip24);
        updateDateTime = (TextView)findViewById(R.id.lastUpadte);
        
        
        //debug counter
        counter++;
        
        //TODO: Check freshness of the file (maybe reorganise the lookup procedure by
        //moving the XML retrieval to the the end.)
        
        //Get XML Data from weather.uwaterloo.ca
        boolean test = getXMLData();
		testView.setText("Result: " + test + "; Count: " + counter);
		try {
			if (!test)
				testView.append("\nNo Internet Connection");
			//Read XML Data from local file
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
			temp24MinNum = Float.valueOf(xmlContents.get("temperature_24hrmin_C"));
		} else {
			//Convert to Fahrenheit
			tempNum = ((Float.valueOf(xmlContents.get("temperature_current_C"))) * (9/5)) + 32;
			temp24Num = ((Float.valueOf(xmlContents.get("temperature_24hrmax_C"))) * (9/5)) + 32;
			temp24MinNum = ((Float.valueOf(xmlContents.get("temperature_24hrmin_C"))) * (9/5)) + 32;
		}
		
		//Convert Update Time
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		//Date String
		String dateString = xmlContents.get("observation_month_number") + "/" +
				xmlContents.get("observation_day") + "/" + xmlContents.get("observation_year") +
				" " + xmlContents.get("observation_hour") + ":" + xmlContents.get("observation_minute");
		try {
			currentFileDateTime = df.parse(dateString);
		} catch (ParseException e) {
			testView.append("\nDate Parsing Error");
			//e.printStackTrace();
		}
		
		//Update temperature
		tempValue.setText(String.valueOf(tempNum) + tempSuffix);
		temp24.setText(String.valueOf(temp24Num) + tempSuffix);
		temp24Min.setText(String.valueOf(temp24MinNum) + tempSuffix);
		//precipitation_24hr_mm
		precip24.setText(xmlContents.get("precipitation_24hr_mm") + " mm");
		updateDateTime.setText(df.format(currentFileDateTime));
		
		//Return result of update operation
		return success;
    }
    
}