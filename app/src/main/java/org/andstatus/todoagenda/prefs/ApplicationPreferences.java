package org.andstatus.todoagenda.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.andstatus.todoagenda.EndedSomeTimeAgo;
import org.andstatus.todoagenda.TextShading;
import org.andstatus.todoagenda.widget.EventEntryLayout;
import org.andstatus.todoagenda.widget.WidgetHeaderLayout;

import java.util.List;
import java.util.Map;

import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_ABBREVIATE_DATES;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_ABBREVIATE_DATES_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_ACTIVE_SOURCES;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_DATE_FORMAT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_DATE_FORMAT_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_DAY_HEADER_ALIGNMENT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENTS_BACKGROUND_COLOR;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENTS_BACKGROUND_COLOR_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENTS_ENDED;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENT_ENTRY_LAYOUT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENT_RANGE;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_EVENT_RANGE_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_FILL_ALL_DAY;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_FILL_ALL_DAY_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_HIDE_BASED_ON_KEYWORDS;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_HIDE_DUPLICATES;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_HORIZONTAL_LINE_BELOW_DAY_HEADER;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_INDICATE_ALERTS;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_INDICATE_RECURRING;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_LOCKED_TIME_ZONE_ID;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_MULTILINE_DETAILS;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_MULTILINE_DETAILS_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_MULTILINE_TITLE;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_MULTILINE_TITLE_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_PAST_EVENTS_BACKGROUND_COLOR;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_PAST_EVENTS_BACKGROUND_COLOR_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_REFRESH_PERIOD_MINUTES;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_REFRESH_PERIOD_MINUTES_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_DATE_ON_WIDGET_HEADER;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_DAYS_WITHOUT_EVENTS;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_DAY_HEADERS;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_END_TIME;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_END_TIME_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_EVENT_ICON;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_LOCATION;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_LOCATION_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_NUMBER_OF_DAYS_TO_EVENT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_ONLY_CLOSEST_INSTANCE_OF_RECURRING_EVENT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_PAST_EVENTS_UNDER_ONE_HEADER;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_SHOW_PAST_EVENTS_WITH_DEFAULT_COLOR;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_TEXT_SIZE_SCALE;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_TODAYS_EVENTS_BACKGROUND_COLOR;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_TODAYS_EVENTS_BACKGROUND_COLOR_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_WIDGET_HEADER_BACKGROUND_COLOR;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_WIDGET_HEADER_BACKGROUND_COLOR_DEFAULT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_WIDGET_HEADER_LAYOUT;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_WIDGET_ID;
import static org.andstatus.todoagenda.prefs.InstanceSettings.PREF_WIDGET_INSTANCE_NAME;

public class ApplicationPreferences {

    private ApplicationPreferences() {
        // prohibit instantiation
    }

