package View_Create_Posts

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.shivamvermasv380.test_games_finder.R
import android.widget.ArrayAdapter
import java.util.*
import android.widget.AdapterView
import com.shivamvermasv380.test_games_finder.MainActivity.OkHttpRequestAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class CreatePost: AppCompatActivity() {
    var token: String? = null
    var editText: EditText? = null
    var gameName: Spinner?=null
    var spinner1: Spinner? = null
    var spinner2: Spinner? = null
    var playersRequired: EditText?= null
    var cal = Calendar.getInstance()
    var list = arrayListOf<Int>()
    var userId:String?= null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("user")
        editText = findViewById(R.id.Date)
        playersRequired = findViewById(R.id.Nos_OfPlayersRequired)

        var day: Int = cal.get(Calendar.DAY_OF_MONTH)
        var month: Int = cal.get(Calendar.MONTH)
        var year: Int = cal.get(Calendar.YEAR)

        gameName = findViewById(R.id.Game)
        spinner1 = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)

        val adapter0 = ArrayAdapter.createFromResource(
            this,
            R.array.game_list, R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter0.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner


        gameName?.adapter = adapter0

        var datePicker = DatePickerDialog(this)

        // click on edittext to set the value

        // click on edittext to set the value
        with(editText) {

            // click on edittext to set the value

            // click on edittext to set the value
            this?.setOnClickListener(View.OnClickListener {
                datePicker = DatePickerDialog(
                    this@CreatePost,
                    { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                        editText?.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                    }, year, month, day
                )
                // set maximum date to be selected as today
                datePicker.getDatePicker().setMinDate(cal.getTimeInMillis())

                // show the dialog
                datePicker.show()
            })
        }
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.time_list, R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner


        spinner1?.adapter = adapter

        //spinner2?.adapter= adapter
        spinner1?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                // Your code here
                var str = spinner1?.selectedItem.toString()
                var z = str.toInt()
                list.clear()
                Log.d("z","$z")

                for(i in z+1 until 25){
                    list.add(i)
                }
                Log.d("list","$list")
                val adapter2: ArrayAdapter<*> =
                    ArrayAdapter<Any?>(this@CreatePost, R.layout.simple_spinner_item, list as List<Any?>)
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

                spinner2?.setAdapter(adapter2)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        })
    }


    fun postValues(view: View) {
        if(gameName?.selectedItem.toString().isNullOrEmpty() || spinner1?.selectedItem.toString().isNullOrEmpty() || spinner2?.selectedItem.toString().isNullOrEmpty()|| editText?.text.toString().isNullOrEmpty()|| playersRequired?.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Please fill all fields :(", Toast.LENGTH_LONG).show()
        }else {

            val url = "https://find-a-player.herokuapp.com/api/posts"
            var gn:String = gameName?.selectedItem.toString()
            var from:String = spinner1?.selectedItem.toString()
            var to:String = spinner2?.selectedItem.toString()

            val map: HashMap<String, String> = hashMapOf("gameName" to "$gn" ,"date" to "${editText?.text.toString()}","from" to "$from","to" to "$to","playersRequired" to "${playersRequired?.text.toString()}")

            Log.d("Create Post Map","$map")
            var client = OkHttpClient()
            var request = OkHttpRequestAuth(client)
            request.POST(url, map, token!!,object: Callback {
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
                                Log.d("statusInCreatePost","$status")
                            }
                            if(status.isNullOrEmpty()){
                                Log.d("Json Status","pass")
                                val intent= Intent( this@CreatePost,ViewPost::class.java)
                                intent.putExtra("token","$token")
                                intent.putExtra("user","$userId")
                                startActivity(intent)
                            }else{
                                Log.d("Json Status","fail")
                                Toast.makeText(this@CreatePost,"${json.get("message")}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {

                }
            })

        }
    }

}





