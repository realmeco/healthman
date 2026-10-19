package com.healthman.app;

import android.app.*;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import org.json.*;

public class MainActivity extends Activity {
  private final int INK = Color.rgb(24, 39, 38),
      GREEN = Color.rgb(22, 112, 85),
      MUTED = Color.rgb(102, 119, 114);
  private SharedPreferences prefs;
  private JSONArray exercises, meals;
  private LinearLayout content;
  private LocalDate date = LocalDate.now();
  private int tab = 0;
  private final String[] days = {"월", "화", "수", "목", "금", "토", "일"};
  private final String[][] guides = {
    {
      "레그 프레스",
      "하체 · 대퇴사두근 / 둔근",
      "1. 등과 골반을 패드에 붙이고 발을 어깨너비로 둡니다.\n2. 안전장치를 해제하고 무릎을 천천히 굽힙니다.\n3. 발바닥 전체로 밀며 시작 자세로 돌아옵니다.",
      "무릎을 완전히 잠그지 마세요. 허리가 뜨기 전까지만 내리고 안전장치 사용법을 먼저 확인하세요."
    },
    {
      "랫 풀다운",
      "등 · 광배근",
      "1. 허벅지 패드를 조절하고 바를 어깨보다 조금 넓게 잡습니다.\n"
          + "2. 가슴을 펴고 팔꿈치를 아래로 당겨 바를 윗가슴 쪽으로 내립니다.\n"
          + "3. 반동 없이 천천히 팔을 폅니다.",
      "목 뒤로 당기거나 몸을 크게 젖히지 마세요. 어깨 통증이 생기면 멈추세요."
    },
    {
      "체스트 프레스",
      "가슴 · 대흉근 / 삼두근",
      "1. 손잡이가 가슴 중간에 오도록 좌석을 조절합니다.\n2. 등을 패드에 붙이고 손목을 곧게 유지하며 밀어냅니다.\n3. 팔꿈치를 천천히 굽혀 돌아옵니다.",
      "어깨를 으쓱하거나 팔꿈치를 과하게 뒤로 보내지 마세요. 가벼운 중량부터 시작하세요."
    },
    {
      "시티드 로우",
      "등 · 능형근 / 광배근",
      "1. 발을 지지대에 두고 척추를 중립으로 유지합니다.\n2. 팔꿈치를 몸 가까이 두고 손잡이를 복부 쪽으로 당깁니다.\n3. 몸통을 고정하고 천천히 돌아옵니다.",
      "허리를 앞뒤로 흔들지 마세요. 중량을 내려놓을 때 손가락이 끼지 않게 주의하세요."
    },
    {
      "러닝머신",
      "유산소 · 전신",
      "1. 안전 클립을 옷에 연결하고 낮은 속도에서 시작합니다.\n2. 정면을 보며 편안한 보폭으로 걷습니다.\n3. 속도를 천천히 낮춘 후 벨트가 멈추면 내려옵니다.",
      "움직이는 벨트 위에 갑자기 올라서지 마세요. 어지럽거나 흉통이 있으면 즉시 중단하세요."
    }
  };

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    prefs = getSharedPreferences("healthman", MODE_PRIVATE);
    exercises = read("exercises");
    meals = read("meals");
    if (state != null) {
      tab = state.getInt("tab");
      date = LocalDate.parse(state.getString("date", LocalDate.now().toString()));
    }
    getWindow().setStatusBarColor(Color.rgb(245, 248, 244));
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    render();
  }

  @Override
  public void onSaveInstanceState(Bundle out) {
    super.onSaveInstanceState(out);
    out.putInt("tab", tab);
    out.putString("date", date.toString());
  }

  private JSONArray read(String key) {
    try {
      return new JSONArray(prefs.getString(key, "[]"));
    } catch (JSONException e) {
      return new JSONArray();
    }
  }

  private void save() {
    prefs
        .edit()
        .putString("exercises", exercises.toString())
        .putString("meals", meals.toString())
        .apply();
  }

  private int dp(int x) {
    return Math.round(x * getResources().getDisplayMetrics().density);
  }

  private LinearLayout column() {
    LinearLayout l = new LinearLayout(this);
    l.setOrientation(LinearLayout.VERTICAL);
    return l;
  }

  private android.graphics.drawable.Drawable bg(int color, int radius) {
    GradientDrawable d = new GradientDrawable();
    d.setColor(color);
    d.setCornerRadius(dp(radius));
    return d;
  }

  private TextView label(String s, int size, int color) {
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(size);
    t.setTextColor(color);
    t.setPadding(0, dp(6), 0, dp(6));
    return t;
  }

  private void title(String s) {
    TextView t = label(s, 27, INK);
    t.setTypeface(null, Typeface.BOLD);
    content.addView(t);
  }

  private Button button(String text, Runnable action) {
    Button b = new Button(this);
    b.setText(text);
    b.setTextColor(GREEN);
    b.setAllCaps(false);
    b.setOnClickListener(v -> action.run());
    return b;
  }

  private LinearLayout card() {
    LinearLayout c = column();
    c.setPadding(dp(18), dp(12), dp(18), dp(12));
    c.setBackground(bg(Color.WHITE, 18));
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
    p.setMargins(0, dp(8), 0, dp(8));
    content.addView(c, p);
    return c;
  }

  private void render() {
    LinearLayout root = column();
    root.setBackgroundColor(Color.rgb(245, 248, 244));
    root.setPadding(dp(20), dp(12), dp(20), dp(8));
    setContentView(root);
    root.setOnApplyWindowInsetsListener(
        (v, insets) -> {
          v.setPadding(
              dp(20),
              dp(12) + insets.getSystemWindowInsetTop(),
              dp(20),
              dp(8) + insets.getSystemWindowInsetBottom());
          return insets;
        });
    TextView brand = label("HEALTHMAN   /   나를 위한 건강 습관", 12, GREEN);
    brand.setTypeface(null, Typeface.BOLD);
    root.addView(brand);
    ScrollView scroll = new ScrollView(this);
    content = column();
    scroll.addView(content);
    root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
    if (tab == 0) home();
    else if (tab == 1) routine();
    else if (tab == 2) diet();
    else equipment();
    LinearLayout nav = new LinearLayout(this);
    String[] tabs = {"홈", "운동 루틴", "식단", "기구 가이드"};
    for (int i = 0; i < tabs.length; i++) {
      final int n = i;
      Button b =
          button(
              tabs[i],
              () -> {
                tab = n;
                render();
              });
      b.setTextSize(12);
      if (tab == i) {
        b.setTextColor(Color.WHITE);
        b.setBackground(bg(GREEN, 12));
      }
      nav.addView(b, new LinearLayout.LayoutParams(0, dp(56), 1));
    }
    root.addView(nav);
  }

  private int weekday() {
    return date.getDayOfWeek().getValue();
  }

  private int exerciseCount() {
    int n = 0;
    for (int i = 0; i < exercises.length(); i++)
      if (exercises.optJSONObject(i).optInt("day") == weekday()) n++;
    return n;
  }

  private int completed() {
    int n = 0;
    for (int i = 0; i < exercises.length(); i++) {
      JSONObject e = exercises.optJSONObject(i);
      if (e.optInt("day") == weekday()
          && prefs.getBoolean("done:" + date + ":" + e.optString("id"), false)) n++;
    }
    return n;
  }

  private int calories() {
    int n = 0;
    for (int i = 0; i < meals.length(); i++) {
      JSONObject m = meals.optJSONObject(i);
      if (m.optString("date").equals(date.toString())) n += m.optInt("kcal");
    }
    return n;
  }

  private void datePicker() {
    LinearLayout row = new LinearLayout(this);
    row.setGravity(Gravity.CENTER);
    row.addView(
        button(
            "‹",
            () -> {
              date = date.minusDays(1);
              render();
            }));
    TextView t =
        label(
            date.format(DateTimeFormatter.ofPattern("MM월 dd일")) + " (" + days[weekday() - 1] + ")",
            16,
            INK);
    t.setGravity(Gravity.CENTER);
    t.setContentDescription("날짜 선택: " + date);
    t.setOnClickListener(
        v ->
            new DatePickerDialog(
                    this,
                    (picker, year, month, day) -> {
                      date = LocalDate.of(year, month + 1, day);
                      render();
                    },
                    date.getYear(),
                    date.getMonthValue() - 1,
                    date.getDayOfMonth())
                .show());
    row.addView(t, new LinearLayout.LayoutParams(0, -2, 1));
    row.addView(
        button(
            "›",
            () -> {
              date = date.plusDays(1);
              render();
            }));
    content.addView(row);
  }

  private void home() {
    title("오늘도, 한 걸음 더.");
    content.addView(label("작은 기록이 꾸준한 변화를 만듭니다.", 15, MUTED));
    datePicker();
    LinearLayout hero = card();
    hero.setBackground(bg(GREEN, 18));
    hero.addView(label("운동 완료", 14, Color.WHITE));
    hero.addView(label(completed() + " / " + exerciseCount() + "개", 34, Color.WHITE));
    hero.addView(label("기록한 식단  ·  " + calories() + " kcal", 17, Color.WHITE));
    content.addView(
        button(
            "오늘의 운동 확인하기 →",
            () -> {
              tab = 1;
              render();
            }));
    LinearLayout c = card();
    c.addView(label("나만의 일주일을 설계하세요", 20, INK));
    c.addView(label("요일별 운동을 추가하고 완료를 체크하세요. 식단은 날짜별로 기록할 수 있어요.", 15, MUTED));
    content.addView(
        button(
            "식단 기록하기 +",
            () -> {
              tab = 2;
              render();
            }));
    content.addView(label("가이드는 일반적인 참고 정보입니다. 개인의 상태와 기구 모델에 따라 전문가의 지도를 받으세요.", 12, MUTED));
  }

  private void routine() {
    title("나의 운동 루틴");
    content.addView(label("요일별 계획 · 날짜별 완료 기록", 14, MUTED));
    datePicker();
    content.addView(button("+ " + days[weekday() - 1] + "요일 운동 추가", this::addExercise));
    if (exerciseCount() == 0) {
      LinearLayout c = card();
      c.addView(label("아직 계획한 운동이 없어요", 19, INK));
      c.addView(label("운동 이름과 세트, 횟수를 정해 첫 루틴을 만들어 보세요.", 15, MUTED));
    }
    for (int i = 0; i < exercises.length(); i++) {
      JSONObject e = exercises.optJSONObject(i);
      if (e.optInt("day") != weekday()) continue;
      final int index = i;
      LinearLayout c = card();
      CheckBox check = new CheckBox(this);
      check.setText(
          getString(
              R.string.exercise_summary, e.optString("name"), e.optInt("sets"), e.optInt("reps")));
      check.setTextSize(17);
      check.setTextColor(INK);
      String key = "done:" + date + ":" + e.optString("id");
      check.setChecked(prefs.getBoolean(key, false));
      check.setOnCheckedChangeListener((b, v) -> prefs.edit().putBoolean(key, v).apply());
      c.addView(check);
      c.addView(
          button(
              "루틴에서 삭제",
              () ->
                  confirmDelete(
                      () -> {
                        exercises.remove(index);
                        save();
                        render();
                      })));
    }
  }

  private EditText field(LinearLayout form, String hint, boolean number) {
    EditText e = new EditText(this);
    e.setHint(hint);
    e.setSingleLine(true);
    if (number) e.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    form.addView(e);
    return e;
  }

  private void form(String title, LinearLayout fields, Runnable submit) {
    AlertDialog d =
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setView(fields)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장", null)
            .create();
    d.setOnShowListener(
        v ->
            d.getButton(-1)
                .setOnClickListener(
                    b -> {
                      try {
                        submit.run();
                        save();
                        d.dismiss();
                        render();
                      } catch (IllegalArgumentException ex) {
                        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                      }
                    }));
    d.show();
  }

  private LinearLayout fields() {
    LinearLayout l = column();
    l.setPadding(dp(20), dp(8), dp(20), dp(8));
    return l;
  }

  private void addExercise() {
    LinearLayout f = fields();
    EditText name = field(f, "운동 이름 (예: 스쿼트)", false),
        sets = field(f, "세트 수 (1~30)", true),
        reps = field(f, "세트당 횟수 (1~200)", true);
    form(
        "운동 추가",
        f,
        () -> {
          String n = PlanRules.requiredName(name.getText().toString());
          int s = PlanRules.positiveNumber(sets.getText().toString(), 30),
              r = PlanRules.positiveNumber(reps.getText().toString(), 200);
          try {
            exercises.put(
                new JSONObject()
                    .put("id", java.util.UUID.randomUUID().toString())
                    .put("day", weekday())
                    .put("name", n)
                    .put("sets", s)
                    .put("reps", r));
          } catch (JSONException e) {
            throw new IllegalStateException(e);
          }
        });
  }

  private void diet() {
    title("나의 식단");
    content.addView(label("먹을 계획과 섭취 열량을 기록하세요", 14, MUTED));
    datePicker();
    LinearLayout sum = card();
    sum.addView(label("기록한 총 열량", 14, MUTED));
    sum.addView(label(calories() + " kcal", 30, GREEN));
    content.addView(button("+ 식단 추가", this::addMeal));
    String[] types = {"아침", "점심", "저녁", "간식"};
    for (String type : types) {
      content.addView(label(type, 20, INK));
      boolean found = false;
      for (int i = 0; i < meals.length(); i++) {
        JSONObject m = meals.optJSONObject(i);
        if (!m.optString("date").equals(date.toString()) || !m.optString("type").equals(type))
          continue;
        found = true;
        final int index = i;
        LinearLayout c = card();
        c.addView(label(m.optString("name"), 18, INK));
        c.addView(label(m.optInt("kcal") + " kcal", 15, GREEN));
        c.addView(
            button(
                "삭제",
                () ->
                    confirmDelete(
                        () -> {
                          meals.remove(index);
                          save();
                          render();
                        })));
      }
      if (!found) content.addView(label("등록한 식단이 없습니다.", 14, MUTED));
    }
    content.addView(label("열량은 직접 입력한 값입니다. 개인별 필요 열량이나 영양 처방을 의미하지 않습니다.", 12, MUTED));
  }

  private void addMeal() {
    LinearLayout f = fields();
    Spinner type = new Spinner(this);
    type.setAdapter(
        new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            new String[] {"아침", "점심", "저녁", "간식"}));
    f.addView(type);
    EditText name = field(f, "음식 이름과 양 (예: 닭가슴살 100g)", false),
        kcal = field(f, "열량 kcal (1~5000)", true);
    form(
        "식단 추가",
        f,
        () -> {
          String n = PlanRules.requiredName(name.getText().toString());
          int k = PlanRules.positiveNumber(kcal.getText().toString(), 5000);
          try {
            meals.put(
                new JSONObject()
                    .put("date", date.toString())
                    .put("type", type.getSelectedItem().toString())
                    .put("name", n)
                    .put("kcal", k));
          } catch (JSONException e) {
            throw new IllegalStateException(e);
          }
        });
  }

  private void confirmDelete(Runnable action) {
    new AlertDialog.Builder(this)
        .setTitle("삭제할까요?")
        .setMessage("삭제한 항목은 복구할 수 없습니다.")
        .setNegativeButton("취소", null)
        .setPositiveButton("삭제", (d, w) -> action.run())
        .show();
  }

  private void equipment() {
    title("기구 사용 가이드");
    content.addView(label("처음 사용하는 기구도 차근차근", 14, MUTED));
    EditText search = new EditText(this);
    search.setHint("기구 이름 또는 운동 부위 검색");
    search.setSingleLine(true);
    content.addView(search);
    LinearLayout list = column();
    content.addView(list);
    Runnable update =
        () -> {
          list.removeAllViews();
          String q = search.getText().toString().trim();
          for (String[] g : guides) {
            if (!(g[0] + g[1]).contains(q)) continue;
            Button b =
                button(
                    g[0] + "\n" + g[1] + "  →",
                    () ->
                        new AlertDialog.Builder(this)
                            .setTitle(g[0])
                            .setMessage(g[1] + "\n\n사용 순서\n" + g[2] + "\n\n주의사항\n" + g[3])
                            .setPositiveButton("확인", null)
                            .show());
            b.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            b.setPadding(dp(16), dp(12), dp(16), dp(12));
            list.addView(b, new LinearLayout.LayoutParams(-1, dp(88)));
          }
          if (list.getChildCount() == 0) list.addView(label("검색 결과가 없습니다.", 15, MUTED));
        };
    search.addTextChangedListener(
        new android.text.TextWatcher() {
          public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

          public void onTextChanged(CharSequence s, int st, int before, int count) {
            update.run();
          }

          public void afterTextChanged(android.text.Editable e) {}
        });
    update.run();
    content.addView(label("기구의 안내문과 안전장치를 먼저 확인하세요. 처음에는 트레이너에게 자세 확인을 요청하세요.", 12, MUTED));
  }
}
