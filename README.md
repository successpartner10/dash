# 🚗 Smart Dash Cam — one app, two automatic modes

> **Live app:** https://successpartner10.github.io/dash/
>
> Installable **PWA** · single-file app · no build step, no server. Works offline once installed.

**Smart Dash Cam** combines a **Driving Mode** and a **Parking Mode** into one app — and instead of
giving you 8 hours of video, its **AI timeline** tells you what actually mattered:

```
2:14 PM — Vehicle approached your car
2:15 PM — Person remained beside driver's door for 37 seconds
2:16 PM — Possible impact detected
2:16 PM — Person moved away
```

---

## ✨ Features

### 🚗 Driving mode
- **GPS + speed overlay** — real location & km/h from your device's GPS
- **Loop recording** — 1 / 3 / 5 minute segments, oldest overwritten automatically
- **Auto-start** — begins recording when power is applied / on launch
- **Accident detection** — real accelerometer impact detection + harsh-braking detection
- **Record audio** — microphone audio in footage (on/off toggle)
- **Burned-in timestamp** — time · speed · GPS watermark written into the video itself
- **Protected footage** — clips locked from overwrite on impact
- **Front / rear camera** switch
- **Local storage meter** — real on-device usage & quota
- **Low-power mode** — screen off, sensors still armed

### 🅿️ Parking mode
- **Person detection** — AI recognition with live bounding boxes
- **Vehicle detection** — with **license-plate capture**
- **Impact detection** — accelerometer-based
- **Motion zones** — tap any of the 9 zones to arm / disarm
- **Live remote video** — stream the view from anywhere
- **Two-way talk** — speak through the camera
- **Push notifications** — person / impact alerts
- **Cloud event clips** — protected moments auto-uploaded to **Google Drive** (real, with Gmail sign-in)

### ✨ AI event timeline (the killer feature)
- Live chronological feed with **AI-written descriptions**
- Severity chips (person / vehicle / impact / system)
- **Clip thumbnails** captured at the exact moment of each event
- **AI digest** — a one-paragraph summary of "what actually mattered"
- Full-screen **timeline view** with filters (people / vehicles / impacts / system)
- **Copy-to-clipboard export** of the whole timeline

### 📱 PWA (installable app)
- **Install to your home screen** — tap the install icon (or the browser's "Install app" prompt)
- **Full-screen standalone** mode, own icon & splash color
- **Offline** — a service worker caches the app so it opens with no connection

### 🎨 Design
- Clean, modern **Apple-style** interface — light surfaces, generous spacing, soft shadows,
  pill buttons and segmented controls.
- Typography: **Montserrat** (headings, labels, buttons) + **Raleway** (body) via Google Fonts.
- Crisp **inline SVG icons** throughout (no emoji) — traffic, camera, shield, cloud, settings…
- Dark frosted-glass HUD overlays the live camera feed, iOS-style.

---

## 💾 Where is your footage stored?

**On the device itself — nothing leaves the phone unless you download it.**

| What | Where | Persists after reload? |
|---|---|---|
| Loop-recording segments (real camera) | **Origin Private File System (OPFS)** — the browser's per-site private filesystem, via `navigator.storage.getDirectory()` | ✅ Yes |
| Snapshots | Same OPFS, in a `clips/` folder | ✅ Yes |
| Settings & preferences | `localStorage` | ✅ Yes |
| Cloud clips (upload status) | Simulated (no backend yet) | ❌ demo only |

You can see exactly what's on disk in the **💾 Local storage** card in the Live view: each file's
name, real size and timestamp, with **⬇ download** and **🗑 delete** buttons, plus a "Clear all"
button. The Storage meter (top pill + stat card) shows **real usage vs. your browser's storage
quota** (`navigator.storage.estimate()`).

Notes:
- Browsers cap per-site storage (typically a percentage of free disk). The meter reflects the
  real quota.
- Loop recording keeps the newest **12 segments** on disk and deletes the rest — that's the
  "loop" behavior.
- If the browser/preview doesn't support OPFS, the app falls back to in-memory clips for the
  session and tells you to download them before leaving.

---

## ▶️ How to use

1. Open **https://successpartner10.github.io/dash/** (best on a phone).
2. **Live view starts from your real camera — rear (road-facing) by default.** Tap **"Use my real
   camera"** (or it auto-starts on return visits) for the actual camera, real GPS + speed, real
   frame-diff motion detection, and real loop recording. Use the **🔄 FRONT/REAR** button to
   switch to the selfie camera if needed. If no camera is available — or it's blocked (e.g. a
   sandboxed preview) — it automatically falls back to the 🎬 **demo feed**.
