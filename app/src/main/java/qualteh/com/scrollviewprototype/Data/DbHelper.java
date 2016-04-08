// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import qualteh.com.scrollviewprototype.Model.Building;
import qualteh.com.scrollviewprototype.Model.MainCoordinates;
import qualteh.com.scrollviewprototype.Model.MapModel;
import qualteh.com.scrollviewprototype.Model.Storage;

// Referenced classes of package qualteh.com.scrollviewprototype.Data:
//            IDbHelper, IDbContract

public class DbHelper extends SQLiteOpenHelper
    implements IDbHelper, IDbContract
{

    public DbHelper(Context context)
    {
        super(context, "map_model.db", null, 1);
    }

    public void addBuildings(SQLiteDatabase sqlitedatabase, List list, int i)
    {
        for (int j = 0; j < list.size(); j++)
        {
            String query = ( new StringBuilder() ).append( "INSERT INTO building_table(_map_id, _name, _building_stroke_width, _building_stroke_color) VALUES (" ).append( i ).append( ", '" ).append( ( ( Building ) list.get( j ) ).getName() ).append( "', " ).append( ( ( Building ) list.get( j ) ).getStrokeWidth() ).append( ", '" ).append( ( ( Building ) list.get( j ) ).getStrokeColor() ).append( "' )" ).toString();
           // Log.d("Debug",query);
            sqlitedatabase.execSQL( query );
            for (int k = 0; k < ((Building)list.get(j)).getCoordinates().size(); k += 2)
            {
                query = (new StringBuilder()).append("INSERT INTO building_coordinates_table(_x, _y, _building_id) VALUES (").append(((Building)list.get(j)).getCoordinates().get(k)).append(", ").append(((Building)list.get(j)).getCoordinates().get(k + 1)).append(", ").append(j).append(")").toString();
             //   Log.d("Debug",query);
                sqlitedatabase.execSQL(query);
            }

        }

    }

    public void addMainCoordinates(SQLiteDatabase sqlitedatabase, MainCoordinates maincoordinates, int i)
    {
        for (int j = 0; j < maincoordinates.getCoordinates().size(); j += 2)
        {
            String query = (new StringBuilder()).append("INSERT INTO map_model_coordinates_table(_x, _y, _map_id ) VALUES (").append(maincoordinates.getCoordinates().get(j)).append(", ").append(maincoordinates.getCoordinates().get(j + 1)).append(", ").append(i).append(" )").toString();
           // Log.d("Debug",query);
            sqlitedatabase.execSQL(query);
        }

    }

    public void addMapModel(SQLiteDatabase sqlitedatabase, MapModel mapmodel)
    {
        String query = (new StringBuilder()).append("INSERT INTO map_model_table(_map_id, _name, _version, _map_stroke_width, _map_stroke_color) VALUES (").append(mapmodel.getId()).append(", '").append(mapmodel.getName()).append("', ").append(mapmodel.getVersion()).append(", ").append(mapmodel.getMainCoordinates().getStrokeWidth()).append(", '").append(mapmodel.getMainCoordinates().getStrokeColor()).append("' ").append(")").toString();
       // Log.d("Debug",query);
        sqlitedatabase.execSQL(query);
        addMainCoordinates(sqlitedatabase, mapmodel.getMainCoordinates(), mapmodel.getId());
        addBuildings(sqlitedatabase, mapmodel.getBuildings(), mapmodel.getId());
        addStorages(sqlitedatabase, mapmodel.getStorage(), mapmodel.getId());
    }

    public void addStorages(SQLiteDatabase sqlitedatabase, List list, int i)
    {
        for (int j = 0; j < list.size(); j++)
        {
            String query = (new StringBuilder().append("INSERT INTO storage_table(_storage_map_id, _storage_id, _storage_stroke_width, _storage_stroke_color) VALUES (").append(i).append(", '").append(((Storage)list.get(j)).getId()).append("', ").append(((Storage)list.get(j)).getStrokeWidth()).append(", '").append(((Storage)list.get(j)).getStrokeColor()).append("' )").toString());
           // Log.d("Debug",query);
            sqlitedatabase.execSQL(query);
            for (int k = 0; k < ((Storage)list.get(j)).getCoordinates().size(); k += 2)
            {
                query =(new StringBuilder()).append("INSERT INTO storage_table_coordinates(_x, _y, _storage_id) VALUES (").append(((Storage)list.get(j)).getCoordinates().get(k)).append(", ").append(((Storage)list.get(j)).getCoordinates().get(k + 1)).append(", '").append(((Storage)list.get(j)).getId()).append("')").toString();
             //   Log.d("Debug",query);
                sqlitedatabase.execSQL(query);
            }

        }

    }

    public void dropBuildings(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS building_table");
    }

    public void dropBuildingsCoordinates(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS building_coordinates_table");
    }

    public void dropMainCoordinates(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS building_coordinates_table");
    }

    public void dropStorages(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS storage_table");
    }

    public void dropStoragesCoordinates(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL( "DROP TABLE IF EXISTS storage_table_coordinates" );
    }

    public List<Double> getBuildingCoordinates(SQLiteDatabase sqlitedatabase, int i)
    {
        ArrayList arraylist = new ArrayList();
        String query = (new StringBuilder()).append("SELECT * FROM building_coordinates_table WHERE _building_id = ").append(i).toString();
       // Log.d("Debug",query);
        Cursor cursor = sqlitedatabase.rawQuery(query, null);
        boolean isRecord;
        isRecord = cursor.moveToFirst();
//        sqlitedatabase = Boolean.valueOf(cursor.moveToFirst());
        i = cursor.getColumnIndex("_x");
        int j = cursor.getColumnIndex("_y");
        while(isRecord){
            arraylist.add(Double.valueOf(cursor.getDouble(i)));
            arraylist.add(Double.valueOf(cursor.getDouble(j)));
            isRecord=cursor.moveToNext();
        }
        cursor.close();
        return arraylist;
    }

    public List<Building> getBuildings(SQLiteDatabase sqlitedatabase)
    {
        Log.d("Fuck 2",sqlitedatabase.isOpen()+"");
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM building_table", null);
        boolean flag = cursor.moveToFirst();
        int i = cursor.getColumnIndex("_name");
        int k = cursor.getColumnIndex("_building_stroke_width");
        int l = cursor.getColumnIndex("_building_stroke_color");
        for (; flag; flag = cursor.moveToNext())
        {
            Building building = new Building();
            building.setName(cursor.getString(i));
            building.setStrokeWidth(Integer.valueOf(cursor.getInt(k)));
            building.setStrokeColor(cursor.getString(l));
            arraylist.add(building);
        }


        for (int j = 0; j < arraylist.size(); j++)
        {
            ((Building)arraylist.get(j)).setCoordinates(getBuildingCoordinates(sqlitedatabase, j));
        }
        cursor.close();
        Log.d( "Fuck", sqlitedatabase + "" );
        return arraylist;
    }

    public MainCoordinates getMainCoordinates(SQLiteDatabase sqlitedatabase, int i)
    {
        MainCoordinates mainCoordinates = new MainCoordinates();
        Cursor cursor = sqlitedatabase.rawQuery( "SELECT * FROM map_model_coordinates_table WHERE _map_id = '" + i + "'", null );
        boolean flag = cursor.moveToFirst();
        i = 0;
        int j = cursor.getColumnIndex("_x");
        int k = cursor.getColumnIndex("_y");
        //mainCoordinates.getCoordinates().clear();
        while (flag) 
        {
            mainCoordinates.getCoordinates().add(Double.valueOf(cursor.getDouble(j)));
            mainCoordinates.getCoordinates().add(Double.valueOf(cursor.getDouble(k)));
            flag = cursor.moveToNext();
            i++;
        }
        cursor.close();
        return mainCoordinates;
    }

    public MapModel getMapModel(SQLiteDatabase sqlitedatabase)
    {
        Log.d("TAG", "Loading Map Data");
        MapModel mapmodel = new MapModel();
        MainCoordinates maincoordinates = new MainCoordinates();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM map_model_table", null);
        boolean flag = cursor.moveToFirst();
        int i = cursor.getColumnIndex("_map_id");
        int j = cursor.getColumnIndex("_name");
        int k = cursor.getColumnIndex("_version");
        int l = cursor.getColumnIndex("_map_stroke_width");
        int i1 = cursor.getColumnIndex("_map_stroke_color");
        while(flag)
        {
            mapmodel.setId(i);
            mapmodel.setName( cursor.getString(j));
            mapmodel.setVersion( Integer.valueOf( cursor.getInt(k)));
            maincoordinates.setStrokeWidth( Integer.valueOf( cursor.getInt(l)));
            maincoordinates.setStrokeColor( cursor.getString(i1));
            flag = cursor.moveToNext();
        }

        Log.d( "Fuck Start", sqlitedatabase.isOpen() + "" );
        maincoordinates.setCoordinates( getMainCoordinates( sqlitedatabase, mapmodel.getId() ).getCoordinates() );
        Log.d( "Fuck 0", sqlitedatabase.isOpen() + "" );
        mapmodel.setMainCoordinates( maincoordinates );
        Log.d( "Fuck 1", sqlitedatabase.isOpen() + "" );
        mapmodel.setBuildings( getBuildings( sqlitedatabase ) );
        mapmodel.setStorage( getStorages( sqlitedatabase ) );
        cursor.close();
        return mapmodel;
    }

    public List<Double> getStorageCoordinates(SQLiteDatabase sqlitedatabase, String s)
    {
        ArrayList<Double> arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery((new StringBuilder()).append("SELECT * FROM storage_table_coordinates WHERE _storage_id = '").append(s).append("'").toString(), null);
        boolean isRecord = Boolean.valueOf(cursor.moveToFirst());
        int j = cursor.getColumnIndex("_x");
        int k = cursor.getColumnIndex("_y");
        while(isRecord)
        {
            arraylist.add(Double.valueOf(cursor.getDouble(j)));
            arraylist.add(Double.valueOf(cursor.getDouble(k)));
            isRecord = Boolean.valueOf(cursor.moveToNext());
        }

        cursor.close();
        return arraylist;
    }

    public List<Storage> getStorages(SQLiteDatabase sqlitedatabase)
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM storage_table", null);
        boolean flag = cursor.moveToFirst();
        int i = cursor.getColumnIndex("_storage_id");
        int k = cursor.getColumnIndex("_storage_stroke_width");
        int l = cursor.getColumnIndex("_storage_stroke_color");
        for (; flag; flag = cursor.moveToNext())
        {
            Storage storage = new Storage();
            storage.setId(cursor.getString(i));
            storage.setStrokeWidth(Integer.valueOf(cursor.getInt(k)));
            storage.setStrokeColor(cursor.getString(l));
            arraylist.add(storage);
        }

        cursor.close();
        for (int j = 0; j < arraylist.size(); j++)
        {
            ((Storage)arraylist.get(j)).setCoordinates(getStorageCoordinates(sqlitedatabase, ((Storage)arraylist.get(j)).getId()));
        }

        return arraylist;
    }

    public boolean isCurrentVersion(SQLiteDatabase sqlitedatabase, Integer integer)
    {
        boolean value = false;
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM map_model_table LIMIT 1", null);
        cursor.moveToFirst();
        Log.d("Cool","cool");
        if (cursor.getInt(cursor.getColumnIndex("_version")) == integer.intValue())
        {
            value=true;
        }
        cursor.close();
        return value;
    }

    public void onCreate(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS map_model_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _map_id INTEGER NOT NULL, _name VARCHAR(255) NOT NULL, _version INTEGER NOT NULL, _map_stroke_width INTEGER NOT NULL,_map_stroke_color VARCHAR(7) NOT NULL )");
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS map_model_coordinates_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _map_id INTEGER NOT NULL )");
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS building_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _map_id INTEGER NOT NULL, _name VARCHAR(255) NOT NULL, _building_stroke_width INTEGER NOT NULL, _building_stroke_color VARCHAR(7) NOT NULL ) ");
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS building_coordinates_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _building_id INTEGER NOT NULL ) ");
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS storage_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _storage_id VARCHAR(10) NOT NULL, _storage_map_id INTEGER NOT NULL, _storage_stroke_width INTEGER NOT NULL, _storage_stroke_color VARCHAR(7) NOT NULL ) ");
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS storage_table_coordinates (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _storage_id VARCHAR(10) NOT NULL )");
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS map_model_table");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS map_model_coordinates_table");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS building_table");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS building_coordinates_table");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS storage_table");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS storage_table_coordinates");
        onCreate(sqlitedatabase);
    }

    public void updateBuildings(SQLiteDatabase sqlitedatabase, List list, int i)
    {
        int j = 1;
        for (i = 0; i < list.size(); i++)
        {
            for (int k = 0; k < ((Building)list.get(i)).getCoordinates().size(); k += 2)
            {
                sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE building_coordinates_table SET _x = ").append(((Building)list.get(i)).getCoordinates().get(k)).append(", ").append("_y").append(" = ").append(((Building)list.get(i)).getCoordinates().get(k + 1)).append(" WHERE ").append("_id").append(" = ").append(j).append("").toString());
                j++;
            }

        }

        for (i = 0; i < list.size(); i++)
        {
            sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE building_table SET _name = '").append(((Building)list.get(i)).getName()).append("', ").append("_building_stroke_width").append(" = ").append(((Building)list.get(i)).getStrokeWidth()).append(",").append("_building_stroke_color").append(" = '").append(((Building)list.get(i)).getStrokeColor()).append("' WHERE ").append("_id").append(" = ").append(i + 1).append("").toString());
        }

    }

    public void updateMainCoordinates(SQLiteDatabase sqlitedatabase, MainCoordinates maincoordinates, int i)
    {
        for (i = 0; i < maincoordinates.getCoordinates().size(); i += 2)
        {
            sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE map_model_coordinates_table SET _x = ").append(maincoordinates.getCoordinates().get(i)).append(", ").append("_y").append(" = ").append(maincoordinates.getCoordinates().get(i + 1)).append(" WHERE ").append("_id").append(" = ").append(i / 2 + 1).append("").toString());
        }

    }

    public void updateMapModel(SQLiteDatabase sqlitedatabase, MapModel mapmodel, int i)
    {
        sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE map_model_table SET _name = '").append(mapmodel.getName()).append("', ").append("_version").append(" = ").append(mapmodel.getVersion()).append(", ").append("_map_stroke_width").append(" = ").append(mapmodel.getMainCoordinates().getStrokeWidth()).append(", ").append("_map_stroke_color").append(" = '").append(mapmodel.getMainCoordinates().getStrokeColor()).append("' WHERE ").append("_map_id").append(" = ").append(i).toString());
        updateMainCoordinates(sqlitedatabase, mapmodel.getMainCoordinates(), i);
        updateBuildings(sqlitedatabase, mapmodel.getBuildings(), i);
        updateStorages(sqlitedatabase, mapmodel.getStorage(), i);
    }

    public void updateStorages(SQLiteDatabase sqlitedatabase, List list, int i)
    {
        int j = 1;
        for (i = 0; i < list.size(); i++)
        {
            for (int k = 0; k < ((Storage)list.get(i)).getCoordinates().size(); k += 2)
            {
                sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE storage_table_coordinates SET _x = ").append(((Storage)list.get(i)).getCoordinates().get(k)).append(", ").append("_y").append(" = ").append(((Storage)list.get(i)).getCoordinates().get(k + 1)).append(" WHERE ").append("_id").append(" = '").append(j).append("'").toString());
                j++;
            }

        }

        for (i = 0; i < list.size(); i++)
        {
            sqlitedatabase.execSQL((new StringBuilder()).append("UPDATE storage_table SET _storage_id = '").append(((Storage)list.get(i)).getId()).append("', ").append("_storage_stroke_width").append(" = ").append(((Storage)list.get(i)).getStrokeWidth()).append(", ").append("_storage_stroke_color").append(" = '").append(((Storage)list.get(i)).getStrokeColor()).append("' WHERE ").append("_id").append(" = ").append(i + 1).append("").toString());
        }

    }
}