    public static void fromInstanceSettings(Context context, Integer widgetId) {
        synchronized (ApplicationPreferences.class) {
            InstanceSettings settings = AllSettings.instanceFromId(context, widgetId);
            setWidgetId(context, widgetId == 0 ? settings.getWidgetId() : widgetId);
            setBoolean(context, PREF_SHOW_DATE_ON_WIDGET_HEADER, settings.getShowDateOnWidgetHeader());
            setString(context, PREF_WIDGET_INSTANCE_NAME, settings.getWidgetInstanceName());
            setActiveEventSources(context, settings.getActiveEventSources());
            setEventRange(context, settings.getEventRange());
            setEventsEnded(context, settings.getEventsEnded());
            setFillAllDayEvents(context, settings.getFillAllDayEvents());
            setHideBasedOnKeywords(context, settings.getHideBasedOnKeywords());
            setInt(context, PREF_WIDGET_HEADER_BACKGROUND_COLOR, settings.getWidgetHeaderBackgroundColor());
            setInt(context, PREF_PAST_EVENTS_BACKGROUND_COLOR, settings.getPastEventsBackgroundColor());
            setInt(context, PREF_TODAYS_EVENTS_BACKGROUND_COLOR, settings.getTodaysEventsBackgroundColor());
            setInt(context, PREF_EVENTS_BACKGROUND_COLOR, settings.getEventsBackgroundColor());
            setShowDaysWithoutEvents(context, settings.getShowDaysWithoutEvents());
            setShowDayHeaders(context, settings.getShowDayHeaders());
            setHorizontalLineBelowDayHeader(context, settings.getHorizontalLineBelowDayHeader());
            setShowPastEventsUnderOneHeader(context, settings.getShowPastEventsUnderOneHeader());
            setShowPastEventsWithDefaultColor(context, settings.getShowPastEventsWithDefaultColor());
            setShowEventIcon(context, settings.getShowEventIcon());
            setShowNumberOfDaysToEvent(context, settings.getShowNumberOfDaysToEvent());
            setBoolean(context, PREF_SHOW_END_TIME, settings.getShowEndTime());
            setBoolean(context, PREF_SHOW_LOCATION, settings.getShowLocation());
            setString(context, PREF_DATE_FORMAT, settings.getDateFormat());
            setAbbreviateDates(context, settings.getAbbreviateDates());
            setLockedTimeZoneId(context, settings.getLockedTimeZoneId());
            setRefreshPeriodMinutes(context, settings.getRefreshPeriodMinutes());
            setString(context, PREF_EVENT_ENTRY_LAYOUT, settings.getEventEntryLayout().value);
            setBoolean(context, PREF_MULTILINE_TITLE, settings.isMultilineTitle());
            setBoolean(context, PREF_MULTILINE_DETAILS, settings.isMultilineDetails());
            setBoolean(context, PREF_SHOW_ONLY_CLOSEST_INSTANCE_OF_RECURRING_EVENT, settings
                    .getShowOnlyClosestInstanceOfRecurringEvent());
            setHideDuplicates(context, settings.getHideDuplicates());
            setBoolean(context, PREF_INDICATE_ALERTS, settings.getIndicateAlerts());
            setBoolean(context, PREF_INDICATE_RECURRING, settings.getIndicateRecurring());
            for (Map.Entry<TextShadingPref, TextShading> entry: settings.shadings.entrySet()) {
                setString(context, entry.getKey().preferenceName, entry.getValue().name());
            }
            setString(context, PREF_WIDGET_HEADER_LAYOUT, settings.getWidgetHeaderLayout().value);
            setString(context, PREF_TEXT_SIZE_SCALE, settings.getTextSizeScale().preferenceValue);
            setString(context, PREF_DAY_HEADER_ALIGNMENT, settings.getDayHeaderAlignment());
        }
    }

    public static void save(Context context, int wigdetId) {
        if (wigdetId != 0 && wigdetId == getWidgetId(context)) {
            AllSettings.saveFromApplicationPreferences(context, wigdetId);
        }
    }

    public static int getWidgetId(Context context) {
        return getInt(context, PREF_WIDGET_ID, 0);
    }

    public static void setWidgetId(Context context, int value) {
        setInt(context, PREF_WIDGET_ID, value);
    }

    public static boolean noTaskSources(Context context) {
        List<OrderedEventSource> sources = getActiveEventSources(context);
        for(OrderedEventSource orderedSource: sources) {
            if (!orderedSource.source.providerType.isCalendar) return false;
        }
        return true;
    }

