package com.healthman.app;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlanRulesTest {
  @Test
  public void acceptsValidInput() {
    assertEquals(120, PlanRules.positiveNumber(" 120 ", 5000));
    assertEquals("스쿼트", PlanRules.requiredName(" 스쿼트 "));
  }

  @Test
  public void rejectsInvalidNumbers() {
    for (String v : new String[] {"0", "-1", "5001", "1.5", "abc", "999999999999"}) {
      try {
        PlanRules.positiveNumber(v, 5000);
        fail(v);
      } catch (IllegalArgumentException expected) {
      }
    }
  }

  @Test
  public void rejectsBlankName() {
    try {
      PlanRules.requiredName("   ");
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }
}
