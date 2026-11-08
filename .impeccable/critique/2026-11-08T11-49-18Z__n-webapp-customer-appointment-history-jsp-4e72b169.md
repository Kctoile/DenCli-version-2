---
target: DenCli/src/main/webapp/customer/appointment_history.jsp
total_score: 35
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 1
target_identity: "file:C:\\Users\\ad\\OneDrive\\Desktop\\DenCli\\DenCli\\src\\main\\webapp\\customer\\appointment_history.jsp"
target_fingerprint: "sha256:5c32296a8b9e3f76561fe7f42d637bd13784dabd9f8697940e3af67d3d43f9b7"
target_path: "C:\\Users\\ad\\OneDrive\\Desktop\\DenCli\\DenCli\\src\\main\\webapp\\customer\\appointment_history.jsp"
timestamp: 2026-11-08T11-49-18Z
slug: n-webapp-customer-appointment-history-jsp-4e72b169
closed: true
---
## Design Health Score

| # | Heuristic | Score | Key Issue |
|---|-----------|-------|-----------|
| 1 | Visibility of System Status | 4 | Real-time toast feedback, button spinners, live DOM updates |
| 2 | Match System / Real World | 4 | Clear clinical terminology in Vietnamese (Chờ xác nhận, Đã check-in) |
| 3 | User Control and Freedom | 4 | Dedicated cancellation modal with 'Giữ Lịch' dismissal |
| 4 | Consistency and Standards | 3 | Uses inline hex codes (#2563EB, #FFF1F2) instead of DESIGN.md tokens |
| 5 | Error Prevention | 4 | Confirmation dialog, disabled states during async fetch, status restrictions |
| 6 | Recognition Rather Than Recall | 3 | Modal repeats appointment summary; list lacks search filter |
| 7 | Flexibility and Efficiency | 3 | Fast modal actions, but no keyword filter for long appointment lists |
| 8 | Aesthetic and Minimalist Design | 3 | Side-tab accent border on policy alert; inline font-sizes off ramp |
| 9 | Error Recovery | 4 | In-modal alert display upon network/server rejection |
| 10 | Help and Documentation | 3 | Clear cancellation policy box explaining check-in constraints |
| **Total** | | **35/40** | **Good** |

## Design Specificity Verdict

**LLM assessment**: The screen feels tailored specifically to a high-end dental clinic self-service portal. The real-time cancellation dialog, reason selection tailored to medical appointments, and instant toast alerts create a confident patient experience. However, lingering generic elements—namely the thick blue side-border on the policy alert card and font import drift—detract slightly from the bespoke atelier aesthetic.

**Deterministic scan**: 8 findings (1 slop warning: side-tab accent border on line 47; 1 font import warning: Figtree not declared in DESIGN.md; 3 color advisories: inline hex values #2563EB, #FFF1F2, #FEF3C7; 2 font-size advisories: 3.5rem and 0.75rem).

## Priority Issues

- **[P1] Token Drift & Side-tab Border**: Line 47 uses order-left: 4px solid #2563EB !important and hardcoded colors.
  - *Why it matters*: Looks like an AI-generated template and bypasses the --den-info and --den-border token system.
  - *Fix*: Replace with a soft border and --den-info tokens from DESIGN.md.
  - *Suggested command*: /impeccable polish customer/appointment_history.jsp
- **[P2] Font Import Discrepancy**: Google Fonts imports Figtree alongside Plus Jakarta Sans, but Figtree is not in DESIGN.md.
  - *Why it matters*: Unused font assets add HTTP latency and violate design system coherence.
  - *Fix*: Remove unused Figtree font link or document it.
  - *Suggested command*: /impeccable typeset customer/appointment_history.jsp
- **[P3] Lack of Instant Filter on Table**: Unlike medical_records.jsp, appointment history relies strictly on pagination without quick search.
  - *Why it matters*: Returning patients with many appointments must paginate rather than quickly filtering by doctor or date.
  - *Fix*: Add a compact search/filter input matching the medical records pattern.
  - *Suggested command*: /impeccable layout customer/appointment_history.jsp

## Persona Red Flags

- **Jordan (First-Timer)**: Reassured by the clear modal explanation and confirmation step. No red flags.
- **Alex (Power User)**: Annoyed at having to paginate to find past appointments with a specific doctor; would benefit from a quick filter bar.
- **Sam (Accessibility)**: Modal focus trap and Esc handling are present via Bootstrap 5. High contrast is maintained.

## Minor Observations
- The empty state icon uses ont-size: 3.5rem inline instead of a reusable CSS display class.
- Status badges use custom CSS classes (status-pending, status-confirmed) rather than global --den- token variables.
