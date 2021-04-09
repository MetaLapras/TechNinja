package com.techninja.techninja

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.techninja.techninja.utils.TAGG
import com.techninja.techninja.utils.USER_REFERENCE
import dmax.dialog.SpotsDialog
import io.reactivex.disposables.CompositeDisposable


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var currentUser:FirebaseUser?=null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var compositeDisposable = CompositeDisposable()
    private var dialog: AlertDialog?=null
    lateinit var btnSignIn:SignInButton
    private var listener: FirebaseAuth.AuthStateListener?=null
    private lateinit var userRef: DatabaseReference
    private var provider: List<AuthUI.IdpConfig>? = null

    companion object{
        const val RC_SIGN_IN =1717
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        //config google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        // Initialize Firebase Auth
        userRef = FirebaseDatabase.getInstance().getReference(USER_REFERENCE)
        mAuth = FirebaseAuth.getInstance();

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        btnSignIn.setOnClickListener(View.OnClickListener {
            signIn()
        })

    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(listener!!)
    }

    override fun onStop() {
        mAuth!!.removeAuthStateListener (listener!!)
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAGG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAGG, "Google sign in failed", e)
                }
            } else {
                Log.w(TAGG, "Google sign in failed", exception)
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGG, "signInWithCredential:success"+mAuth.currentUser)
                    val user = mAuth.currentUser
                    startActivity(Intent(this,HomeActivity::class.java))

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGG, "signInWithCredential:failure", task.exception)

                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}

