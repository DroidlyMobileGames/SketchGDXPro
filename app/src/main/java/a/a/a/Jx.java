package a.a.a;


import android.text.TextUtils;
import android.util.Pair;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import mod.agus.jcoderz.beans.ViewBeans;
import mod.agus.jcoderz.editor.manage.library.locallibrary.ManageLocalLibrary;
import mod.agus.jcoderz.handle.component.ConstVarComponent;
import mod.hasrat.control.logic.PermissionManager;
import mod.hey.studios.activity.managers.nativelib.ManageNativelibsActivity;
import mod.hey.studios.build.BuildSettings;
import mod.hey.studios.project.ProjectSettings;
import mod.hilal.saif.android_manifest.AndroidManifestInjector;
import mod.hilal.saif.blocks.CommandBlock;
import mod.hilal.saif.events.LogicHandler;

public class Jx {

    public static final String EOL = "\r\n";
    public static final Pattern WIDGET_NAME_PATTERN = Pattern.compile("\\w*\\..*\\.");
    private final ProjectSettings settings;
    private final PermissionManager permissionManager;
    private final String packageName;
    private final ProjectFileBean projectFileBean;
    private final eC projectDataManager;
    private final jq buildConfig;
    /**
     * Fields with static initializer that added Components need,
     * e.g. {"private Timer _timer = new Timer();"}
     */
    private final ArrayList<String> fieldsWithStaticInitializers = new ArrayList<>();
    /**
     * Fields of the currently generating class,
     * e.g. {"private FloatingActionBar _fab;"}
     */
    private final ArrayList<String> fields = new ArrayList<>();
    private final ArrayList<String> lists = new ArrayList<>();
    private final ArrayList<String> views = new ArrayList<>();
    /**
     * Field declarations from components. Can include static initializer, but doesn't have to.
     */
    private final ArrayList<String> components = new ArrayList<>();
    /**
     * Statements to be added to initialize(Bundle),
     * e.g. {"_drawer.addDrawerListener(_toggle);"}
     */
    private final ArrayList<String> initializeMethodCode = new ArrayList<>();
    private final ManageLocalLibrary mll;
    /**
     * Component initializer lines which get added to <code>_initialize(Bundle)</code>
     */
    private final ArrayList<String> componentInitializers = new ArrayList<>();
    /**
     * Code of More Blocks that have been created
     */
    private final ArrayList<String> moreBlocks = new ArrayList<>();
    /**
     * Code of inner Adapter classes, used for Widgets like ListView or RecyclerView
     */
    private final ArrayList<String> adapterClasses = new ArrayList<>();
    /**
     * Filled with request code constants for FilePicker components
     */
    private final ArrayList<String> filePickerRequestCodes = new ArrayList<>();
    private Hx eventManager;
    private ArrayList<String> imports = new ArrayList<>();
    private String onCreateEventCode = "";
    private String onCreateEventCode2 = "";
    private String onCreateEventCode3 = "";
    private String onCreateEventCode4 = "";
    private String onCreateEventCode5 = "";
    private String onCreateEventCode6 = "";
    private String onCreateEventCode7 = "";
    public Jx(jq jqVar, ProjectFileBean projectFileBean, eC eCVar) {
        packageName = jqVar.packageName;
        this.projectFileBean = projectFileBean;
        projectDataManager = eCVar;
        buildConfig = jqVar;
        mll = new ManageLocalLibrary(eCVar.a);
        settings = new ProjectSettings(eCVar.a);
        permissionManager = new PermissionManager(eCVar.a, projectFileBean.getJavaName());

    }

    public String activityResult() {
        ArrayList<BlockBean> blocks = jC.a(projectDataManager.a).a(projectFileBean.getJavaName(), "onActivityResult_onActivityResult");
        return Lx.j(new Fx(projectFileBean.getActivityName(), buildConfig, "", blocks).a(), false);
    }

