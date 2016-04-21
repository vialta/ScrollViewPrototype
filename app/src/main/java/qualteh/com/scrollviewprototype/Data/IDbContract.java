// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype.Data;


public interface IDbContract
{

    public static final String BUILDING_COLUMNS[] = {
        "_id", "_map_id", "_name", "_building_stroke_width", "_building_stroke_color"
    };
    public static final String BUILDING_COORDINATE_COLUMNS[] = {
        "_id", "_x", "_y", "_building_id"
    };
    public static final String COLUMN_BUILDING_COORDINATE_BUILDING_ID = "_building_id ";
    public static final String COLUMN_BUILDING_COORDINATE_X = "_x";
    public static final String COLUMN_BUILDING_COORDINATE_Y = "_y";
    public static final String COLUMN_BUILDING_ID = "_id";
    public static final String COLUMN_BUILDING_MAP_ID = "_map_id";
    public static final String COLUMN_BUILDING_NAME = "_name";
    public static final String COLUMN_BUILDING_STROKE_COLOR = "_building_stroke_color";
    public static final String COLUMN_BUILDING_STROKE_WIDTH = "_building_stroke_width";
    public static final String COLUMN_DEFAULT_ID = "_id";
    public static final String COLUMN_MAP_COORDINATE_MAP_ID = "_map_id";
    public static final String COLUMN_MAP_COORDINATE_X = "_x";
    public static final String COLUMN_MAP_COORDINATE_Y = "_y";
    public static final String COLUMN_MAP_ID = "_map_id";
    public static final String COLUMN_MAP_NAME = "_name";
    public static final String COLUMN_MAP_STROKE_COLOR = "_map_stroke_color";
    public static final String COLUMN_MAP_STROKE_WIDTH = "_map_stroke_width";
    public static final String COLUMN_MAP_VERSION = "_version";
    public static final String COLUMN_STORAGE_COORDINATE_STORAGE_ID = "_storage_id";
    public static final String COLUMN_STORAGE_COORDINATE_X = "_x";
    public static final String COLUMN_STORAGE_COORDINATE_Y = "_y";
    public static final String COLUMN_STORAGE_ID = "_storage_id";
    public static final String COLUMN_STORAGE_MAP_ID = "_storage_map_id";
    public static final String COLUMN_STORAGE_NAME = "_name";
    public static final String COLUMN_STORAGE_STROKE_COLOR = "_storage_stroke_color";
    public static final String COLUMN_STORAGE_STROKE_WIDTH = "_storage_stroke_width";
    public static final String COLUMN_STORAGE_STOCK = "_stock";
    public static final String COLUMN_STORAGE_CAPACITY = "_capacity";
    public static final String COLUMN_COMMISSION_ID = "_commission_id";
    public static final String COLUMN_COMMISSION_COORDINATE_COMMISSION_ID ="_commission_id";
    public static final String COLUMN_COMMISSION_COORDINATE_X = "_x";
    public static final String COLUMN_COMMISSION_COORDINATE_Y = "_y";
    public static final String COLUMN_COMMISSION_STROKE_COLOR = "_commission_stroke_color";
    public static final String COLUMN_COMMISSION_STROKE_WIDTH = "_commission_stroke_width";
    public static final String COLUMN_COMMISSION_COORDINATE_ID = "_id";

