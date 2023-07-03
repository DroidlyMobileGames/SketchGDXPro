package com.besome.sketch.editor.manage.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.tools.r8.graph.P;
import com.android.tools.r8.internal.I;
import com.besome.sketch.beans.EventBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sketchware.remodgdx.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import a.a.a.By;
import a.a.a.Fw;
import a.a.a.MA;
import a.a.a.MB;
import a.a.a.bB;
import a.a.a.eC;
import a.a.a.jC;
import a.a.a.mB;
import a.a.a.qw;
import a.a.a.wq;
import a.a.a.xB;
import a.a.a.xw;
import mod.SketchwareUtil;

public class ManageViewActivity extends BaseAppCompatActivity implements OnClickListener, ViewPager.OnPageChangeListener {
    private static final int TAB_COUNT = 1;
    private static final int REQUEST_CODE_ADD_ACTIVITY = 264;
    private static final int REQUEST_CODE_ADD_CUSTOM_VIEW = 266;

    private LinearLayout actionButtonsContainer;
    private boolean selecting = false;
    private String isAppCompatEnabled = "N";
    // signature mustn't be changed: used in La/a/a/Bw;->a(Landroidx/recyclerview/widget/RecyclerView;II)V, La/a/a/tw;->a(Landroidx/recyclerview/widget/RecyclerView;II)V
    public FloatingActionButton s;
    private Fw activitiesFragment = new Fw();
    private xw customViewsFragment = new xw();
    private ViewPager viewPager;
    private final int[] x = new int[19];
    private String sc_id;
    private String viewName = "";
    private int viewtype = 0;
    private int typeselected = 0;
    private ArrayList<HashMap<String, Object>> _dialogselector = new ArrayList<>();
    private String selectiontext = "";
    public ArrayList<String> checknames = new ArrayList<>();


    public final String a(int var1, String var2) {
        String var3 = wq.b(var1);
        StringBuilder var4 = new StringBuilder();
        var4.append(var3);
        int[] var5 = x;
        int var6 = var5[var1] + 1;
        var5[var1] = var6;
        var4.append(var6);
        String var9 = var4.toString();
        ArrayList<ViewBean> var12 = jC.a(sc_id).d(var2);
        var2 = var9;

        while (true) {
            boolean var7 = false;
            Iterator<ViewBean> var10 = var12.iterator();

            boolean var13;
            while (true) {
                var13 = var7;
                if (!var10.hasNext()) {
                    break;
                }

                if (var2.equals(var10.next().id)) {
                    var13 = true;
                    break;
                }
            }

            if (!var13) {
                return var2;
            }

            StringBuilder var8 = new StringBuilder();
            var8.append(var3);
            int[] var11 = x;
            var6 = var11[var1] + 1;
            var11[var1] = var6;
            var8.append(var6);
            var2 = var8.toString();
        }
    }

    @Override
    public void onPageScrollStateChanged(int var1) {
    }

    @Override
    public void onPageScrolled(int var1, float var2, int var3) {
    }

    public final void a(ProjectFileBean var1, ArrayList<ViewBean> var2) {
        jC.a(sc_id);
        for (ViewBean viewBean : eC.a(var2)) {
            viewBean.id = a(viewBean.type, var1.getXmlName());
            jC.a(sc_id).a(var1.getXmlName(), viewBean);

            if (viewBean.type == ViewBean.VIEW_TYPE_WIDGET_BUTTON
                    && var1.fileType == ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY) {
                jC.a(sc_id).a(var1.getJavaName(),
                        EventBean.EVENT_TYPE_VIEW,
                        viewBean.type, viewBean.id, "onClick");

            }
        }
    }

    public void a(boolean var1) {//Checks if the user long pressed the list of projects
        selecting = var1;
        invalidateOptionsMenu();
        if (selecting) {
            actionButtonsContainer.setVisibility(View.VISIBLE);
        } else {
            actionButtonsContainer.setVisibility(View.GONE);
        }
        activitiesFragment.a(selecting);


    }

    @Override
    public void onPageSelected(int var1) {
        s.show();
    }

    public ArrayList<String> l() {
        ArrayList<String> projectLayoutFiles = new ArrayList<>();

        projectLayoutFiles.add("debug");
        ArrayList<ProjectFileBean> activitiesFiles = activitiesFragment.c();
        for (ProjectFileBean projectFileBean : activitiesFiles) {
            projectLayoutFiles.add(projectFileBean.fileName);
        }

        return projectLayoutFiles;
    }

