---
name: DenCli Dental Clinic
description: Modern European Healthcare & Clinical Dental Management Platform
colors:
  primary: "#0EA5E9"
  primary-dark: "#0284C7"
  primary-contrast: "#075985"
  primary-light: "#E0F2FE"
  primary-subtle: "#F0F9FF"
  navy: "#1E3A5F"
  navy-dark: "#0D1B2A"
  coral: "#FF6B6B"
  indigo: "#6366F1"
  danger: "#DC2626"
  success: "#047857"
  warning: "#92400E"
  warning-bg: "#FFFBEB"
  warning-text: "#78350F"
  warning-border: "#FDE68A"
  info-bg: "#EFF6FF"
  info-border: "#BFDBFE"
  danger-bg: "#FEF2F2"
  coral-light: "#FFF1F2"
  bg: "#F8FAFC"
  surface: "#FFFFFF"
  surface-subtle: "#F1F5F9"
  text-primary: "#0F172A"
  text-secondary: "#1E293B"
  text-muted: "#334155"
  border: "#CBD5E1"
  border-subtle: "#E2E8F0"
typography:
  display:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, sans-serif"
    fontSize: "clamp(2rem, 5vw, 3.25rem)"
    fontWeight: 800
    lineHeight: 1.15
    letterSpacing: "-0.025em"
  headline:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, sans-serif"
    fontSize: "1.75rem"
    fontWeight: 700
    lineHeight: 1.25
    letterSpacing: "-0.02em"
  title:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, sans-serif"
    fontSize: "1.25rem"
    fontWeight: 600
    lineHeight: 1.35
  body:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, BlinkMacSystemFont, Segoe UI, sans-serif"
    fontSize: "1rem"
    fontWeight: 400
    lineHeight: 1.6
  label:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, sans-serif"
    fontSize: "0.875rem"
    fontWeight: 600
    letterSpacing: "0.01em"
  caption:
    fontFamily: "Plus Jakarta Sans, Inter, -apple-system, sans-serif"
    fontSize: "0.75rem"
    fontWeight: 700
rounded:
  sm: "8px"
  md: "12px"
  lg: "16px"
  card: "20px"
  pill: "9999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.surface}"
    rounded: "{rounded.sm}"
    padding: "10px 20px"
  button-primary-hover:
    backgroundColor: "{colors.primary-dark}"
  button-logout:
    backgroundColor: "{colors.danger}"
    textColor: "{colors.surface}"
    rounded: "{rounded.pill}"
    padding: "8px 18px"
  card-default:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    padding: "24px"
---

# Design System: DenCli Dental Clinic

## Overview

**Creative North Star: "The European Precision Sanctuary"**

DenCli embodies a high-trust, modern European dental healthcare philosophy. The aesthetic avoids cold sterile hospital tropes and generic corporate SaaS palettes alike, projecting instead the serene authority of an advanced dental atelier. The visual grammar couples crystalline Mediterranean blues and deep alpine navies with generous white space, crisp typographic rhythm, and tactile micro-interactions.

Every surface balances clinical efficiency for dentists and reception staff with reassurance and absolute transparency for patients managing appointments, invoices, and electronic medical records (EMR).

**Key Characteristics:**
- **Clinical Serenity & Modern Authority:** Deep alpine navy anchors trust; vibrant sky cyan signals cutting-edge dental technology and precision.
- **High-Contrast Readability by Default:** No washed-out grays; text tokens meet or exceed WCAG AA/AAA standards across all light and dark containers.
- **Tactile State Confidence:** Every interactive element provides immediate, haptic-feeling feedback with subtle scale transforms, state toasts, and real-time WebSocket cues.

## Colors

The DenCli palette reflects medical sterility softened by Mediterranean light, paired with deliberate high-contrast accents for clinical safety.

### Primary
- **Mediterranean Sky Cyan** (`#0EA5E9`): Used for primary calls-to-action, active appointment slot indicators, brand badges, and positive interactive focus states.
- **Deep Marine Cyan** (`#0284C7`): Used for hover states on primary controls and high-emphasis focal points.
- **High-Contrast Clinical Cyan** (`#075985`): Used for primary text on light backgrounds and outline buttons to guarantee AA/AAA contrast ratios.

