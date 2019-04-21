
package com.fts;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.fts.sqlite.Client;
import com.fts.sqlite.TableProvider;

import org.json.JSONObject;

public class RNFtsSqliteLibraryModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  TableProvider tableProvider = null;
  Client client = null;

  public RNFtsSqliteLibraryModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @ReactMethod
  public void client(String dbName, Integer dbVersion, Promise promise) {
    try {
      this.client = new Client(getReactApplicationContext(), dbName, dbVersion);

      this.tableProvider = new TableProvider(this.client);

      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void beginTransactionDB(Promise promise) {
    try {
      this.client.beginTransaction();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void commitDB(Promise promise) {
    try {
      this.client.commit();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void rollbackDB(Promise promise) {
    try {
      this.client.rollback();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void closeClient(Promise promise) {
    try {
      this.client.close();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void execSQL(String sql, Promise promise) {
    try {
      this.client.execSQL(sql);
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void insert(String tableName, String obj, Promise promise) {
    try {
      JSONObject jsonObj = new JSONObject(obj);

      Long rowId = this.tableProvider.insert(tableName, jsonObj);

      promise.resolve(rowId.toString());
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void beginTransaction(Promise promise) {
    try {
      this.tableProvider.beginTransaction();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void commit(Promise promise) {
    try {
      this.tableProvider.commit();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void rollback(Promise promise) {
    try {
      this.tableProvider.rollback();
      promise.resolve(true);
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void query(String sql, String selectionArgs, Promise promise) {
    try {
      String[] stringArgs = {};

      if (!selectionArgs.isEmpty()) {
        stringArgs = selectionArgs.split(",");
      }

      JSONObject result = this.tableProvider.query(sql, stringArgs);

      promise.resolve(result.toString());
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void delete(String sql, String whereQuery, String whereArgs, Promise promise) {
    try {
      String[] stringArgs = {};

      if (!whereArgs.isEmpty()) {
        stringArgs = whereArgs.split(",");
      }

      Integer deletedRows = this.tableProvider.delete(sql, whereQuery, stringArgs);
      promise.resolve(deletedRows.toString());
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @ReactMethod
  public void update(String tableName, String obj, String whereQuery, String whereArgs, Promise promise) {
    try {
      JSONObject jsonObj = new JSONObject(obj);

      String[] stringArgs = {};

      if (!whereArgs.isEmpty()) {
        stringArgs = whereArgs.split(",");
      }

      Integer updateRows = this.tableProvider.update(tableName, jsonObj, whereQuery, stringArgs);
      promise.resolve(updateRows.toString());
    } catch (Exception ex) {
      promise.reject(ex.getMessage());
    }
  }

  @Override
  public String getName() {
    return "RNFtsSqliteLibrary";
  }
}