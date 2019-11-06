package org.noel.dunsceal.activities.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.noel.dunsceal.R
import com.google.android.material.snackbar.Snackbar
import org.noel.dunsceal.helpers.auth.UserDatabaseHelper
import org.noel.dunsceal.helpers.auth.UserInputValidation
import org.noel.dunsceal.model.DunUser

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterActivity

    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var textInputLayoutConfirmPassword: TextInputLayout
    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextConfirmPassword: TextInputEditText
    private lateinit var appCompatButtonRegister: AppCompatButton
    private lateinit var appCompatTextViewLoginLink: AppCompatTextView
    private lateinit var userInputValidation: UserInputValidation
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register_activity)
        supportActionBar!!.hide()
        initViews()
        initListeners()
        initObjects()
    }

    /**
     * This method is to initialize views
     */
    private fun initViews() {
        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        textInputLayoutName = findViewById<View>(R.id.textInputLayoutName) as TextInputLayout
        textInputLayoutEmail = findViewById<View>(R.id.textInputLayoutEmail) as TextInputLayout
        textInputLayoutPassword =
            findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout
        textInputLayoutConfirmPassword =
            findViewById<View>(R.id.textInputLayoutConfirmPassword) as TextInputLayout
        textInputEditTextName = findViewById<View>(R.id.textInputEditTextName) as TextInputEditText
        textInputEditTextEmail =
            findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        textInputEditTextPassword =
            findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText
        textInputEditTextConfirmPassword =
            findViewById<View>(R.id.textInputEditTextConfirmPassword) as TextInputEditText
        appCompatButtonRegister =
            findViewById<View>(R.id.appCompatButtonRegister) as AppCompatButton
        appCompatTextViewLoginLink =
            findViewById<View>(R.id.appCompatTextViewLoginLink) as AppCompatTextView
    }

    /**
     * This method is to initialize listeners
     */
    private fun initListeners() {
        appCompatButtonRegister!!.setOnClickListener(this)
        appCompatTextViewLoginLink!!.setOnClickListener(this)
    }

    /**
     * This method is to initialize objects to be used
     */
    private fun initObjects() {
        userInputValidation = UserInputValidation(activity)
        userDatabaseHelper = UserDatabaseHelper(activity)
    }

    /**
     * This implemented method is to listen the click on view
     * @param v
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonRegister -> postDataToSQLite()
            R.id.appCompatTextViewLoginLink -> finish()
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private fun postDataToSQLite() {
        if (!userInputValidation!!.isInputEditTextFilled(
                textInputEditTextName,
                textInputLayoutName,
                getString(R.string.error_message_name)
            )
        ) {
            return
        }
        if (!userInputValidation!!.isInputEditTextFilled(
                textInputEditTextEmail,
                textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!userInputValidation!!.isInputEditTextEmail(
                textInputEditTextEmail,
                textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!userInputValidation!!.isInputEditTextFilled(
                textInputEditTextPassword,
                textInputLayoutPassword,
                getString(R.string.error_message_password)
            )
        ) {
            return
        }
        if (!userInputValidation!!.isInputEditTextMatches(
                textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match)
            )
        ) {
            return
        }

        if (!userDatabaseHelper!!.checkUser(textInputEditTextEmail!!.text.toString().trim())) {

            var user = DunUser(
                name = textInputEditTextName!!.text.toString().trim(),
                email = textInputEditTextEmail!!.text.toString().trim(),
                password = textInputEditTextPassword!!.text.toString().trim()
            )

            userDatabaseHelper!!.addUser(user)


            // Snack Bar to show success message that record saved successfully
            Snackbar.make(
                nestedScrollView!!,
                getString(R.string.success_message),
                Snackbar.LENGTH_LONG
            ).show()

            val intentLogin = Intent(applicationContext, LoginActivity::class.java)
            intentLogin.putExtra("MSG", R.string.success_message)
            startActivity(intentLogin)
            emptyInputEditText()
            setContentView(R.layout.user_login_activity)
        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(
                nestedScrollView!!,
                getString(R.string.error_email_exists),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
        textInputEditTextConfirmPassword!!.text = null
    }
}