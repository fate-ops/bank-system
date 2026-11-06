package ch.bbw;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Mutable relative transaction date. Conversion resolves the offset against the current time.
 * @author lro@gmx.ch
 * @version 1.0
 */
public class Scheduled {

    /**
     * The milliseconds until the date is due. Negative values mean that the date is past due.
     */
    private long millisecondsUntilDue;
    /**
     * Creates a date with the given relative offset.
     * @param millisecondsUntilDue offset in milliseconds; negative values are past due
     */
    public Scheduled(long millisecondsUntilDue) {
        this.millisecondsUntilDue = millisecondsUntilDue;
    }

    /**
     * Checks whether the relative offset is negative.
     * @return true if the offset is negative.
     */
    public boolean isPastDue() {
        return millisecondsUntilDue < 0;
    }

    /**
     * Creates a date with zero offset.
     * @return a new date with zero offset.
     */
    public static Scheduled now() {
        return new Scheduled(0);
    }

    /**
     * Converts the Scheduled date to a unix timestamp.
     * @return milliseconds since the Unix epoch
     */
    public long toUnix(){
        return toInstant().toEpochMilli();
    }

    /**
     * Converts the Scheduled date to an Instant.
     * @return the Instant
     */
    public Instant toInstant(){

        return Instant.now().plus(millisecondsUntilDue, ChronoUnit.MILLIS);
    }

    /**
     * Adds a duration to the Scheduled date.
     * @param duration the duration to add
     * @param unit the unit of the duration
     * @return the Scheduled date
     * @throws UnsupportedTemporalTypeException for an unsupported unit
     */
    public Scheduled plus(long duration, ChronoUnit unit){
            return switch (unit) {
                case MILLIS    -> plusMillis(duration);
                case SECONDS   -> plusSeconds(duration);
                case MINUTES   -> plusMinutes(duration);
                case HOURS     -> plusHours(duration);
                case DAYS      -> plusDays(duration);
                case WEEKS -> plusWeeks(duration);
                case MONTHS    -> plusMonths(duration);
                case YEARS     -> plusYears(duration);
                default -> throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit + "\nI guess you have to implement it yourself ;)");
            };
    }

    /**
     * Adds a duration measured in milliseconds.
     * @param millisToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusMillis(long millisToAdd){
        millisecondsUntilDue += millisToAdd;
        return this;
    }

    /**
     * Adds a duration measured in seconds.
     * @param secondsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusSeconds(long secondsToAdd){
        millisecondsUntilDue += secondsToAdd * 1000;
        return this;
    }

    /**
     * Adds a duration measured in minutes.
     * @param minutesToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusMinutes(long minutesToAdd){
        millisecondsUntilDue += minutesToAdd * 1000 * 60;
        return this;
    }

    /**
     * Adds a duration measured in hours.
     * @param hoursToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusHours(long hoursToAdd){
        millisecondsUntilDue += hoursToAdd * 1000 * 60 * 60;
        return this;
    }

    /**
     * Adds a duration measured in 24-hour days.
     * @param daysToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusDays(long daysToAdd){
        millisecondsUntilDue += daysToAdd * 1000 * 60 * 60 * 24;
        return this;
    }

    /**
     * Adds a duration measured in seven-day weeks.
     * @param weeksToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusWeeks(long weeksToAdd){
        millisecondsUntilDue += weeksToAdd * 1000 * 60 * 60 * 24 * 7;
        return this;
    }

    /**
     * Adds a duration measured in 30-day months.
     * @param monthsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusMonths(long monthsToAdd){
        millisecondsUntilDue += monthsToAdd * 1000 * 60 * 60 * 24 * 30;
        return this;
    }

    /**
     * Adds a duration measured in 365-day years.
     * @param yearsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled plusYears(long yearsToAdd){
        millisecondsUntilDue += yearsToAdd * 1000 * 60 * 60 * 24 * 365;
        return this;
    }

    /**
     * Subtracts a duration from the Scheduled date.
     * @param duration the duration to subtract
     * @param unit the unit of the duration
     * @return the Scheduled date
     * @throws UnsupportedTemporalTypeException for an unsupported unit
     */
    public Scheduled minus(long duration, ChronoUnit unit){
        return switch (unit) {
            case MILLIS    -> minusMillis(duration);
            case SECONDS   -> minusSeconds(duration);
            case MINUTES   -> minusMinutes(duration);
            case HOURS     -> minusHours(duration);
            case DAYS      -> minusDays(duration);
            case WEEKS -> minusWeeks(duration);
            case MONTHS    -> minusMonths(duration);
            case YEARS     -> minusYears(duration);
            default -> throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit + "\nI guess you have to implement it yourself ;)");
        };
    }

    /**
     * Subtracts a duration measured in milliseconds.
     * @param millisToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusMillis(long millisToAdd){
        millisecondsUntilDue -= millisToAdd;
        return this;
    }

    /**
     * Subtracts a duration measured in seconds.
     * @param secondsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusSeconds(long secondsToAdd){
        millisecondsUntilDue -= secondsToAdd * 1000;
        return this;
    }

    /**
     * Subtracts a duration measured in minutes.
     * @param minutesToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusMinutes(long minutesToAdd){
        millisecondsUntilDue -= minutesToAdd * 1000 * 60;
        return this;
    }

    /**
     * Subtracts a duration measured in hours.
     * @param hoursToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusHours(long hoursToAdd){
        millisecondsUntilDue -= hoursToAdd * 1000 * 60 * 60;
        return this;
    }

    /**
     * Subtracts a duration measured in 24-hour days.
     * @param daysToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusDays(long daysToAdd){
        millisecondsUntilDue -= daysToAdd * 1000 * 60 * 60 * 24;
        return this;
    }

    /**
     * Subtracts a duration measured in seven-day weeks.
     * @param weeksToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusWeeks(long weeksToAdd){
        millisecondsUntilDue -= weeksToAdd * 1000 * 60 * 60 * 24 * 7;
        return this;
    }

    /**
     * Subtracts a duration measured in 30-day months.
     * @param monthsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusMonths(long monthsToAdd){
        millisecondsUntilDue -= monthsToAdd * 1000 * 60 * 60 * 24 * 30;
        return this;
    }

    /**
     * Subtracts a duration measured in 365-day years.
     * @param yearsToAdd signed number of units
     * @return this instance after updating the offset
     */
    public Scheduled minusYears(long yearsToAdd){
        millisecondsUntilDue -= yearsToAdd * 1000 * 60 * 60 * 24 * 365;
        return this;
    }

    /**
     * Converts the Scheduled date to a String.
     * @return the String representation of the Scheduled date
     */
    @Override
    public String toString() {
        return toInstant().toString();
    }
}
