package vn.edu.vlu.lab09.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.net.URLEncoder
import vn.edu.vlu.lab09.R
import vn.edu.vlu.lab09.adapter.PhotoAdapter
import vn.edu.vlu.lab09.data.MockDataProvider
import vn.edu.vlu.lab09.databinding.ActivityMainBinding

/**
 * Main Activity displaying Online Image Gallery via Glide and RecyclerView.
 * Target CLO 2 (ViewBinding & UI), CLO 3 (Data & Async), CLO 4 (Firebase Auth session & logout).
 * Features:
 * - 100% ViewBinding (zero findViewById)
 * - RecyclerView + PhotoAdapter loading 10+ remote HTTPS photos asynchronously
 * - Challenge 2 (+7.5%): Circular user avatar using Glide circleCrop() with dynamic avatar URL based on email
 * - Secure logout session clearing activity back stack (FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK)
 * - Logcat diagnostics
 */
class MainActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "Lab09_MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        // Setup User Profile Header (Challenge 2)
        setupUserProfile()

        // Setup RecyclerView & Glide Adapter
        setupRecyclerView()

        // Load Photo Data
        loadPhotos()

        // Setup Logout button in header
        binding.btnLogoutHeader.setOnClickListener {
            performLogout()
        }
    }

    /**
     * Challenge 2 (+7.5%): Display a circular user avatar in the header using
     * Glide's circleCrop() transformation with a dynamic avatar URL based on user email.
     */
    private fun setupUserProfile() {
        val user = auth.currentUser
        val email = user?.email ?: "Guest User"
        binding.tvUserEmail.text = email

        Log.d(TAG, "Current logged-in session user: $email (UID: ${user?.uid})")

        // Construct dynamic avatar URL using UI-Avatars service
        val encodedName = try {
            URLEncoder.encode(email.substringBefore("@"), "UTF-8")
        } catch (e: Exception) {
            "User"
        }
        val dynamicAvatarUrl = "https://ui-avatars.com/api/?name=$encodedName&background=6750A4&color=fff&size=256&bold=true"

        Log.d(TAG, "Loading dynamic circular avatar: $dynamicAvatarUrl")

        Glide.with(this)
            .load(dynamicAvatarUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivUserAvatar)
    }

    /**
     * Configure RecyclerView with LinearLayoutManager and PhotoAdapter.
     */
    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter { photo ->
            Toast.makeText(this, "Chọn: ${photo.title}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "User clicked photo ID=${photo.id}: ${photo.title}")
        }

        binding.rvPhotos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photoAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadPhotos() {
        val photos = MockDataProvider.getSamplePhotos()
        Log.d(TAG, "Loaded ${photos.size} sample photos into RecyclerView")
        photoAdapter.submitList(photos)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Securely sign out user, clear back stack using Intent flags, and return to AuthActivity.
     */
    private fun performLogout() {
        Log.d(TAG, "Signing out user: ${auth.currentUser?.email}")
        auth.signOut()

        Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
