# 🔧 Smart Dash Cam — Step-by-Step Setup

> Follow this on one screen while you click on the other. Total time: ~20 minutes.
> **Stuck anywhere? Paste what you see (or a screenshot description) back to me.**

---

## The goal, in 30 seconds

| Phone | Job |
|---|---|
| **Old phone** (camera) | Mounted in car → *Driving mode* (dashcam). In a window → *Parking mode* (sentry). |
| **Daily phone** (viewer) | Opens the same app → *Remote tab* → watches live, talks, sounds the siren. |

Three optional "keys" turn on the cloud bits. **Without any keys, the whole app already works**
on one phone (camera, GPS, recording, AI detection, timeline). The keys add: Drive cloud clips
(#1), remote live view + walkie-talkie (#2), and bulletproof remote video (#3, optional).

---

# Part A — Open the app & test the basics (0 keys, ~2 min)

1. On your **phone**, open Chrome/Safari and go to:
   **`https://successpartner10.github.io/dash/`**
   > ⚠️ Do this in the browser address bar. **Not** inside a chat/preview pane — the camera
   > only works on this real HTTPS URL.
2. You'll see the welcome screen → tap **"Use my real camera"** → **Allow** when the browser
   asks for camera (and, later, location/microphone).
3. You should now see your camera feed with the HUD (time / speed / GPS on the top-right).
4. Tap the **🚗 Driving / 🅿️ Parking** toggle to flip modes.
   - **Parking mode** → move your hand in front of the camera → a box appears around the
     motion and "Motion detected" / "Person detected" events start filling the feed.
5. Done. The core is working. (Desktop webcams work too, but phones are the real use case.)

**If the camera shows the demo scene instead:** you're on the preview pane, or permission was
denied — open the github.io URL in the address bar and tap Allow.

---

# Part B — Key 1: Google Drive cloud clips (~5 min)

**What you get:** protected clips auto-upload to a "Smart Dash Cam" folder in your Drive.

## B1. Create the project
1. In a browser (desktop is easier), go to **https://console.cloud.google.com/**
2. Sign in with your Gmail.
3. Top bar → click the **project selector** (shows "Select a project" or a project name).
4. In the popup, click **NEW PROJECT** (top-right).
5. **Project name:** `smart-dash-cam` → **Location:** leave "No organization" → **CREATE**.
6. Wait a few seconds → click the **notification** ("Project created") or **SELECT PROJECT**
   to open it.

## B2. Enable the Drive API
1. Left menu (**☰** hamburger) → **APIs & Services** → **Library**.
2. In the search box type: `Google Drive API` → click the result.
3. Click the blue **ENABLE** button.
4. Wait for it to finish (a few seconds). ← *This is the step that can't be skipped; without
   it, uploads fail with "Drive API has not been used in project".*

## B3. Consent screen (new "Google Auth Platform" UI — 2024+)
> Google renamed this in 2024: the old "OAuth consent screen" menu is now **Google Auth
> Platform**, split into **Branding / Audience / Data Access / Clients** tabs. If you don't
> see "Google Auth Platform" under APIs & Services, paste this URL:
> `https://console.cloud.google.com/auth/branding`
1. Left menu → **APIs & Services** → **Google Auth Platform**.
2. If it says **"Google Auth Platform not configured yet"** → click **Get Started**.
3. **Branding** tab:
   - **App name:** `Smart Dash Cam`
   - **User support email:** your Gmail
   - **Developer contact email** (bottom): your Gmail
   - **Save**.
4. **Audience** tab:
   - Select **External**.
   - **Test users** section → **Add users** → your Gmail → **Save**.
   > This is what stops the "Access blocked / app not verified" screen — your Gmail must be
   > listed here while the app is in *Testing* mode.
5. **Data Access** tab:
   - **Add or remove scopes** → search `drive.file` → tick **`.../auth/drive.file`** → **Save**.

## B4. Create the Client ID
1. **Clients** tab (still inside Google Auth Platform) → **Create client**.
2. **Application type:** `Web application` → **Name:** `Smart Dash Cam`.
3. **Authorized JavaScript origins** → **+ Add URI** → paste exactly:
   `https://successpartner10.github.io`
   > No trailing slash, no `www`, no path.
4. Click **Create**.
5. Copy the **Client ID** (looks like `1234567890-abc...apps.googleusercontent.com`).

## B5. Paste it into the app
1. In the app → tap **⚙️ Settings** (gear top-right) or the **Settings** tab.
2. Scroll to **Cloud & alerts** → find **Google Drive (Gmail login)**.
3. Paste the Client ID into the box → tap **Save Client ID**.
4. Tap **🔑 Sign in**.
5. A **Google popup** opens → pick your Gmail → **Allow**.
   > If you see "Google hasn't verified this app": click **Advanced** → **Go to Smart Dash
   > Cam (unsafe)**. That's normal for a personal test app.
6. The app shows **"☁️ Connected · you@gmail.com"**. Done — protected clips now upload for
   real, and you can see them under **Cloud event clips**.

**Test it:** Parking mode → trigger motion → check drive.google.com for the **Smart Dash Cam**
folder.

---

# Part C — Key 2: Remote live view + walkie-talkie (~3 min)

**What you get:** watch the camera phone live from your daily phone, hold-to-talk, siren, and
pushed event alerts — the "Alfred" part.

## C1. Create the Supabase project
1. Go to **https://supabase.com/** → **Start your project** (sign in with GitHub or email).
2. Dashboard → **New project**.
3. - **Name:** `smart-dash-cam`
   - **Database password:** click *Generate a password* (you won't need to remember it)
   - **Region:** closest to you (e.g. US East)
   - **Plan:** Free
4. Click **Create new project** and wait **1–2 minutes** while it spins up.

## C2. Copy the two keys
1. Left sidebar → **⚙️ Project Settings** (gear) → **API** (under "Configuration").
2. Copy **Project URL** — e.g. `https://abcd1234.supabase.co`.
3. Copy **anon public** key — a long string starting `eyJ...`.
4. Paste both into the app: **Settings → Remote access** → the two fields → **Save**.
   > No SQL, no tables, no config — Supabase Realtime broadcast works out of the box with
   > these two values. Keys stay only in this browser.

## C3. Two-phone test (the fun part)
1. **Camera phone** (old phone, camera running):
   - Settings → **Remote access** → pick a room code, e.g. `home-1`
   - Tap **Start broadcast (camera)** → status shows **BROADCASTING**.
2. **Viewer phone** (daily phone):
   - Open the app → paste the **same two Supabase keys** → Save
   - Enter the **same room code** (`home-1`) → tap **Watch room (viewer)**.
   - The app switches to the **Remote** tab → you see the camera **live**.
3. Try the controls: **Hold to talk** (speak, it plays on the camera phone) · **Siren** ·
   **Snapshot** · watch the **event feed** fill as motion happens.

**If no video appears:** same room code on both? · camera phone still broadcasting? · both on
the github.io URL? · restrictive network? → add a TURN server (Part D).

---

# Part D — Key 3 (optional): TURN relay

Only needed if remote video fails on a strict network/carrier NAT.

Free options: **Metered** (metered.ca), **Open Relay** (metered.ca/tools/openrelay), or
self-host **coturn**. Then in the app: **Settings → Remote access** → paste the
`turn:host:3478` URL (+ username + credential if required) → **Save**. It's added to WebRTC
automatically.

---

# Part E — (Optional) Android APK with background sentry

Only if you want the camera monitoring with the **screen off** (Android only; Apple forbids
background camera). Needs a computer with Android Studio.

```bash
git clone https://github.com/successpartner10/dash.git
cd dash/native && npm install && ./sync-web.sh && npx cap open android
# In Android Studio → Build → Build APK(s)
```
Full details in **native/BUILD.md**.

---

# The two-phone setup at a glance

```
OLD PHONE (camera)                        DAILY PHONE (viewer)
├─ open github.io URL → Use my camera     ├─ open github.io URL
├─ Driving = dashcam · Parking = sentry   ├─ same Gmail → Drive clips sync
├─ zones / night / siren / schedule       └─ Remote tab → live view / talk / siren
└─ Start broadcast (room code)                  ↑ same Supabase keys + room code
```

---

# Troubleshooting (match the symptom)

| Symptom | Fix |
|---|---|
| Camera shows demo, not real | Use the **github.io URL in the address bar**; tap Allow |
| "Access blocked / app not verified" | Add your Gmail under **Test users** (B3, Audience tab) |
| Drive upload fails | **Enable the Google Drive API** (B2) |
| Drive sign-in popup won't open | Origins must be exactly `https://successpartner10.github.io` (B4, Clients tab); allow popups |
| Remote: no video | Same room code both phones; camera broadcasting; add TURN (Part D) |
| Supabase "load fail" | Wrong Project URL or anon key (C2) |
| No GPS fix | Outdoors; grant location; enable high-accuracy |
| Recording stopped at 15% battery | That's the low-battery protection — plug in |

**Still stuck?** Paste the exact error text (or what the screen says) back to me and I'll walk
you through it.
