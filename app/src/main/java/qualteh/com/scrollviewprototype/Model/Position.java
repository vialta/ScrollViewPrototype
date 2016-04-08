// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Model;

import android.util.Log;
import qualteh.com.scrollviewprototype.Consts;

public class Position
{

    private double geoX;
    private double geoY;
    private int uiX;
    private int uiY;

    public Position(double d, double d1)
    {
        geoX = d;
        geoY = d1;
    }

    public void calculateUIPosition()
    {
        uiX = (int)((geoX - 21.462893999999999D) * 1000000D * Consts.UI_X_FACTOR + 20D * Consts.SIZE_MULTIPLIER);
        uiY = (int)(Math.abs(geoY - 46.321987999999997D) * 1000000D * Consts.UI_Y_FACTOR + 10D * Consts.SIZE_MULTIPLIER);
        Log.d("MACHINEUIXYPRE", (new StringBuilder()).append(uiX).append(" ").append(uiY).append(" ").toString());
        Log.d("MACHINEUIXYPOS", (new StringBuilder()).append(uiX).append(" ").append(uiY).append(" ").toString());
    }

    public double getGeoX()
    {
        return geoX;
    }

    public double getGeoY()
    {
        return geoY;
    }

    public int getUiX()
    {
        return uiX;
    }

    public int getUiY()
    {
        return uiY;
    }

    public void setGeoX(double d)
    {
        geoX = d;
    }

    public void setGeoY(double d)
    {
        geoY = d;
    }

    public void setUiX(int i)
    {
        uiX = i;
    }

    public void setUiY(int i)
    {
        uiY = i;
    }

    public void updateUIPositionByDensity(double d)
    {
        uiX = (int)((double)uiX * d);
        uiY = (int)((double)uiY * d);
    }
}