    public final void m() {
        jC.b(sc_id).a(activitiesFragment.c());
        //jC.b(sc_id).b(customViewsFragment.c());
        jC.b(sc_id).l();
        jC.b(sc_id).j();
        jC.a(sc_id).a(jC.b(sc_id));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProjectFileBean projectFileBean;

        if (requestCode == REQUEST_CODE_ADD_ACTIVITY) {

            if (resultCode == RESULT_OK) {
                projectFileBean = data.getParcelableExtra("project_file");
                activitiesFragment.a(projectFileBean);//Loads the project files from the projectlist to the list

                if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER) || projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB)) {
                    jC.c(sc_id).c().useYn = "Y";
                }

                if (data.hasExtra("preset_views")) {
                    a(projectFileBean, data.getParcelableArrayListExtra("preset_views"));
                }

            }

        }


    }

    @Override
    public void onBackPressed() {
        if (selecting) {
            a(false);
        } else {
            k();

            try {
                new Handler().postDelayed(() -> (
                        new a(getApplicationContext())).execute(),
                        500L);
            } catch (Exception e) {
                e.printStackTrace();
                h();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!mB.a()) {
            int viewId = v.getId();
            if (viewId == R.id.btn_cancel) {
                if (selecting) {
                    a(false);
                }
            } else if (viewId == R.id.btn_delete) {
                if (selecting) {
                    activitiesFragment.f();//Deletes the activity from the project
                    a(false);
                    activitiesFragment.g();//After deltion this handles resetting the views to not show the delete/cancel buttons
                    bB.a(getApplicationContext(),
                            xB.b().a(getApplicationContext(),
                                    R.string.common_message_complete_delete), bB.TOAST_WARNING).show();
                    s.show();
                }
            } else if (viewId == R.id.fab) {
                newViewDialog();
            }
            //DNA MOBILE EDIT
        }
    }

    /**Creates a new class while checking the class type*/
    public void newView(int viewtype,final String name){
        String ext = "";
        if (viewtype == 0){
            ext = "_fragment";//Creates a Screen extension
        }
        if (viewtype == 1){
            ext = "_dialog_fragment";//Converts to an empty object class
        }
        a(false);
        try {
        ProjectFileBean var8 =
                new ProjectFileBean(0,
                        name + ext, 0, 0,
                        true, true, false, false);

        activitiesFragment.a(var8);


        }catch (Exception e){

        }

    }

    private void newViewDialog(){
        final AlertDialog dialog4 = new AlertDialog.Builder(ManageViewActivity.this).create();
        View inflate = getLayoutInflater ().inflate(R.layout.manage_view_dialog_select, null);
        dialog4.setView(inflate);
        final EditText edittext1 = (EditText)inflate.findViewById(R.id.edittext1);
        final TextView title = (TextView) inflate.findViewById(R.id.manageviewdialogtitle);
        final TextView cancel = (TextView)inflate.findViewById(R.id.manageviewcancel);
        final TextView save = (TextView)inflate.findViewById(R.id.manageviewsave);
        final RecyclerView manageviewselect = (RecyclerView)inflate.findViewById(R.id.manageviewselector);

        edittext1.setHint("Enter Name");

        manageviewselect.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        manageviewselect.setAdapter(new Recyclerview1Adapter(_dialogselector));
        cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                dialog4.dismiss();
            }
        });
        save.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                viewName = edittext1.getText().toString();
                if (!viewName.equals("")) {
                    if (!edittext1.getText().toString().trim().isEmpty()) {
                        if (!checknames.contains(edittext1.getText().toString().toLowerCase())) {
                            newView(viewtype, viewName);
                            dialog4.dismiss();
                        }else {
                            SketchwareUtil.toast("Name already exists");
                        }

                    }else {
                        SketchwareUtil.toast("Name cannot have spaces");
                    }
                }else {
                    SketchwareUtil.toast("Enter view name");
                }
            }
        });
        dialog4.setCancelable(true);
        dialog4.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!super.j()) {
            finish();
        }
        selectiontext = "[{\"a\":\" Screen \"},{\"a\":\"|\"},{\"a\":\" Class   \"}]";
        _dialogselector = new Gson().fromJson(selectiontext,
                new TypeToken<ArrayList<HashMap<String,
                        Object>>>(){}.getType());

        setContentView(R.layout.manage_view);
        Toolbar m = findViewById(R.id.toolbar);
        setSupportActionBar(m);
        findViewById(R.id.layout_main_logo).setVisibility(View.GONE);
        getSupportActionBar().setTitle(xB.b().a(getApplicationContext(), R.string.design_actionbar_title_manager_view));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        m.setNavigationOnClickListener(view -> {
            if (!mB.a()) onBackPressed();
        });

        actionButtonsContainer = findViewById(R.id.layout_btn_group);
        Button delete = findViewById(R.id.btn_delete);
        Button cancel = findViewById(R.id.btn_cancel);
        delete.setText(xB.b().a(getApplicationContext(), R.string.common_word_delete));
        cancel.setText(xB.b().a(getApplicationContext(), R.string.common_word_cancel));
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (savedInstanceState == null) {
            sc_id = getIntent().getStringExtra("sc_id");
            isAppCompatEnabled = getIntent().getStringExtra("compatUseYn");

        } else {
            sc_id = savedInstanceState.getString("sc_id");
            isAppCompatEnabled = savedInstanceState.getString("compatUseYn");
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new b(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(TAB_COUNT);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
        s = findViewById(R.id.fab);
        s.setOnClickListener(this);

        if (getIntent().getStringExtra("checkgameview").equals("poop")){
            onBackPressed();
        }
        ArrayList<ProjectFileBean> projectFiles = jC.b(sc_id).b();
        if (projectFiles != null) {
            for (int i = 0; i < projectFiles.size(); i++) {
                checknames.add(projectFiles.get(i).fileName.replace("_fragment","").replace("_dialog","").toLowerCase());
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_screen_menu, menu);
        menu.findItem(R.id.menu_screen_delete).setVisible(!selecting);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_screen_delete) {
            a(!selecting);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!super.j()) {
            finish();

        }

    }

    @Override
    public void onSaveInstanceState(Bundle newState) {
        newState.putString("sc_id", sc_id);
        newState.putString("compatUseYn", isAppCompatEnabled);

        super.onSaveInstanceState(newState);
    }

    public class a extends MA {
        public a(Context var2) {
            super(var2);
            addTask(this);
        }

        @Override
        public void a() {
            h();
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void a(String var1) {
            h();

        }

        @Override
        public void b() {
            try {
                publishProgress(xB.b().a(getApplicationContext(), R.string.common_message_progress));
                m();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    throw new By(xB.b().a(super.a, R.string.common_error_unknown));
                } catch (By ex) {
                    ex.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            return a(voids);
        }
    }

    public class b extends FragmentPagerAdapter {
        public String[] f;

        public b(FragmentManager fragmentManager) {
            super(fragmentManager);
            f = new String[]{
                    xB.b().a(getApplicationContext(), R.string.common_word_view)
                            .toUpperCase(), xB.b().a(getApplicationContext(),
                    R.string.common_word_custom_view).toUpperCase()};
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return f[position];
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment var3 = (Fragment) super.instantiateItem(container, position);
            activitiesFragment = (Fw) var3;
            System.out.println("GET POOP" + checknames);
            return var3;
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {

            return position != 1 ? new Fw() : null;
        }
    }
    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {

        ArrayList<HashMap<String, Object>> _data;

        public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = getLayoutInflater();
            View _v = _inflater.inflate(R.layout.manageviewrecyclerlayout,
                    null);
            RecyclerView.LayoutParams _lp = new RecyclerView
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, @SuppressLint("RecyclerView") final int _position) {

            View _view = _holder.itemView;

            final TextView selectiontxt = _view.findViewById(R.id.selectiontxt);
            selectiontxt.setText(_dialogselector.get(_position).get("a").toString());

            if (_position == typeselected){
                selectiontxt.setBackgroundColor(Color.WHITE);
                selectiontxt.setTextColor(Color.BLUE);
            }else {

                selectiontxt.setBackgroundColor(Color.TRANSPARENT);
                selectiontxt.setTextColor(Color.WHITE);
            }

            RecyclerView.LayoutParams _lp =
                    new RecyclerView.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            _view.setLayoutParams(_lp);

            selectiontxt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (_position!=1) {
                        if (_position == 2) {
                            viewtype = 1;
                            typeselected = 2;
                        }
                        if (_position == 0){
                            viewtype = 0;
                            typeselected = 0;
                        }
                        if (_position == 4){
                            viewtype = 69;
                            typeselected = 4;

                        }
                        notifyDataSetChanged();
                    }
                }
            });

            selectiontxt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (_position == 0){
                        SketchwareUtil.toast("Creates a Screen Class");
                    }
                    if (_position == 2){
                        SketchwareUtil.toast("Creates an Object Class");
                    }
                    if (_position == 4){
                        SketchwareUtil.toast("Required to load your Screens");
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }
    }
}
