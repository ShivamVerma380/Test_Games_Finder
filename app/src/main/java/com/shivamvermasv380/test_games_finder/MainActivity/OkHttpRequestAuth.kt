package com.shivamvermasv380.test_games_finder.MainActivity

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.util.HashMap


class OkHttpRequestAuth(client: OkHttpClient) {
    internal var client = OkHttpClient()

    init {
        this.client = client
    }

    fun POST(url: String, parameters: HashMap<String, String>, token:String, callback: Callback): Call {

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            JSONObject(parameters as Map<*, *>).toString()
        )


        Log.d("Request Body","$body")
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer $token")
            .post(body)
            .build()


        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun GET(url: String,token:String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer $token")
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
//    fun GET_Posts(url: String, parameters: HashMap<String, String>, token: String, callback: Callback): Call? {
//        val body: RequestBody = RequestBody.create(
//            MediaType.parse("application/json; charset=utf-8"),
//            JSONObject(parameters as Map<*, *>).toString()
//        )
//
//
//        Log.d("RequestBody","$body")
//        val request = Request.Builder()
//            .url(url)
//            .addHeader("Authorization","Bearer $token")
//            .post(body)
//            .build()
//
//
//        val call = client.newCall(request)
//        call.enqueue(callback)
//        return call
//    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}