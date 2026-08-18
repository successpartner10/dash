# 🗺️ Smart Dash Cam — Product Roadmap

> Live app: https://successpartner10.github.io/dash/ · Repo: `successpartner10/dash`
>
> Last updated: 2026-08-18

This document is the single source of truth for where the product is, what's missing, and in what
order to build it. **Smart Dash Cam is one shell, two products:**

| Track | What it is | Closest reference |
|---|---|---|
| 🚗 **Dashcam mode** | Record drives with speed/GPS overlay, loop recording, accident detection, protected footage | Viofo / Garmin dashcams |
| 🅿️ **Security-camera mode** | Use an old phone as a motion-sensor sentry you watch from another device | AlfredCamera |

Both share the **AI event timeline** — instead of hours of video, an event feed that says what
actually mattered.

---

## 1. Feature status matrix (real vs. simulated vs. missing)

Legend: ✅ **real** · 🟡 **simulated/demo-only** · ⛔ **missing** · 🔶 **partial**

### 🚗 Dashcam track

| Capability | Status | Notes |
|---|---|---|
| Rear (road-facing) camera | ✅ | `facingMode: environment` |
| GPS + speed (real) | ✅ | `geolocation.watchPosition`, m/s→km/h |
| Loop recording (1/3/5 min) | ✅ | `MediaRecorder`, 12-segment OPFS loop |
| Auto-start | ✅ | On launch / power |
| Front/rear switch | ✅ | |
| Local storage (persistent) | ✅ | OPFS — recordings/clips/pending folders, download/delete UI |
| Protected footage | ✅ | Protect button + auto-lock on impact |
| G-sensor accident detection | 🟡 | Demo-triggered only — **no real accelerometer** |
| **Record audio** | ⛔ | Requested, not built. Needs audio track + on/off setting |
| **Burned-in timestamp/speed/GPS watermark** | ⛔ | Requested, not built. Needs canvas-composite recording |
| Night/HDR tuning | 🔶 | Relies on phone camera defaults |
| Hardwire/low-voltage power | ⛔ | Phone-on-USB only |

### 🅿️ Security-camera track (Alfred-style)

| Capability | Status | Notes |
|---|---|---|
| Motion detection (real) | ✅ | Frame-diff on live feed, bounding blob |
| Person / vehicle **classified** detection | 🟡 | Labels are demo-only; real feed = generic motion |
| Impact detection | 🟡 | Demo-triggered; no DeviceMotion wiring |
| Motion zones (arm/disarm 9 cells) | 🔶 | UI works; gates only the **demo** feed, not real camera |
| Two-way talk | 🔶 | Mic capture works — but no **remote viewer** to talk to |
| Notifications | 🔶 | Same-device browser notifications only |
| Cloud event clips | ✅ | Real Google Drive (OAuth, offline queue, auto-retry) |
| **Live remote video (second device)** | ⛔ | The defining Alfred feature — needs a backend |
| **Background/screen-off sentry** | ⛔ | Needs native shell (browsers pause camera in background) |
| **True push notifications** | ⛔ | FCM/APNs — needs backend |
| Night vision / low-light | 🟡 | Demo-only concept |

---

## 2. The three blockers (and what unlocks them)

Everything missing traces back to three technical dependencies:

