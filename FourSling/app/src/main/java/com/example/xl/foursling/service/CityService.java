package com.example.xl.foursling.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.xl.foursling.db.DBManger;

/**
 * Created by admin on 2016/12/18.
 */

public class CityService extends Service {
    public DBManger dbManger;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开始导入sql数据
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbManger = new DBManger(getApplicationContext());
        dbManger.openSqlite();
        dbManger.downSqlite();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
