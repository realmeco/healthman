package com.healthman.app;

public final class PlanRules {
  private PlanRules() {}

  public static int positiveNumber(String text, int max) {
    int value;
    try {
      value = Integer.parseInt(text.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("숫자를 입력해 주세요.");
    }
    if (value < 1 || value > max) throw new IllegalArgumentException("1~" + max + " 사이로 입력해 주세요.");
    return value;
  }

  public static String requiredName(String text) {
    String value = text.trim();
    if (value.isEmpty() || value.length() > 80)
      throw new IllegalArgumentException("이름은 1~80자로 입력해 주세요.");
    return value;
  }
}
