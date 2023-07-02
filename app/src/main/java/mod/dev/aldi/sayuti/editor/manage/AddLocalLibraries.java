package mod.dev.aldi.sayuti.editor.manage;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.sketchware.remodgdx.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mod.SketchwareUtil;
import mod.agus.jcoderz.lib.FilePathUtil;
import mod.agus.jcoderz.lib.FileUtil;
import mod.hey.studios.activity.managers.nativelib.ManageNativelibsActivity;
import mod.hey.studios.util.Helper;

public class AddLocalLibraries {
    Context context;
    private String local_libraryGSON = "[{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-1.11/classes.jar\",\"name\":\"libgdx-1.11\",\"packageName\":\"com.badlogic.gdx\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-freetype-1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-freetype-1.11/classes.jar\",\"name\":\"libgdx-freetype-1.11\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxbackend1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxbackend1.11/classes.jar\",\"name\":\"libgdxbackend1.11\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxloader/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxloader/classes.jar\",\"name\":\"libgdxloader\"}]";
    private String local_libs_path = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/libs/local_libs/");
    private String projNum;
    private FilePathUtil fpu = new FilePathUtil();
    private String projectPath = "null";
    public AddLocalLibraries(Context context,String projNum){
        this.context = context;
        this.projNum = projNum;
        local_libs_path = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/libs/local_libs/");
        projectPath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/").concat(projNum.concat("/local_library"));
        if (!FileUtil.isExistFile(local_libs_path + "libgdx-1.11")) {
            FileUtil.makeDir(local_libs_path);
            setupLibraries();
        }
    }

    private void setupLibraries(){
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        }catch (IOException e){

        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            File outFile = null;
            if (filename.equals("libgdx-1.11.zip")) {
                try {
                    in = assetManager.open("libgdx-1.11.zip");
                    outFile = new File(local_libs_path, "libgdx-1.11.zip");
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
        unzip(local_libs_path + "libgdx-1.11.zip",local_libs_path);
        FileUtil.deleteFile(local_libs_path + "libgdx-1.11.zip");
    }

    public boolean checkPath(){
        boolean pathexists = false;
        if (!FileUtil.isExistFile(projectPath)) {
            pathexists = true;
        }
        return pathexists;
    }
    public void setProjectLibraries(){
        local_libraryGSON = "[{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-1.11/classes.jar\",\"name\":\"libgdx-1.11\",\"packageName\":\"com.badlogic.gdx\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-freetype-1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdx-freetype-1.11/classes.jar\",\"name\":\"libgdx-freetype-1.11\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxbackend1.11/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxbackend1.11/classes.jar\",\"name\":\"libgdxbackend1.11\"},{\"dexPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxloader/classes.dex\",\"jarPath\":\"/storage/emulated/0/.sketchwaregames/libs/local_libs/libgdxloader/classes.jar\",\"name\":\"libgdxloader\"}]";
        FileUtil.writeFile(projectPath,local_libraryGSON);
    }
    public void unzip(String _fileZip,String _destDir){
        try
        {
            java.io.File outdir = new java.io.File(_destDir);
            java.util.zip.ZipInputStream zin = new java.util.zip.ZipInputStream
                    (new java.io.FileInputStream(_fileZip));
            java.util.zip.ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null)
            {
                name = entry.getName();
                if(entry.isDirectory())
                {
                    mkdirs(outdir, name);
                    continue;
                }

                dir = dirpart(name);
                if(dir != null)
                    mkdirs(outdir, dir);

                extractFile(zin, outdir, name);
            }
            zin.close();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
    }
    private static void extractFile(java.util.zip.ZipInputStream in, java.io.File outdir, String name) throws java.io.IOException
    {
        byte[] buffer = new byte[4096];
        java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(outdir, name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1)
            out.write(buffer, 0, count);
        out.close();
    }

    private static void mkdirs(java.io.File outdir, String path)
    {
        java.io.File d = new java.io.File(outdir, path);
        if(!d.exists())
            d.mkdirs();
    }

    private static String dirpart(String name)
    {
        int s = name.lastIndexOf(java.io.File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }
}
