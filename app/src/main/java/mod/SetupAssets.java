package mod;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import java.io.File;

import dev.aldi.sayuti.editor.view.palette.IconYoutubePlayer;
import mod.agus.jcoderz.lib.FileUtil;

public class SetupAssets {
    String projID;
    String imagespath,soundspath,fontspath;
    Context c;

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
