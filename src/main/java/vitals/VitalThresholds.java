package vitals;

/**
 * Encapsulates clinical thresholds for all vital signs with context.
 * 
 * Single source of truth for what constitutes healthy vs. critical vitals.
 * Includes clinical context for end-user display.
 */
public final class VitalThresholds {
  private final VitalRange temperatureRange;
  private final VitalRange pulseRateRange;
  private final VitalRange spo2Range;

  /**
   * Standard adult vital sign thresholds as defined in the README requirements.
   */
  public static final VitalThresholds STANDARD_ADULT = new VitalThresholds(
      new VitalRange(95.0f, 102.0f, "Normal for adults: 95-102°F"),
      new VitalRange(60.0f, 100.0f, "Normal for adults: 60-100 bpm"),
      new VitalRange(90.0f, 100.0f, "Normal for adults: 90-100%"));

  public VitalThresholds(VitalRange temperatureRange, VitalRange pulseRateRange,
      VitalRange spo2Range) {
    this.temperatureRange = temperatureRange;
    this.pulseRateRange = pulseRateRange;
    this.spo2Range = spo2Range;
  }

  public VitalRange getTemperatureRange() {
    return temperatureRange;
  }

  public VitalRange getPulseRateRange() {
    return pulseRateRange;
  }

  public VitalRange getSpo2Range() {
    return spo2Range;
  }
}
