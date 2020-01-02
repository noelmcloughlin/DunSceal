package ie.noel.dunsceal.views.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import ie.noel.dunsceal.R
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.VIEW
import kotlinx.android.synthetic.main.fragment_login_screen.*
import org.jetbrains.anko.toast

open class LoginView : BaseView(), View.OnClickListener {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)
        progressBar.visibility = ProgressBar.INVISIBLE

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        presenter.fetchData()

        // Button event handling
        signUp.setOnClickListener {
            val email = fieldEmail.text.toString()
            val password = fieldPassword.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doSignUp(email, password)
            }
        }

        logIn.setOnClickListener {
            val email = fieldEmail.text.toString()
            val password = fieldPassword.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doLogin(email, password)
            }
        }

        signOutButton.setOnClickListener(this)
        verifyEmailButton.setOnClickListener(this)
        sign_in_button.setOnClickListener(this)

        // Present Login activity
        presenter.app.auth = FirebaseAuth.getInstance()

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        // Get google sign-in
        presenter.app.googleSignInClient = GoogleSignIn.getClient(this, gso)
        loader = createLoader(this)
        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setColorScheme(0)
    }

    override fun showProgress() {
        progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = ProgressBar.INVISIBLE
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showLoader(loader, "Creating Account...")
        // [START create_user_with_email]
        presenter.app.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    updateUI(presenter.app.auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
                // [START_EXCLUDE]
                hideLoader(loader)
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        showLoader(loader, "Logging In...")
        // [START sign_in_with_email]
        presenter.app.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI(presenter.app.auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                hideLoader(loader)
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // Disable button
        verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = presenter.app.auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }
        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideLoader(loader)
        if (user != null) {
            status.text =
                getString(R.string.emailpassword_status_fmt, user.email, user.isEmailVerified)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)
            emailPasswordButtons.visibility = View.GONE
            emailPasswordFields.visibility = View.GONE
            signedInButtons.visibility = View.VISIBLE
            verifyEmailButton.isEnabled = !user.isEmailVerified

            presenter.dataStore!!.fetchDuns {
                this.navigateTo(VIEW.HOME)
            }

        } else {
            status.setText(R.string.signed_out)
            detail.text = null
            emailPasswordButtons.visibility = View.VISIBLE
            emailPasswordFields.visibility = View.VISIBLE
            signedInButtons.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signUp -> createAccount(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.logIn -> signIn(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.signOutButton -> presenter.doLogout()
            R.id.verifyEmailButton -> sendEmailVerification()
            R.id.sign_in_button -> googleSignIn()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_SIGN_IN = 9001
    }

    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START google signin]
    private fun googleSignIn() {
        val signInIntent = presenter.app.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END google signin]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        showLoader(loader, "Logging In with Google...")
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        presenter.app.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    updateUI(presenter.app.auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(logIn, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                hideLoader(loader)
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]
}