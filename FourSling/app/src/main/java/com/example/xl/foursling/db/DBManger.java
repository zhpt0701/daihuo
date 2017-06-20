package com.example.xl.foursling.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.xl.foursling.R;

import java.io.*;

/**
 * Created by Administrator on 2016/4/20 0020.
 */
public class DBManger {
    public static final String packagesname = "com.example.xl.foursling";
    public static final String wenjian = "china_data.db";
    public static final String DB_PATH = "/data"+ Environment.getDataDirectory()+"/"+packagesname;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    public DBManger(Context context1){
            context = context1;
    }

    public void openSqlite() {
        sqLiteDatabase = openSqlites(DB_PATH + "/" + packagesname);
    }

    private SQLiteDatabase openSqlites(String s) {
        try {
            File file = new File(s);
            if (!file.exists()) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                Log.i(context+"","===8jksdlj==");
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.china_data); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(s);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    Log.i(context+"","===8jksdlj"+count+"-=-=");
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(s,
                    null);
            return db;
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void downSqlite() {
        sqLiteDatabase.close();
    }
}
