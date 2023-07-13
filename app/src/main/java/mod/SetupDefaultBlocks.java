package mod;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mod.agus.jcoderz.lib.FilePathUtil;
import mod.agus.jcoderz.lib.FileUtil;

public class SetupDefaultBlocks {

    private FilePathUtil fpu = new FilePathUtil();
    private String blockpath = "";
    Context context;

    public SetupDefaultBlocks(Context context){
        this.context = context;
        blockpath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/resources/block/My Block");
        if (!FileUtil.isDirectory(blockpath)){
            FileUtil.makeDir(blockpath);
            checkPath();
        }else {
            checkPath();
        }

    }
    public void checkPath(){
        if (!FileUtil.isExistFile(blockpath + "/block.json")){
        }
        copyBlocks();
    }

    private void copyBlocks() {
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
            if (filename.equals("block.json")) {
                try {
                    in = assetManager.open("block.json");
                    outFile = new File(blockpath + "/", "block.json");
                    out = new FileOutputStream(outFile);
                    _copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    in = null;
                    out = null;
                } catch (IOException e) {

                }
            }
            if (filename.equals("palette.json")) {
                try {
                    in = assetManager.open("palette.json");
                    outFile = new File(blockpath + "/", "palette.json");
                    out = new FileOutputStream(outFile);
                    _copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    in = null;
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
    }
}