    public static final String CREATE_TABLE_BUILDING = "CREATE TABLE IF NOT EXISTS building_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _map_id INTEGER NOT NULL, _name VARCHAR(255) NOT NULL, _building_stroke_width INTEGER NOT NULL, _building_stroke_color VARCHAR(7) NOT NULL ) ";
    public static final String CREATE_TABLE_BUILDING_COORDINATE = "CREATE TABLE IF NOT EXISTS building_coordinates_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _building_id INTEGER NOT NULL ) ";
    public static final String CREATE_TABLE_MAP_COORDINATE = "CREATE TABLE IF NOT EXISTS map_model_coordinates_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _map_id INTEGER NOT NULL )";
    public static final String CREATE_TABLE_MAP_MODEL = "CREATE TABLE IF NOT EXISTS map_model_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _map_id INTEGER NOT NULL, _name VARCHAR(255) NOT NULL, _version INTEGER NOT NULL, _map_stroke_width INTEGER NOT NULL,_map_stroke_color VARCHAR(7) NOT NULL )";
    public static final String CREATE_TABLE_STORAGE = "CREATE TABLE IF NOT EXISTS storage_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _storage_id VARCHAR(10) NOT NULL, _storage_map_id INTEGER NOT NULL, _storage_stroke_width INTEGER NOT NULL, _storage_stroke_color VARCHAR(7) NOT NULL, _stock INTEGER NOT NULL, _capacity INTEGER NOT NULL ) ";
    public static final String CREATE_TABLE_STORAGE_COORDINATE = "CREATE TABLE IF NOT EXISTS storage_table_coordinates (_id INTEGER PRIMARY KEY AUTOINCREMENT, _x REAL NOT NULL, _y REAL NOT NULL, _storage_id VARCHAR(10) NOT NULL )";
    public static final String CREATE_TABLE_COMMISSION = "CREATE TABLE IF NOT EXISTS commission_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, _commission_id VARCHAR(10) NOT NULL, _commission_stroke_width INTEGER NOT NULL, _commission_stroke_color VARCHAR(7) NOT NULL )";
    public static final String CREATE_TABLE_COMMISSION_COORDINATE = "CREATE TABLE IF NOT EXISTS commission_table_coordinates (_id INTEGER PRIMARY KEY AUTOINCREMENT, _commission_id INTEGER NOT NULL, _x REAL NOT NULL, _y REAL NOT NULL)";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "map_model.db";

    public static final String DROP_TABLE_BUILDING = "DROP TABLE IF EXISTS building_table";
    public static final String DROP_TABLE_BUILDING_COORDINATES = "DROP TABLE IF EXISTS building_coordinates_table";
    public static final String DROP_TABLE_MAP_MODEL = "DROP TABLE IF EXISTS map_model_table";
    public static final String DROP_TABLE_MAP_MODEL_COORDINATES = "DROP TABLE IF EXISTS map_model_coordinates_table";
    public static final String DROP_TABLE_STORAGE = "DROP TABLE IF EXISTS storage_table";
    public static final String DROP_TABLE_STORAGE_COORDINATES = "DROP TABLE IF EXISTS storage_table_coordinates";
    public static final String DROP_TABLE_COMMISSION = "DROP TABLE IF EXISTS commission_table";
    public static final String DROP_TABLE_COMMISSION_COORDINATES = "DROP TABLE IF EXISTS commission_table_coordinates";
    public static final String MAP_COLUMNS[] = {
        "_map_id", "_name", "_version", "_map_stroke_width", "_map_stroke_color"
    };
    public static final String MAP_COORDINATE_COLUMNS[] = {
        "_id", "_x", "_y", "_map_id"
    };
    public static final String STORAGE_COLUMNS[] = {
        "_id", "_storage_id", "_storage_map_id", "_name", "_storage_stroke_width", "_storage_stroke_color", "_filling"
    };
    public static final String STORAGE_COORDINATE_COLUMNS[] = {
        "_id", "_x", "_y", "_storage_id"
    };
    public static final String TABLE_NAME_BUILDING = "building_table";
    public static final String TABLE_NAME_BUILDING_COORDINATES = "building_coordinates_table";
    public static final String TABLE_NAME_MAP_MODEL = "map_model_table";
    public static final String TABLE_NAME_MAP_MODEL_COORDINATES = "map_model_coordinates_table ";
    public static final String TABLE_NAME_STORAGE = "storage_table";
    public static final String TABLE_NAME_STORAGE_COORDINATES = "storage_table_coordinates";
    public static final String TABLE_NAME_COMMISSION = "commission_table";
    public static final String TABLE_NAME_COMMISSION_COORDINATES = "commission_table_coordinates";

}