3. Tap **📲 Install** to add it to your home screen as a real app.
4. Toggle **Driving / Parking** modes from the top bar.
5. Watch the **AI timeline** fill up instead of watching 8 hours of video.

> **Camera requirements:** `getUserMedia` (camera), geolocation (GPS), `MediaRecorder`
> (recording) and notifications all require **HTTPS** and a **top-level page**. The GitHub
> Pages URL provides both.

---

## 🗂️ File structure

```
dash/
├── index.html      ← the entire app (HTML + CSS + JS, zero dependencies)
├── manifest.json   ← PWA manifest (name, icons, standalone)
├── sw.js           ← service worker (offline cache)
├── icon-192.png    ← app icons (192 / 512 / 180)
├── icon-512.png
├── icon-180.png
├── README.md       ← this file
└── .nojekyll       ← disables Jekyll processing on GitHub Pages
```

---

## 🚀 Deploy / update on GitHub

The repo lives at `github.com/successpartner10/dash` and is published via **GitHub Pages**.

```bash
git clone https://github.com/successpartner10/dash.git
cd dash
# edit files …
git add -A
git commit -m "Update dashcam app"
git push origin main
```

Pages rebuilds automatically — the live URL is **https://successpartner10.github.io/dash/**.

*(First-time Pages setup: repo → Settings → Pages → Source: `main` branch, root → Save.)*

### 📦 Offline zip
- **GitHub:** click **Code → Download ZIP** on the repo page — the whole app comes as a zip.
- **Locally:** a ready-made zip is also included with this project (`dash-offline.zip`).

---

## ⚙️ Settings reference

| Group | Setting | Default |
|---|---|---|
| Driving | Auto-start on power | ✅ on |
| Driving | Loop segment length | 3 min |
| Driving | Video quality | 1080p FHD |
| Driving | G-sensor sensitivity | Medium |
| Driving | Protect on harsh braking | ✅ on |
| Parking | Person detection | ✅ on |
| Parking | Vehicle detection | ✅ on |
| Parking | Impact detection | ✅ on |
| Parking | Motion zones | ✅ on |
| Parking | License-plate capture | ✅ on |
| Parking | Two-way talk | ✅ on |
| Cloud | Upload event clips | ✅ on |
| Cloud | Push notifications | ✅ on |
| Cloud | Live remote video | ✅ on |
| Cloud | Low-power mode | off |

Settings persist in `localStorage` (with an in-memory fallback for sandboxed previews).

---

## 🔍 What's real vs. simulated

| Capability | Demo feed | Real camera (GitHub Pages, HTTPS) |
|---|---|---|
| Video | Simulated canvas render | ✅ real camera stream |
| GPS / speed | Simulated | ✅ real `geolocation` |
| Person / vehicle / impact detection | Scripted scenario + drawn entities | ✅ real frame-diff **motion** detection (blob boxes) |
| Loop recording | Simulated counter | ✅ real `MediaRecorder`, **persisted to OPFS** |
| Audio in footage | — | ✅ real (on/off toggle) |
| Burned-in timestamp/speed/GPS | — | ✅ drawn into the video via canvas composite |
| G-sensor impact | Demo-triggered | ✅ real accelerometer (`devicemotion`) |
| Storage meter | Simulated | ✅ real `navigator.storage.estimate()` usage/quota |
| License-plate capture | Simulated plate strings | ⚠️ needs on-device OCR (see roadmap) |
| Cloud upload | Simulated until you connect Drive | ✅ **real Google Drive upload** (needs your OAuth Client ID) — offline queue + auto-retry |
| Push notifications | Browser notifications only | ⚠️ true push needs a backend (FCM/APNs) |
| True background parking mode | n/a | ⚠️ needs a native wrapper |

---

## ☁️ Google Drive cloud (Gmail sign-in)

