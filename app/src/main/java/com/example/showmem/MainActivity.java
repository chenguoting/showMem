package com.example.showmem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends Activity {
    private final static String TAG = "MemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MemThread().start();
    }

    class MemThread extends Thread {
        @Override
        public void run() {
            Log.i(TAG, "start...");
            File root = new File("/sdcard");
            ArrayList<Data> list = new ArrayList<>();
            File[] files = root.listFiles();
            for(File file : files) {
                long mem = getMem(file);
                putData(list, new Data(mem, file));
                Log.i(TAG, format(mem)+" "+file.getAbsolutePath());
            }
            Log.i(TAG, "sort result");
            for(Data data : list) {
                Log.i(TAG, format(data.mem)+" "+data.file.getAbsolutePath());
            }
            Log.i(TAG, "finish");
        }
    }

    void putData(ArrayList<Data> list, Data data) {
        for(int i=0;i<list.size();i++) {
            if(list.get(i).mem < data.mem) {
                list.add(i, data);
                return;
            }
        }
        list.add(data);
    }

    class Data {
        long mem;
        File file;

        public Data(long mem, File file) {
            this.mem = mem;
            this.file = file;
        }
    }

    long getMem(File file) {
        if(file.isFile()) {
            return file.length();
        }
        else {
            long mem = 0;
            File[] files = file.listFiles();
            for(File f : files) {
                mem += getMem(f);
            }
            return mem;
        }
    }

    String format(long mem) {
        String str = "";
        if(mem > 1024 * 1024 *1024) {
            str = (mem / (1024f * 1024 * 1024))+"G";
        }
        else if(mem > 1024 * 1024) {
            str = (mem / (1024f * 1024))+"M";
        }
        else if(mem > 1024) {
            str = (mem / 1024f)+"K";
        }
        else {
            str = mem +"B";
        }
        return str;
    }
}
