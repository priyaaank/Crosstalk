package com.barefoot.crosstalk.views;

import java.util.List;

import android.os.Bundle;

import com.barefoot.crosstalk.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationSelect extends MapActivity {
	
	private MapView mapView;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.location_select);
		mapView = (MapView) findViewById(R.id.location_select_map);
		
		List<Overlay> overlays = mapView.getOverlays();
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
	
}