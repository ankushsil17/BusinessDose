package com.example.businessdose

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {
    lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }


    override fun onItemClicked(item: News) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(item.url))
        }

    }

private fun MainActivity.fetchData() {
    val url = " https://newsdata.io/api/1/news?apikey=pub_9182aa890283172844f6a6f8d1db5bfcc624&q=technology "

    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
       Response.Listener {
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for (i in 0 until newsJsonArray.length()) {
                val newsJsonObject = newsJsonArray.getJSONObject(i)
                val news = News(
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("creator"),
                    newsJsonObject.getString("link"),
                    newsJsonObject.getString("image_url")
                )
                newsArray.add(news)
            }
            mAdapter.updateNews(newsArray)
        },
      Response.ErrorListener  {

        }
    )

// Access the RequestQueue through your singleton class.
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

}
