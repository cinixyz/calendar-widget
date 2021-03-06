package org.andstatus.todoagenda;

import org.andstatus.todoagenda.prefs.ApplicationPreferences;
import org.andstatus.todoagenda.provider.QueryRow;
import org.andstatus.todoagenda.util.DateUtil;
import org.andstatus.todoagenda.widget.CalendarEntry;
import org.andstatus.todoagenda.widget.WidgetEntry;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.andstatus.todoagenda.RemoteViewsFactory.MIN_MILLIS_BETWEEN_RELOADS;
import static org.junit.Assert.assertEquals;

/**
 * @author yvolk@yurivolkov.com
 */
public class RecurringEventsTest extends BaseWidgetTest {

    private int eventId = 0;

    /**
     * @see <a href="https://github.com/plusonelabs/calendar-widget/issues/191">Issue 191</a> and
     * <a href="https://github.com/plusonelabs/calendar-widget/issues/46">Issue 46</a>
     */
    @Test
    public void testShowRecurringEvents() {
        generateEventInstances();
        assertEquals("Entries: " + factory.getWidgetEntries().size(), 15, countCalendarEntries());
        provider.startEditing();
        ApplicationPreferences.setShowOnlyClosestInstanceOfRecurringEvent(provider.getContext(), true);
        provider.saveSettings();
        generateEventInstances();
        assertEquals("Entries: " + factory.getWidgetEntries().size(), 1, countCalendarEntries());
    }

    int countCalendarEntries() {
        int count = 0;
        for (WidgetEntry widgetEntry : factory.getWidgetEntries()) {
            if (CalendarEntry.class.isAssignableFrom(widgetEntry.getClass())) {
                count++;
            }
        }
        return count;
    }

    void generateEventInstances() {
        EnvironmentChangedReceiver.sleep(MIN_MILLIS_BETWEEN_RELOADS);
        provider.clear();
        DateTime date = DateUtil.now(provider.getSettings().getTimeZone()).withTimeAtStartOfDay();
        long millis = date.getMillis() + TimeUnit.HOURS.toMillis(10);
        eventId++;
        for (int ind = 0; ind < 15; ind++) {
            millis += TimeUnit.DAYS.toMillis(1);
            provider.addRow(new QueryRow().setEventId(eventId).setTitle("Work each day")
                    .setBegin(millis).setEnd(millis + TimeUnit.HOURS.toMillis(9)));
        }
        factory.onDataSetChanged();
        factory.logWidgetEntries(TAG);
    }
}