Protected clips, impact events and snapshots upload to a **“Smart Dash Cam” folder in your
Google Drive**. It's fully client-side OAuth (authorization-code + PKCE via Google Identity
Services) — **no backend needed**, so it works on GitHub Pages.

### One-time setup (~5 minutes)

1. Go to **https://console.cloud.google.com/** and create a project (e.g. `smart-dash-cam`).
2. **APIs & Services → Library** → search **Google Drive API** → **Enable**.
3. **APIs & Services → OAuth consent screen**:
   - User type: **External**
   - App name `Smart Dash Cam`, add your support & developer email.
   - **Scopes:** add `.../auth/drive.file` (non-sensitive — “create & delete only the files
     this app creates”).
   - **Test users:** add your Gmail. Leave the app in **Testing** (fine for personal use, up
     to 100 test users).
4. **APIs & Services → Credentials → Create credentials → OAuth client ID → Web application**:
   - **Authorized JavaScript origins:** `https://successpartner10.github.io`
   - Create, then copy the **Client ID** (looks like `xxxx.apps.googleusercontent.com`).
5. Open the app → **Settings → Cloud → Google Drive** → paste the Client ID → **Save** → tap
   **🔑 Sign in**.

Now protected clips upload for real. The **Cloud event clips** panel lists your Drive files
with real sizes/timestamps and a **⬇ download** button. Sign out anytime (it revokes the token).

### Offline queue & auto-retry
Uploads never just fail:

- If you're **offline**, clips wait in a queue (persisted in the OPFS `pending/` folder, so
  they survive a page reload) and upload automatically when you're back online.
- Failed uploads **retry automatically with exponential backoff** (10s → 20s → … → capped at
  5 min). After 6 attempts an item is marked failed — with a **Retry** / **Dismiss** button in
  the cloud panel.
- The panel shows live queue state: ⏫ uploading / ⏳ pending / ⚠️ failed.

### Drive storage meter
When connected, the cloud panel shows:

- **Google Drive storage** — your account's real usage vs. quota
  (`about.storageQuota`), rendered as a progress bar.
- **This app's folder** — file count and total bytes in the “Smart Dash Cam” folder.

### Drive retention (12 GB rule)
The “Smart Dash Cam” folder is kept at **≤ 12 GB automatically**. Once the folder exceeds
12 GB, every new upload triggers cleanup that deletes the **oldest** clips until at least
**500 MB** is removed (so adding ~250 MB frees ~500 MB — the folder always shrinks back under
the limit). Cleanup runs at most once every 2 minutes.

### Notes

- The Client ID is stored **only in your browser** (`localStorage`); nothing is sent to any
  server but Google.
- Drive access uses the `drive.file` scope — the app can only see/create files it made.
- The sandboxed in-app preview can't reach Google (its origin isn't authorized); use the
  GitHub Pages URL for the real sign-in.
- The demo feed also uploads real files (JPEG event snapshots, throttled to ≤1/minute) so you
  can test end-to-end without a camera.

---

## 🛣️ Roadmap to a real product

See **[ROADMAP.md](ROADMAP.md)** — the full two-track plan (dashcam vs. security-camera),
feature status matrix, technical blockers, and the phase-by-phase build sequence.

TL;DR:

1. **Native app** — wrap this UI with [Capacitor](https://capacitorjs.com) (or rebuild in
   React Native / Flutter) for installable Android/iOS builds + background recording
   (Android foreground service + persistent notification).
2. **Real ML** — on-device YOLO / MobileNet for person & vehicle detection, plus a
   Tesseract-style engine for license-plate OCR (or a small edge TPU).
3. **Cloud** — Supabase storage + webhook (or Firebase) for real clip upload, FCM/APNs push,
   and an LLM summarizer that turns raw detections into the human-readable timeline.
4. **Power** — run from the car's accessory socket so parking mode can act as a sentry.

---

## 🔐 Note on security
Never commit secrets. If you add a backend later, keep API keys out of this repo and in
server-side environment variables. This app is fully client-side and sends nothing anywhere
until you wire up a backend.

*Built as an interactive prototype. The demo scenario is time-compressed (a 37-second
"linger" plays out in ~6 s) so the full loop is watchable in seconds.*
