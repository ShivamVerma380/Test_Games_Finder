package ViewAccount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequestAuth
import com.shivamvermasv380.test_games_finder.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class View_Created_Posts: AppCompatActivity(), Created_Post_Item_Clicked {
    var token:String?=null
    var userId:String?=null
    var name:String?=null
    var email:String?=null
    private lateinit var mAdapter:Created_Post_Status_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_created_posts)
        val extras = intent.extras
        token= extras?.getString("token")
        userId= extras?.getString("user")
        name = extras?.getString("name")
        email = extras?.getString("email")
        Toast.makeText(applicationContext,"token:$token\n name:$name \n Email:$email \n UserID:$userId",
            Toast.LENGTH_LONG).show()
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_created_post_status) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = Created_Post_Status_Adapter(this)
        recyclerView.adapter = mAdapter

    }
    private fun fetchData() {
        val url="https://find-a-player.herokuapp.com/api/posts/$userId"
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)

        request.GET(url,token!!,object : Callback {
            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        var json = JSONObject(responseData)
                        Log.d("Successfull Request", "$json")
                        val postsJsonArray = json.getJSONArray("posts")
                        Log.d("postsJSONArray","$postsJsonArray")
                        val postsArray = ArrayList<View_Created_Posts_data_class>()
                        for (i in 0 until postsJsonArray.length()){
                            val postsJsonObject = postsJsonArray.getJSONObject(i)

                            val gamesInfo= View_Created_Posts_data_class(
                                postsJsonObject.getString("gameName"),
                                postsJsonObject.getString("date"),
                                postsJsonObject.getString("_id")
                            )
                            postsArray.add(gamesInfo)
                            Log.d("postsMarker","$postsArray")
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

    override fun onCreateItemClicked(item: View_Created_Posts_data_class) {
//        TODO("Not yet implemented")
//        Log.d("In OnItemClicked","null")
           val postId:String = item.postId
//        Log.d("postIdInView" ,"$postId")
//        val intent = Intent(applicationContext,Post_ApprovalRequests::class.java)
//        intent.putExtra("postId","$postId")
//        startActivity(intent)
        Log.d("onCreateItemClicked","Function Called")
        val intent = Intent(applicationContext,Post_ApprovalRequests::class.java)
        intent.putExtra("postId","$postId")
        intent.putExtra("token","$token")
        startActivity(intent)

    }


}