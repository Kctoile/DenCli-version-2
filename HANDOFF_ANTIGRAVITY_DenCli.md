# DenCli Handoff for Antigravity

Date: 2026-10-05
Workspace: `C:\Users\ad\OneDrive\Desktop\DenCli`

## User Requirements

1. Implement the requested DenCli features from `C:\Users\ad\OneDrive\Desktop\DenCliBug_Prompt\dencli_feature_and_nav_fix_prompt.md`:
   - Role-aware navbar dashboard navigation.
   - Customer appointment history with pending-only cancellation.
   - Customer upcoming revisit list.
   - Customer medical history with diagnosis, services, invoice total, and prescriptions.
   - Doctor patient history in the examination screen.
   - Doctor revisit date and note saved when recording an examination.
2. Address the listed SonarCloud findings in `C:\Users\ad\OneDrive\Desktop\DenCliBug_Prompt\sonarcloud-dencli-issues.md`, prioritizing actual security/reliability defects.
3. Also fix the five review findings supplied in chat:
   - Verify VNPAY callback signatures.
   - Remove plaintext password fallback and plaintext seed credentials.
   - Bind doctor identity to examination and prescription writes.
   - Do not accept client-provided prescription unit prices.
   - Make appointment status transitions atomic.
4. Integrate both SQL migrations into the base database script `DenCli.sql`.

## Implemented

- `DenCli.sql` now includes `appointments.revisit_date` and `appointments.revisit_note`, a revisit-date index, and a unique doctor/date/time slot index. It already had a unique key on `examination_results.appointment_id`, so no duplicate unique index was added. The file remains UTF-16LE with BOM.
- Kept upgrade scripts under `DenCli/sql/` for existing databases:
  - `migration_revisit_and_slot_unique.sql`
  - `migration_medical_history_integrity.sql` (updated to recognize any existing unique index over `appointment_id` rather than duplicate the base key).
- Added `DenCli/sql/migration_hash_seed_passwords.sql` for old databases containing the known plaintext development seed credentials.
- The 12 seed password values in `DenCli.sql` were mechanically replaced with BCrypt hashes (1 `admin`, 11 `123`). The unrelated `abc123` display name remains unchanged.
- `PasswordUtil.checkPassword` now accepts BCrypt only; a regression test rejects plaintext and checks both hashed sample passwords.
- VNPAY utility removes hard-coded credentials, fails closed when credentials are absent, and canonicalizes/signature-verifies callback parameters while excluding the signature fields. Callback rejects invalid signatures.
- Examination persistence verifies the appointment belongs to the logged-in doctor and is `Checked In`; diagnosis, prescribed services, and revisit fields share one DB transaction. DAO rejects examination results without an appointment.
- Customer medical history reads completed records from persisted examination/service/prescription tables and calculates invoice totals from saved billing data.
- Customer revisitation queries the database by logged-in patient and `revisit_date >= today`; its request attribute now matches the JSP (`revisits`).
- Doctor history endpoint checks doctor role and restricts history to patients associated with that doctor. Examination JSP includes a modal that renders fetched history using `textContent`.
- Prescription writes require the logged-in doctor to own the examination appointment. Client `unit_price` is ignored; `PrescriptionDAOImpl` selects the authoritative price from `medicines` during insert.
- Appointment check-in, confirmation, cancellation, completion, and invoice completion now use conditional state updates; patient cancellation checks `patient_id` and cancellable states atomically. Doctor completion is bound to `doctor_id`.
- Navbar links to `/doctor/dashboard` and `/staff/dashboard`; servlet mappings alias those paths to the existing examination/reception pages. Booking CTA is hidden for staff/admin/doctor.
- Customer cancellation sends the CSRF token header accepted by `CsrfUtil`.
- WebSocket endpoint guards null sessions, avoids `remove(null)`, and sanitizes/formats log messages. Email logging no longer includes recipient, subject, HTML body, or OTP.

## Verification So Far

- `mvn -q -f .\DenCli\pom.xml -DskipTests compile`: passed twice before the latest interrupted VNPAY callback hardening attempt.
- Focused password tests: 6 passed.
- Earlier VNPAY utility tests: 4 passed.
- Multi-file focused test batch: summary 31 passed, 8 failed. All reported failures were in `PrescriptionServiceTest` with `Type mismatch: cannot convert from PrescriptionServiceImpl to PrescriptionService`. The application compile passed, so verify against Maven's test compiler and inspect the current test file before editing.
- Language diagnostics returned no errors for the touched Java/JSP files in earlier checks.
- Static SQL assertions passed for revisit columns/indexes and existing medical-history uniqueness.
- No SQL migration has been executed against a live SQL Server in this session.

## Immediate Follow-Up

1. Inspect current contents of `PrescriptionServiceTest.java`, `PrescriptionService.java`, and `PrescriptionServiceImpl.java`. The test file was concurrently reformatted and currently contains repeated import blocks. Do not overwrite unrelated/user edits. Run `mvn -q -f .\DenCli\pom.xml test` to get authoritative test compilation/results.
2. Re-run focused tests after fixing any confirmed test/source mismatch: `AppointmentServiceTest`, `AppointmentStateValidationTest`, `BillingServiceTest`, `PrescriptionServiceTest`, `PasswordUtilTest`, and `VnPayUtilTest`.
3. VNPAY callback is currently signature-verified but does **not** yet compare `vnp_TmnCode` to configured `VNP_TMN_CODE`, reject duplicate query parameters, or compare signed `vnp_Amount` with the invoice total. A final patch attempt had an unknown tool outcome; re-read `VnPayReturnServlet.java` before applying anything.
4. Run final Maven test/build and inspect `git diff`, especially `DenCli.sql` encoding and all files concurrently edited.
5. Execute `migration_revisit_and_slot_unique.sql`, `migration_medical_history_integrity.sql`, and `migration_hash_seed_passwords.sql` against a disposable SQL Server/database if available. Existing DBs with duplicate appointment slots or examination results need cleanup before unique indexes/migrations can succeed.

## SonarCloud Work Still Open

The report lists 382 issues; this session fixed only selected high-impact items. Still unaddressed include the broad `printStackTrace()` set (S4507), many servlet exception-flow findings (S1989), CDN SRI findings (Web:S5725), accessibility findings, several cognitive-complexity/string-literal findings, and miscellaneous maintainability issues. Do not blindly replace all servlet exceptions with JSON handling; preserve HTML/redirect contracts and take these in focused batches.

## Preserve Existing Work

The worktree was already dirty. Keep user/automation changes in `.vscode/launch.json`, `DenCli/src/main/webapp/index.jsp`, `DenCli/src/main/webapp/WEB-INF/views/common/sidebar.jsp`, `DenCli/src/test/java/com/devjava/dencli/automation/LoginAutomationTest.java`, `DenCli/sql/migration_revisit_and_slot_unique.sql`, and `_write_test.txt`. Several feature files are untracked but were present during the session; do not delete or reset them.
