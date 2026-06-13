package vitals;

/**
 * Represents the healthy range for a single vital sign with clinical context.
 * 
 * Includes human-readable descriptions so alerts include context like "Normal for adults: 95-102°F"
 */
public final class VitalRange {
  private final float minimum;
  private final float maximum;
  private final String clinicalContext;

  /**
   * Creates a vital range with clinical context.
   *
   * @param minimum Minimum healthy value
   * @param maximum Maximum healthy value
   * @param clinicalContext Human-readable context, e.g., "Normal for adults: 95-102°F"
   */
  public VitalRange(float minimum, float maximum, String clinicalContext) {
    this.minimum = minimum;
    this.maximum = maximum;
    this.clinicalContext = clinicalContext;
  }

  public float getMinimum() {
    return minimum;
  }

  public float getMaximum() {
    return maximum;
  }

  public String getClinicalContext() {
    return clinicalContext;
  }

  public boolean isInRange(float value) {
    return value >= minimum && value <= maximum;
  }
}
