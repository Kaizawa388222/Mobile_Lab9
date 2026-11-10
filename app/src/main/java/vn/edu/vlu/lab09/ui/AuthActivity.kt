package vn.edu.vlu.lab09.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.edu.vlu.lab09.R
import vn.edu.vlu.lab09.databinding.ActivityAuthBinding

/**
 * Authentication Screen supporting Email/Password Login & Registration.
 * Target CLO 2 (ViewBinding & UI), CLO 4 (Firebase Authentication)
 * Features:
 * - Auto-login check (currentUser != null)
 * - Toggleable UI (MaterialButtonToggleGroup)
 * - Client-side validation (regex, length >= 6, password matching)
 * - Firebase business exception handling
 * - Challenge 1: sendPasswordResetEmail() dialog flow
 */
class AuthActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "Lab09_AuthActivity"
    }

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Step 2: Auto-Login Check
        checkAutoLogin()

        // Setup UI listeners
        setupToggleGroup()
        setupSubmitButton()
        setupForgotPasswordButton()
    }

    /**
     * If user session is already persisted, navigate directly to MainActivity.
     */
    private fun checkAutoLogin() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "Auto-login detected for user: ${currentUser.email}")
            navigateToMainActivity()
        }
    }

    /**
     * Configure toggle between Login and Registration modes.
     */
    private fun setupToggleGroup() {
        binding.toggleAuthMode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnTabLogin -> {
                        isLoginMode = true
                        binding.tilConfirmPassword.visibility = View.GONE
                        binding.btnForgotPassword.visibility = View.VISIBLE
                        binding.btnSubmit.text = getString(R.string.btn_login)
                        clearErrors()
                    }
                    R.id.btnTabRegister -> {
                        isLoginMode = false
                        binding.tilConfirmPassword.visibility = View.VISIBLE
                        binding.btnForgotPassword.visibility = View.GONE
                        binding.btnSubmit.text = getString(R.string.btn_register)
                        clearErrors()
                    }
                }
            }
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString()?.trim().orEmpty()
            val confirmPassword = binding.etConfirmPassword.text?.toString()?.trim().orEmpty()

            if (validateInputs(email, password, confirmPassword)) {
                if (isLoginMode) {
                    performLogin(email, password)
                } else {
                    performRegistration(email, password)
                }
            }
        }
    }

    /**
     * Client-side validation:
     * - Email format check via Patterns.EMAIL_ADDRESS
     * - Password length >= 6
     * - Confirm password matches (for registration)
     */
    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        clearErrors()
        var isValid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_weak_password)
            isValid = false
        }

        if (!isLoginMode) {
            if (confirmPassword != password) {
                binding.tilConfirmPassword.error = getString(R.string.error_password_mismatch)
                isValid = false
            }
        }

        return isValid
    }

    private fun clearErrors() {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
    }

    /**
     * Perform Firebase Email/Password Login with comprehensive exception handling.
     */
    private fun performLogin(email: String, password: String) {
        setLoadingState(true)
        Log.d(TAG, "Attempting login for: $email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                setLoadingState(false)
                if (task.isSuccessful) {
                    Log.d(TAG, "Login successful for: ${auth.currentUser?.email}")
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    val exception = task.exception
                    Log.e(TAG, "Login failed: ${exception?.message}", exception)
                    handleFirebaseException(exception)
                }
            }
    }

    /**
     * Perform Firebase Email/Password Registration.
     */
    private fun performRegistration(email: String, password: String) {
        setLoadingState(true)
        Log.d(TAG, "Attempting registration for: $email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                setLoadingState(false)
                if (task.isSuccessful) {
                    Log.d(TAG, "Registration successful for: ${auth.currentUser?.email}")
                    Toast.makeText(this, getString(R.string.success_registered), Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    val exception = task.exception
                    Log.e(TAG, "Registration failed: ${exception?.message}", exception)
                    handleFirebaseException(exception)
                }
            }
    }

    /**
     * Handles specific Firebase exceptions according to Lab 09 rubric:
     * - FirebaseAuthWeakPasswordException
     * - FirebaseAuthUserCollisionException
     * - FirebaseAuthInvalidCredentialsException
     * - FirebaseNetworkException
     */
    private fun handleFirebaseException(exception: Exception?) {
        val errorMessage = when (exception) {
            is FirebaseAuthWeakPasswordException -> getString(R.string.error_weak_password)
            is FirebaseAuthUserCollisionException -> getString(R.string.error_user_collision)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.error_invalid_credentials)
            is FirebaseNetworkException -> getString(R.string.error_network)
            else -> getString(R.string.error_general, exception?.localizedMessage ?: "Không xác định")
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    /**
     * Challenge 1 (+7.5%): "Forgot Password?" feature using sendPasswordResetEmail
     */
    private fun setupForgotPasswordButton() {
        binding.btnForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun showForgotPasswordDialog() {
        val currentEmailInput = binding.etEmail.text?.toString()?.trim().orEmpty()

        val inputLayout = TextInputLayout(this).apply {
            setPadding(48, 16, 48, 0)
            hint = getString(R.string.email_hint)
        }
        val inputEdit = TextInputEditText(this).apply {
            setText(currentEmailInput)
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        inputLayout.addView(inputEdit)

        AlertDialog.Builder(this)
            .setTitle(R.string.reset_password_dialog_title)
            .setMessage(R.string.reset_password_dialog_msg)
            .setView(inputLayout)
            .setPositiveButton(R.string.btn_send) { dialog, _ ->
                val email = inputEdit.text?.toString()?.trim().orEmpty()
                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    sendPasswordReset(email)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun sendPasswordReset(email: String) {
        setLoadingState(true)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                setLoadingState(false)
                if (task.isSuccessful) {
                    Log.d(TAG, "Password reset email sent to $email")
                    Toast.makeText(this, getString(R.string.success_reset_email), Toast.LENGTH_LONG).show()
                } else {
                    Log.e(TAG, "Failed to send reset email: ${task.exception?.message}")
                    handleFirebaseException(task.exception)
                }
            }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        binding.btnSubmit.isEnabled = !isLoading
        binding.btnTabLogin.isEnabled = !isLoading
        binding.btnTabRegister.isEnabled = !isLoading
        binding.btnForgotPassword.isEnabled = !isLoading
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Terminate AuthActivity to avoid returning via Back button
    }
}
