package a.a.a;

import android.content.Context;

import com.sketchware.remodgdx.R;

import java.util.ArrayList;

import mod.agus.jcoderz.editor.event.ManageEvent;
import mod.hey.studios.util.Helper;
import mod.hilal.saif.events.EventsHandler;

public class oq {

    /**
     * The known Event names that can be added to all Activities.
     */
    public static final String[] a = EventsHandler.getActivityEvents();

    /**
     * @return The resource ID for an Event's icon
     */
    public static int a(String eventName) {
        switch (eventName) {
            case "initializeLogic":
            case "onBackPressed":
            case "onPostCreate":
            case "onStart":
            case "onStop":
            case "onDestroy":
            case "onResume":
            case "onPause":
            case "moreBlock":
                return R.drawable.bg_event_type_activity;

            case "onBannerAdClicked":
            case "onClick":
                return R.drawable.event_on_click_48dp;

            default:
                return ManageEvent.d(eventName);
        }
    }
    //Description of each event//
    public static String a(String eventName, Context context) {
        switch (eventName) {
            case "show":
                return Helper.getResString(R.string.event_initialize);

            case "render":
                return Helper.getResString(R.string.event_onpostcreated);

            case "resize":
                return Helper.getResString(R.string.event_onstart);

            case "pause":
                return Helper.getResString(R.string.event_onstop);

            case "resume":
                return Helper.getResString(R.string.event_ondestroy);

            case "hide":
                return Helper.getResString(R.string.event_onresume);

            case "dispose":
                return Helper.getResString(R.string.event_onpause);

            default:
                return ManageEvent.e(eventName);
        }
    }

    public static String[] a() {
        return a;
    }

    public static String[] a(Gx classInfo) {
        ArrayList<String> eventList = new ArrayList<>();
        ManageEvent.h(classInfo, eventList);
        return eventList.toArray(new String[0]);
    }

    public static String[] b(Gx classInfo) {
        ArrayList<String> eventList = new ArrayList<>();
        ManageEvent.b(classInfo, eventList);
        return eventList.toArray(new String[0]);
    }

    public static String[] b(String listenerName) {
        ArrayList<String> eventList = new ArrayList<>();
        ManageEvent.c(listenerName, eventList);
        return eventList.toArray(new String[0]);
    }

    public static String[] c(Gx classInfo) {
        ArrayList<String> eventList = new ArrayList<>();
        ManageEvent.a(classInfo, eventList);
        return eventList.toArray(new String[0]);
    }
}
