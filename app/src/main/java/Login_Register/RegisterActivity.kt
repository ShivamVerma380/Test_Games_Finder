package Login_Register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequest
import com.shivamvermasv380.test_games_finder.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class RegisterActivity: AppCompatActivity() {
    var text_Name: TextView? = null
    var name: String? = null
    var text_email: TextView? = null
    var email: String? = null
    var text_password: TextView? = null
    var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        val bundle: Bundle? = intent.extras
        text_Name = findViewById(R.id.Name)
        text_email = findViewById(R.id.Email)
        text_password = findViewById(R.id.Password)

    }

    fun register(view: android.view.View) {
        Log.d("call", "Register function called")
        setContentView(R.layout.register_activity)
        name = text_Name?.text.toString()
        email = text_email?.text.toString()
        password = text_password?.text.toString()
        val url = "https://find-a-player.herokuapp.com/api/users/"
        val map: HashMap<String, String> = hashMapOf(
            "name" to "$name",
            "email" to "$email",
            "password" to "$password"
        )
        Log.d("Idol Map", "$map")
        var client = OkHttpClient()
        var request = OkHttpRequest(client)
        request.POST(url, map, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        var json = JSONObject(responseData)
                        Log.d("Successfull Request", "$json")
                        var status:String ?= null
                        if(json.has("status")){
                            status= json.get("status") as String
                        }

                        if(status.isNullOrEmpty()){
                            Log.d("Json Status","pass")
                            val intent= Intent(applicationContext,LoginActivity::class.java)
                            startActivity(intent)

                        }else{
                            Log.d("Json Status","fail")
                            Toast.makeText(this@RegisterActivity,"${json.get("message")}", Toast.LENGTH_LONG).show()
                        }
                        //CheckRequest(json)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Request Failure.")
            }
        })
    }
    /*fun CheckRequest(var jsonObject){

    }*/
}