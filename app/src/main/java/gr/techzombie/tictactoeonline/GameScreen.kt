package gr.techzombie.tictactoeonline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_game_screen.*
import kotlinx.android.synthetic.main.player_ticket.view.*
import java.lang.Exception
import kotlin.random.Random

var myRef:DatabaseReference?=null
var onlinePlayers=ArrayList<String>()
var username:String?=null
var uid:String?=null
val randomNumber = Random
var daNumber = 0
class GameScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        var bundle = intent.extras
        username = emailToUsername(bundle!!.getString("email").toString())
        uid = bundle!!.getString("uid").toString()
        textView3.text=username

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        myRef=database.getReference()
//        myRef.child("Users").child("Online").push().setValue("litsa")
        getOnlinePlayers(myRef!!)

    }

    inner class PlayerAdapter:BaseAdapter{
        var listOfPlayers=ArrayList<String>()
        var context:Context?=null
        constructor(context: Context,listOfPlayers: ArrayList<String>){
            this.context = context
            this.listOfPlayers = listOfPlayers
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var player = listOfPlayers[position]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView = inflator.inflate(R.layout.player_ticket,null)
            myView.textView6.text = player
            myView.textView8.text = "Online"
            return myView
        }

        override fun getItem(position: Int): Any {
            return listOfPlayers[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfPlayers.size
        }

    }

    override fun onResume() {
        super.onResume()
        daNumber = randomNumber.nextInt()
        myRef!!.child("Users").child("Online").child(daNumber.toString()).setValue(username)
    }

    override fun onPause() {
        super.onPause()
        myRef!!.child("Users").child("Online").child(daNumber.toString()).removeValue()
    }


    fun emailToUsername(str:String):String{
        val value = str.split("@")
        return value[0]
    }

    fun getOnlinePlayers(myRef:DatabaseReference){
        myRef.child("Users").child("Online")
            .addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val td = snapshot.value as HashMap<String,Any>
                    if (td!=null){
                        Toast.makeText(applicationContext,"td is not null OK",Toast.LENGTH_LONG).show()
                        onlinePlayers.clear()
                        for (key in td.keys){
                            onlinePlayers!!.add(td[key].toString())
                        }
                        var adapter = PlayerAdapter(applicationContext, onlinePlayers)
                        listView.adapter = adapter
                    }
                }catch (ex:Exception){}
            }
        })
    }

}
