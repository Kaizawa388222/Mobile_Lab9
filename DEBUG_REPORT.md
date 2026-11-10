# DEBUG REPORT - LAB 09
**Student:** Nguyễn Trung Đức - MSSV: 2474802010092  
**Date:** 10/11/2026  
**Course:** Mobile Application Programming (71ITSE30603 / 72ITSE30603)

---

## Issue 1: Android Resource Linking Failure for Adaptive App Icon
- **Symptoms:** Gradle build failed during `:app:processDebugResources` with error:
  `resource drawable/ic_launcher_background not found` and `resource drawable/ic_launcher_foreground not found`.
- **Root Cause:** When copying baseline resources, vector assets `ic_launcher_background.xml` and `ic_launcher_foreground.xml` referenced by `mipmap-anydpi-v26/ic_launcher.xml` and `mipmap-anydpi-v26/ic_launcher_round.xml` were missing in `res/drawable`.
- **Solution:** Added `ic_launcher_background.xml` and `ic_launcher_foreground.xml` into `res/drawable/`.
- **Verification:** `:app:processDebugResources` compiled cleanly with exit code 0.

---

## Issue 2: RecyclerView Glide Image Flickering & Memory Leaks on Fast Scrolling
- **Symptoms:** When fast-scrolling through the photo gallery, recycled views could load images intended for previous positions, causing image flickering and memory retention.
- **Root Cause:** Asynchronous image load tasks in Glide can outlive the view recycling lifecycle if not canceled when the view is recycled.
- **Solution:** Implemented `onViewRecycled(holder: PhotoViewHolder)` in `PhotoAdapter`:
  ```kotlin
  override fun onViewRecycled(holder: PhotoViewHolder) {
      super.onViewRecycled(holder)
      Glide.with(holder.itemView.context).clear(holder.binding.ivPhoto)
  }
  ```
- **Verification:** Images load cleanly, view holder reuse does not display residual images, and memory footprint remains stable.

---

## Issue 3: Firebase Auth Navigation Back Stack Pollution on Logout
- **Symptoms:** If a user logs out from `MainActivity`, pressing the Android hardware Back button could return to `MainActivity` without an active session.
- **Root Cause:** Launching `AuthActivity` without intent flags leaves `MainActivity` in the back stack.
- **Solution:** Cleared the entire activity task stack using Intent flags:
  ```kotlin
  val intent = Intent(this, AuthActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  }
  startActivity(intent)
  finish()
  ```
- **Verification:** Back stack is cleared; pressing Back exits the app gracefully.
