# LAB 09: FIREBASE AUTHENTICATION & ASYNCHRONOUS IMAGE LOADING WITH GLIDE
**Course:** Mobile Application Programming (71ITSE30603 / 72ITSE30603)  
**Student:** Nguyễn Trung Đức - MSSV: 2474802010092  
**Package Name:** `vn.edu.vlu.lab09`

---

## 🎯 Target CLOs & Architectural Highlights
- **CLO 2 (ViewBinding & UI):** 100% ViewBinding (zero `findViewById`). Adaptive Material Design 3 UI using `MaterialButtonToggleGroup`, `TextInputLayout` with password toggle, and `CircularProgressIndicator`.
- **CLO 3 (Data & Async):** `PhotoItem` model, asynchronous image loading into `RecyclerView` via Glide with placeholder, error fallback, cross-fade animation, and disk cache optimization (`DiskCacheStrategy.ALL`). Safe request cancellation inside `onViewRecycled()`.
- **CLO 4 (Firebase & Third-Party SDKs):** Email/Password authentication using `FirebaseAuth`, auto-login via `currentUser`, input validations, comprehensive exception handling (weak password, user collision, invalid credentials, network failure), and clean session logout with back stack clearing flags (`FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK`).
- **CLO 5 (Code Quality & AI Compliance):** Clean modular architecture, comprehensive documentation (`DEBUG_REPORT.md`, `AI_USAGE.md`), and clean commit history.

---

## 🌟 Extra In-Class Challenges (+15% Bonus Points)
- **Challenge 1 (+7.5%):** "Forgot Password?" dialog flow calling `auth.sendPasswordResetEmail(email)` with client-side email format verification.
- **Challenge 2 (+7.5%):** Dynamic circular user avatar in the header using Glide's `circleCrop()` transformation with a dynamic avatar URL generated from the user's email (`ui-avatars.com`).

---

## 🔑 Keystore Signatures for Firebase Console
- **SHA-1:** `E5:65:FB:F3:36:2A:D7:F0:85:F7:65:3D:42:A9:6F:2E:6A:2E:23:87`
- **SHA-256:** `0C:44:18:97:16:DE:F0:47:D1:45:E9:87:90:C1:95:E3:43:D2:A5:98:3B:08:CD:48:39:6E:B6:5C:CF:8B:4C:43`

---

## 🧪 Test User Credentials (For Grading Verification)
- **Email:** `teststudent2474802010092@vlu.edu.vn`
- **Password:** `Vlu@2026`
