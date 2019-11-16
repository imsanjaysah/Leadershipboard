package com.example.testapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.collection.SimpleArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class LeadershipboardAdapter(private val pages: Int, val context: Context) :
    RecyclerView.Adapter<LeadershipboardAdapter.LeadershipViewHolder>() {

    private var isScrolling: Boolean = false;
    private var currentPosition: Int = -1;
    private var loadedPages: SimpleArrayMap<Int, List<Leader>> = SimpleArrayMap()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadershipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LeadershipViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: LeadershipViewHolder, position: Int) {
        //Resetting the view
        holder.clear()
        //If pages are scrolling, don't load data
        if (isScrolling) {
            return
        }
        //If data is loaded for the 1st time from the json for the current position, then saving it in a collection so that
        //there is no need to parse the json again for the position.
        if (!loadedPages.containsKey(position)) {
            val pageData = parseJsonPage(position + 1)
            loadedPages.put(position, pageData)
        }
        //Updating Ui from the data saved in the collection
        loadedPages[position]?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = pages

    /**
     * Function to parse the json file. Every json file contains 2 leaders data.
     */
    fun parseJsonPage(pageNo: Int): List<Leader>? {
        Log.d("Adapter", "Parsing Page $pageNo")
        try {
            context.applicationContext.assets.open("file$pageNo.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val leaderType = object : TypeToken<List<Leader>>() {}.type
                    val pageLeaders: List<Leader> = Gson().fromJson(jsonReader, leaderType)
                    Log.d("Adapter", "Parsing Page $pageLeaders")

                    return pageLeaders
                }
            }
        } catch (ex: Exception) {

            Log.e("Adapter", "Error ", ex)
        }
        return null
    }

    /**
     * Function to update recyclerview scrolling status
     */
    fun setScrollingStatus(scrollingStatus: Boolean) {
        isScrolling = scrollingStatus

    }

    /**
     * Function to set the current position after scrolling is stopped
     */
    fun setCurrentPosition(position: Int) {
        currentPosition = position
        Log.d("Scrolling stopped", currentPosition.toString())

        //Updating UI for the current position and it's left and right adjacent positions
        notifyItemRangeChanged(currentPosition - 1, 3)
    }


    inner class LeadershipViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_rank_2_items, parent, false)) {
        private var mRankView1: TextView? = null
        private var mNameView1: TextView? = null
        private var mRankView2: TextView? = null
        private var mNameView2: TextView? = null


        init {
            mRankView1 = itemView.findViewById(R.id.textViewRankId)
            mNameView1 = itemView.findViewById(R.id.textViewLeaderName)
            mRankView2 = itemView.findViewById(R.id.textViewRankId2)
            mNameView2 = itemView.findViewById(R.id.textViewLeaderName2)
        }

        fun bind(leaders: List<Leader>) {
            mRankView1?.text = leaders[0].rank.toString()
            mNameView1?.text = leaders[0].name
            mRankView2?.text = leaders[1].rank.toString()
            mNameView2?.text = leaders[1].name
        }
        fun clear() {
            mRankView1?.text = "--"
            mNameView1?.text = "--"
            mRankView2?.text = "--"
            mNameView2?.text = "--"
        }

    }

}