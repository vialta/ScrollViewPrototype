// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Data;

import android.database.sqlite.SQLiteDatabase;
import java.util.List;

import qualteh.com.scrollviewprototype.Model.Building;
import qualteh.com.scrollviewprototype.Model.MainCoordinates;
import qualteh.com.scrollviewprototype.Model.MapModel;
import qualteh.com.scrollviewprototype.Model.Storage;

public interface IDbHelper
{

    public abstract void addBuildings ( SQLiteDatabase sqlitedatabase, List<Building> list, int i );

    public abstract void addMainCoordinates ( SQLiteDatabase sqlitedatabase, MainCoordinates maincoordinates, int i );

    public abstract void addMapModel ( SQLiteDatabase sqlitedatabase, MapModel mapmodel );

    public abstract void addStorages ( SQLiteDatabase sqlitedatabase, List<Storage> list, int i );

    public abstract void dropBuildings ( SQLiteDatabase sqlitedatabase );

    public abstract void dropBuildingsCoordinates ( SQLiteDatabase sqlitedatabase );

    public abstract void dropMainCoordinates ( SQLiteDatabase sqlitedatabase );

    public abstract void dropStorages ( SQLiteDatabase sqlitedatabase );

    public abstract void dropStoragesCoordinates ( SQLiteDatabase sqlitedatabase );

    public abstract List getBuildingCoordinates ( SQLiteDatabase sqlitedatabase, int i );

    public abstract List getBuildings ( SQLiteDatabase sqlitedatabase );

    public abstract MainCoordinates getMainCoordinates ( SQLiteDatabase sqlitedatabase, int i );

    public abstract MapModel getMapModel ( SQLiteDatabase sqlitedatabase );

    public abstract List getStorageCoordinates ( SQLiteDatabase sqlitedatabase, String s );

    public abstract List getStorages ( SQLiteDatabase sqlitedatabase );

    public abstract void updateBuildings ( SQLiteDatabase sqlitedatabase, List list, int i );

    public abstract void updateMainCoordinates ( SQLiteDatabase sqlitedatabase, MainCoordinates maincoordinates, int i );

    public abstract void updateMapModel ( SQLiteDatabase sqlitedatabase, MapModel mapmodel, int i );

    public abstract void updateStorages ( SQLiteDatabase sqlitedatabase, List list, int i );
}
