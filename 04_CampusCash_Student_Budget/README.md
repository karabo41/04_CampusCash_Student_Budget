# CampusCash

Student Budget Theme redesign of the original SmartBudget Android/Kotlin SQLite project.

## What changed
- New app name: **CampusCash**
- New palette: primary `#7C3AED`, accent `#F97316`, background `#FFF7ED`
- Dashboard redesigned around a spending summary card, goal progress card, status card, and themed navigation buttons.
- Typography hierarchy updated using `sans-serif`.
- Icon direction: `playful-rounded`.
- Navigation presentation: card-style action buttons preserving the original screen flow.

## What was preserved
- Kotlin package, activities, IDs used by `findViewById`, SQLite helpers, managers, expense/category/goal/reminder/report logic, and application flow.
- Existing screens were not removed.

## Files changed
- `app/src/main/AndroidManifest.xml`
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/values-night/themes.xml`
- `app/src/main/res/drawable/bg_button.xml`
- `app/src/main/res/drawable/bg_card.xml`
- `app/src/main/res/drawable/bg_card_alt.xml`
- `app/src/main/res/drawable/bg_input.xml`
- `app/src/main/res/drawable/bg_nav_button.xml`
- `app/src/main/res/drawable/ic_launcher_foreground.xml`
- `app/src/main/res/layout/activity_login.xml`
- `app/src/main/res/layout/activity_main.xml`
- `app/src/main/res/layout/activity_add_expense.xml`
- `app/src/main/res/layout/activity_category.xml`
- `app/src/main/res/layout/activity_category_limit.xml`
- `app/src/main/res/layout/activity_expense_list.xml`
- `app/src/main/res/layout/activity_goals.xml`
- `app/src/main/res/layout/activity_graph.xml`
- `app/src/main/res/layout/activity_register.xml`
- `app/src/main/res/layout/activity_reminder.xml`
- `app/src/main/res/layout/activity_reports.xml`
- `app/src/main/res/layout/activity_badges.xml`
- `app/src/main/res/layout/activity_photo_viewer.xml`

## Design notes
This direction gives the same budgeting application a different market position without changing the database or business rules. It is intended for visual comparison before choosing a final UI.
