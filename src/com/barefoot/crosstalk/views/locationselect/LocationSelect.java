package com.barefoot.crosstalk.views.locationselect;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.barefoot.crosstalk.R;
import com.barefoot.crosstalk.components.location.LocationHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationSelect extends MapActivity {
	
	private MapView map;
	private GeoPoint myLastKnownLocation;
	private SitesOverlay sitesOverlay;
	
	private GeoPoint currentPinGeoLocation;
	private int currentZoomLevel;
	private int resultStatus = RESULT_CANCELED;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_select);
		map = (MapView)findViewById(R.id.location_select_map);
		
		myLastKnownLocation = new LocationHelper(getApplicationContext()).getBestLastKnownGeoPoint();
		zoomIfLocationPresent(myLastKnownLocation, map);
		
		Drawable marker=getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		bindUIComponents();
		
		sitesOverlay = new SitesOverlay(marker);
		map.getOverlays().add(sitesOverlay);
	}

	private void bindUIComponents() {
		ImageButton toggleButton = (ImageButton)this.findViewById(R.id.location_select_map_toggle_button);
		toggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sitesOverlay.togglePinVisbility();
			}
		});
	}

	private void zoomIfLocationPresent(GeoPoint myLastKnownLocation, MapView mapView) {
		GeoPoint defaultCenteredLocation = new GeoPoint(36597889, -3515625);
		int defaultZoomLevel = 1;
		if(myLastKnownLocation != null) {
			defaultZoomLevel = 17;
			defaultCenteredLocation = myLastKnownLocation;
		}
		
		map.getController().setCenter(defaultCenteredLocation);
		map.getController().setZoom(defaultZoomLevel);
		map.setBuiltInZoomControls(true);
	}

 	@Override
	protected boolean isRouteDisplayed() {
		return(false);
	}
 	
 	@Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		if (keyCode == KeyEvent.KEYCODE_BACK) {
 			Intent data = null;
 			if(resultStatus == RESULT_OK) {
	 			data = new Intent();
	 			data.putExtra("latitude", currentPinGeoLocation.getLatitudeE6());
	 			data.putExtra("longitude", currentPinGeoLocation.getLongitudeE6());
	 			data.putExtra("zoom_level", currentZoomLevel);
	        }
 			setResult(resultStatus, data);
 			finish();
 			return true;
	      }
	      return super.onKeyDown(keyCode, event);
 	}
 	
 	private void updateResult(GeoPoint geopoint) {
 		currentPinGeoLocation = geopoint;
 		currentZoomLevel = map.getZoomLevel();
 		resultStatus = RESULT_OK;
 	}
 	
 	private void resetResult() {
 		currentPinGeoLocation = null;
 		currentZoomLevel = 0;
 		resultStatus = RESULT_CANCELED;
 	}

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<LocationPin> items=new ArrayList<LocationPin>();
		private Drawable marker=null;
		private LocationPin inDrag=null;
		private ImageView dragImage=null;

		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker=marker;

			dragImage=(ImageView)findViewById(R.id.drag);
			
			populate();
		}
		
		protected void togglePinVisbility() {
			if(size() == 0 && inDrag == null) {
				updateResult(map.getMapCenter());
				items.add(new LocationPin(map.getMapCenter(),"Selector", "Select Location", dragImage));				
			} else if(items.size() > 0 && inDrag == null)  {
				resetResult();
				items.clear();
			}
			
			populate();
			map.invalidate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return items.get(i);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView,boolean shadow) {
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		}
 		
		@Override
		public int size() {
			return items.size();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			boolean result=false;
				final int action=event.getAction();
				final int x=(int)event.getX();
				final int y=(int)event.getY();
	
				if (action==MotionEvent.ACTION_DOWN) {
					if(items != null  && items.size() > 0) {
						Point p=new Point(0,0);
						map.getProjection().toPixels(getLocationPin().getPoint(), p);
						if (hitTest(getLocationPin(), marker, x-p.x, y-p.y)) { 
							getLocationPin().swapMeWithImpersonater(x, y, map, p);
							inDrag = getLocationPin();
							items.remove(getLocationPin());
							populate();
							dragImage.setVisibility(View.VISIBLE);
							resetResult();
							result = true;
						}
					}
				}
				else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
					inDrag.setImpersonaterPosition(x, y);
					result=true;
				}
				else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
					items.clear();
					LocationPin newLocationPin = inDrag.swapImpersonaterWithMe(x, y, map);
					items.add(newLocationPin);
					populate();
					inDrag = null;
					updateResult(newLocationPin.getPoint());
					result=true;
				}

			return(result || super.onTouchEvent(event, mapView));
		}

		private LocationPin getLocationPin() {
			if(items.size() > 0) {
				return items.get(0);
			}
			
			return null;
		}
	}
}