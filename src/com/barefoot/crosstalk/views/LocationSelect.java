package com.barefoot.crosstalk.views;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
				sitesOverlay.togglePin();
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

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items=new ArrayList<OverlayItem>();
		private Drawable marker=null;
		private OverlayItem inDrag=null;
		private ImageView dragImage=null;
		private int xDragImageOffset=0;
		private int yDragImageOffset=0;
		private int xDragTouchOffset=0;
		private int yDragTouchOffset=0;

		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker=marker;

			dragImage=(ImageView)findViewById(R.id.drag);
			xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
			yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
			
			populate();
		}
		
		protected void togglePin() {
			if(size() == 0 && inDrag == null) {
				items.add(new OverlayItem(map.getMapCenter(),"Selector", "Select Location"));				
			} else if(items.size() > 0 && inDrag == null)  {
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
						result = swapDraggedItemWithDragImage(x, y);		
					}
				}
				else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
					setDragImagePosition(x, y);
					result=true;
				}
				else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
					swapDragImageWithDroppedItem(x, y);
					result=true;
				}

			return(result || super.onTouchEvent(event, mapView));
		}
		
		private boolean swapDraggedItemWithDragImage(int x, int y) {
			OverlayItem item = items.get(0);
			Point p=new Point(0,0);
			map.getProjection().toPixels(item.getPoint(), p);
			if (hitTest(item, marker, x-p.x, y-p.y)) {
				inDrag=item;
				items.remove(inDrag);
				populate();

				xDragTouchOffset=0;
				yDragTouchOffset=0;

				setDragImagePosition(p.x, p.y);
				dragImage.setVisibility(View.VISIBLE);

				xDragTouchOffset=x-p.x;
				yDragTouchOffset=y-p.y;
				return true;
			}
			return false;
		}
		
		private void swapDragImageWithDroppedItem(int x, int y) {
			dragImage.setVisibility(View.GONE);

			GeoPoint pt=map.getProjection().fromPixels(x-xDragTouchOffset, y-yDragTouchOffset);
			OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(),inDrag.getSnippet());
			items.clear();
			items.add(toDrop);
			populate();

			inDrag=null;
		}

		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams)dragImage.getLayoutParams();
			lp.setMargins(x-xDragImageOffset-xDragTouchOffset, y-yDragImageOffset-yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}
}