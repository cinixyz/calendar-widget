package org.andstatus.todoagenda.calendar;

import android.content.Context;
import android.util.Log;

import org.andstatus.todoagenda.prefs.AllSettings;
import org.andstatus.todoagenda.prefs.InstanceSettings;
import org.andstatus.todoagenda.prefs.OrderedEventSource;
import org.andstatus.todoagenda.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import static org.andstatus.todoagenda.util.StringUtil.nonEmpty;
import static org.andstatus.todoagenda.util.StringUtil.notNull;

public class CalendarEvent {

    private final Context context;
    private final int widgetId;
    private final DateTimeZone zone;
    private final boolean allDay;

    private OrderedEventSource eventSource;
    private int eventId;
    private String title = "";
    private DateTime startDate;
    private DateTime endDate;
    private int color;
    private boolean mHasDefaultCalendarColor;
    private String location = "";
    private boolean alarmActive;
    private boolean recurring;

    public CalendarEvent(Context context, int widgetId, DateTimeZone zone, boolean allDay) {
        this.context = context;
        this.widgetId = widgetId;
        this.zone = zone;
        this.allDay = allDay;
    }

    public OrderedEventSource getEventSource() {
        return eventSource;
    }

    public CalendarEvent setEventSource(OrderedEventSource eventSource) {
        this.eventSource = eventSource;
        return this;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = allDay ? startDate.withTimeAtStartOfDay() : startDate;
        fixEndDate();
    }

    public void setStartMillis(long startMillis) {
        this.startDate = dateFromMillis(startMillis);
        fixEndDate();
    }

    public long getStartMillis() {
        return dateToMillis(startDate);
    }

    private DateTime dateFromMillis(long millis) {
        return allDay ? fromAllDayMillis(millis) : new DateTime(millis, zone);
    }

    private static volatile long fixTimeOfAllDayEventLoggedAt = 0;
    /**
     * Implemented based on this answer: http://stackoverflow.com/a/5451245/297710
     */
    private DateTime fromAllDayMillis(long millis) {
        String msgLog = "millis=" + millis;
        DateTime fixed;
        try {
            DateTime utcDate = new DateTime(millis, DateTimeZone.UTC);
            LocalDateTime ldt = new LocalDateTime()
                    .withYear(utcDate.getYear())
                    .withMonthOfYear(utcDate.getMonthOfYear())
                    .withDayOfMonth(utcDate.getDayOfMonth())
                    .withMillisOfDay(0);
            int hour = 0;
            while (zone.isLocalDateTimeGap(ldt)) {
                Log.v("fixTimeOfAllDayEvent", "Local Date Time Gap: " + ldt + "; " + msgLog);
                ldt = ldt.withHourOfDay(++hour);
            }
            fixed = ldt.toDateTime(zone);
            msgLog += " -> " + fixed;
            if (Math.abs(System.currentTimeMillis() - fixTimeOfAllDayEventLoggedAt) > 1000) {
                fixTimeOfAllDayEventLoggedAt = System.currentTimeMillis();
                Log.v("fixTimeOfAllDayEvent", msgLog);
            }
        } catch (org.joda.time.IllegalInstantException e) {
            throw new org.joda.time.IllegalInstantException(msgLog + " caused by: " + e);
        }
        return fixed;
    }

    private void fixEndDate() {
        if (endDate == null || !endDate.isAfter(startDate)) {
            endDate = allDay ? startDate.plusDays(1) : startDate.plusSeconds(1);
        }
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = notNull(title);
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = allDay ? endDate.withTimeAtStartOfDay() : endDate;
        fixEndDate();
    }

    public void setEndMillis(long endMillis) {
        this.endDate = dateFromMillis(endMillis);
        fixEndDate();
    }

    public long getEndMillis() {
        return dateToMillis(endDate);
    }

    private long dateToMillis(DateTime date) {
        return allDay ? toAllDayMillis(date) : date.getMillis();
    }

    private long toAllDayMillis(DateTime date) {
        DateTime utcDate = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0,
                DateTimeZone.UTC);
        return utcDate.getMillis();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hasDefaultCalendarColor() {
        return mHasDefaultCalendarColor;
    }

    public void setDefaultCalendarColor() {
        mHasDefaultCalendarColor = true;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setLocation(String location) {
        this.location = notNull(location);
    }

    public String getLocation() {
        return location;
    }

    public void setAlarmActive(boolean active) {
        this.alarmActive = active;
    }

    public boolean isAlarmActive() {
        return alarmActive;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isRecurring() {
        return recurring;
    }

    @Override
    public String toString() {
        return "CalendarEvent [eventId=" + eventId
                + (nonEmpty(title) ? ", title=" + title : "")
                + ", startDate=" + getStartDate()
                + (endDate != null ? ", endDate=" + endDate : "")
                + ", color=" + color
                + (mHasDefaultCalendarColor ? " is default" : "")
                + ", allDay=" + allDay
                + ", alarmActive=" + alarmActive
                + ", recurring=" + recurring
                + (nonEmpty(location) ? ", location=" + location : "") +
                "; Source [" + eventSource + "]]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CalendarEvent that = (CalendarEvent) o;
        if (eventId != that.eventId || !startDate.equals(that.startDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId;
        result += 31 * startDate.hashCode();
        return result;
    }

    public boolean isActive() {
        DateTime now = DateUtil.now(zone);
        return startDate.isBefore(now) && endDate.isAfter(now);
    }

    public boolean isPartOfMultiDayEvent() {
        return endDate.withTimeAtStartOfDay().isAfter(startDate.withTimeAtStartOfDay());
    }

    public InstanceSettings getSettings() {
        return AllSettings.instanceFromId(context, widgetId);
    }

    public Context getContext() {
        return context;
    }
}
