# 🚗 Smart Dash Cam — one app, two automatic modes

> **Live app:** https://successpartner10.github.io/dash/
>
> A single-file web app. No build step, no dependencies, no server. Works offline once downloaded.

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
- **Accident detection** — G-sensor impact detection + harsh-braking detection
- **Protected footage** — clips locked from overwrite on impact
- **Front / rear camera** switch
- **Local storage meter**
- **Low-power mode** — screen off, sensors still armed

### 🅿️ Parking mode
- **Person detection** — AI recognition with live bounding boxes
- **Vehicle detection** — with **license-plate capture**
- **Impact detection** — accelerometer-based
- **Motion zones** — tap any of the 9 zones to arm / disarm
- **Live remote video** — stream the view from anywhere
- **Two-way talk** — speak through the camera
- **Push notifications** — person / impact alerts
- **Cloud event clips** — protected moments auto-uploaded

### ✨ AI event timeline (the killer feature)
- Live chronological feed with **AI-written descriptions**
- Severity chips (person / vehicle / impact / system)
- **Clip thumbnails** captured at the exact moment of each event
- **AI digest** — a one-paragraph summary of "what actually mattered"
- Full-screen **timeline view** with filters (people / vehicles / impacts / system)
- **Copy-to-clipboard export** of the whole timeline

---

## ▶️ How to use

1. Open **https://successpartner10.github.io/dash/** (best on a phone).
2. Pick a feed:
   - 🎬 **Demo feed** — simulated camera, GPS, speed and a scripted scenario
     (person approaches & lingers → vehicle passes & plate captured → impact → person leaves).
     Everything works instantly.
   - 📷 **Use my real camera** — your actual camera, real GPS + speed, real frame-diff
     motion detection, and real loop recording (`MediaRecorder`).
3. Toggle **Driving / Parking** modes from the top bar.
4. Watch the **AI timeline** fill up instead of watching 8 hours of video.

> **Camera requirements:** `getUserMedia` (camera), geolocation (GPS), `MediaRecorder`
> (recording) and notifications all require **HTTPS** and a **top-level page**. The GitHub
> Pages URL provides both. (In the in-app sandboxed preview, the camera is blocked, so the
> app automatically falls back to the demo feed.)

---

## 🗂️ File structure

```
dash/
├── index.html     ← the entire app (HTML + CSS + JS, zero dependencies)
├── README.md      ← this file
└── .nojekyll      ← disables Jekyll processing on GitHub Pages
```

---

## 🚀 Deploy / update on GitHub

The repo lives at `github.com/successpartner10/dash` and is published via **GitHub Pages**.

```bash
git clone https://github.com/successpartner10/dash.git
cd dash
# edit index.html …
git add index.html
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
| Loop recording | Simulated counter | ✅ real `MediaRecorder` segments |
| License-plate capture | Simulated plate strings | ⚠️ needs on-device OCR (see roadmap) |
| Cloud upload / push | Simulated status changes | ⚠️ needs a backend (GitHub Pages is static) |
| True background parking mode | n/a | ⚠️ needs a native wrapper |

---

## 🛣️ Roadmap to a real product

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
