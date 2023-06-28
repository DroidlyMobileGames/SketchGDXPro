package com.besome.sketch.help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sketchware.remodgdx.R;

public class ModdingInfo extends AppCompatActivity {
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modding_info);
        about = findViewById(R.id.abouttext);
        about.setText("ABOUT SKETCHGDX\n\n" + "  SketchGDX was modded using the original Sketchware Pro Modded firmware found on Github.  This version of Sketchware was solely modded for the purpose of providing a way for developers that want to learn game development to do so using libGDX.  The modder of this software does not make any money or profits off the application as the original source code is available on Github for use of any changes needed.  \n\n  Thank you for using SketchGDX as this was a long process to remod to make it what it is today!");
    }
}