package ru.firemoon777.doubleclock;

import android.graphics.Color;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Tutorial implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                TextView tv = (TextView) param.thisObject;
                String text = tv.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                try {
                    Calendar calendar = Calendar.getInstance();
                    Date date = dateFormat.parse(text);
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR, -2);
                    text = dateFormat.format(calendar.getTime()) + " / " + text;
                } catch (Exception e) {
                    tv.setTextColor(Color.RED);
                }
                tv.setText(text);
            }
        });
    }
}
