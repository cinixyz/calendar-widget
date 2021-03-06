package org.andstatus.todoagenda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.andstatus.todoagenda.prefs.AllSettings;
import org.andstatus.todoagenda.provider.EventProviderType;
import org.andstatus.todoagenda.provider.MockCalendarContentProvider;
import org.andstatus.todoagenda.util.DateUtil;
import org.joda.time.DateTimeZone;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * @author yvolk@yurivolkov.com
 */
public class TestRunListener extends RunListener {
    private static final String TAG = "testSuite";
    private final DateTimeZone storedZone;

    public TestRunListener() {
        Log.i(TAG,  "TestRunListener created");
        storedZone = DateTimeZone.getDefault();
    }

    @Override
    public void testSuiteFinished(Description description) throws Exception {
        super.testSuiteFinished(description);
        Log.i(TAG, "Test Suite finished: " + description);
        if (description.toString().equals("null")) restoreWidgets();
    }

    private void restoreWidgets() {
        MockCalendarContentProvider.tearDown();

        DateUtil.setNow(null);
        DateTimeZone.setDefault(storedZone);
        AllSettings.forget();
        EventProviderType.forget();
        EnvironmentChangedReceiver.forget();

        refreshWidgets();
        Log.i(TAG, "App restored");
    }

    // Context is not exactly what the widgets use normally...
    private static void refreshWidgets() {
        Intent intent = new Intent(RemoteViewsFactory.ACTION_REFRESH);
        InstrumentationRegistry.getInstrumentation().getTargetContext().sendBroadcast(intent);
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        EnvironmentChangedReceiver.updateAllWidgets(targetContext);
        EnvironmentChangedReceiver.sleep(2000);
    }
}