    public String initializeLogic() {
        ArrayList<BlockBean> blocks =
                jC.a(projectDataManager.a).a(projectFileBean.getJavaName(),
                        "initializeLogic_initializeLogic");
        return Lx.j(new Fx(projectFileBean.getActivityName(),
                buildConfig, "", blocks).a(), false);
    }

    private void extraVariables() {
        for (Map.Entry<String, ArrayList<BlockBean>> blocks : jC.a(projectDataManager.a)
                .b(projectFileBean.getJavaName()).entrySet()) {
            for (BlockBean block : blocks.getValue()) {
                switch (block.opCode) {
                    case "addCustomVariable":
                        if (!block.parameters.get(0).trim().isEmpty()) {
                            fields.add(block.parameters.get(0));
                        }
                        break;

                    case "addInitializer":
                        if (!block.parameters.get(0).trim().isEmpty()) {
                            initializeMethodCode.add(block.parameters.get(0));
                        }
                        break;
                }
            }
        }
    }

    private void removeExtraImports() {
        ArrayList<String> newImports = new ArrayList<>();
        for (String value : imports) {
            if (!newImports.contains(value) && !value.trim().isEmpty()) {
                newImports.add(value);
            }
        }
        imports = newImports;
    }

    /**
     * @return Import to be added to the currently generating class
     * (includes import of default launcher activity)
     */
    private String getLauncherActivity(String packageName) {
        String theImport = "";

        String activityName = ProjectFileBean.getActivityName(
                AndroidManifestInjector.getLauncherActivity(
                        projectDataManager.a));
        if (!activityName.equals("MainActivity")) {
            theImport = "import " + packageName + "." + activityName + ";" + EOL;
        }

        return theImport;
    }
    /**
     * @return Generated Java code of the current View (not Widget)
     */
    @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
    public String generateCode() {
        boolean isDialogFragment = false;
        boolean isBottomDialogFragment = false;
        boolean isFragment = false;
        isDialogFragment = projectFileBean.fileName.contains("_dialog");
        isFragment = projectFileBean.fileName.contains("_fragment") && !projectFileBean.fileName.contains("_dialog");

        extraVariables();
        if (isFragment){
            handleScreen();
        }else if (isDialogFragment){
            handleClass();
        }else {
            handleAppCompat();
        }

        addFieldsDeclaration();
        addDrawerComponentInitializer();
        initializeEventsCodeGenerator();
        addMoreBlockCodes();
        addRequestCodeConstants();
        addImportsForBlocks();
        addLocalLibraryImports();

        StringBuilder sb = new StringBuilder(8192);
        sb.append("package ").append(packageName).append(";").append(EOL)
                .append(EOL);
        if (projectFileBean.getActivityName().equals("MainActivity")) {
            sb.append(getLauncherActivity(packageName));
        }

        removeExtraImports();
        Collections.sort(imports);
        for (String anImport : imports) {
            sb.append("import ").append(anImport).append(";").append(EOL);
        }

        String importsAddedByImportBlocks = LogicHandler.imports(eventManager.b());
        if (!importsAddedByImportBlocks.isEmpty()) {
            sb.append(importsAddedByImportBlocks).append(EOL);
        }
        sb.append(EOL);

        if (isFragment && !isDialogFragment){
            sb.append("public class ")
                    .append(projectFileBean.getActivityName()
                            .replace("Fragment","View"));
        }
        if (isDialogFragment) {
            sb.append("public class ")
                    .append(projectFileBean.getActivityName()
                            .replace("DialogFragment", "View"));
        }
            if (!isDialogFragment && !isFragment) {
                sb.append("public class ")
                        .append(projectFileBean
                                .getActivityName()).append(" extends ");
            }

        if (isDialogFragment) {

        } else if (isFragment) {//Fragment is a class instead so it doesn't extend any classes by default
            sb.append(" implements Screen");
        } else {
            if (!isDialogFragment && !isFragment && projectFileBean.getJavaName().equals("Main.java")) {
                sb.append("AndroidApplication");
            }else {
                sb.append("Game");
            }
        }
        sb.append(" {").append(EOL);

        boolean activityHasFields = false;

        for (String constant : filePickerRequestCodes) {
            if (constant.length() > 0) {
                activityHasFields = true;
                sb.append(EOL);
                sb.append(constant);
            }
        }

        if (fieldsWithStaticInitializers.size() > 0) {
            if (activityHasFields) sb.append(EOL);
            activityHasFields = true;

            for (String componentFieldDeclaration : fieldsWithStaticInitializers) {
                if (componentFieldDeclaration.length() > 0) {
                    sb.append(EOL);
                    sb.append(componentFieldDeclaration);
                }
            }
        }

        if (fields.size() > 0) {
            if (activityHasFields) sb.append(EOL);
            activityHasFields = true;

            for (String field : fields) {
                if (field.length() > 0) {
                    sb.append(EOL);
                    sb.append(field);
                }
            }
        }

        if (lists.size() > 0) {
            if (activityHasFields) sb.append(EOL);
            activityHasFields = true;

            for (String value : lists) {
                if (value.length() > 0) {
                    sb.append(EOL);
                    sb.append(value);
                }
            }
        }

        if (views.size() > 0) {
            if (activityHasFields) sb.append(EOL);
            activityHasFields = true;

            for (String viewDeclaration : views) {
                if (viewDeclaration.length() > 0) {
                    sb.append(EOL);
                    sb.append(viewDeclaration);
                }
            }
        }

        if (components.size() > 0) {
            if (activityHasFields) sb.append(EOL);
            activityHasFields = true;

            for (String componentFieldDeclaration : components) {
                if (componentFieldDeclaration.length() > 0) {
                    sb.append(EOL);
                    sb.append(componentFieldDeclaration);
                }
            }
        }

        if (activityHasFields) sb.append(EOL);

        sb.append(EOL);

        if (isFragment) {
            sb.append("private Gameview game;").append(EOL);
            sb.append("public ".concat(projectFileBean.getActivityName().replace("Fragment","View").concat("(Gameview game) {"))).append(EOL);
            sb.append("this.game = game;").append(EOL);
            sb.append("}").append(EOL);
        }
        if (isDialogFragment){
            sb.append("private Gameview game;").append(EOL);
            sb.append("public ").append(projectFileBean.getActivityName().replace("DialogFragment", "View")).append("(Gameview game) {").append(EOL);
            sb.append("this.game = game;").append(EOL);
            sb.append("initializeClass();");
        }
        //If Main
        if (!isDialogFragment && !isFragment) {//Default Main don't touch
            if (projectFileBean.getJavaName().equals("Main.java")) {
                sb.append("@Override").append(EOL);
                sb.append("protected void onCreate(Bundle _savedInstanceState) {").append(EOL);
                sb.append("super.onCreate(_savedInstanceState);").append(EOL);
            } else {
                sb.append("@Override").append(EOL);
                sb.append("public void create() {").append(EOL);
            }
        }
        //Writes the main logic for the class when loading src code

        if (!isFragment && !isDialogFragment) {//Check if Main Class as Main class should not be altered to handle loading game
            if (projectFileBean.getJavaName().equals("Main.java")) {
                sb.append("initializeLogic();").append(EOL);
            } else {
                sb.append("initializeCreate();").append(EOL);//This allows the developer to modify the create method for the Game the components for the Game class will handle other methods like Rendering
            }
        }

        //Handles other methods if exists
        if (!TextUtils.isEmpty(initializeLogic())) {
            sb.append(EOL);
            sb.append(initializeLogic());
        }

        for (String value : initializeMethodCode) {
            if (value.length() > 0) {
                sb.append(EOL);
                sb.append(value);
            }
        }

        for (String componentInitializer : componentInitializers) {
            if (componentInitializer.length() > 0) {
                sb.append(EOL);
                sb.append(componentInitializer);
            }
        }

        String hxG = eventManager.g();
        if (hxG.length() > 0) {
            sb.append(EOL);
            sb.append(EOL);
            sb.append(hxG);
        }

        String hxC = eventManager.c();
        if (hxC.length() > 0) {
            sb.append(EOL);
            sb.append(EOL);
            sb.append(hxC);
        }

        String hxD = eventManager.d();
        if (hxD.length() > 0) {
            sb.append(EOL);
            sb.append(EOL);
            sb.append(hxD);
        }

        String hxF = eventManager.f();
        if (hxF.length() > 0) {
            sb.append(EOL);
            sb.append(EOL);
            sb.append(hxF);
        }

        sb.append(EOL);
        /*//Other Methods End//*/
        //VERY IMPORTANT DNA MOBILE IF CLASS IS Main.java we replace the entire class to become the AndroidLoader
        //Otherwise any other Activities will be replaced as Screens for our game

        if (!isFragment && !isDialogFragment) {//This means that the user has created a Game Activity(First options)
            if (projectFileBean.getJavaName().equals("Main.java")) {
                sb.append("}").append(EOL);
                sb.append("private void initializeLogic() {").append(EOL);
            } else {
                sb.append("}").append(EOL);
                sb.append("private void initializeCreate() {").append(EOL);
            }
            if (onCreateEventCode.length() > 0) {
                sb.append(onCreateEventCode).append(EOL);

            }
        }

        if (isDialogFragment){
            sb.append("}").append(EOL);
            sb.append("private void initializeClass() {").append(EOL);
            if (onCreateEventCode.length() > 0) {
                sb.append(onCreateEventCode).append(EOL);

            }
        }

        if (isFragment && !isDialogFragment) {
            sb.append("@Override").append(EOL);
            sb.append("public void show() {").append(EOL);

            if (onCreateEventCode.length() > 0) {
                sb.append(onCreateEventCode).append(EOL);

            }sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void render(float delta) {").append(EOL);

            if (onCreateEventCode2.length() > 0) {
                sb.append(onCreateEventCode2).append(EOL);

            }sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void resize(int width, int height) {").append(EOL);

            if (onCreateEventCode3.length() > 0) {
                sb.append(onCreateEventCode3).append(EOL);

            } sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void pause() {").append(EOL);

            if (onCreateEventCode4.length() > 0) {
                sb.append(onCreateEventCode4).append(EOL);

            }sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void resume() {").append(EOL);

            if (onCreateEventCode5.length() > 0) {
                sb.append(onCreateEventCode5).append(EOL);

            }sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void hide() {").append(EOL);

            if (onCreateEventCode6.length() > 0) {
                sb.append(onCreateEventCode6).append(EOL);

            }sb.append("}").append(EOL);

            sb.append("@Override").append(EOL);
            sb.append("public void dispose() {").append(EOL);

            if (onCreateEventCode7.length() > 0) {
                sb.append(onCreateEventCode7).append(EOL);
            }

        }
        sb.append("}").append(EOL);

        ArrayList<ViewBean> beans = projectDataManager.d(projectFileBean.getXmlName());

        if (eventManager.k.length() > 0) {
            sb.append(EOL);
            sb.append(eventManager.k).append(EOL);
        }
        if (eventManager.l.length() > 0) {
            sb.append(EOL);
            sb.append(eventManager.l);
            sb.append(EOL);
        }

        String base = LogicHandler.base(eventManager.b());
        if (base.length() > 0) {
            sb.append(EOL);
            sb.append(base);
        }

        //Loads all the more blocks & the code
        for (String moreBlocksCode : moreBlocks) {
            sb.append(EOL);
            sb.append(moreBlocksCode).append(EOL);
        }

        sb.append(EOL);
        sb.append("}").append(EOL);
        String code = sb.toString();

        return CommandBlock.CB(Lx.j(code, false));
    }

    private String getListDeclarationAndAddImports(int listType, String listName) {
        String typeName = mq.b(listType);
        addImports(mq.getImportsByTypeName(typeName));
        return Lx.a(typeName, listName, Lx.AccessModifier.PRIVATE);
    }

    private String getComponentDeclarationAndAddImports(ComponentBean componentBean) {
        String typeName = mq.a(componentBean.type);
        addImports(mq.getImportsByTypeName(typeName));
        return Lx.a(typeName, componentBean.componentId, Lx.AccessModifier.PRIVATE, componentBean.param1, componentBean.param2, componentBean.param3);
    }

    private String getDrawerViewDeclarationAndAddImports(ViewBean viewBean) {
        String viewType = WIDGET_NAME_PATTERN.matcher(viewBean.convert).replaceAll("");
        if (viewType.equals("")) {
            viewType = viewBean.getClassInfo().a();
        }
        addImports(mq.getImportsByTypeName(viewType));
        return Lx.a(viewType, "_drawer_" + viewBean.id, Lx.AccessModifier.PRIVATE);
    }

    /**
     * @return Definition line for a Variable
     */
    private String getVariableDeclarationAndAddImports(int variableType, String name) {
        String variableTypeName = mq.c(variableType);
        addImports(mq.getImportsByTypeName(variableTypeName));
        return Lx.a(variableTypeName, name, Lx.AccessModifier.PRIVATE);
    }

    private String getViewDeclarationAndAddImports(ViewBean viewBean) {
        String viewType = WIDGET_NAME_PATTERN.matcher(viewBean.convert).replaceAll("");
        if (viewType.equals("")) {
            viewType = viewBean.getClassInfo().a();
        }
        addImports(mq.getImportsByTypeName(viewType));
        return Lx.a(viewType, viewBean.id, Lx.AccessModifier.PRIVATE);
    }



    private void addImport(String classToImport) {
        if (!imports.contains(classToImport)) {
            imports.add(classToImport);
        }
    }

    private void addImports(ArrayList<String> imports) {
        if (imports != null) {
            for (String value : imports) {
                addImport(value);
            }
        }
    }

    /**
     * @see Lx#getComponentInitializerCode(String, String, String...)
     */
    private String getComponentBeanInitializer(ComponentBean componentBean) {
        return Lx.getComponentInitializerCode(mq.a(componentBean.type), componentBean.componentId, componentBean.param1, componentBean.param2, componentBean.param3);
    }

    //Creates the Gameview class only Show method (create) is required
    private void handleAppCompat() {
        //ALL IMPORTS DNA MOBILE
        addImport("android.os.*");
        addImport("android.view.*");
        addImport("com.badlogic.gdx.graphics.*");

        //Main initializer to help with rendering the main logic in the within the class//
        onCreateEventCode = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Show_show",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Show_show")).a();
    }

    //Creates everything the game screen classes require including most imports//
    private void handleScreen() {
        addImport("android.os.Bundle");
        addImport("com.badlogic.gdx.graphics.*");

        onCreateEventCode = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Show_show",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Show_show")).a();
        onCreateEventCode2 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Render_render",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Render_render")).a();
        onCreateEventCode3 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Resize_resize",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Resize_resize")).a();
        onCreateEventCode4 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Pause_pause",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Pause_pause")).a();
        onCreateEventCode5 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Resume_resume",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Resume_resume")).a();
        onCreateEventCode6 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Hide_hide",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Hide_hide")).a();
        onCreateEventCode7 = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Dispose_dispose",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Dispose_dispose")).a();

    }
    public void handleClass(){
        addImport("android.os.*");
        addImport("android.view.*");
        addImport("com.badlogic.gdx.graphics.*");
        //handles allowing the user to edit event when inside the logiceditor
        onCreateEventCode = new Fx(projectFileBean.getActivityName(), buildConfig,
                "Show_show",
                projectDataManager.a(
                        projectFileBean.getJavaName(),
                        "Show_show")).a();
    }


    private String getDrawerViewInitializer(ViewBean viewBean) {
        String replaceAll = WIDGET_NAME_PATTERN.matcher(viewBean.convert).replaceAll("");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        return Lx.getDrawerViewInitializer(replaceAll, viewBean.id, "_nav_view");
    }

    private String getViewInitializer(ViewBean viewBean) {
        String replaceAll = WIDGET_NAME_PATTERN.matcher(viewBean.convert).replaceAll("");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        if (projectFileBean.fileName.contains("_fragment")) {
            return Lx.getViewInitializer(replaceAll, viewBean.id, true);
        }
        return Lx.getViewInitializer(replaceAll, viewBean.id, false);
    }

    private void addMoreBlockCodes() {
        String javaName = projectFileBean.getJavaName();
        ArrayList<Pair<String, String>> pairs = projectDataManager.i(javaName);
        for (int index = 0, pairsSize = pairs.size(); index < pairsSize; index++) {
            Pair<String, String> next = pairs.get(index);
            String name = next.first + "_moreBlock";
            String code = Lx.getMoreBlockCode(next.first, next.second, new Fx(projectFileBean.getActivityName(), buildConfig, name, projectDataManager.a(javaName, name)).a());
            if (index < (pairsSize - 1)) {
                moreBlocks.add(code);
            } else {
                // Removes unnecessary newline at end of More Block code
                moreBlocks.add(code.substring(0, code.length() - 2));
            }
        }
    }

    private void initializeEventsCodeGenerator() {
        eventManager = new Hx(buildConfig, projectFileBean, projectDataManager);
        addImports(eventManager.getImports());
    }

    /**
     * Adds imports for blocks used in the currently generated Activity.
     */
    private void addImportsForBlocks() {
        for (Map.Entry<String, ArrayList<BlockBean>> entry : projectDataManager.b(projectFileBean.getJavaName()).entrySet()) {
            for (BlockBean blockBean : entry.getValue()) {
                switch (blockBean.opCode) {
                    case "toStringWithDecimal":
                    case "toStringFormat":
                        addImport("java.text.DecimalFormat");
                        break;

                    case "strToListMap":
                    case "strToListStr":
                    case "strToMap":
                    case "GsonStringToListString":
                    case "GsonStringToListNumber":
                        addImport("com.google.gson.Gson");
                        addImport("com.google.gson.reflect.TypeToken");
                        break;

                    case "mapToStr":
                    case "listMapToStr":
                    case "GsonListTojsonString":
                        addImport("com.google.gson.Gson");
                        break;

                    case "setTypeface":
                        addImport("android.graphics.Typeface");
                        break;

                    case "copyToClipboard":
                        addImport("android.content.ClipData");
                        addImport("android.content.ClipboardManager");
                        break;

                    case "fileutilGetLastSegmentPath":
                        addImport("android.net.Uri");
                        break;

                    case "setImageUrl":
                        addImport("com.bumptech.glide.Glide");
                        break;

                    case "interstitialAdLoad":
                    case "rewardedVideoAdLoad":
                        addImport("com.google.android.gms.ads.AdRequest");
                        addImport("com.google.android.gms.ads.LoadAdError");
                        break;
                }
            }
        }
    }

    /**
     * Handles the Activity's Drawer Views and Components
     */
    private void addDrawerComponentInitializer() {
        ArrayList<ViewBean> viewBeans = projectDataManager.d(
                projectFileBean.getXmlName());
        for (ViewBean viewBean : viewBeans) {
            initializeMethodCode.add(getViewInitializer(viewBean));
        }
        if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            ArrayList<ViewBean> drawerBeans = projectDataManager.d(projectFileBean.getDrawerXmlName());
            for (ViewBean viewBean : drawerBeans) {
                initializeMethodCode.add(getDrawerViewInitializer(viewBean));
            }
        }
        ArrayList<ComponentBean> componentBeans = projectDataManager.e(projectFileBean.getJavaName());
        for (ComponentBean componentBean : componentBeans) {
            componentInitializers.add(getComponentBeanInitializer(componentBean));
        }
    }

    /**
     * Handles the file's request code constants.
     */
    private void addRequestCodeConstants() {
        int startValue = 100;
        for (ComponentBean next : projectDataManager.e(projectFileBean.getJavaName())) {
            switch (next.type) {
                case ComponentBean.COMPONENT_TYPE_CAMERA:
                case ComponentBean.COMPONENT_TYPE_FILE_PICKER:
                case 31:
                    int incrementedValue = startValue + 1;
                    filePickerRequestCodes.add(Lx.getRequestCodeConstant(next.componentId, incrementedValue));
                    startValue = incrementedValue;
                    break;
            }
        }
    }

    private void addFieldsDeclaration() {
        String javaName = projectFileBean.getJavaName();
        for (Pair<Integer, String> next : projectDataManager.k(javaName)) {
            int variableId = next.first;
            String variableValue = next.second;
            if (variableId == 9) {
                addImport(variableValue);
            } else if (variableId == 6) {
                fields.add(variableValue + ";");
            } else {
                fields.add(getVariableDeclarationAndAddImports(variableId, variableValue));
            }
        }
        for (Pair<Integer, String> next2 : projectDataManager.j(javaName)) {
            lists.add(getListDeclarationAndAddImports(next2.first, next2.second));
        }
        for (ViewBean viewBean : projectDataManager.d(projectFileBean.getXmlName())) {
            views.add(getViewDeclarationAndAddImports(viewBean));
        }
        if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            for (ViewBean viewBean : projectDataManager.d(projectFileBean.getDrawerXmlName())) {
                views.add(getDrawerViewDeclarationAndAddImports(viewBean));
            }
        }
        ArrayList<ComponentBean> componentBeans = projectDataManager.e(javaName);
        for (ComponentBean bean : componentBeans) {
            components.add(getComponentDeclarationAndAddImports(bean));
        }

        boolean hasTimer = false;
        boolean hasFirebaseDB = false;
        boolean hasFirebaseStorage = false;
        boolean hasInterstitialAd = false;
        boolean hasRewardedVideoAd = false;
        for (ComponentBean bean : componentBeans) {
            switch (bean.type) {
                case ComponentBean.COMPONENT_TYPE_TIMERTASK:
                    hasTimer = true;
                    break;

                case ComponentBean.COMPONENT_TYPE_FIREBASE:
                    hasFirebaseDB = true;
                    break;

                case ComponentBean.COMPONENT_TYPE_FIREBASE_STORAGE:
                    hasFirebaseStorage = true;
                    break;

                case ComponentBean.COMPONENT_TYPE_INTERSTITIAL_AD:
                    hasInterstitialAd = true;
                    break;

                case ComponentBean.COMPONENT_TYPE_REWARDED_VIDEO_AD:
                    hasRewardedVideoAd = true;
                    break;
            }
        }
        if (hasTimer) {
            fieldsWithStaticInitializers.add(Lx.getComponentFieldCode("Timer"));
        }
        if (hasFirebaseDB) {
            fieldsWithStaticInitializers.add(Lx.getComponentFieldCode("FirebaseDB"));
        }
        if (hasFirebaseStorage) {
            fieldsWithStaticInitializers.add(Lx.getComponentFieldCode("FirebaseStorage"));
        }
        if (hasInterstitialAd) {
            fieldsWithStaticInitializers.add(Lx.getComponentFieldCode("InterstitialAd"));
        }
        if (hasRewardedVideoAd) {
            fieldsWithStaticInitializers.add(Lx.getComponentFieldCode("RewardedVideoAd"));
        }
    }

    /**
     * Adds Local libraries' imports
     */
    private void addLocalLibraryImports() {
        for (String value : mll.getImportLocalLibrary()) {
            addImport(value);
        }
    }
}