package hcmut.edu.vn.tutor_support_system.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateTimeUtil {

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
  public static final DateTimeFormatter DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private DateTimeUtil() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static String formatDate(LocalDate date) {
    return date != null ? date.format(DATE_FORMATTER) : null;
  }

  public static String formatTime(LocalTime time) {
    return time != null ? time.format(TIME_FORMATTER) : null;
  }

  public static String formatDateTime(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
  }

  public static String formatDateForDisplay(LocalDate date) {
    return date != null ? date.format(DISPLAY_DATE_FORMATTER) : null;
  }

  public static String formatDateTimeForDisplay(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DISPLAY_DATETIME_FORMATTER) : null;
  }

  public static LocalDate parseDate(String dateString) {
    return dateString != null && !dateString.isEmpty()
        ? LocalDate.parse(dateString, DATE_FORMATTER)
        : null;
  }

  public static LocalTime parseTime(String timeString) {
    return timeString != null && !timeString.isEmpty()
        ? LocalTime.parse(timeString, TIME_FORMATTER)
        : null;
  }

  public static LocalDateTime parseDateTime(String dateTimeString) {
    return dateTimeString != null && !dateTimeString.isEmpty()
        ? LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER)
        : null;
  }

  public static boolean isToday(LocalDate date) {
    return date != null && date.equals(LocalDate.now());
  }

  public static boolean isPast(LocalDate date) {
    return date != null && date.isBefore(LocalDate.now());
  }

  public static boolean isFuture(LocalDate date) {
    return date != null && date.isAfter(LocalDate.now());
  }

  public static boolean isPast(LocalDateTime dateTime) {
    return dateTime != null && dateTime.isBefore(LocalDateTime.now());
  }

  public static boolean isFuture(LocalDateTime dateTime) {
    return dateTime != null && dateTime.isAfter(LocalDateTime.now());
  }

  public static long daysBetween(LocalDate start, LocalDate end) {
    return start != null && end != null ? ChronoUnit.DAYS.between(start, end) : 0;
  }

  public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
    return start != null && end != null ? ChronoUnit.HOURS.between(start, end) : 0;
  }

  public static long minutesBetween(LocalTime start, LocalTime end) {
    return start != null && end != null ? ChronoUnit.MINUTES.between(start, end) : 0;
  }

  public static boolean isWeekday(LocalDate date) {
    if (date == null) return false;
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
  }

  public static boolean isWeekend(LocalDate date) {
    if (date == null) return false;
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
  }

  public static LocalDate getStartOfSemester(String semester) {
    if (semester == null || semester.length() != 3) return null;

    int yearLastTwoDigits = Integer.parseInt(semester.substring(0, 2));
    int semesterNumber = Integer.parseInt(semester.substring(2, 3));

    int fullYear = 2000 + yearLastTwoDigits;
    if (semesterNumber == 2 || semesterNumber == 3) {
      fullYear++;
    }

    return switch (semesterNumber) {
      case 1 -> LocalDate.of(fullYear - 1, 9, 1);
      case 2 -> LocalDate.of(fullYear, 1, 1);
      case 3 -> LocalDate.of(fullYear, 5, 1);
      default -> null;
    };
  }

  public static LocalDate getEndOfSemester(String semester) {
    if (semester == null || semester.length() != 3) return null;

    int yearLastTwoDigits = Integer.parseInt(semester.substring(0, 2));
    int semesterNumber = Integer.parseInt(semester.substring(2, 3));

    int fullYear = 2000 + yearLastTwoDigits;
    if (semesterNumber == 2 || semesterNumber == 3) {
      fullYear++;
    }

    return switch (semesterNumber) {
      case 1 -> LocalDate.of(fullYear, 12, 31);
      case 2 -> LocalDate.of(fullYear, 4, 30);
      case 3 -> LocalDate.of(fullYear, 8, 31);
      default -> null;
    };
  }

  public static boolean isWithinSemester(LocalDate date, String semester) {
    LocalDate start = getStartOfSemester(semester);
    LocalDate end = getEndOfSemester(semester);

    return date != null
        && start != null
        && end != null
        && !date.isBefore(start)
        && !date.isAfter(end);
  }

  public static String getCurrentSemester() {
    LocalDate now = LocalDate.now();
    int year = now.getYear();
    int month = now.getMonthValue();

    int yearLastTwoDigits;
    int semesterNumber;

    if (month >= 9 && month <= 12) {
      yearLastTwoDigits = year % 100;
      semesterNumber = 1;
    } else if (month >= 1 && month <= 4) {
      yearLastTwoDigits = (year - 1) % 100;
      semesterNumber = 2;
    } else {
      yearLastTwoDigits = (year - 1) % 100;
      semesterNumber = 3;
    }

    return String.format("%02d%d", yearLastTwoDigits, semesterNumber);
  }
}
