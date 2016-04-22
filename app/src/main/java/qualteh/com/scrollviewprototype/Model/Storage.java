// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import qualteh.com.scrollviewprototype.Consts;

public class Storage
{

    private Map additionalProperties;
    private List<Double> coordinates = new ArrayList<>(  );
    private String id;
    private String strokeColor;
    private Integer strokeWidth;
    private Integer stock;
    private Integer capacity;

    public void adjustCoordinates(double d)
    {
        for (int i = 0; i < coordinates.size(); i++)
        {
            coordinates.set(i, coordinates.get( i ) * d * Consts.SIZE_MULTIPLIER );
        }

        for (int j = 1; j < coordinates.size(); j += 2)
        {
            coordinates.set(j, ( ( double ) Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER * d - ( Double ) coordinates.get( j ) ) + 10D * Consts.SIZE_MULTIPLIER * d );
        }

    }

    public void adjustCoordinates(int i)
    {
        for (int j = 0; j < coordinates.size(); j++)
        {
            coordinates.set(j, ( Double ) coordinates.get( j ) * ( double ) i * Consts.SIZE_MULTIPLIER );
        }

        for (int k = 1; k < coordinates.size(); k += 2)
        {
            coordinates.set(k, ( ( double ) Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER * ( double ) i - ( Double ) coordinates.get( k ) ) + 10D * Consts.SIZE_MULTIPLIER * ( double ) i );
        }

    }

//    public void adjustStock(){
//        if(stock>capacity)
//            filling=100;
//        if(filling<0)
//            filling=0;
//    }

    public Map getAdditionalProperties()
    {
        return additionalProperties;
    }

    public List<Double> getCoordinates()
    {
        return coordinates;
    }

    public String getId()
    {
        return id;
    }

    public String getStrokeColor()
    {
        return strokeColor;
    }

    public Integer getStrokeWidth()
    {
        return strokeWidth;
    }

    public Integer getStock(){ return stock;}

    public Integer getCapacity() {return capacity;}

    public void setAdditionalProperty(String s, Object obj)
    {
        additionalProperties.put(s, obj);
    }

    public void setCoordinates(List list)
    {
        coordinates = list;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setStrokeColor(String s)
    {
        strokeColor = s;
    }

    public void setStrokeWidth(Integer integer)
    {
        strokeWidth = integer;
    }

    public void setStock ( Integer integer){ stock=integer;}

    public void setCapacity ( Integer integer){ capacity=integer;}
}
