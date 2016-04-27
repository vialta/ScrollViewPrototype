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
import qualteh.com.scrollviewprototype.Model.Commission;
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

    public void addBuildings( SQLiteDatabase sqLiteDatabase, List<Building> buildingList, int i)
    {
        for (int j = 0; j < buildingList.size(); j++)
        {
            String query = "INSERT INTO "+
                    TABLE_NAME_BUILDING+
                    "(" +
                    COLUMN_BUILDING_MAP_ID+
                    ", " +
                    COLUMN_BUILDING_NAME+
                    ", " +
                    COLUMN_BUILDING_STROKE_WIDTH +
                    ", " +
                    COLUMN_BUILDING_STROKE_COLOR +
                    ") " +
                    "VALUES " +
                    "(" +
                    i +
                    ", '" +
                    (  buildingList.get( j ) ).getName() +
                    "', " +
                    (  buildingList.get( j ) ).getStrokeWidth() +
                    ", '" +
                    (  buildingList.get( j ) ).getStrokeColor() +
                    "' )" ;

            sqLiteDatabase.execSQL( query );
            for (int k = 0; k < (buildingList.get(j)).getCoordinates().size(); k += 2)
            {
                query = "INSERT INTO " +
                        TABLE_NAME_BUILDING_COORDINATES +
                        "(" +
                        COLUMN_BUILDING_COORDINATE_X +
                        ", " +
                        COLUMN_BUILDING_COORDINATE_Y +
                        ", " +
                        COLUMN_BUILDING_COORDINATE_BUILDING_ID +
                        ") " +
                        "VALUES " +
                        "("+
                        (buildingList.get(j)).getCoordinates().get(k)+
                        ", "+
                        (buildingList.get(j)).getCoordinates().get(k + 1)+
                        ", "+
                        j+
                        ")";
                sqLiteDatabase.execSQL(query);
            }
        }
    }

    public void addMainCoordinates( SQLiteDatabase sqLiteDatabase, MainCoordinates maincoordinates, int i)
    {
        for (int j = 0; j < maincoordinates.getCoordinates().size(); j += 2)
        {
            String query = "INSERT INTO " +
                    TABLE_NAME_MAP_MODEL_COORDINATES+
                    "(" +
                    COLUMN_MAP_COORDINATE_X +
                    ", " +
                    COLUMN_MAP_COORDINATE_Y +
                    ", " +
                    COLUMN_MAP_COORDINATE_MAP_ID +
                    " ) " +
                    "VALUES " +
                    "("+
                    maincoordinates.getCoordinates().get(j)+
                    ", "+
                    maincoordinates.getCoordinates().get(j + 1)+
                    ", "+
                    i+
                    " )";


            sqLiteDatabase.execSQL(query);
        }

    }

    public void addMapModel( SQLiteDatabase sqLiteDatabase, MapModel mapModel)
    {
        String query = "INSERT INTO " +
                TABLE_NAME_MAP_MODEL+
                "(" +
                COLUMN_MAP_ID +
                ", " +
                COLUMN_MAP_NAME +
                ", " +
                COLUMN_MAP_VERSION +
                ", " +
                COLUMN_MAP_STROKE_WIDTH +
                ", " +
                COLUMN_MAP_STROKE_COLOR +
                ") VALUES ("+
                mapModel.getId()+
                ", '"+
                mapModel.getName()+
                "', "+
                mapModel.getVersion()+
                ", "+
                mapModel.getMainCoordinates().getStrokeWidth()+
                ", '"+
                mapModel.getMainCoordinates().getStrokeColor()+
                "' "+
                ")";

        sqLiteDatabase.execSQL(query);
        addMainCoordinates( sqLiteDatabase, mapModel.getMainCoordinates(), mapModel.getId());
        addBuildings( sqLiteDatabase, mapModel.getBuildings(), mapModel.getId());
        addStorages( sqLiteDatabase, mapModel.getStorage(), mapModel.getId());
        addCommissions( sqLiteDatabase, mapModel.getCommissions(), mapModel.getId() );
    }

    public void addStorages( SQLiteDatabase sqLiteDatabase, List<Storage> list, int mapId)
    {
        for (int j = 0; j < list.size(); j++)
        {
            String query = "INSERT INTO " +
                    TABLE_NAME_STORAGE+
                    "(" +
                    COLUMN_STORAGE_MAP_ID +
                    ", " +
                    COLUMN_STORAGE_ID +
                    ", " +
                    COLUMN_STORAGE_STROKE_WIDTH +
                    ", " +
                    COLUMN_STORAGE_STROKE_COLOR +
                    ", " +
                    COLUMN_STORAGE_STOCK +
                    ", " +
                    COLUMN_STORAGE_CAPACITY +
                    ") " +
                    "VALUES ("+
                    mapId+
                    ", '"+
                    (list.get(j)).getId()+
                    "', "+
                    (list.get(j)).getStrokeWidth() +
                    ", '"+
                    (list.get(j)).getStrokeColor()+
                    "', "+
                    (list.get(j) ).getStock() +
                    ", "+
                    (list.get( j ).getCapacity())+
                    " )";

            sqLiteDatabase.execSQL(query);
            for (int k = 0; k < (list.get(j)).getCoordinates().size(); k += 2)
            {
                query ="INSERT INTO " +
                        TABLE_NAME_STORAGE_COORDINATES +
                        "(" +
                        COLUMN_STORAGE_COORDINATE_X +
                        ", " +
                        COLUMN_STORAGE_COORDINATE_Y +
                        ", " +
                        COLUMN_STORAGE_COORDINATE_STORAGE_ID +
                        ") VALUES ("+
                        (list.get(j)).getCoordinates().get(k)+
                        ", "+
                        (list.get(j)).getCoordinates().get(k + 1)+
                        ", '"+(list.get(j)).getId()+"')";

                sqLiteDatabase.execSQL(query);
            }
        }
    }

    @Override
    public void addCommissions ( SQLiteDatabase sqLiteDatabase, List<Commission> list, int i ) {
        for (int j = 0; j < list.size(); j++)
        {
            String query = "INSERT INTO " +
                    TABLE_NAME_COMMISSION+
                    "(" +
                    COLUMN_DEFAULT_ID +
                    ", " +
                    COLUMN_COMMISSION_ID +
                    ", " +
                    COLUMN_COMMISSION_STROKE_WIDTH+
                    ", '" +
                    COLUMN_COMMISSION_STROKE_COLOR +
                    "') " +
                    "VALUES ("+
                    (j+1)+
                    ", '"+
                    (list.get(j)).getId()+
                    "', "+
                    (list.get(j)).getStrokeWidth() +
                    ", '"+
                    (list.get(j)).getStrokeColor()+
                    "' )";
            Log.d("Query",query);
            sqLiteDatabase.execSQL(query);
            for (int k = 0; k < (list.get(j)).getCoordinates().size(); k += 2)
            {
                query ="INSERT INTO " +
                        TABLE_NAME_COMMISSION_COORDINATES +
                        "(" +
                        COLUMN_COMMISSION_COORDINATE_X +
                        ", " +
                        COLUMN_COMMISSION_COORDINATE_Y +
                        ", " +
                        COLUMN_COMMISSION_COORDINATE_COMMISSION_ID+
                        ") VALUES ("+
                        (list.get(j)).getCoordinates().get(k)+
                        ", "+
                        (list.get(j)).getCoordinates().get(k + 1)+
                        ", '"+
                        (list.get(j)).getId()+
                        "')";
                Log.d("Query",query);
                sqLiteDatabase.execSQL(query);
            }
        }
    }

    public void dropBuildings(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE_BUILDING);
    }

    public void dropBuildingsCoordinates(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE_BUILDING_COORDINATES);
    }

    public void dropMainTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(DROP_TABLE_MAP_MODEL);
    }

    public void dropMainCoordinates(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE_MAP_MODEL_COORDINATES);
    }

    public void dropStorages(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE_STORAGE);
    }

    public void dropStoragesCoordinates(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( DROP_TABLE_STORAGE_COORDINATES );
    }

    @Override
    public void dropCommission ( SQLiteDatabase sqLiteDatabase ) {
        sqLiteDatabase.execSQL( DROP_TABLE_COMMISSION );
    }

    @Override
    public void dropCommissionCoordinates ( SQLiteDatabase sqLiteDatabase ) {
        sqLiteDatabase.execSQL( DROP_TABLE_COMMISSION_COORDINATES );
    }

    public List<Double> getBuildingCoordinates(SQLiteDatabase sqlitedatabase, int i) {
        ArrayList arraylist = new ArrayList();
        String query = "SELECT * FROM " +
                TABLE_NAME_BUILDING_COORDINATES +
                " WHERE " +
                COLUMN_BUILDING_COORDINATE_BUILDING_ID +
                " = "+
                i+
                "";
        Cursor cursor = sqlitedatabase.rawQuery(query, null);
        boolean isRecord;
        isRecord = cursor.moveToFirst();
        int xColumnIndex = cursor.getColumnIndex(COLUMN_BUILDING_COORDINATE_X);
        int yColumnIndex = cursor.getColumnIndex(COLUMN_BUILDING_COORDINATE_Y);
        while(isRecord){
            arraylist.add( cursor.getDouble( xColumnIndex ) );
            arraylist.add( cursor.getDouble( yColumnIndex ) );
            isRecord=cursor.moveToNext();
        }
        cursor.close();
        return arraylist;
    }

    public List<Building> getBuildings(SQLiteDatabase sqlitedatabase)
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM " +
                TABLE_NAME_BUILDING, null);
        boolean flag = cursor.moveToFirst();
        int buildingNameIndex = cursor.getColumnIndex(COLUMN_BUILDING_NAME);
        int buildingStrokeWidthIndex = cursor.getColumnIndex(COLUMN_BUILDING_STROKE_WIDTH);
        int buildingStrokeColorIndex = cursor.getColumnIndex(COLUMN_BUILDING_STROKE_COLOR);
        for (; flag; flag = cursor.moveToNext())
        {
            Building building = new Building();
            building.setName(cursor.getString(buildingNameIndex));
            building.setStrokeWidth( cursor.getInt( buildingStrokeWidthIndex ) );
            building.setStrokeColor(cursor.getString(buildingStrokeColorIndex));
            arraylist.add(building);
        }
        for (int j = 0; j < arraylist.size(); j++)
        {
            ((Building)arraylist.get(j)).setCoordinates(getBuildingCoordinates(sqlitedatabase, j));
        }
        cursor.close();
        return arraylist;
    }

    public MainCoordinates getMainCoordinates(SQLiteDatabase sqlitedatabase, int i)
    {
        MainCoordinates mainCoordinates = new MainCoordinates();
        Cursor cursor = sqlitedatabase.rawQuery( "SELECT * FROM " +
                TABLE_NAME_MAP_MODEL_COORDINATES+
                " WHERE " +
                COLUMN_MAP_ID +
                " = " +
                i +
                " ", null );
        boolean flag = cursor.moveToFirst();
        int j = cursor.getColumnIndex(COLUMN_MAP_COORDINATE_X);
        int k = cursor.getColumnIndex(COLUMN_MAP_COORDINATE_Y);

        while (flag) 
        {
            mainCoordinates.getCoordinates().add( cursor.getDouble( j ) );
            mainCoordinates.getCoordinates().add( cursor.getDouble( k ) );
            flag = cursor.moveToNext();
        }
        cursor.close();
        return mainCoordinates;
    }

    public MapModel getMapModel(SQLiteDatabase sqlitedatabase)
    {
        MapModel mapmodel = new MapModel();
        MainCoordinates maincoordinates = new MainCoordinates();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM " +
                TABLE_NAME_MAP_MODEL, null);
        boolean flag = cursor.moveToFirst();
        int i = cursor.getColumnIndex(COLUMN_MAP_ID);
        int j = cursor.getColumnIndex(COLUMN_MAP_NAME);
        int k = cursor.getColumnIndex(COLUMN_MAP_VERSION);
        int l = cursor.getColumnIndex(COLUMN_MAP_STROKE_WIDTH);
        int i1 = cursor.getColumnIndex(COLUMN_MAP_STROKE_COLOR);
        while(flag)
        {
            mapmodel.setId(i);
            mapmodel.setName( cursor.getString(j));
            mapmodel.setVersion( Integer.valueOf( cursor.getInt(k)));
            maincoordinates.setStrokeWidth( Integer.valueOf( cursor.getInt(l)));
            maincoordinates.setStrokeColor( cursor.getString(i1));
            flag = cursor.moveToNext();
        }

        maincoordinates.setCoordinates( getMainCoordinates( sqlitedatabase, mapmodel.getId() ).getCoordinates() );
        mapmodel.setMainCoordinates( maincoordinates );
        mapmodel.setBuildings( getBuildings( sqlitedatabase ) );
        mapmodel.setStorage( getStorages( sqlitedatabase ) );
        mapmodel.setCommissions( getCommissions( sqlitedatabase ) );
        cursor.close();
        return mapmodel;
    }

    public List<Double> getStorageCoordinates(SQLiteDatabase sqlitedatabase, String s)
    {
        ArrayList<Double> arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT * FROM " +
                "storage_table_coordinates" +
                " WHERE " +
                "_storage_id" +
                " = '"+
                s+
                "'", null);
        boolean isRecord = cursor.moveToFirst();
        int j = cursor.getColumnIndex("_x");
        int k = cursor.getColumnIndex("_y");
        while(isRecord)
        {
            arraylist.add( cursor.getDouble( j ) );
            arraylist.add( cursor.getDouble( k ) );
            isRecord = cursor.moveToNext();
        }

        cursor.close();
        return arraylist;
    }

    public List<Storage> getStorages(SQLiteDatabase sqLiteDatabase)
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                TABLE_NAME_STORAGE, null);
        boolean flag = cursor.moveToFirst();
        int storageIdIndex = cursor.getColumnIndex(COLUMN_STORAGE_ID);
        int storageStrokeWidthIndex = cursor.getColumnIndex(COLUMN_STORAGE_STROKE_WIDTH);
        int storageStrokeColorIndex = cursor.getColumnIndex(COLUMN_STORAGE_STROKE_COLOR);
        int storageStockIndex = cursor.getColumnIndex( COLUMN_STORAGE_STOCK );
        int storageCapacityIndex = cursor.getColumnIndex( COLUMN_STORAGE_CAPACITY );
        for (; flag; flag = cursor.moveToNext())
        {
            Storage storage = new Storage();
            storage.setId(cursor.getString(storageIdIndex));
            storage.setStrokeWidth( cursor.getInt( storageStrokeWidthIndex ) );
            storage.setStrokeColor(cursor.getString(storageStrokeColorIndex));
            storage.setStock( cursor.getInt( storageStockIndex ) );
            storage.setCapacity( cursor.getInt( storageCapacityIndex ) );
            arraylist.add(storage);
        }

        cursor.close();
        for (int j = 0; j < arraylist.size(); j++)
        {
            ((Storage)arraylist.get(j)).setCoordinates(getStorageCoordinates(sqLiteDatabase, ((Storage)arraylist.get(j)).getId()));
        }

        return arraylist;
    }

    @Override
    public List getCommissions ( SQLiteDatabase sqLiteDatabase ) {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME_COMMISSION, null);
        boolean flag = cursor.moveToFirst();
        int commissionIdIndex = cursor.getColumnIndex(COLUMN_COMMISSION_ID);
        int commissionStrokeWidth = cursor.getColumnIndex(COLUMN_COMMISSION_STROKE_WIDTH);
        int commissionStrokeColor = cursor.getColumnIndex(COLUMN_COMMISSION_STROKE_COLOR);
        for (; flag; flag = cursor.moveToNext())
        {
            Commission commission = new Commission();
            commission.setId(cursor.getString(commissionIdIndex));
            commission.setStrokeWidth( cursor.getInt( commissionStrokeWidth ) );
            commission.setStrokeColor(cursor.getString(commissionStrokeColor));
            arraylist.add(commission);
        }

        cursor.close();
        for (int j = 0; j < arraylist.size(); j++)
        {
            ((Commission)arraylist.get(j)).setCoordinates(getCommissionsCoordinates(sqLiteDatabase, ((Commission)arraylist.get(j)).getId()));
        }

        return arraylist;
    }

    @Override
    public List getCommissionsCoordinates ( SQLiteDatabase sqLiteDatabase, String s ) {
        ArrayList<Double> arraylist = new ArrayList();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                TABLE_NAME_COMMISSION_COORDINATES +
                " WHERE " +
                COLUMN_COMMISSION_COORDINATE_COMMISSION_ID +
                " = '"+
                s+
                "'", null);
        boolean isRecord = cursor.moveToFirst();
        int indexCommissionCoordinateX = cursor.getColumnIndex(COLUMN_COMMISSION_COORDINATE_X);
        int indexCommissionCoordinateY = cursor.getColumnIndex(COLUMN_COMMISSION_COORDINATE_Y);
        while(isRecord)
        {
            arraylist.add( cursor.getDouble( indexCommissionCoordinateX ) );
            arraylist.add( cursor.getDouble( indexCommissionCoordinateY ) );
            isRecord = cursor.moveToNext();
        }

        cursor.close();
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
        sqlitedatabase.execSQL( CREATE_TABLE_MAP_MODEL );
        sqlitedatabase.execSQL( CREATE_TABLE_MAP_COORDINATE );
        sqlitedatabase.execSQL( CREATE_TABLE_BUILDING );
        sqlitedatabase.execSQL( CREATE_TABLE_BUILDING_COORDINATE );
        sqlitedatabase.execSQL( CREATE_TABLE_STORAGE );
        sqlitedatabase.execSQL( CREATE_TABLE_STORAGE_COORDINATE );
        sqlitedatabase.execSQL( CREATE_TABLE_COMMISSION );
        sqlitedatabase.execSQL( CREATE_TABLE_COMMISSION_COORDINATE );
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        sqlitedatabase.execSQL( DROP_TABLE_COMMISSION_COORDINATES );
        sqlitedatabase.execSQL( DROP_TABLE_COMMISSION );
        sqlitedatabase.execSQL( DROP_TABLE_STORAGE_COORDINATES );
        sqlitedatabase.execSQL( DROP_TABLE_STORAGE );
        sqlitedatabase.execSQL( DROP_TABLE_BUILDING_COORDINATES );
        sqlitedatabase.execSQL( DROP_TABLE_BUILDING );
        sqlitedatabase.execSQL( DROP_TABLE_MAP_MODEL_COORDINATES );
        sqlitedatabase.execSQL( DROP_TABLE_MAP_MODEL );

        onCreate(sqlitedatabase);
    }

    public void updateBuildings(SQLiteDatabase sqlitedatabase, List<Building> list, int i)
    {
        int j = 1;
        for (i = 0; i < list.size(); i++)
        {
            for (int k = 0; k < (list.get(i)).getCoordinates().size(); k += 2)
            {
                sqlitedatabase.execSQL( "UPDATE " +
                                "building_coordinates_table " +
                                "SET " +
                                "_x " +
                                "= "+
                                (list.get(i)).getCoordinates().get(k)+
                                ", "+
                                "_y"+
                                " = "+
                                (list.get(i)).getCoordinates().get(k + 1)+
                                " WHERE "+
                                "_id"+
                                " = "+
                                j);
                j++;
            }

        }

        for (i = 0; i < list.size(); i++)
        {
            sqlitedatabase.execSQL("UPDATE " +
                    "building_table " +
                    "SET " +
                    "_name " +
                    "= '"+
                    (list.get(i)).getName()+
                    "', "+
                    "_building_stroke_width"+
                    " = "+
                    (list.get(i)).getStrokeWidth()+
                    ","+
                    "_building_stroke_color"+
                    " = '"+
                    (list.get(i)).getStrokeColor()+
                    "' WHERE "+
                    "_id"+
                    " = "+
                    (i + 1));
        }

    }

    public void updateMainCoordinates(SQLiteDatabase sqlitedatabase, MainCoordinates maincoordinates, int i)
    {
        for (i = 0; i < maincoordinates.getCoordinates().size(); i += 2)
        {
            sqlitedatabase.execSQL("UPDATE " +
                    "map_model_coordinates_table" +
                    " SET " +
                    "_x " +
                    "= "+
                    maincoordinates.getCoordinates().get(i)+
                    ", "+
                    "_y"+
                    " = "+
                    maincoordinates.getCoordinates().get(i + 1)+
                    " WHERE "+
                    "_id"+
                    " = "+
                    (i / 2 + 1));
        }

    }

    public void updateMapModel(SQLiteDatabase sqlitedatabase, MapModel mapModel, int i)
    {
        sqlitedatabase.execSQL("UPDATE " +
                "map_model_table " +
                "SET " +
                "_name" +
                " = '"+
                mapModel.getName()+
                "', "+
                "_version"+
                " = "+
                mapModel.getVersion()+
                ", "+
                "_map_stroke_width"+
                " = "+
                mapModel.getMainCoordinates().getStrokeWidth()+
                ", "+
                "_map_stroke_color"+
                " = '"+
                mapModel.getMainCoordinates().getStrokeColor()+
                "' WHERE "+
                "_map_id"+
                " = "+
                (i));
        updateMainCoordinates(sqlitedatabase, mapModel.getMainCoordinates(), i);
        updateBuildings(sqlitedatabase, mapModel.getBuildings(), i);
        updateStorages(sqlitedatabase, mapModel.getStorage(), i);
        updateCommissions( sqlitedatabase, mapModel.getCommissions(), i );
    }

    public void updateStorages(SQLiteDatabase sqlitedatabase, List<Storage> list, int i) {
        int j = 1;
        for (i = 0; i < list.size(); i++)
        {
            for (int k = 0; k < (list.get(i)).getCoordinates().size(); k += 2)
            {
                sqlitedatabase.execSQL("UPDATE " +
                        TABLE_NAME_STORAGE_COORDINATES +
                        " SET " +
                        COLUMN_STORAGE_COORDINATE_X +
                        " = "+
                        (list.get(i)).getCoordinates().get(k)+
                        ", "+
                        COLUMN_STORAGE_COORDINATE_Y+
                        " = "+
                        (list.get(i)).getCoordinates().get(k + 1)+
                        " WHERE "+
                        COLUMN_DEFAULT_ID+
                        " = '"+
                        (j)+
                        "'");
                j++;
            }

        }

        for (i = 0; i < list.size(); i++)
        {
            sqlitedatabase.execSQL("UPDATE " +
                    TABLE_NAME_STORAGE +
                    " SET " +
                    COLUMN_STORAGE_ID +
                    "= '"+
                    (list.get(i)).getId() +
                    "', "+
                    COLUMN_STORAGE_STROKE_WIDTH+
                    " = "+
                    (list.get(i)).getStrokeWidth()+
                    ", "+
                    COLUMN_STORAGE_STROKE_COLOR+
                    " = '"+
                    (list.get(i)).getStrokeColor()+
                    "', " +
                    COLUMN_STORAGE_STOCK+
                    " = '"+
                    ( list.get( i ) ).getStock() +
                    "', "+
                    COLUMN_STORAGE_CAPACITY+
                    " = '"+
                    ( list.get( i ) ).getCapacity() +
                    "' WHERE "+
                    COLUMN_DEFAULT_ID+
                    " = "+
                    (i + 1));
        }
    }

    public void updateCommissions( SQLiteDatabase sqlitedatabase, List<Commission> list, int i) {
        int j = 1;
        for (i = 0; i < list.size(); i++) {
            for (int k = 0; k < (list.get(i)).getCoordinates().size(); k += 2) {
                sqlitedatabase.execSQL("UPDATE " +
                        TABLE_NAME_COMMISSION_COORDINATES +
                        " SET " +
                        COLUMN_COMMISSION_COORDINATE_X+
                        " = "+
                        (list.get(i)).getCoordinates().get(k)+
                        ", "+
                        COLUMN_COMMISSION_COORDINATE_Y+
                        " = "+
                        (list.get(i)).getCoordinates().get(k + 1)+
                        " WHERE "+
                        COLUMN_DEFAULT_ID+
                        " = '"+
                        (j)+
                        "'");
                j++;
            }
        }
        for (i = 0; i < list.size(); i++) {
            sqlitedatabase.execSQL("UPDATE " +
                    TABLE_NAME_COMMISSION +
                    " SET " +
                    COLUMN_COMMISSION_ID +
                    "= '"+
                    (list.get(i)).getId() +
                    "', "+
                    COLUMN_COMMISSION_STROKE_WIDTH+
                    " = "+
                    (list.get(i)).getStrokeWidth()+
                    ", "+
                    COLUMN_COMMISSION_STROKE_COLOR+
                    " = '"+
                    (list.get(i)).getStrokeColor()+

                    "' WHERE "+
                    COLUMN_DEFAULT_ID+
                    " = "+
                    (i + 1));
        }

    }

}
