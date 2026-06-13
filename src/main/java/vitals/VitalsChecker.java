package vitals;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitors vital signs and performs comprehensive health assessments.
 * 
 * Checks temperature, pulse rate, and oxygen saturation against clinical thresholds.
 * Reports ALL failing vitals (not just the first) with clinical context for end-user display.
 */
public final class VitalsChecker {
  private static final int ALERT_FLASH_COUNT = 6;

  private final VitalThresholds thresholds;

  /**
   * Creates a monitor with standard adult thresholds.
   */
  public VitalsChecker() {
    this.thresholds = VitalThresholds.STANDARD_ADULT;
  }

  /**
   * Creates a monitor with custom thresholds (e.g., pediatric).
   */
  public VitalsChecker(VitalThresholds thresholds) {
    this.thresholds = thresholds;
  }

  /**
   * Performs a comprehensive health assessment of a patient.
   * 
   * Checks all three vital signs and collects ALL failures with clinical context.
   * Returns detailed assessment ready for display to medical staff.
   *
   * @param vitals Patient's current vital signs
   * @return Assessment with status, all failing vitals, and clinical context
   */
  public PatientHealthAssessment assessPatientHealth(VitalsSnapshot vitals) {
    List<VitalFailure> failures = new ArrayList<>();

    // Check temperature
    if (!thresholds.getTemperatureRange().isInRange(vitals.getTemperature())) {
      failures.add(new VitalFailure(
          "Temperature",
          "Temperature is critical!",
          thresholds.getTemperatureRange().getClinicalContext(),
          vitals.getTemperature()));
    }

    // Check pulse rate
    if (!thresholds.getPulseRateRange().isInRange(vitals.getPulseRate())) {
      failures.add(new VitalFailure(
          "Pulse Rate",
          "Pulse Rate is out of range!",
          thresholds.getPulseRateRange().getClinicalContext(),
          vitals.getPulseRate()));
    }

    // Check SpO2
    if (!thresholds.getSpo2Range().isInRange(vitals.getSpo2())) {
      failures.add(new VitalFailure(
          "Oxygen Saturation",
          "Oxygen Saturation out of range!",
          thresholds.getSpo2Range().getClinicalContext(),
          vitals.getSpo2()));
    }

    // Determine status
    PatientHealthAssessment.Status status = failures.isEmpty()
        ? PatientHealthAssessment.Status.HEALTHY
        : PatientHealthAssessment.Status.CRITICAL;

    return new PatientHealthAssessment(status, failures);
  }

  /**
   * Legacy method for backward compatibility.
   * Kept for existing code that may depend on the original API.
   */
  static boolean vitalsOk(float temperature, float pulseRate, float spo2)
      throws InterruptedException {
    VitalsChecker checker = new VitalsChecker();
    VitalsSnapshot vitals = new VitalsSnapshot(temperature, pulseRate, spo2);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);
    if (assessment.isHealthy()) {
      return true;
    }

    showCriticalAlert(firstLegacyAlertMessage(assessment), System.out::print, ms -> {
    });
    return false;
  }

  private static String firstLegacyAlertMessage(PatientHealthAssessment assessment) {
    if (assessment.getFailingVitals().isEmpty()) {
      return "";
    }
    return assessment.getFailingVitals().get(0).getAlertMessage();
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

  interface AlertOutput {
    void write(String text);
  }

  interface Sleeper {
    void sleep(long milliseconds) throws InterruptedException;
  }
}
