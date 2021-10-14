package ViewAccount

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivamvermasv380.test_games_finder.R
import org.w3c.dom.Text
import java.util.ArrayList

class Post_Approve_Request_Adapter(private val listener: Post_Request_Item_Clicked): RecyclerView.Adapter<Approve_Posts_View_Holder>(){
    private val items: ArrayList<Post_Status_Info> = ArrayList()
    private lateinit var buttonApprove:Button
    private lateinit var buttonDecline:Button
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Approve_Posts_View_Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        val viewHolder = Approve_Posts_View_Holder(view)
//      view.setOnClickListener{
//         listener.onPost_Req_Item_Clicked(items[viewHolder.adapterPosition])
//       }

        buttonApprove = view.findViewById(R.id.item_req_Approve)

        buttonApprove.setOnClickListener{
            listener.onApprove_Req_Item_Clicked(items[viewHolder.adapterPosition])
        }
        buttonDecline = view.findViewById(R.id.item_req_Decline)
        buttonDecline.setOnClickListener{
            listener.onDecline_Req_Item_Clicked(items[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updatePost(updatedPosts: ArrayList<Post_Status_Info>) {
        items.clear()
        items.addAll(updatedPosts)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Approve_Posts_View_Holder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = "User Name:${currentItem.username}"
        holder.titlephoneNumber.text = "Phone Number:${currentItem.userphone}"
        holder.titleplayerId.text= "Player Game Id:${currentItem.userplayerId}"
        holder.buttonViewApprove.text = "Approve"
        holder.buttonViewDecline.text = "Decline"
        var status:String = currentItem.status

        if(status!= "none"){
            if(currentItem.status=="approved"){
                holder.statusView.text = "approved"
                holder.statusView.setTextColor(Color.parseColor("#BFFF00"))
            }else if(currentItem.status=="declined"){
                holder.statusView.text = "declined"
                holder.statusView.setTextColor(Color.parseColor("#FF0000"))
            }
            buttonApprove.visibility= View.GONE
            buttonDecline.visibility = View.GONE
        }
    }

}
class Approve_Posts_View_Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.item_req_userId)
    val titlephoneNumber:TextView = itemView.findViewById(R.id.item_req_user_phone_no)
    val titleplayerId:TextView = itemView.findViewById(R.id.item_req_playerId)
    val buttonViewApprove: Button = itemView.findViewById(R.id.item_req_Approve)
    val buttonViewDecline: Button = itemView.findViewById(R.id.item_req_Decline)
    val statusView:TextView = itemView.findViewById(R.id.item_req_status)
}

interface Post_Request_Item_Clicked {
    fun onApprove_Req_Item_Clicked(item: Post_Status_Info)
    fun onDecline_Req_Item_Clicked(item:Post_Status_Info)
}