# ✅ Smart Dash Cam — One-Page Setup Checklist

> Go from repo → working two-phone security cam in one sitting.
> **Prefer click-by-click detail?** See **[STEP-BY-STEP.md](STEP-BY-STEP.md)** — every menu,
> button and field spelled out, with a troubleshooting table at the end.
> The app is already live at **https://successpartner10.github.io/dash/** — this page is
> only about the **3 keys** that make the cloud features real. Everything else works with
> zero setup (demo feed, camera, GPS, recording, AI detection, timeline).

---

## The 3 keys, in order (≈15 min total)

| # | Key | Unlocks | Where | Time |
|---|---|---|---|---|
| 1 | Google OAuth **Client ID** | ☁️ Real Drive cloud clips | console.cloud.google.com | ~5 min |
| 2 | Supabase **URL + anon key** | 🌐 Remote live view · walkie-talkie · siren · push | supabase.com | ~2 min |
| 3 | *(optional)* **TURN server** | Remote video behind strict NATs | coturn / Metered / Open Relay | ~5 min |

Paste all of them in the app: **Settings → Cloud** and **Settings → Remote access**.
They're stored **only in that browser**, never sent anywhere but Google/Supabase.

---

## 1️⃣ Google Drive cloud clips (~5 min)

1. **console.cloud.google.com** → sign in with your Gmail → **Create project** (`smart-dash-cam`).
2. **APIs & Services → Library** → search **Google Drive API** → **Enable**.
3. **APIs & Services → OAuth consent screen** → **External** → name it `Smart Dash Cam`,
   add your email (support + developer).
   - **Scopes** → add `.../auth/drive.file`
   - **Test users** → add your Gmail (leave the app in *Testing*).
4. **Credentials → Create OAuth client ID → Web application**:
   - **Authorized JavaScript origins** → exactly `https://successpartner10.github.io` (no slash, no www).
5. Copy the **Client ID** → app → **Settings → Cloud** → paste → **Save** → **🔑 Sign in**.

**Result:** protected clips auto-upload to a **"Smart Dash Cam"** folder in your Drive,
kept ≤ 12 GB automatically, visible from any device signed into that Gmail.

**If it fails:** "Access blocked" = your Gmail isn't in Test users · popup doesn't open =
origin doesn't match · "Drive API not used in project" = you skipped step 2.

---

## 2️⃣ Remote live view + walkie-talkie (~2 min)

1. **supabase.com** → sign in (GitHub or email) → **New project** (any name, any region, free tier).
2. Wait ~60 s for the project to provision.
3. **Settings (gear) → API** → copy **Project URL** and **anon public key**.
4. App → **Settings → Remote access** → paste both → **Save**.

**Result (two phones, same room code):**

| Phone | Action |
|---|---|
| **Camera phone** (old phone in car/window) | Remote access → **Start broadcast**, room e.g. `home-1` |
| **Viewer phone** (your daily phone) | Remote access → **Watch room**, same code → **Remote** tab |

You get: live video, **hold-to-talk** (walkie-talkie), **Siren**, **Snapshot**, and a live
**event feed** of motion/impact alerts pushed from the camera. No SQL or tables needed.

**If video won't connect:** both phones must be on the same room code · the camera phone
must be broadcasting (Remote tab is *not* the live view — that's the camera's own tab) ·
restrictive network? add a TURN server (step 3).

---

## 3️⃣ (Optional) TURN relay — when P2P is blocked

Free options: [Metered](https://www.metered.ca/stun-turn), [Open Relay](https://www.metered.ca/tools/openrelay/),
or self-host [coturn](https://github.com/coturn/coturn).

Paste `turn:relay.example.com:3478` + username + credential into **Settings → Remote access**
→ Save. The app adds it to WebRTC automatically.

---

## 4️⃣ (Optional) Build the Android APK — background sentry

Only if you want the camera running with the **screen off** (Android only):

```bash
cd native && npm install && ./sync-web.sh && npx cap open android
# Android Studio → Build → Build APK(s)
```

See **native/BUILD.md**. iOS cannot background the camera (Apple policy) — screen-on only.

---

## The two-phone setup at a glance

```
OLD PHONE (camera)                     DAILY PHONE (viewer)
├─ open app → Use my real camera       ├─ open app
├─ Driving mode = dashcam              ├─ same Gmail → Drive clips sync
└─ Parking mode = sentry               └─ Remote tab → live view / talk / siren
      ├─ zones, night, siren, schedule, privacy
      └─ Start broadcast (room code)
```

---

## Quick troubleshooting

| Symptom | Fix |
|---|---|
| Camera shows demo feed | Open the **github.io URL** (not the sandboxed preview) and tap Allow |
| Drive sign-in blocked | Gmail in **Test users**; origin = `https://successpartner10.github.io` |
| Drive upload fails | Enable the **Google Drive API** in the project |
| Remote: no video | Same room code on both phones; camera is broadcasting; add TURN |
| No GPS fix | Outdoors, high-accuracy on, browser location permission |
| Low battery | Recording auto-stops < 15% — plug in (this is intended) |

Full details: **README.md** (features, usage) · **ROADMAP.md** (what's real vs. next) ·
**native/BUILD.md** (APK).
