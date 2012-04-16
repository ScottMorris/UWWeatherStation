package ca.scottibmorris.weather;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherGraphs extends Activity {

	/* 
     * Class Resource Variables
     */
	
	ImageView tempGraphImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		
		/* 
	     * Resource Variables
	     */
		
		/*
		 * Other Variables
		 */
		
		String graph;
		File filePath;
		
		/*
		 * Title Bar Objects
		 * *****************************************
		 */
		//University logo object
        ImageView logo = (ImageView)findViewById(R.id.uwLogo);
        logo.setImageResource(R.drawable.uwaterloo_logo);
        
        //Title Bar logo TextView Object
        TextView titleBarText = (TextView)findViewById(R.id.weatherLogoText);
        
        //Set Title Bar colours to White
        logo.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        titleBarText.setTextColor(Color.WHITE);
        /*
         * End of Title Bar Objects
         * ****************************************
         */
		
		tempGraphImage = (ImageView)findViewById(R.id.tempGraphImageView);
		try {
			graph = fetchGraphs();
		filePath = getFileStreamPath(graph);
		tempGraphImage.setImageDrawable(Drawable.createFromPath(filePath.getPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			tempGraphImage.setImageResource(R.drawable.icon);
			e.printStackTrace();
		}
	}
	
	/*
	 * fetchGraphs
	 * Retrieve weather graphs from weather.uwaterloo.ca 
	 */
	
	private String fetchGraphs() throws IOException {
		
		//Download garph data
		String tempGraphURLString = "http://weather.uwaterloo.ca/download/AirTemp_0.jpg";
		URL tempGraphURL = new URL(tempGraphURLString);
		URLConnection conn = tempGraphURL.openConnection();
		BufferedInputStream imgIn = new BufferedInputStream(conn.getInputStream());
		String tempGraphFilename = "tempGraph.jpg";
		FileOutputStream fos = openFileOutput(tempGraphFilename, MODE_PRIVATE);
		//Write data to file
		int i;
		while ((i = imgIn.read()) != -1) {
		    fos.write(i);
		}
		fos.flush();
		
		return "tempGraph.jpg";
	}
	
}
