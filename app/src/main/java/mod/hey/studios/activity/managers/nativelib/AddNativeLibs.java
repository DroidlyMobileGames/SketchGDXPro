package mod.hey.studios.activity.managers.nativelib;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mod.agus.jcoderz.lib.FilePathUtil;
import mod.agus.jcoderz.lib.FileUtil;

public class AddNativeLibs {
    private FilePathUtil fpu = new FilePathUtil();
    private String numProj;
    Context context;

    public AddNativeLibs(Context context,String numProj){
        this.context = context;
        this.numProj = numProj;
        FileUtil.makeDir(fpu.getPathNativelibs(numProj));
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/arm64-v8a");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/armeabi");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/armeabi-v7a");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/x86");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/x86_64");
        copyNativeLibraries();
    }
    public void copyNativeLibraries(){
        //Copys items from assets to whatever location you'd like
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        }catch (IOException e){

        }
        for (String filename : files){
            InputStream in = null;
            OutputStream out = null;
            File outFile = null;
            if (filename.equals("arm64-v8a.zip")) {
                try {

                    in = assetManager.open("arm64-v8a.zip");
                    outFile = new File(fpu.getPathNativelibs(numProj) + "/arm64-v8a", "arm64-v8a.zip");

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
            if (filename.equals("armeabi.zip")) {
                try {

                    in = assetManager.open("armeabi.zip");
                    outFile = new File(fpu.getPathNativelibs(numProj) + "/armeabi", "armeabi.zip");

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

            if (filename.equals("armeabi-v7a.zip")) {
                try {

                    in = assetManager.open("armeabi-v7a.zip");
                    outFile = new File(fpu.getPathNativelibs(numProj) + "/armeabi-v7a", "armeabi-v7a.zip");

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
            if (filename.equals("x86.zip")) {
                try {

                    in = assetManager.open("x86.zip");
                    outFile = new File(fpu.getPathNativelibs(numProj) + "/x86", "x86.zip");

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
            if (filename.equals("x86_64.zip")) {
                try {

                    in = assetManager.open("x86_64.zip");
                    outFile = new File(fpu.getPathNativelibs(numProj) + "/x86_64", "x86_64.zip");

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

        if (!FileUtil.isExistFile(fpu.getPathNativelibs(numProj) + "/armeabi/libgdx.so")) {
            unzip(fpu.getPathNativelibs(numProj) + "/armeabi/armeabi.zip",
                    fpu.getPathNativelibs(numProj) + "/armeabi");
        }
        if (!FileUtil.isExistFile(fpu.getPathNativelibs(numProj) + "/arm64-v8a/libgdx.so")) {
            unzip(fpu.getPathNativelibs(numProj) + "/arm64-v8a/arm64-v8a.zip",
                    fpu.getPathNativelibs(numProj) + "/arm64-v8a");
        }
        if (!FileUtil.isExistFile(fpu.getPathNativelibs(numProj) + "/armeabi-v7a/libgdx.so")) {
            unzip(fpu.getPathNativelibs(numProj) + "/armeabi-v7a/armeabi-v7a.zip",
                    fpu.getPathNativelibs(numProj) + "/armeabi-v7a");
        }
        if (!FileUtil.isExistFile(fpu.getPathNativelibs(numProj) + "/x86/libgdx.so")) {
            unzip(fpu.getPathNativelibs(numProj) + "/x86/x86.zip",
                    fpu.getPathNativelibs(numProj) + "/x86");
        }
        if (!FileUtil.isExistFile(fpu.getPathNativelibs(numProj) + "/x86_64/libgdx.so")) {
            unzip(fpu.getPathNativelibs(numProj) + "/x86_64/x86_64.zip",
                    fpu.getPathNativelibs(numProj) + "/x86_64");
        }
        FileUtil.deleteFile(fpu.getPathNativelibs(numProj) + "/armeabi/armeabi.zip");
        FileUtil.deleteFile(fpu.getPathNativelibs(numProj) + "/arm64-v8a/arm64-v8a.zip");
        FileUtil.deleteFile(fpu.getPathNativelibs(numProj) + "/armeabi-v7a/armeabi-v7a.zip");
        FileUtil.deleteFile(fpu.getPathNativelibs(numProj) + "/x86/x86.zip");
        FileUtil.deleteFile(fpu.getPathNativelibs(numProj) + "/x86_64/x86_64.zip");
        FileUtil.deleteFile(fpu.getPathLocalLibrary(numProj) + "/x86_64/x86_64.zip");
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
