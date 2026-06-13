package vitals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test suite for vital signs validation logic. */
public class VitalsCheckerTest {

  @Test
  public void returnsTrueForInRangeVitals() throws InterruptedException {
    assertTrue(VitalsChecker.vitalsOk(98.6f, 72.0f, 98.0f));
  }

  @Test
  public void failsForEachSingleOutOfRangeVital() throws InterruptedException {
    assertFalse(VitalsChecker.vitalsOk(94.9f, 70.0f, 98.0f));
    assertFalse(VitalsChecker.vitalsOk(98.6f, 59.9f, 98.0f));
    assertFalse(VitalsChecker.vitalsOk(98.6f, 72.0f, 89.9f));
  }

  @Test
  public void supportsBoundaryValues() throws InterruptedException {
    assertTrue(VitalsChecker.vitalsOk(95.0f, 60.0f, 90.0f));
    assertTrue(VitalsChecker.vitalsOk(102.0f, 100.0f, 90.0f));
  }

  @Test
  public void failsJustOutsideBoundaries() throws InterruptedException {
    assertFalse(VitalsChecker.vitalsOk(102.1f, 70.0f, 98.0f));
    assertFalse(VitalsChecker.vitalsOk(98.6f, 100.1f, 98.0f));
    assertFalse(VitalsChecker.vitalsOk(95.0f, 60.0f, 89.9f));
  }

  @Test
  public void reportsFirstFailingVitalMessage() {
    assertEquals(
        "Temperature is critical!",
        VitalsChecker.firstAlertMessage(94.9f, 72.0f, 98.0f));
    assertEquals(
        "Pulse Rate is out of range!",
        VitalsChecker.firstAlertMessage(98.6f, 59.9f, 98.0f));
    assertEquals(
        "Oxygen Saturation out of range!",
        VitalsChecker.firstAlertMessage(98.6f, 72.0f, 89.9f));
  }

  @Test
  public void prioritizesFirstFailureWhenMultipleVitalsAreOffRange() {
    assertEquals("Temperature is critical!", VitalsChecker.firstAlertMessage(94.9f, 110.0f, 80.0f));
  }

  @Test
  public void reportsNullMessageWhenAllVitalsAreInRange() {
    assertNull(VitalsChecker.firstAlertMessage(98.6f, 72.0f, 98.0f));
  }
}
