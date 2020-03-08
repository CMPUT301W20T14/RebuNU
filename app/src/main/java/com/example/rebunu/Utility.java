package com.example.rebunu;

import android.location.Location;

import androidx.annotation.Nullable;

public class Utility {
    public static Integer getEstimatePrice(Request request, @Nullable Float priceFactor) {
        float[] dis = new float[1];
        Integer price = 0;

        Location start = request.getStart();
        Location end = request.getEnd();
        Location.distanceBetween(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude(), dis);

        if(priceFactor != null) {
            price = (Integer)((Float)(dis[0] * priceFactor)).intValue();
        } else {
            price = (Integer)((Float)(dis[0])).intValue();
        }
        return price;
    }
}
