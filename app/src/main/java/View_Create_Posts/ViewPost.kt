package View_Create_Posts

import Login_Register.LoginActivity
import ViewAccount.ViewAccountInformation
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequest
import com.shivamvermasv380.test_games_finder.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ViewPost: AppCompatActivity(), PostItemClicked {
    var token:String?=null
    var userId:String?=null
    private lateinit var mAdapter:PostListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)
        val extras = intent.extras
        token= extras?.getString("token")
        userId= extras?.getString("user")
        Log.d("ViewPost token","$token")
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = PostListAdapter(this)
        recyclerView.adapter = mAdapter


    }

    private fun fetchData() {
        val url="https://find-a-player.herokuapp.com/api/posts"
        Log.d("fetchDataCalled","fetchDataCalled")
        var client = OkHttpClient()
        var request = OkHttpRequest(client)
        request.GET(url, object : Callback {
            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        Log.d("In try block","In try block")
                        var json = JSONObject(responseData)
                        Log.d("Successfull Request", "$json")
                        val postsJsonArray = json.getJSONArray("posts")
                        Log.d("postsJSONArray","$postsJsonArray")
                        val postsArray = ArrayList<GamesInfo>()
                        for (i in 0 until postsJsonArray.length()){
                            val postsJsonObject = postsJsonArray.getJSONObject(i)

                            val gamesInfo= GamesInfo(
                                postsJsonObject.getString("gameName"),
                                postsJsonObject.getString("date"),
                                postsJsonObject.getString("from"),
                                postsJsonObject.getString("to"),
                                postsJsonObject.getString("playersRequired"),
                                postsJsonObject.getString("image"),
                                postsJsonObject.getString("_id")
                            )
                            postsArray.add(gamesInfo)
                            Log.d("postsInViewPost","$postsArray")
                        }
                        mAdapter.updatePost(postsArray)
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

//    override fun onItemClicked(item: GamesInfo) {
//
//    }

    fun create_post(view: android.view.View) {
        if(token.isNullOrEmpty()){
            val intent= Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("token",token)
            intent.putExtra("user",userId)
            startActivity(intent)
        }else{
            val intent = Intent(applicationContext,CreatePost::class.java)
            intent.putExtra("token",token)
            intent.putExtra("user",userId)
            startActivity(intent)
        }
    }



    override fun onItemClicked(item: GamesInfo) {

        val postId:String = item.postId
        val intent = Intent(applicationContext,Ask_To_Join_Information::class.java)
        intent.putExtra("token","$token")
        intent.putExtra("_id","$postId")
        intent.putExtra("user",userId)
        startActivity(intent)
    }

    fun View_Account_Information(view: android.view.View) {
        if(token.isNullOrEmpty()){
            val intent= Intent(applicationContext,LoginActivity::class.java)
            intent.putExtra("token",token)
            intent.putExtra("user",userId)
            startActivity(intent)
        }else{

            val intent= Intent(applicationContext, ViewAccountInformation::class.java)

            intent.putExtra("token","$token")
            intent.putExtra("user",userId)
            startActivity(intent)
        }
    }
}