package ViewAccount

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequestAuth
import com.shivamvermasv380.test_games_finder.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class Post_ApprovalRequests : AppCompatActivity(), Post_Request_Item_Clicked {
    var token: String? = null
    var postId: String? = null
    var userId: String? = null
    private lateinit var mAdapter: Post_Approve_Request_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_approval_request)
        val extras = intent.extras
        token = extras?.getString("token")
        postId = extras?.getString("postId")
        val recyclerView: RecyclerView =
            findViewById(R.id.post_approval_request_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = Post_Approve_Request_Adapter(this@Post_ApprovalRequests)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://find-a-player.herokuapp.com/api/posts/request/?postId=$postId"
        Log.d("InFetchData", "Called")
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        request.GET(url, token!!, object : Callback {
            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        Log.d("Just try", "Op")
                        Log.d("Response Data", "$responseData")
                        var json = JSONObject(responseData)
                        Log.d("PostsApproveRequest", "$json")
                        val requetsJsonArray = json.getJSONArray("request")
                        Log.d("postsJSONArray", "$requetsJsonArray")
                        val requestsArray = ArrayList<Post_Status_Info>()
                        for (i in 0 until requetsJsonArray.length()) {
                            val postsJsonObject = requetsJsonArray.getJSONObject(i)

                            val postInfo = Post_Status_Info(
                                postsJsonObject.getString("user"),
                                postsJsonObject.getString("phoneNumber"),
                                postsJsonObject.getString("gameID"),
                                postsJsonObject.getString("_id"),
                                postsJsonObject.getString("status")
                            )
                            requestsArray.add(postInfo)
                            Log.d("posts_request_Array", "$requestsArray")
                        }
                        mAdapter.updatePost(requestsArray)
                        Log.d("PostsApproveArray", "$requestsArray")
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

    override fun onApprove_Req_Item_Clicked(item: Post_Status_Info) {
        var requestuserId = item.userrequestId
        var userstatus = "approved"
        postRequest(requestuserId, userstatus)
        Toast.makeText(applicationContext, "Token:$token", Toast.LENGTH_LONG).show()
        recreate()
    }

    override fun onDecline_Req_Item_Clicked(item: Post_Status_Info) {
        var requestuserId = item.userrequestId
        var userstatus = "declined"
        postRequest(requestuserId, userstatus)
        Log.d("ReCreateFunctiongoingtobecalled", "+1")
        Toast.makeText(applicationContext, "Token:$token", Toast.LENGTH_LONG).show()
        recreate()
    }

    private fun postRequest(requestuserId: String, userstatus: String) {
        var url = "https://find-a-player.herokuapp.com/api/posts/request/setstatus"
        val map = hashMapOf(
            "status" to "$userstatus",
            "requestId" to "$requestuserId",
            "postId" to "$postId"
        )
        val client = OkHttpClient()
        val request = OkHttpRequestAuth(client)
        request.POST(url, map, token!!, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("Approve_Req_Post", "Successfull")
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        Thread.sleep(3000)
                        var json = JSONObject(responseData)
                        println("Request Successful!!")
                        Log.d("BeforeJsonReq", "$json")
                        var status: String? = null
                        if (json.has("success")) {
                            status = json.get("success") as String

                        }

                        if (status.isNullOrEmpty()) {
                            Log.d("Json Status", "pass")

                            Toast.makeText(
                                this@Post_ApprovalRequests,
                                "Done Successfull",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            Log.d("Json Status", "fail")
                            Toast.makeText(
                                this@Post_ApprovalRequests, "${json.get("message")}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("Approve_Req_Post", "Error!!")
            }

        })

    }


}