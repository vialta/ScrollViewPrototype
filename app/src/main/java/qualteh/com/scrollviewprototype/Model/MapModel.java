// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Referenced classes of package qualteh.com.scrollviewprototype.Model:
//            Building, Storage, MainCoordinates

public class MapModel
{

    private Map additionalProperties;
    private List<Building> buildings = new ArrayList<Building>(  );
    private int id;
    private MainCoordinates mainCoordinates;
    private String name;
    private List<Storage> storage = new ArrayList<Storage>(  );
    private List<Commission> commissions = new ArrayList<>(  );
    private Integer version;



    public void adjustCoordinates(double d)
    {
        for(Building b : buildings){
            b.adjustCoordinates( d );
        }
        for(Storage s:storage){
            s.adjustCoordinates( d );
        }
        for(Commission c:commissions){
            c.adjustCoordinates( d );
        }
    }

    public void adjustCoordinates(int i)
    {
        for(Building b : buildings){
            b.adjustCoordinates( i );
        }
        for(Storage s:storage){
            s.adjustCoordinates( i );
        }
        for(Commission c:commissions){
            c.adjustCoordinates( i );
        }
    }

    public Map getAdditionalProperties()
    {
        return additionalProperties;
    }

    public List<Building> getBuildings()
    {
        return buildings;
    }

    public int getId()
    {
        return id;
    }

    public MainCoordinates getMainCoordinates()
    {
        return mainCoordinates;
    }

    public String getName()
    {
        return name;
    }

    public List<Commission> getCommissions(){ return commissions; }

    public List<Storage> getStorage()
    {
        return storage;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setAdditionalProperty(String s, Object obj)
    {
        additionalProperties.put(s, obj);
    }

    public void setBuildings(List<Building> list)
    {
        buildings = list;
    }

    public void setId(int i)
    {
        id = i;
    }

    public void setMainCoordinates(MainCoordinates maincoordinates)
    {
        mainCoordinates = maincoordinates;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setCommissions(List<Commission> list){ commissions=list; }

    public void setStorage(List<Storage> list)
    {
        storage = list;
    }

    public void setVersion(Integer integer)
    {
        version = integer;
    }
}