    public static List<OrderedEventSource> getActiveEventSources(Context context) {
        return OrderedEventSource.fromJsonString(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_ACTIVE_SOURCES, null));
    }

    public static void setActiveEventSources(Context context, List<OrderedEventSource> sources) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_ACTIVE_SOURCES, OrderedEventSource.toJsonString(sources));
        editor.apply();
    }

    public static int getEventRange(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs == null ? 0 : parseIntSafe(prefs.getString(PREF_EVENT_RANGE, PREF_EVENT_RANGE_DEFAULT));
    }

    public static void setEventRange(Context context, int value) {
        setString(context, PREF_EVENT_RANGE, Integer.toString(value));
    }

    public static EndedSomeTimeAgo getEventsEnded(Context context) {
        return EndedSomeTimeAgo.fromValue(PreferenceManager.getDefaultSharedPreferences(context).getString(
                PREF_EVENTS_ENDED, ""));
    }

    public static void setEventsEnded(Context context, EndedSomeTimeAgo value) {
        setString(context, PREF_EVENTS_ENDED, value.save());
    }

    public static boolean getFillAllDayEvents(Context context) {
        return getBoolean(context, PREF_FILL_ALL_DAY, PREF_FILL_ALL_DAY_DEFAULT);
    }

    private static void setFillAllDayEvents(Context context, boolean value) {
        setBoolean(context, PREF_FILL_ALL_DAY, value);
    }

    public static String getHideBasedOnKeywords(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_HIDE_BASED_ON_KEYWORDS, "");
    }

    private static void setHideBasedOnKeywords(Context context, String value) {
        setString(context, PREF_HIDE_BASED_ON_KEYWORDS, value);
    }

    public static int getWidgetHeaderBackgroundColor(Context context) {
        return getInt(context, PREF_WIDGET_HEADER_BACKGROUND_COLOR,
                PREF_WIDGET_HEADER_BACKGROUND_COLOR_DEFAULT);
    }

    public static int getPastEventsBackgroundColor(Context context) {
        return getInt(context, PREF_PAST_EVENTS_BACKGROUND_COLOR,
                PREF_PAST_EVENTS_BACKGROUND_COLOR_DEFAULT);
    }

    public static int getTodaysEventsBackgroundColor(Context context) {
        return getInt(context, PREF_TODAYS_EVENTS_BACKGROUND_COLOR,
                PREF_TODAYS_EVENTS_BACKGROUND_COLOR_DEFAULT);
    }

    public static int getEventsBackgroundColor(Context context) {
        return getInt(context, PREF_EVENTS_BACKGROUND_COLOR,
                PREF_EVENTS_BACKGROUND_COLOR_DEFAULT);
    }

    public static boolean getHorizontalLineBelowDayHeader(Context context) {
        return getBoolean(context, PREF_HORIZONTAL_LINE_BELOW_DAY_HEADER, false);
    }

    private static void setHorizontalLineBelowDayHeader(Context context, boolean value) {
        setBoolean(context, PREF_HORIZONTAL_LINE_BELOW_DAY_HEADER, value);
    }

    public static boolean getShowDaysWithoutEvents(Context context) {
        return getBoolean(context, PREF_SHOW_DAYS_WITHOUT_EVENTS, false);
    }

    private static void setShowDaysWithoutEvents(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_DAYS_WITHOUT_EVENTS, value);
    }

    public static boolean getShowDayHeaders(Context context) {
        return getBoolean(context, PREF_SHOW_DAY_HEADERS, true);
    }

    private static void setShowDayHeaders(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_DAY_HEADERS, value);
    }

    public static boolean getShowPastEventsUnderOneHeader(Context context) {
        return getBoolean(context, PREF_SHOW_PAST_EVENTS_UNDER_ONE_HEADER, false);
    }

    private static void setShowPastEventsUnderOneHeader(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_PAST_EVENTS_UNDER_ONE_HEADER, value);
    }

    public static boolean getShowEventIcon(Context context) {
        return getBoolean(context, PREF_SHOW_EVENT_ICON, false);
    }

    public static boolean getShowNumberOfDaysToEvent(Context context) {
        return getBoolean(context, PREF_SHOW_NUMBER_OF_DAYS_TO_EVENT, false);
    }

    public static void setShowEventIcon(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_EVENT_ICON, value);
    }

    public static void setShowNumberOfDaysToEvent(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_NUMBER_OF_DAYS_TO_EVENT, value);
    }

    public static boolean getShowPastEventsWithDefaultColor(Context context) {
        return getBoolean(context, PREF_SHOW_PAST_EVENTS_WITH_DEFAULT_COLOR, false);
    }

    public static void setShowPastEventsWithDefaultColor(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_PAST_EVENTS_WITH_DEFAULT_COLOR, value);
    }

    public static boolean getShowEndTime(Context context) {
        return getBoolean(context, PREF_SHOW_END_TIME, PREF_SHOW_END_TIME_DEFAULT);
    }

    public static boolean getShowLocation(Context context) {
        return getBoolean(context, PREF_SHOW_LOCATION, PREF_SHOW_LOCATION_DEFAULT);
    }

    public static String getDateFormat(Context context) {
        return getString(context, PREF_DATE_FORMAT, PREF_DATE_FORMAT_DEFAULT);
    }

    public static boolean getAbbreviateDates(Context context) {
        return getBoolean(context, PREF_ABBREVIATE_DATES, PREF_ABBREVIATE_DATES_DEFAULT);
    }

    public static void setAbbreviateDates(Context context, boolean value) {
        setBoolean(context, PREF_ABBREVIATE_DATES, value);
    }

    public static String getLockedTimeZoneId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCKED_TIME_ZONE_ID, "");
    }

    public static void setLockedTimeZoneId(Context context, String value) {
        setString(context, PREF_LOCKED_TIME_ZONE_ID, value);
    }

    public static void setRefreshPeriodMinutes(Context context, int value) {
        setString(context, PREF_REFRESH_PERIOD_MINUTES, Integer.toString(value > 0
                ? value
                : PREF_REFRESH_PERIOD_MINUTES_DEFAULT));
    }

    public static int getRefreshPeriodMinutes(Context context) {
        int stored = getIntStoredAsString(context, PREF_REFRESH_PERIOD_MINUTES, PREF_REFRESH_PERIOD_MINUTES_DEFAULT);
        return stored > 0 ? stored : PREF_REFRESH_PERIOD_MINUTES_DEFAULT;
    }

    public static boolean isTimeZoneLocked(Context context) {
        return !TextUtils.isEmpty(getLockedTimeZoneId(context));
    }

    public static EventEntryLayout getEventEntryLayout(Context context) {
        return EventEntryLayout.fromValue(
                getString(context, PREF_EVENT_ENTRY_LAYOUT, ""));
    }

    public static boolean isMultilineTitle(Context context) {
        return getBoolean(context, PREF_MULTILINE_TITLE, PREF_MULTILINE_TITLE_DEFAULT);
    }

    public static boolean isMultilineDetails(Context context) {
        return getBoolean(context, PREF_MULTILINE_DETAILS, PREF_MULTILINE_DETAILS_DEFAULT);
    }

    public static boolean getShowOnlyClosestInstanceOfRecurringEvent(Context context) {
        return getBoolean(context, PREF_SHOW_ONLY_CLOSEST_INSTANCE_OF_RECURRING_EVENT, false);
    }

    public static void setShowOnlyClosestInstanceOfRecurringEvent(Context context, boolean value) {
        setBoolean(context, PREF_SHOW_ONLY_CLOSEST_INSTANCE_OF_RECURRING_EVENT, value);
    }

    public static boolean getHideDuplicates(Context context) {
        return getBoolean(context, PREF_HIDE_DUPLICATES, false);
    }

    public static void setHideDuplicates(Context context, boolean value) {
        setBoolean(context, PREF_HIDE_DUPLICATES, value);
    }

    private static void setString(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getIntStoredAsString(Context context, String key, int defaultValue) {
        try {
            String stringValue = getString(context, key, "");
            if (TextUtils.isEmpty(stringValue)) return defaultValue;

            return Integer.parseInt(stringValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getString(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    private static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    private static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, defaultValue);
    }

    public static String getWidgetInstanceName(Context context) {
        return getString(context, PREF_WIDGET_INSTANCE_NAME, "");
    }

    public static WidgetHeaderLayout getWidgetHeaderLayout(Context context) {
        return WidgetHeaderLayout.fromValue(getString(context, PREF_WIDGET_HEADER_LAYOUT, ""));
    }

    public static boolean getShowDateOnWidgetHeader(Context context) {
        return getBoolean(context, PREF_SHOW_DATE_ON_WIDGET_HEADER, true);
    }

    public static boolean noPastEvents(Context context) {
        return !getShowPastEventsWithDefaultColor(context) &&
                getEventsEnded(context) == EndedSomeTimeAgo.NONE &&
                noTaskSources(context);
    }

    public static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
