package View_Create_Posts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shivamvermasv380.test_games_finder.R


class PostListAdapter(private val listener: PostItemClicked): RecyclerView.Adapter<PostsViewHolder>(){
    private val items: ArrayList<GamesInfo> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val viewHolder = PostsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = "Game Name:${currentItem.gameName}"
        holder.timedateView.text="Date:${currentItem.date}     Time: ${currentItem.from} - ${currentItem.to}"
        holder.playersRequiredView.text="Nos of Players Required: ${currentItem.PlayersRequired}"
        holder.postId.text= ""
        holder.join.text = "Ask to Join"
        val url:String = "https://find-a-player.herokuapp.com${currentItem.imageUrl}"
        Log.d("photourl","$url")
        Glide.with(holder.itemView.context).load(url).into(holder.gameImageView)
    }
    fun updatePost(updatedPosts: ArrayList<GamesInfo>) {
        items.clear()
        items.addAll(updatedPosts)
        notifyDataSetChanged()
    }

}
class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.gameName)
    val timedateView: TextView = itemView.findViewById(R.id.DateTime)
    val playersRequiredView: TextView = itemView.findViewById(R.id.PlayersRequired)
    val gameImageView: ImageView = itemView.findViewById(R.id.gameImage)
    val postId: TextView = itemView.findViewById(R.id.PostId)
    var join: TextView = itemView.findViewById(R.id.Join)
}

interface PostItemClicked {
    fun onItemClicked(item: GamesInfo)
}