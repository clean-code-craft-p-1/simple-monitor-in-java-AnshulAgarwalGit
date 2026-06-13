package vitals;

/**
 * Immutable snapshot of a patient's vital signs at a specific moment in time.
 * 
 * Encapsulates the three primary vital signs: temperature, pulse rate, and oxygen saturation.
 * Using a value object instead of primitives makes the API self-documenting and type-safe.
 */
public final class VitalsSnapshot {
  private final float temperature;
  private final float pulseRate;
  private final float spo2;

  /**
   * Creates a snapshot of vital signs.
   *
   * @param temperature Body temperature in Fahrenheit
   * @param pulseRate Heartbeats per minute
   * @param spo2 Oxygen saturation percentage (0-100)
   */
  public VitalsSnapshot(float temperature, float pulseRate, float spo2) {
    this.temperature = temperature;
    this.pulseRate = pulseRate;
    this.spo2 = spo2;
  }

  public float getTemperature() {
    return temperature;
  }

  public float getPulseRate() {
    return pulseRate;
  }

  public float getSpo2() {
    return spo2;
  }

  @Override
  public String toString() {
    return String.format("VitalsSnapshot{temp=%.1f°F, pulse=%.0f bpm, spo2=%.0f%%}",
        temperature, pulseRate, spo2);
  }
}
