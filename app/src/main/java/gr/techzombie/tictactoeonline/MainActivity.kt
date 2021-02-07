package gr.techzombie.tictactoeonline

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        //authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null) {
            var intent = Intent(this,GameScreen::class.java)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("email",currentUser.email)
            startActivity(intent)
        }
    }

    fun bu_login(view:View){
        mAuth!!.createUserWithEmailAndPassword(editText.text.toString(), editText2.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else { // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
                // ...
            }
    }
}

