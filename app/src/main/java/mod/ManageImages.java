package mod;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sketchware.remodgdx.R;

import java.net.FileNameMap;
import java.util.ArrayList;

import mod.agus.jcoderz.lib.FileUtil;

public class ManageImages extends AppCompatActivity {
    public final int REQ_CD_PICKER = 478;
    public String filename,filepath;
    private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
    private String projectID;
    private Button buttontest;
    private String imageassetspath;
    private String fileextension;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_images);
        buttontest = findViewById(R.id.buttontest);
        projectID = getIntent().getStringExtra("sc_id");
        imageassetspath = FileUtil.getExternalStorageDir().concat("/.sketchwaregames/data/" + projectID + "/files/assets/images/");
        buttontest.setText(projectID);
        picker.setType("image/*");
        picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ImagePickerDialog(false);
            }
        });
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

        imageview1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dialog5.hide();
                startActivityForResult(picker, REQ_CD_PICKER);
            }
        });
        textview4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                filename = edittext1.getText().toString();
                dialog5.hide();
                saveImageToImageAssets();
            }
        });
        if (_check) {
            imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(
                    filepath, 1024, 1024));
            fileextension = filepath.substring(filepath.lastIndexOf("."));
            filename = Uri.parse(filepath).getLastPathSegment().replace(fileextension,"");
            edittext1.setText(filename);
        }
        dialog5.setCancelable(true);
        dialog5.show();
    }

    public void saveImageToImageAssets(){
        FileUtil.copyFile(filepath,imageassetspath + filename + fileextension);
    }
    public void loadProjectImageAssets(){

    }
}