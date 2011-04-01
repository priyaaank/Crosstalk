package com.barefoot.crosstalk.views.locationselect;

import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationPin extends OverlayItem {
	
	private ImageView dragImage=null;
	private int xDragImageOffset=0;
	private int yDragImageOffset=0;
	private int xDragTouchOffset=0;
	private int yDragTouchOffset=0;

	public LocationPin(GeoPoint point, String title, String snippet, ImageView impersonaterImage) {
		super(point, title, snippet);
		this.dragImage = impersonaterImage;
		xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
		yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
	}
	
	protected void swapMeWithImpersonater(final int x, final int y, final MapView map, final Point p) {
		xDragTouchOffset=0;
		yDragTouchOffset=0;

		setImpersonaterPosition(p.x, p.y);
		xDragTouchOffset=x-p.x;
		yDragTouchOffset=y-p.y;
	}
	
	protected LocationPin swapImpersonaterWithMe(final int x, final int y, final MapView map) {
		dragImage.setVisibility(View.GONE);
		GeoPoint pointToDropAt = map.getProjection().fromPixels(x-xDragTouchOffset, y-yDragTouchOffset);
		return new LocationPin(pointToDropAt, this.getTitle(), this.getSnippet(), dragImage);
	}

	protected void setImpersonaterPosition(final int x, final int y) {
		RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams)dragImage.getLayoutParams();
		lp.setMargins(x-xDragImageOffset-xDragTouchOffset, y-yDragImageOffset-yDragTouchOffset, 0, 0);
		dragImage.setLayoutParams(lp);
	}
}
