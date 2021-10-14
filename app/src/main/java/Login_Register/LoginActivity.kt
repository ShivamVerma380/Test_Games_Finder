package Login_Register

import View_Create_Posts.ViewPost
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


class LoginActivity : AppCompatActivity(){
    var email: TextView?=null
    var password: TextView?=null
    var string_email:String?=null
    var token:String?= null
    var password_email:String?=null
    private lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val bundle: Bundle?=intent.extras
        email= findViewById(R.id.Email)
        password= findViewById(R.id.Password)
    }
    fun log_In(view: android.view.View) {
        //log in from login_activity
        Log.d("call", "Log_In function called")
        //setContentView(R.layout.login_activity)
        email= findViewById(R.id.Email)
        password= findViewById(R.id.Password)
        string_email = email?.text.toString()
        password_email = password?.text.toString()
        Toast.makeText(this,"$string_email:$password_email", Toast.LENGTH_LONG).show()
        val url = "https://find-a-player.herokuapp.com/api/users/login"
        val map: HashMap<String, String> = hashMapOf("email" to "$string_email" ,"password" to "$password_email")
        Log.d("Idol Map","$map")
        var client = OkHttpClient()
        var request = OkHttpRequest(client)
        request.POST(url, map, object: Callback {
            override fun onResponse(call: Call?, response: Response) {
                val responseData = response.body()?.string()
                runOnUiThread{
                    try {
                        var json = JSONObject(responseData)
                        println("Request Successful!!")
                        Log.d("Successfull Request","$json")
                        token = json.getString("token")
                        Log.d("token","$token")
                        var status:String ?= null
                        if(json.has("status")){
                            status= json.get("status") as String
                        }

                        if(status.isNullOrEmpty()){
                            Log.d("Json Status","pass")
                            userId = json.getString("_id")
                            val intent= Intent(applicationContext, ViewPost::class.java)
                            intent.putExtra("token",token)
                            intent.putExtra("user","$userId")
                            startActivity(intent)

                        }else{
                            Log.d("Json Status","fail")
                            Toast.makeText(this@LoginActivity,"${json.get("message")}", Toast.LENGTH_LONG).show()
                        }

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

    fun Register_login_activity(view: android.view.View) {
        val intent = Intent(applicationContext,RegisterActivity::class.java)
        startActivity(intent)
    }
}