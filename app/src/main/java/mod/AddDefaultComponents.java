package mod;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import mod.agus.jcoderz.lib.FileUtil;

public class AddDefaultComponents {
    private String componentpath;
    private Context context;
    public AddDefaultComponents(Context context){
        this.context = context;
        componentpath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/system/");
        if (!FileUtil.isExistFile(componentpath + "component.json")) {
            FileUtil.makeDir(componentpath);
            copyDefaultComponents();
        }

    }
    public void copyDefaultComponents() {
        //Copys items from assets to whatever location you'd like
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {

        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            File outFile = null;
            if (filename.equals("component.json")) {
                try {
                    in = assetManager.open("component.json");
                    //sortFileString(in);
                    outFile = new File(componentpath, "component.json");

                    out = new FileOutputStream(outFile);
                    _copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {

                }
            }
        }
    }
    private String sortFileString(InputStream in){
        //Come back to this later to sort the components by name
        String componentlist = "";

        ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
        listmap = new Gson().fromJson(componentlist,
                new TypeToken<ArrayList<HashMap<String,
                        Object>>>(){}.getType());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listmap.sort(new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                    return o1.get("varName").toString().compareTo(o2.get("varName").toString());
                }
            });
        }
        String file = new Gson().toJson(listmap);

        return file;
    }
    private void _copyFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        while (true){
            try {
                if (!((read = in.read(buffer)) !=-1)) break;
                out.write(buffer,0,read);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
