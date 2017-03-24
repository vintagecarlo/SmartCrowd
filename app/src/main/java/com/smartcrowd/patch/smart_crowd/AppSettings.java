package com.smartcrowd.patch.smart_crowd;

import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 11/02/2017.
 */
public class AppSettings {


    private int iv;
    private String settingsname;

    public AppSettings(int iv, String settingsname) {
        this.iv = iv;
        this.settingsname = settingsname;
    }

    public int getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }

    public String getSettingsname() {
        return settingsname;
    }

    public void setSettingsname(String settingsname) {
        this.settingsname = settingsname;
    }
}
