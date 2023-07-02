package mod.hey.studios.activity.managers.nativelib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sketchware.remodgdx.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import mod.SketchwareUtil;
import mod.agus.jcoderz.lib.FilePathUtil;
import mod.agus.jcoderz.lib.FileResConfig;
import mod.agus.jcoderz.lib.FileUtil;
import mod.hey.studios.util.Helper;

public class ManageNativelibsActivity extends Activity implements View.OnClickListener {

    private FilePickerDialog filePicker;
    private FloatingActionButton fab;
    private FilePathUtil fpu = new FilePathUtil();
    private FileResConfig frc;
    private GridView gridView;
    private String numProj;
    private String nativeLibrariesPath;
    private ImageView loadFile;

    public void checkDir() {
        if (FileUtil.isExistFile(fpu.getPathNativelibs(numProj))) {
            nativeLibrariesPath = fpu.getPathNativelibs(numProj);
            handleAdapter(nativeLibrariesPath);
            handleFab();

            return;
        }

        FileUtil.makeDir(fpu.getPathNativelibs(numProj));
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/arm64-v8a");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/armeabi");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/armeabi-v7a");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/x86");
        FileUtil.makeDir(fpu.getPathNativelibs(numProj) + "/x86_64");
        copyNativeLibraries();
        checkDir();

    }

    public void copyNativeLibraries(){
        //Copys items from assets to whatever location you'd like
        AssetManager assetManager = getAssets();
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

    private void handleAdapter(String str) {
        ArrayList<String> nativelibsFile = frc.getNativelibsFile(str);
        Helper.sortPaths(nativelibsFile);
        CustomAdapter adapter = new CustomAdapter(nativelibsFile);
        gridView.setAdapter(adapter);
        /*copyNativeLibraries();*/
    }

    private boolean isInMainDirectory() {
        return nativeLibrariesPath.equals(fpu.getPathNativelibs(numProj));
    }

    private void handleFab() {
        if (isInMainDirectory()) {
            if (loadFile != null) {
                loadFile.setVisibility(View.GONE);
            }

            fab.setVisibility(View.VISIBLE);
        } else {
            if (loadFile != null) {
                loadFile.setVisibility(View.VISIBLE);
            }
            fab.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        ImageView back = findViewById(R.id.ig_toolbar_back);
        back.setOnClickListener(Helper.getBackPressedClickListener(this));
        Helper.applyRippleToToolbarView(back);
        ((TextView) findViewById(R.id.tx_toolbar_title)).setText("Native library Manager");

        loadFile = findViewById(R.id.ig_toolbar_load_file);
        Helper.applyRippleToToolbarView(loadFile);
        loadFile.setOnClickListener(v -> filePicker.show());
    }

    @Override
    public void onBackPressed() {
        nativeLibrariesPath = nativeLibrariesPath.substring(0, nativeLibrariesPath.lastIndexOf("/"));

        if (nativeLibrariesPath.contains("native_libs")) {
            handleAdapter(nativeLibrariesPath);
            handleFab();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            createNewDialog();
        }
    }

    private void createNewDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View root = getLayoutInflater().inflate(R.layout.dialog_create_new_file_layout, null);

        final TextView title = root.findViewById(R.id.dialog_create_new_file_layoutTitle);
        final RadioGroup fileType = root.findViewById(R.id.dialog_radio_filetype);
        final TextInputLayout filenameLayout;
        final EditText filename = root.findViewById(R.id.dialog_edittext_name);
        filenameLayout = (TextInputLayout) filename.getParent().getParent();

        title.setText("Create a new folder");
        fileType.setVisibility(View.GONE);
        filenameLayout.setHint("Folder name");

        root.findViewById(R.id.dialog_text_cancel).setOnClickListener(Helper.getDialogDismissListener(dialog));
        root.findViewById(R.id.dialog_text_save).setOnClickListener(save -> {
            String name = filename.getText().toString();

            if (name.isEmpty()) {
                filenameLayout.setError("Invalid folder name");
                return;
            }
            filenameLayout.setError(null);

            String path = fpu.getPathNativelibs(numProj) + "/" + name;

            if (FileUtil.isExistFile(path)) {
                filenameLayout.setError("Folder already exists");
                return;
            }
            filenameLayout.setError(null);

            FileUtil.makeDir(path);
            handleAdapter(nativeLibrariesPath);
            SketchwareUtil.toast("Created folder successfully");

            dialog.dismiss();
        });

        dialog.setView(root);
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        filename.requestFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_file);

        if (getIntent().hasExtra("sc_id")) {
            numProj = getIntent().getStringExtra("sc_id");
        }

        gridView = findViewById(R.id.list_file);
        gridView.setNumColumns(1);

        fab = findViewById(R.id.fab_plus);

        TextView tv = findViewById(R.id.text_info);
        tv.setVisibility(View.GONE);

        frc = new FileResConfig(numProj);
        fpu = new FilePathUtil();

        setupDialog();
        checkDir();

        fab.setOnClickListener(this);
        gridView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (FileUtil.isDirectory(frc.listFileNativeLibs.get(position))) {
                PopupMenu menu = new PopupMenu(this, view);

                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(item -> {
                    FileUtil.deleteFile(frc.listFileNativeLibs.get(position));
                    handleAdapter(nativeLibrariesPath);
                    SketchwareUtil.toast("Deleted");

                    return true;
                });

                menu.show();
            }
            return true;
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (FileUtil.isDirectory(frc.listFileNativeLibs.get(position))) {
                nativeLibrariesPath = frc.listFileNativeLibs.get(position);
                handleAdapter(nativeLibrariesPath);
                handleFab();
            }
        });

        initToolbar();
        finish();
    }

    private void setupDialog() {
        File externalStorageDir = new File(FileUtil.getExternalStorageDir());

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = externalStorageDir;
        properties.error_dir = externalStorageDir;
        properties.offset = externalStorageDir;
        properties.extensions = new String[]{"so"};

        filePicker = new FilePickerDialog(this, properties);
        filePicker.setTitle("Select a native library (.so)");
        filePicker.setDialogSelectionListener(selections -> {
            for (String path : selections) {
                try {
                    FileUtil.copyDirectory(new File(path), new File(nativeLibrariesPath + File.separator + Uri.parse(path).getLastPathSegment()));
                } catch (IOException e) {
                    SketchwareUtil.toastError("Couldn't import library! [" + e.getMessage() + "]");
                }
            }

            handleAdapter(nativeLibrariesPath);
            handleFab();
        });
    }

    private void showDialog(final String path) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View root = getLayoutInflater().inflate(R.layout.dialog_input_layout, null);

        final TextInputLayout filenameLayout = root.findViewById(R.id.dialoginputlayoutLinearLayout2);
        final EditText filename = root.findViewById(R.id.edittext_change_name);
        final TextView cancel = root.findViewById(R.id.text_cancel);
        final TextView save = root.findViewById(R.id.text_save);

        try {
            filename.setText(path.substring(path.lastIndexOf("/") + 1));
        } catch (Exception ignored) {
        }
        filenameLayout.setHint("New filename");

        cancel.setOnClickListener
                (Helper.getDialogDismissListener(dialog));
        save.setOnClickListener(saveButton -> {
            String newName = filename.getText().toString();
            if (!newName.isEmpty()) {
                if (FileUtil.renameFile(path,
                        path.substring(0, path.lastIndexOf(File.separator)) + File.separator + newName)) {
                    SketchwareUtil.toast("Renamed successfully");
                } else {
                    SketchwareUtil.toastError("Renaming failed");
                }

                handleAdapter(nativeLibrariesPath);
                handleFab();
                dialog.dismiss();
            } else {
                filenameLayout.setError("Enter a name");
            }
        });

        dialog.setView(root);
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        filename.requestFocus();
    }

    private class CustomAdapter extends BaseAdapter {

        private final ArrayList<String> data;

        public CustomAdapter(ArrayList<String> arrayList) {
            data = arrayList;
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.manage_java_item_hs, parent, false);
            }

            TextView name = convertView.findViewById(R.id.title);
            ImageView icon = convertView.findViewById(R.id.icon);
            ImageView more = convertView.findViewById(R.id.more);

            if (FileUtil.isDirectory(getItem(position))) {
                more.setVisibility(View.GONE);
            } else {
                more.setVisibility(View.VISIBLE);
            }

            name.setText(Uri.parse(getItem(position)).getLastPathSegment());
            icon.setImageResource(getImageRes(position));

            more.setOnClickListener(v -> {
                PopupMenu menu = new PopupMenu(ManageNativelibsActivity.this, v);
                menu.inflate(R.menu.popup_menu_double);

                menu.getMenu().getItem(0).setVisible(false);
                menu.getMenu().getItem(1).setVisible(false);
                menu.getMenu().getItem(2).setVisible(false);

                menu.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle().toString();
                    switch (title) {
                        case "Delete":
                            FileUtil.deleteFile(frc.listFileNativeLibs.get(position));
                            handleAdapter(nativeLibrariesPath);
                            break;

                        case "Rename":
                            showDialog(frc.listFileNativeLibs.get(position));
                            break;

                        default:
                            return false;
                    }

                    return true;
                });

                menu.show();
            });

            return convertView;
        }

        private int getImageRes(int position) {
            return FileUtil.isDirectory(getItem(position)) ? R.drawable.ic_folder_48dp : R.drawable.file_96;
        }
    }
}
