package mod;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.BitmapDrawableKt;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.r8.internal.Ur;
import com.bumptech.glide.BitmapOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sketchware.remodgdx.R;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.HashMap;

import a.a.a.mB;
import a.a.a.xB;
import mod.agus.jcoderz.lib.FileUtil;

public class ManageImages extends AppCompatActivity {
    public final int REQ_CD_PICKER = 478;
    public String filename,filepath;
    private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
    private String projectID;
    private FloatingActionButton fab;
    private String imageassetspath;
    private String fileextension;
    private String imagesliststr;
    private ArrayList<HashMap<String, Object>> imageslist = new ArrayList<>();
    private GridView imageslistgrid;
    private ArrayList<Integer> positions = new ArrayList<>();
    private boolean deletingenabled = false;
    private ArrayList<String> imagenameslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_manage_images);
        Toolbar m = findViewById(R.id.toolbar);
        setSupportActionBar(m);
        findViewById(R.id.layout_main_logo).setVisibility(View.GONE);
        getSupportActionBar().setTitle("Image Manager");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        m.setNavigationOnClickListener(view -> {
            if (!mB.a()) onBackPressed();
        });

        fab = findViewById(R.id.addimagefab);
        imageslistgrid = findViewById(R.id.imagesgrid);


        projectID = getIntent().getStringExtra("sc_id");
        imageassetspath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/" + projectID + "/files/assets/images/");
        picker.setType("image/png");
        picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ImagePickerDialog(false);
                deletingenabled = false;
                positions.clear();
                ((BaseAdapter)imageslistgrid.getAdapter()).notifyDataSetChanged();
            }
        });
        loadProjectImageAssets();
    }
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {
            case REQ_CD_PICKER:
                if (_resultCode == Activity.RESULT_OK) {
                    ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
                            }
                        }
                        else {
                            _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
                        }
                    }
                    filepath = _filePath.get((int)(0));
                    _ImagePickerDialog(true);
                }
                else {

                }
                break;

            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_screen_menu, menu);
        menu.findItem(R.id.menu_screen_delete).setVisible(deletingenabled);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_screen_delete) {
            deleteImagesSelected();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void deleteImagesSelected() {
        //imageslist
        for (int i = 0; i<positions.size(); i++){
            FileUtil.deleteFile(imageslist.get(positions.get(i)).get("i").toString());
        }
        deletingenabled = false;
        positions.clear();
        invalidateOptionsMenu();
        loadProjectImageAssets();

    }

    public void _ImagePickerDialog(final boolean _check) {
        final AlertDialog dialog5 = new AlertDialog
                .Builder(ManageImages.this).create();
        final View inflate = getLayoutInflater ().inflate(
                R.layout.dialog_add_images, null);
        dialog5.setView(inflate);
        final LinearLayout linear3 = (LinearLayout)inflate.findViewById(R.id.linear3);
        final ImageView imageview1 = (ImageView)inflate.findViewById(R.id.imageview1);
        final EditText edittext1 = (EditText)inflate.findViewById(R.id.edittext1);
        final TextView textview4 = (TextView)inflate.findViewById(R.id.textview4);
        final TextView textview5 = (TextView)inflate.findViewById(R.id.textview5);
        imageview1.setImageResource(R.drawable.imageiconmanager);
        linear3.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)1, 0xFF607D8B, Color.TRANSPARENT));
        imageview1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dialog5.hide();
                startActivityForResult(picker, REQ_CD_PICKER);
            }
        });
        textview4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                filename = edittext1.getText().toString();
                if (!filename.equals("")) {
                    if (!filepath.equals("")) {
                        if (!imagenameslist.contains(filename.toLowerCase())) {
                            dialog5.hide();
                            saveImageToImageAssets();
                            filename = "";
                            filepath = "";
                            fileextension = "";
                        }else {
                            SketchwareUtil.toast("Image name already exists");
                        }
                    }else {
                        SketchwareUtil.toast("Please pick an image");
                    }
                }else {
                    SketchwareUtil.toast("Name cannot be blank");
                }
            }
        });
        if (_check) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inScaled = false;
            Bitmap imagefrompath = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(filepath,bitmapOptions),
                    griditemwidthheight()/2,griditemwidthheight()/2,false);
            imageview1.setImageBitmap(imagefrompath);
            textview5.setVisibility(View.GONE);
            fileextension = filepath.substring(filepath.lastIndexOf("."));
            filename = Uri.parse(filepath).getLastPathSegment().replace(fileextension,"");
            edittext1.setText(filename);
        }
        dialog5.setCancelable(true);
        dialog5.show();
    }

    public void saveImageToImageAssets(){
        FileUtil.copyFile(filepath,imageassetspath + filename + fileextension);
        loadProjectImageAssets();
    }
    public void loadProjectImageAssets(){
        ArrayList<String> liststring = new ArrayList<>();
        liststring.clear();
        imageslist.clear();
        imagenameslist.clear();
        FileUtil.listDir(imageassetspath, liststring);
        for (int i = 0; i < liststring.size(); i++){
            HashMap<String,Object> mapdata = new HashMap<>();
            mapdata.put("i", liststring.get(i).toString());
            mapdata.put("n", Uri.parse(liststring.get(i)).getLastPathSegment().replace(".png",""));
            imagenameslist.add(Uri.parse(liststring.get(i)).getLastPathSegment().replace(".png","").toLowerCase());
            imageslist.add(mapdata);
        }
        imageslistgrid.setAdapter(new Gridview1Adapter(imageslist));
    }

    public void setViewBgFromPath(View view,String path){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path,bitmapOptions),
                griditemwidthheight()/2,griditemwidthheight()/2,false);
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        view.setBackground(bd);
    }
    public void _setViewWidthHeight(final View _view, final int _width, final int _height) {
        _view.getLayoutParams().width = _width;
        _view.getLayoutParams().height = _height;
    }
    public int griditemwidthheight(){
        int widthheight = getResources().getDisplayMetrics().widthPixels/3;
        return widthheight;
    }
    public void enableDelete(int position){
        if (positions.contains(position)){
            positions.remove(positions.indexOf(position));
            if (positions.size()<1){
                deletingenabled = false;
                invalidateOptionsMenu();
            }
        }else {
            positions.add(position);
        }

    }

    public class Gridview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.image_layout, null);
            }

            final LinearLayout imageholder = _view.findViewById(R.id.imageholder);
            final TextView file_name = _view.findViewById(R.id.file_name);
            final LinearLayout linear2 = _view.findViewById(R.id.linear2);
            final ImageView deleteicon = _view.findViewById(R.id.deleteicon);

            _setViewWidthHeight(imageholder,griditemwidthheight(),griditemwidthheight());
            setViewBgFromPath(imageholder,_data.get(_position).get("i").toString());
            file_name.setText(_data.get((int)_position).get("n").toString());

            if (positions.contains(_position)){
                deleteicon.setVisibility(View.VISIBLE);
            }else {
                deleteicon.setVisibility(View.GONE);
            }
            imageholder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!deletingenabled) {
                        deletingenabled = true;
                        positions.add(_position);
                        notifyDataSetChanged();
                        invalidateOptionsMenu();
                    }

                    return false;
                }
            });
            imageholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deletingenabled){
                        if (positions.size()>0) {
                            enableDelete(_position);
                        }
                    }
                    notifyDataSetChanged();
                    invalidateOptionsMenu();
                }
            });
            return _view;
        }
    }
}