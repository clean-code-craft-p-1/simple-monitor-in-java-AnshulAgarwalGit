package vitals;

/**
 * Represents a single vital sign failure with its alert message and clinical context.
 * 
 * End-user friendly: includes which vital failed, what the alert is, and why (clinical context).
 */
public final class VitalFailure {
  private final String vitalName;
  private final String alertMessage;
  private final String clinicalContext;
  private final float actualValue;

  /**
   * Creates a vital failure record.
   *
   * @param vitalName Name of the vital (e.g., "Temperature", "Pulse Rate", "Oxygen Saturation")
   * @param alertMessage Alert message (e.g., "Temperature is critical!")
   * @param clinicalContext Context for end-user (e.g., "Normal for adults: 95-102°F")
   * @param actualValue The actual measured value
   */
  public VitalFailure(String vitalName, String alertMessage, String clinicalContext,
      float actualValue) {
    this.vitalName = vitalName;
    this.alertMessage = alertMessage;
    this.clinicalContext = clinicalContext;
    this.actualValue = actualValue;
  }

  public String getVitalName() {
    return vitalName;
  }

  public String getAlertMessage() {
    return alertMessage;
  }

  public String getClinicalContext() {
    return clinicalContext;
  }

  public float getActualValue() {
    return actualValue;
  }

  /**
   * Returns a human-readable failure description for end-users.
   * Example: "Temperature is critical! (Measured: 103.5°F, Normal for adults: 95-102°F)"
   */
  public String getDetailedDescription() {
    return String.format("%s (Measured: %.1f, %s)", alertMessage, actualValue,
        clinicalContext);
  }

  @Override
  public String toString() {
    return getDetailedDescription();
  }
}
