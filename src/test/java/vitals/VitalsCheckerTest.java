package vitals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test suite for patient health assessment logic. */
public class VitalsCheckerTest {
  private final VitalsChecker checker = new VitalsChecker();

  @Test
  public void returnHealthyWhenAllVitalsInRange() {
    VitalsSnapshot vitals = new VitalsSnapshot(98.6f, 72.0f, 98.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertTrue(assessment.isHealthy());
    assertEquals(0, assessment.getFailureCount());
  }

  @Test
  public void returnCriticalWhenTemperatureOutOfRange() {
    VitalsSnapshot vitals = new VitalsSnapshot(94.9f, 72.0f, 98.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertFalse(assessment.isHealthy());
    assertEquals(1, assessment.getFailureCount());
    assertEquals("Temperature", assessment.getFailingVitals().get(0).getVitalName());
  }

  @Test
  public void returnCriticalWhenPulseOutOfRange() {
    VitalsSnapshot vitals = new VitalsSnapshot(98.6f, 59.9f, 98.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertFalse(assessment.isHealthy());
    assertEquals(1, assessment.getFailureCount());
    assertEquals("Pulse Rate", assessment.getFailingVitals().get(0).getVitalName());
  }

  @Test
  public void returnCriticalWhenSpo2OutOfRange() {
    VitalsSnapshot vitals = new VitalsSnapshot(98.6f, 72.0f, 89.9f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertFalse(assessment.isHealthy());
    assertEquals(1, assessment.getFailureCount());
    assertEquals("Oxygen Saturation", assessment.getFailingVitals().get(0).getVitalName());
  }

  @Test
  public void supportsBoundaryValues() {
    // All at minimum
    VitalsSnapshot vitals = new VitalsSnapshot(95.0f, 60.0f, 90.0f);
    assertTrue(checker.assessPatientHealth(vitals).isHealthy());

    // All at maximum
    vitals = new VitalsSnapshot(102.0f, 100.0f, 100.0f);
    assertTrue(checker.assessPatientHealth(vitals).isHealthy());
  }

  @Test
  public void failsJustOutsideBoundaries() {
    assertFalse(checker.assessPatientHealth(new VitalsSnapshot(102.1f, 70.0f, 98.0f))
        .isHealthy());
    assertFalse(checker.assessPatientHealth(new VitalsSnapshot(98.6f, 100.1f, 98.0f))
        .isHealthy());
    assertFalse(checker.assessPatientHealth(new VitalsSnapshot(95.0f, 60.0f, 89.9f))
        .isHealthy());
  }

  @Test
  public void reportsAllFailingVitals() {
    VitalsSnapshot vitals = new VitalsSnapshot(94.9f, 110.0f, 80.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertEquals(3, assessment.getFailureCount());
    assertEquals("Temperature", assessment.getFailingVitals().get(0).getVitalName());
    assertEquals("Pulse Rate", assessment.getFailingVitals().get(1).getVitalName());
    assertEquals("Oxygen Saturation", assessment.getFailingVitals().get(2).getVitalName());
  }

  @Test
  public void includesClinicalContextInFailures() {
    VitalsSnapshot vitals = new VitalsSnapshot(94.9f, 72.0f, 98.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    VitalFailure failure = assessment.getFailingVitals().get(0);
    assertTrue(failure.getClinicalContext().contains("Normal for adults"));
    assertTrue(failure.getDetailedDescription().contains("Measured:"));
  }

  @Test
  public void providesEndUserReadableSummary() {
    VitalsSnapshot vitals = new VitalsSnapshot(94.9f, 110.0f, 80.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    String summary = assessment.getSummaryMessage();
    assertTrue(summary.contains("CRITICAL"));
    assertTrue(summary.contains("3 vitals"));
    assertTrue(summary.contains("Temperature is critical!"));
  }

  @Test
  public void providesDetailedMessagesForEachFailure() {
    VitalsSnapshot vitals = new VitalsSnapshot(94.9f, 110.0f, 80.0f);
    PatientHealthAssessment assessment = checker.assessPatientHealth(vitals);

    assertEquals(3, assessment.getDetailedMessages().size());
    for (String msg : assessment.getDetailedMessages()) {
      assertTrue(msg.contains("Measured:"));
      assertTrue(msg.contains("Normal for adults"));
    }
  }
}
