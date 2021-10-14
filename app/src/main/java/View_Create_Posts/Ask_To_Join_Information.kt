package View_Create_Posts

import android.os.Bundle
import android.util.Log
import android.widget.EditText
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


class Ask_To_Join_Information: AppCompatActivity() {
    private lateinit var gameId: EditText
    private lateinit var phoneNos: EditText
    var token: String? = null
    var postId: String? = null
    var userId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("user")
        postId = extras?.getString("_id")
        Log.d("postId", "$postId")
        setContentView(R.layout.ask_to_join_information)
        gameId = findViewById(R.id.GameId)
        phoneNos = findViewById(R.id.PhoneNos)

    }

    fun book_slot(view: android.view.View) {
        if (gameId.text.isNullOrEmpty() || phoneNos.text.isNullOrEmpty())
            Toast.makeText(applicationContext, "Please fill all the fields:(", Toast.LENGTH_LONG)
                .show()
        else {
            Toast.makeText(applicationContext, "PostID : $postId", Toast.LENGTH_LONG).show()
            val url="https://find-a-player.herokuapp.com/api/posts/request/$postId"
            val map: HashMap<String, String> = hashMapOf("phoneNumber" to "${phoneNos.text}" ,"gameID" to "${gameId.text}")
            var client = OkHttpClient()
            var request = OkHttpRequestAuth(client)
            Log.d("Ask_To_Join_Map","$map")
            request.POST(url, map, token!!, object: Callback {
                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    runOnUiThread{
                        try {
                            var json = JSONObject(responseData)
                            println("Request Successful!!")
                            Log.d("Successfull Request","$json")
                            var status:String ?= null
                            if(json.has("status")){
                                status= json.get("status") as String
                            }

                            if(status.isNullOrEmpty()){
                                Log.d("Json Status","pass")

                                Toast.makeText(applicationContext,"Applied Successfully", Toast.LENGTH_LONG).show()

                            }else{
                                Log.d("Json Status","fail")
                                Toast.makeText(this@Ask_To_Join_Information,"${json.get("message")}",
                                    Toast.LENGTH_LONG).show()
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

    }
}