package ViewAccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivamvermasv380.test_games_finder.R
import java.util.ArrayList

class Created_Post_Status_Adapter(private val listener: Created_Post_Item_Clicked): RecyclerView.Adapter<Created_Posts_View_Holder>(){
    private val items: ArrayList<View_Created_Posts_data_class> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Created_Posts_View_Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        val viewHolder = Created_Posts_View_Holder(view)
        view.setOnClickListener{
            listener.onCreateItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Created_Posts_View_Holder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = "Game Name:${currentItem.game_name}"
        holder.dateView.text="Date:${currentItem.date}"
    }
    fun updatePost(updatedPosts: ArrayList<View_Created_Posts_data_class>) {
        items.clear()
        items.addAll(updatedPosts)
        notifyDataSetChanged()
    }

}
class Created_Posts_View_Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.idTVGameName)
    val dateView: TextView = itemView.findViewById(R.id.idTVDate)

}

interface Created_Post_Item_Clicked {
    fun onCreateItemClicked(item: View_Created_Posts_data_class)
}