| Blocker | Unlocks | Effort |
|---|---|---|
| **1. Native shell** — [Capacitor](https://capacitorjs.com) (or React Native/Flutter) | Background foreground-service, screen-off sentry, wakelock, local notifications, store-installable APK/IPA | Medium |
| **2. Realtime backend** — Supabase Realtime / Firebase / WebRTC+signaling | **Remote live view from a second device**, device pairing, true push, remote two-way talk | Medium–High |
| **3. On-device ML** — MediaPipe Tasks / TFLite | Person vs. vehicle vs. motion classification, real plate OCR | Medium |

GitHub Pages stays as the **front-end host**; it cannot do #2 by itself (static hosting).

---

## 3. Build sequence

### Phase 0 — Dashcam quick wins ✅ SHIPPED
1. **Record audio** — ✅ mic audio in footage, with an **Audio on/off** setting (Settings → Driving).
2. **Burned-in watermark** — ✅ time · speed · GPS drawn into the video itself
   (canvas `captureStream()` composite + `MediaRecorder`).
3. **Drive 12 GB retention rule** — ✅ oldest-first deletion of ≥ 500 MB once past 12 GB
   (adding ~250 MB frees ~500 MB), swept at most once per 2 min.
4. **Real accelerometer impact** — ✅ `devicemotion` shock detection → protect + event + upload
   (iOS motion permission requested on camera start).

### Phase 1 — Security mode hardening *(web-only, no backend)* ✅ SHIPPED
1. **Zones gate the real camera** — ✅ frame-diff masked to armed zones (3×3 grid).
2. **Real impact detection** — ✅ `devicemotion` (shipped in Phase 0).
3. **Smarter events** — 🔶 real events exist; enter/exit "lingered N seconds" narrative still demo-only.
4. **Sensitivity controls** — 🔶 motion threshold is fixed; expose in Settings next.
5. **Night mode** — ✅ low-light boost (brightness/contrast) live + burned into recordings.
6. **Siren (local)** — ✅ Web Audio two-tone alarm on person/impact + manual Siren button.
7. **Sound-event detection** — ✅ mic RMS spikes trigger events (throttled 15 s).
8. **Digital zoom** — ✅ pinch / drag / mouse-wheel, 1–4×, double-tap reset.
9. **Schedules** — ✅ auto arm/disarm by time, overnight windows supported.
10. **Privacy mode** — ✅ geofence pause within 250 m of home ("Set home" button).

### Phase 2 — Native shell *(Capacitor)* ✅ SCAFFOLDED (build APK locally)
1. ✅ Wrap the existing UI — `native/` Capacitor 6 project generated.
2. ✅ **Android foreground service** — `SentryService.java` (notification + wake lock).
3. ✅ **Wakelock + screen-off sentry** — `SentryPlugin.java` bridge + JS `Native.*` hooks.
4. 🔶 **Local notifications** — not yet (next).
5. 🔶 Ship an **APK** / **IPA** — needs Android Studio/Xcode on your machine (see `native/BUILD.md`).

### Phase 3 — Realtime backend *(the Alfred unlock)* ✅ CODE COMPLETE (needs your Supabase keys)
1. ✅ **Room-based pairing** — shared room code (no Gmail dependency for the channel).
2. ✅ **Remote live view** — WebRTC (STUN) + Supabase Realtime broadcast signaling.
3. ✅ **Event push** — camera broadcasts motion/impact events to the viewer (in-app + Notification).
4. ✅ **Remote two-way talk** — viewer mic track over WebRTC.
5. ✅ **Walkie-talkie** — hold-to-talk on the viewer.
6. ✅ **Remote siren** — viewer triggers the camera's siren.
7. 🔶 **Cloud clip sync / TURN relay / FCM-APNs** — needs relay + backend edge function (Phase 5).

### Phase 4 — Real ML ✅ SHIPPED
1. ✅ **Person / vehicle detection** — TensorFlow.js COCO-SSD (on-device, CDN, lazy-loaded).
2. ✅ **License-plate OCR** — Tesseract.js on vehicle crops (opt-in, throttled).
3. ✅ **Confidence + zone gating** — >0.5 score filter; events throttled 12 s.

### Phase 5 — Hardening & polish
1. Battery: charge-detection, low-battery auto-stop, low-voltage warning (accessory socket).
2. Heat: overheat auto-pause + guidance (phones in windshields are the #1 failure mode).
3. Storage health: format/retention reminders, corrupted-segment skip.
4. Multi-camera: manage several phones from one viewer account.

---

## 4. Decision points (need your call later)

| Decision | Options | Notes |
|---|---|---|
| Identity model | Same Gmail · QR pairing code | Same Gmail first (matches Drive); pairing codes later for Trust Circle |
| Backend | Supabase Realtime · Firebase · self-host | Supabase = quick + generous free tier; Firebase = FCM push built-in |
| Monetization | Free · one-time · subscription (cloud clips) | Cloud storage is the natural paid tier |
| Distribution | PWA + APK · Play Store · TestFlight | Play Store needs a dev account + privacy policy |
| Audio default | Off vs. On | Off is the legal-safe default |
| Retention default | 12 GB / delete 500 MB per 250 MB added | As requested; make configurable later |

---

## 5. Non-goals (for now)
- Multi-channel dashcam hardware (IR, parking-cam wiring) — phone-first.
- Facial recognition / cloud AI on video content — privacy-heavy; revisit only with explicit need.
- iOS background camera (Apple doesn't permit it the way Android does) — parking sentry on iOS
  will be screen-on only.

---

## 8. What "done" looks like
- **Dashcam done:** drives record with audio + burned-in evidence watermark, real G-sensor
  locking, and Drive retention keeps the folder ≤ ~12 GB automatically.
- **Security done:** an old phone sits in a window, armed zones catch people/vehicles, sends a
  push to your phone, and you open a **live view from anywhere** and say "go away" — with the
  moment auto-saved as an AI-described clip in Drive.
