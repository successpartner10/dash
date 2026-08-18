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

### Phase 0 — Dashcam quick wins *(finish the interrupted work — smallest, highest value)*
1. **Record audio** — add `audio: true` to the camera constraints + an **Audio on/off** setting
   (some jurisdictions regulate recording conversations — default **off** is the safe choice).
2. **Burned-in watermark** — draw each frame to a canvas, paint
   `2026-08-18 12:47:03 · 62 km/h · 43.653,-79.383`, record the canvas stream via
   `canvas.captureStream()` + `MediaRecorder`. Note: ~30fps cap + extra CPU on phones.
3. **Drive 12 GB retention rule** — track folder size; past 12 GB, when new footage adds
   250 MB, delete the 500 MB of **oldest** clips (oldest-first, skip nothing in between).
4. **Real accelerometer impact** — `devicemotion` magnitude spike → protect + event + upload.
5. **Audio on/off toggle** (see #1) surfaced in Settings + stored.

### Phase 1 — Security mode hardening *(stays web-only, no backend)*
1. **Zones gate the real camera** — mask the frame-diff to armed zones only (ignore motion
   outside armed cells); lower false positives from trees/cars passing.
2. **Real impact detection** in parking mode (same `devicemotion` as Phase 0.4).
3. **Smarter events** — enter/exit, "lingered N seconds" logic on the real feed (mirror the
   demo's narrative events with real data).
4. **Sensitivity controls that work** — motion threshold + zone size in Settings.
5. **Night mode** — low-light boost (exposure/brightness/gamma) on the processed frame.
6. **Siren (local)** — camera plays a loud alarm when person/impact is detected while armed.
7. **Sound-event detection** — mic-level spikes trigger events (glass break, voices, knocks).
8. **Digital zoom** — pinch/drag zoom on the live view.
9. **Schedules** — auto arm/disarm by time (e.g. arm 22:00–06:00).
10. **Privacy mode** — geofence/quick-toggle to stop monitoring when you're home.

### Phase 2 — Native shell *(Capacitor)*
1. Wrap the existing UI (single `index.html` ports cleanly).
2. **Android foreground service** — camera keeps running with screen off (persistent
   notification, as Android requires).
3. **Wakelock + screen-off sentry** for parking mode.
4. **Local notifications** (no backend needed) for motion/impact on the *camera* device.
5. Ship an **APK** (Android) + TestFlight **IPA** (iOS).

### Phase 3 — Realtime backend *(the Alfred unlock)*
1. **Same-Gmail room** — both devices key a realtime channel by the Google user ID.
2. **Remote live view** — WebRTC (P2P when possible) with a cheap relay fallback.
3. **True push** (FCM/APNs) — motion/impact alerts land on the *viewer* device, with a snapshot.
4. **Remote two-way talk** — mic on viewer → speaker on camera.
5. **Walkie-talkie** — push-to-talk both directions over the same audio channel.
6. **Remote siren** — trigger the alarm from the viewer phone.
7. **Cloud clip sync** — clips visible from any paired device.

### Phase 4 — Real ML
1. **Person / vehicle detection** — MediaPipe object detector or TFLite YOLO-tiny.
2. **License-plate OCR** — TFLite text model on vehicle detections.
3. **Confidence + zone gating** to keep the AI timeline high-signal.

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
