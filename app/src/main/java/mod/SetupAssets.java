package mod;

import java.io.File;

import mod.agus.jcoderz.lib.FileUtil;

public class SetupAssets {
    String projID;
    String imagespath,soundspath,fontspath;

    public SetupAssets(String projID){
       this.projID = projID;
       imagespath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/" + projID + "/files/assets/images");
       soundspath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/" + projID + "/files/assets/sounds");

        writeAssetFolders();
    }

    private void writeAssetFolders() {
        if (!FileUtil.isDirectory(imagespath)){
            FileUtil.makeDir(imagespath);
        }
        if (!FileUtil.isDirectory(soundspath)){
            FileUtil.makeDir(soundspath);
        }
    }


}
