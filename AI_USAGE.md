# AI USAGE REPORT - LAB 09
**Student:** Nguyễn Trung Đức - MSSV: 2474802010092  
**Course:** Mobile Application Programming (71ITSE30603 / 72ITSE30603)  
**CLO 5 (Code Quality & AI Compliance)**

---

## 1. AI Tooling Employed
- **AI Assistant:** Antigravity (Google DeepMind)
- **Primary Model:** Gemini 3.8 Flash (High)

---

## 2. Tasks Supported by AI
1. **Gradle Build Script Configuration:** Formatted `libs.versions.toml` and `app/build.gradle.kts` with proper Firebase BoM, KSP, and Glide dependencies.
2. **Material Design 3 Layout Architecture:** Crafted adaptive XML layouts (`activity_auth.xml`, `activity_main.xml`, `item_photo.xml`) with 100% ViewBinding compatibility.
3. **Robust Error Handling:** Mapped Firebase specific exceptions (`FirebaseAuthWeakPasswordException`, `FirebaseAuthUserCollisionException`, `FirebaseAuthInvalidCredentialsException`, `FirebaseNetworkException`) to user-friendly notifications.
4. **Glide Optimization:** Incorporated disk cache strategy (`DiskCacheStrategy.ALL`), cross-fade transition, and lifecycle cancellation in `onViewRecycled`.
5. **Bonus Challenges:** Architected `sendPasswordResetEmail()` dialog flow and dynamic avatar rendering via `circleCrop()`.

---

## 3. Human Verification & Modifications
- Verified that zero `findViewById` calls exist throughout all Kotlin source files.
- Executed full Gradle compilation test (`./gradlew assembleDebug`) producing a valid 9.13 MB APK.
- Confirmed debug keystore SHA-1 fingerprint for Firebase Console configuration.
- Packaged clean submission files without build caches.