### Secondary
- **Alpine Medical Navy** (`#1E3A5F`): The authoritative foundation color for navigation bars, sidebar branding, and structural headings.
- **Deep Obsidian Navy** (`#0D1B2A`): Deep canvas contrast for hero banners, dark mode backgrounds, and high-impact clinic announcements.
- **Coral Pulse** (`#FF6B6B`): Vibrant accent for urgent attention, promotional health tags, and warm human focal points.

### Tertiary
- **European Indigo** (`#6366F1`): Specialized accent for dental tech tags, doctor credentials, and subtle ambient gradients.

### Neutral
- **Clinical White Surface** (`#FFFFFF`): Primary container background, card surface, and modal body.
- **Soft Paper Wash** (`#F8FAFC`): Base background color for pages and tables, reducing glare while maintaining pristine cleanliness.
- **Deep Slate Body Text** (`#0F172A`): Primary content text, sharp and uncompromisingly legible.
- **Slate Secondary** (`#1E293B`): Subheadings, table headers, and form labels.
- **High-Contrast Slate Muted** (`#334155`): Replaces conventional washed-out grays for secondary metadata, timestamps, and helper text.
- **Slate Border** (`#CBD5E1`): Structural boundary for form controls, dividers, and card borders.
- **Solid Crimson Action** (`#DC2626`): Strictly dedicated to the Logout action (`.btn-nav-logout`) and irreversible destructive operations (cancellations).

### Named Rules
**The High-Contrast Integrity Rule.** No functional text or actionable metadata may use gray lighter than `#334155` on light surfaces or darker than `#CBD5E1` on dark surfaces. Contrast is a clinical safety requirement, not an aesthetic preference.

**The One Crimson Rule.** Red (`#DC2626`) is exclusively reserved for cancellation confirmations, clinical allergy warnings, and session termination (`Đăng xuất`). It is never used as a decorative or marketing accent.

## Typography

**Display Font:** Plus Jakarta Sans (with Inter, -apple-system, sans-serif)  
**Body Font:** Plus Jakarta Sans / Inter  
**Label/Mono Font:** Inter (tab numbers, timestamps, ICD codes)

**Character:** Geometric precision softened by humanist warmth. Generous x-height ensures immediate legibility for patients on mobile devices and clinicians reviewing dental charts under clinic lighting.

### Hierarchy
- **Display** (800 weight, `clamp(2rem, 5vw, 3.25rem)`, line-height `1.15`, letter-spacing `-0.025em`): Landing page hero statement and major clinic banners.
- **Headline** (700 weight, `1.75rem`, line-height `1.25`, letter-spacing `-0.02em`): Section titles, modal headers, EMR record titles.
- **Title** (600 weight, `1.25rem`, line-height `1.35`): Card headings, patient names, service categories.
- **Body** (400 weight, `1rem`, line-height `1.6`): Medical diagnoses, doctor instructions, prescription dosages. Max line-length capped at 75ch.
- **Label** (600 weight, `0.875rem`, letter-spacing `0.01em`): Form field labels, status pills, table column headers.

### Named Rules
**The Heading Immersion Rule.** Any dark container (Navbar, Footer, Hero Banner) must render headings in pure white (`#FFFFFF`) and supporting copy in icy sky tints (`#E0F2FE` or `#CBD5E1`), never inheriting muddy dark text.

## Layout

DenCli utilizes a 12-column responsive grid centered with standard container widths (`container` max-width 1280px). 

- **Clinical Density:** Clinical and administrative surfaces (Doctor examination, Reception desk, Admin user management) prioritize compact vertical rhythm with 16px table padding and progressive disclosure modals.
- **Patient Breathing Room:** Patient-facing portals (Booking wizard, Appointment history, Medical records) utilize comfortable vertical pacing (24px–32px card spacing) with single-column mobile adaptations.
- **Responsive Breakpoints:** Standard Bootstrap breakpoints (`sm: 576px`, `md: 768px`, `lg: 992px`, `xl: 1200px`).

## Elevation & Depth

DenCli employs a hybrid material system combining clean tonal separation with soft directional illumination. Surfaces are predominantly flat and crisp at rest, lifting tactfully upon interaction.

