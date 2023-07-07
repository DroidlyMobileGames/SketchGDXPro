package mod;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.tools.r8.internal.Ur;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sketchware.remodgdx.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import a.a.a.mB;
import mod.agus.jcoderz.lib.FileUtil;

public class ManageSounds extends AppCompatActivity {

    public final int REQ_CD_PICKER = 478;
    public String filename,filepath;
    private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
    private String projectID;
    private FloatingActionButton fab;
    private String soundassetspath;
    private String fileextension;
    private String imagesliststr;
    private ArrayList<HashMap<String, Object>> soundslist = new ArrayList<>();
    private GridView soundslistview;
    private ArrayList<Integer> positions = new ArrayList<>();
    private boolean deletingenabled = false;
    private ArrayList<String> soundnameslist = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_manage_sounds);
        Toolbar m = findViewById(R.id.toolbar);
        setSupportActionBar(m);
        findViewById(R.id.layout_main_logo).setVisibility(View.GONE);
        getSupportActionBar().setTitle("Sound Manager");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        m.setNavigationOnClickListener(view -> {
            if (!mB.a()) onBackPressed();
        });

        fab = findViewById(R.id.addimagefab);
        soundslistview = findViewById(R.id.soundslist);
        projectID = getIntent().getStringExtra("sc_id");
        soundassetspath = FileUtil.getExternalStorageDir()
                .concat("/.sketchwaregames/data/" + projectID + "/files/assets/sounds/");
        picker.setType("audio/mpeg");
        picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _SoundPickerDialog(false);
                deletingenabled = false;
                positions.clear();
                ((BaseAdapter)soundslistview.getAdapter()).notifyDataSetChanged();
            }
        });
        loadProjectSoundsAssets();
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
                    _SoundPickerDialog(true);
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
            deleteSoundsSelected();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void deleteSoundsSelected() {
        //imageslist
        for (int i = 0; i<positions.size(); i++){
            FileUtil.deleteFile(soundslist.get(positions.get(i)).get("s").toString());
        }
        deletingenabled = false;
        positions.clear();
        invalidateOptionsMenu();
        loadProjectSoundsAssets();

    }

    public void _SoundPickerDialog(final boolean _check) {
        final AlertDialog dialog5 = new AlertDialog
                .Builder(ManageSounds.this).create();
        final View inflate = getLayoutInflater ().inflate(
                R.layout.dialog_add_images, null);
        dialog5.setView(inflate);
        final LinearLayout linear3 = (LinearLayout)inflate.findViewById(R.id.linear3);
        final ImageView imageview1 = (ImageView)inflate.findViewById(R.id.imageview1);
        final EditText edittext1 = (EditText)inflate.findViewById(R.id.edittext1);
        final TextView textview1 = (TextView)inflate.findViewById(R.id.textview1);
        final TextView textview2 = (TextView)inflate.findViewById(R.id.textview2);
        final TextView textview4 = (TextView)inflate.findViewById(R.id.textview4);
        final TextView textview5 = (TextView)inflate.findViewById(R.id.textview5);
        textview4.setText("Save");
        textview1.setText("Add Sound");
        textview2.setText("Enter Sound Name");
        textview5.setText("Select Sound");
        imageview1.setImageResource(R.drawable.ic_sound_wave_48dp);
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
                        if (!soundnameslist.contains(filename.toLowerCase())) {
                            dialog5.hide();
                            saveSoundToSoundsAssets();
                            filename = "";
                            filepath = "";
                            fileextension = "";
                        }else {
                            SketchwareUtil.toast("Sound name already exists");
                        }
                    }else {
                        SketchwareUtil.toast("Please pick an sound");
                    }
                }else {
                    SketchwareUtil.toast("Name cannot be blank");
                }
            }
        });
        if (_check) {
            textview5.setVisibility(View.VISIBLE);
            fileextension = filepath.substring(filepath.lastIndexOf("."));
            filename = Uri.parse(filepath).getLastPathSegment().replace(fileextension,"");
            edittext1.setText(filename);
        }
        dialog5.setCancelable(true);
        dialog5.show();
    }

    public void saveSoundToSoundsAssets(){
        FileUtil.copyFile(filepath,soundassetspath + filename + fileextension);
        loadProjectSoundsAssets();
    }
    public void loadProjectSoundsAssets(){
        ArrayList<String> liststring = new ArrayList<>();
        liststring.clear();
        soundslist.clear();
        soundnameslist.clear();
        FileUtil.listDir(soundassetspath, liststring);
        for (int i = 0; i < liststring.size(); i++){
            HashMap<String,Object> mapdata = new HashMap<>();
            mapdata.put("s", liststring.get(i).toString());//Returns the full path
            mapdata.put("n", Uri.parse(liststring.get(i)).getLastPathSegment().replace(".mp3",""));
            soundnameslist.add(Uri.parse(liststring.get(i)).getLastPathSegment().replace(".mp3","").toLowerCase());
            soundslist.add(mapdata);
        }
        soundslistview.setAdapter(new Gridview1Adapter(soundslist));
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
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
            //setViewBgFromPath(imageholder,_data.get(_position).get("i").toString());
            imageholder.setBackgroundResource(R.drawable.ic_sound_wave_48dp);
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
                        notifyDataSetChanged();
                        invalidateOptionsMenu();
                    }else {
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                    Uri.parse(_data.get((int) _position).get("s").toString()));
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    mp.setLooping(false);
                                }
                            });
                        }else {
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                                mediaPlayer = null;
                            }
                        }

                    }


                }
            });
            return _view;
        }
    }
}