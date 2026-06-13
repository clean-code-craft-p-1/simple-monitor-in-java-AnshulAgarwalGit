package vitals;

public final class VitalsChecker {
  private static final float MIN_TEMPERATURE = 95.0f;
  private static final float MAX_TEMPERATURE = 102.0f;
  private static final float MIN_PULSE_RATE = 60.0f;
  private static final float MAX_PULSE_RATE = 100.0f;
  private static final float MIN_SPO2 = 90.0f;
  private static final int ALERT_FLASH_COUNT = 6;

  private static final VitalRule[] VITAL_RULES = {
      new VitalRule("Temperature is critical!", VitalsChecker::isTemperatureOk),
      new VitalRule("Pulse Rate is out of range!", VitalsChecker::isPulseRateOk),
      new VitalRule("Oxygen Saturation out of range!", VitalsChecker::isSpo2Ok)
  };

  private VitalsChecker() {
  }

  static boolean vitalsOk(float temperature, float pulseRate, float spo2)
      throws InterruptedException {
    String alertMessage = firstAlertMessage(temperature, pulseRate, spo2);
    if (alertMessage == null) {
      return true;
    }

    showCriticalAlert(alertMessage, System.out::print, ms -> {
    });
    return false;
  }

  static boolean isTemperatureOk(float temperature, float pulseRate, float spo2) {
    return isInRange(temperature, MIN_TEMPERATURE, MAX_TEMPERATURE);
  }

  static boolean isPulseRateOk(float temperature, float pulseRate, float spo2) {
    return isInRange(pulseRate, MIN_PULSE_RATE, MAX_PULSE_RATE);
  }

  static boolean isSpo2Ok(float temperature, float pulseRate, float spo2) {
    return spo2 >= MIN_SPO2;
  }

  static String firstAlertMessage(float temperature, float pulseRate, float spo2) {
    for (VitalRule vitalRule : VITAL_RULES) {
      if (!vitalRule.isOk(temperature, pulseRate, spo2)) {
        return vitalRule.message;
      }
    }
    return null;
  }

  static void showCriticalAlert(String alertMessage, AlertOutput alertOutput, Sleeper sleeper)
      throws InterruptedException {
    alertOutput.write(alertMessage + System.lineSeparator());
    for (int flashCount = 0; flashCount < ALERT_FLASH_COUNT; flashCount++) {
      alertOutput.write("\r* ");
      sleeper.sleep(1000);
      alertOutput.write("\r *");
      sleeper.sleep(1000);
    }
  }

  private static boolean isInRange(float value, float min, float max) {
    return value >= min && value <= max;
  }

  private interface VitalCheck {
    boolean isOk(float temperature, float pulseRate, float spo2);
  }

  private static final class VitalRule {
    private final String message;
    private final VitalCheck check;

    private VitalRule(String message, VitalCheck check) {
      this.message = message;
      this.check = check;
    }

    private boolean isOk(float temperature, float pulseRate, float spo2) {
      return check.isOk(temperature, pulseRate, spo2);
    }
  }

  interface AlertOutput {
    void write(String text);
  }

  interface Sleeper {
    void sleep(long milliseconds) throws InterruptedException;
  }
}
