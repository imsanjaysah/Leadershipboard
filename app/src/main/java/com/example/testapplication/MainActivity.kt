package com.example.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leadershipRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = LeadershipboardAdapter(15, this@MainActivity)
            addOnScrollListener(LeadershipBoardScrollListener({ isScrolling ->
                (adapter as LeadershipboardAdapter).setScrollingStatus(isScrolling)
                if (!isScrolling) {
                    (adapter as LeadershipboardAdapter).setCurrentPosition((layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
                }
            }, layoutManager as LinearLayoutManager))
        }
    }

    class LeadershipBoardScrollListener(
        val func: (isScrolling: Boolean) -> Unit,
        val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            func.invoke(newState != SCROLL_STATE_IDLE)
        }

    }
}
