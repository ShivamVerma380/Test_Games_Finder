package ViewAccount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequestAuth
import com.shivamvermasv380.test_games_finder.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ViewAccountInformation: AppCompatActivity() {
    var token:String?=null
    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var name:String
    private lateinit var email:String
    var userId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_account_information)
        val extras = intent.extras

        token= extras?.getString("token")
        userId= extras?.getString("user")
        textName = findViewById(R.id.userName)
        textEmail = findViewById(R.id.userEmail)
        fetchData()

    }

    private fun fetchData() {
        val url="https://find-a-player.herokuapp.com/api/users/"
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        request.GET(url, token!!, object : Callback {
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
                            name = json.getString("name")
                            email= json.getString("email")
                            textName.append(name)
                            textEmail.append(email)

                        }else{
                            Log.d("Json Status","fail")
                            Toast.makeText(applicationContext,"${json.get("message")}", Toast.LENGTH_LONG).show()
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

    fun view_created_post_status(view: android.view.View) {
        val intent= Intent(applicationContext,View_Created_Posts::class.java)
        intent.putExtra("name","$name")
        intent.putExtra("email","$email")
        intent.putExtra("token","$token")
        intent.putExtra("user",userId)
        startActivity(intent)
    }
}