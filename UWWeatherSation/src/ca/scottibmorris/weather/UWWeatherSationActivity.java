package ca.scottibmorris.weather;

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
}