### Shadow Vocabulary
- **Ambient Micro** (`0 1px 2px rgba(15, 39, 68, 0.04)`): Baseline resting elevation for table rows and subtle inputs.
- **Card Rest** (`0 2px 6px -1px rgba(15, 39, 68, 0.06)`): Resting state for content panels and appointment cards.
- **Elevated Hover** (`0 6px 18px -2px rgba(15, 39, 68, 0.08)`): Applied on card hover and dropdown menus.
- **Modal Float** (`0 16px 32px -4px rgba(15, 39, 68, 0.14)`): Dialogs, appointment detail drawers, and print modals.
- **Brand Glow** (`0 8px 24px -2px rgba(14, 165, 233, 0.30)`): Primary button hover and active slot selection.

### Named Rules
**The Flat-Rest Floating-Active Rule.** Cards and data containers remain anchored to the canvas with crisp border lines at rest. Ambient glow and elevation appear strictly as dynamic feedback to hover, focus, or selection.

## Shapes

- **Radius Strategy:** Micro-elements (badges, buttons, inputs) use 8px (`--den-radius-sm`) for crisp definition. Containers, cards, and medical charts use 12px–16px (`--den-radius-md`, `--den-radius-lg`). Interactive action pills and badges utilize full pill geometry (`9999px`).
- **Glassmorphism:** Reserved exclusively for fixed navigation headers and sticky topbars (`background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(16px)`), grounding the content flow.

## Components

### Buttons
- **Shape:** Gently rounded (`8px` radius).
- **Primary:** Solid Mediterranean Cyan (`#0EA5E9`) with white text and `10px 20px` padding. On hover, shifts to Deep Marine (`#0284C7`) with `translateY(-1px)` and brand glow.
- **Outline:** High-Contrast Cyan border and text (`#075985`), shifting to solid `#075985` on hover.
- **Logout Action:** Pill shape (`9999px`), solid crimson (`#DC2626`), high-contrast white text (`#FFFFFF`), with prominent hover brightness.

### Cards / Containers
- **Corner Style:** Rounded 12px (`var(--den-radius-md)`).
- **Background:** Crisp pure white (`#FFFFFF`) on light paper canvas (`#F8FAFC`).
- **Border:** 1px solid slate border (`#CBD5E1`).
- **Padding:** 20px to 24px responsive padding.

### Status Badges & Chips
- **Confirmed / Success:** Light mint background (`#ECFDF5`), deep forest text (`#065F46`), emerald border (`#A7F3D0`).
- **Pending / Warning:** Warm cream background (`#FFFBEB`), deep amber text (`#78350F`), amber border (`#FDE68A`).
- **Cancelled / Danger:** Soft blush background (`#FEF2F2`), crimson text (`#991B1B`), rose border (`#FECACA`).

### Inputs / Fields
- **Style:** 1px solid `#CBD5E1`, 8px radius, white background.
- **Focus:** 2px ring in Sky Blue (`#38BDF8`) with border shift to `#0EA5E9`, without layout shift.

### Modals
- **Style:** 16px corner radius, backdrop blur overlay (`rgba(13, 27, 42, 0.45)`), prominent close button, sticky footer action row.

## Do's and Don'ts

### Do:
- **Do** maintain `#334155` or darker for all secondary descriptions, sublabels, and timestamps to ensure WCAG AA readability.
- **Do** confirm cancellations and destructive actions inside dedicated Bootstrap 5 dialogs with explicit reason dropdowns.
- **Do** display instantaneous feedback on asynchronous actions (WebSocket slot updates, appointment cancellations) using toast notifications and localized DOM swaps.
- **Do** provide print-optimized layouts (`@media print`) for invoices and medical prescriptions.

### Don't:
- **Don't** use faint gray (`#94A3B8` or `#CBD5E1`) for body text on white backgrounds.
- **Don't** render dark navbars with generic gray logout links; always use the high-contrast `.btn-nav-logout` button.
- **Don't** perform full page reloads when confirming appointment status changes; use realtime DOM updates with toast confirmations.
- **Don't** invent arbitrary colors outside the cyan/navy/slate/crimson palette system.

