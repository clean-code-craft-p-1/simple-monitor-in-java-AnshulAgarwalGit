package vitals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the complete health assessment of a patient based on vital signs.
 * 
 * Clear and end-user friendly: includes status (HEALTHY/CRITICAL), all failing vitals with context,
 * and ready-to-display messages for medical staff.
 */
public final class PatientHealthAssessment {
  public enum Status {
    HEALTHY("Patient vitals are within normal range"),
    CRITICAL("Immediate medical attention required");

    private final String description;

    Status(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }

  private final Status status;
  private final List<VitalFailure> failingVitals;

  /**
   * Creates a patient health assessment.
   *
   * @param status HEALTHY or CRITICAL
   * @param failingVitals List of all vital sign failures (empty if HEALTHY)
   */
  public PatientHealthAssessment(Status status, List<VitalFailure> failingVitals) {
    this.status = status;
    this.failingVitals = Collections.unmodifiableList(new ArrayList<>(failingVitals));
  }

  public Status getStatus() {
    return status;
  }

  public boolean isHealthy() {
    return status == Status.HEALTHY;
  }

  public boolean isCritical() {
    return status == Status.CRITICAL;
  }

  /**
   * Returns all vitals that failed the clinical threshold check.
   * 
   * @return Unmodifiable list of failing vitals (empty if patient is HEALTHY)
   */
  public List<VitalFailure> getFailingVitals() {
    return failingVitals;
  }

  /**
   * Returns the number of vital signs that are out of range.
   */
  public int getFailureCount() {
    return failingVitals.size();
  }

  /**
   * Returns a summary message for display to medical staff.
   * 
   * Example: "CRITICAL: 2 vitals out of range - Temperature is critical! Pulse Rate is out of range!"
   */
  public String getSummaryMessage() {
    if (isHealthy()) {
      return "HEALTHY: All vitals within normal range";
    }

    StringBuilder summary = new StringBuilder();
    summary.append(status).append(": ").append(failingVitals.size());
    summary.append(" vital").append(failingVitals.size() > 1 ? "s" : "");
    summary.append(" out of range -");

    for (VitalFailure failure : failingVitals) {
      summary.append(" ").append(failure.getAlertMessage());
    }

    return summary.toString();
  }

  /**
   * Returns detailed descriptions for each failing vital.
   * 
   * Example output:
   * - Temperature is critical! (Measured: 103.5°F, Normal for adults: 95-102°F)
   * - Pulse Rate is out of range! (Measured: 105, Normal for adults: 60-100 bpm)
   */
  public List<String> getDetailedMessages() {
    List<String> messages = new ArrayList<>();
    for (VitalFailure failure : failingVitals) {
      messages.add(failure.getDetailedDescription());
    }
    return messages;
  }

  @Override
  public String toString() {
    return getSummaryMessage();
  }
}
