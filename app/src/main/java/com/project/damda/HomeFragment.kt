package com.project.damda

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recipeRecommendRv: RecyclerView

    private lateinit var urlBuilder: StringBuilder
    private val basic = "Grid_20150827000000000226_1" //기본 정보
    private val ingredient = "Grid_20150827000000000227_1" //재료 정보
    private val process = "Grid_20150827000000000228_1"//과정 정보

    private lateinit var recipeId: String
    private lateinit var recipeName: String
    private lateinit var recipeInfo: String
    private lateinit var recipeCookType: String
    private lateinit var recipeCookCategory: String
    private lateinit var recipeTime: String
    private lateinit var recipeKcal: String
    private lateinit var recipePerson: String
    private lateinit var recipeLevel: String
    private lateinit var recipeIngredientsCategory: String
    private lateinit var recipePrice: String

    private lateinit var mainActivity: MainActivity
    private lateinit var recipeRecommendAdapter: RecipeRecommendRecyclerAdapter
    private var datas = mutableListOf<RecipeRecommendItem>()
    private var recipeRecommendItems = mutableListOf<RecipeRecommendItem>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        recipeRecommendRv = v.findViewById(R.id.recipeRecommendRv)

        urlBuilder = StringBuilder(BuildConfig.API_RECIPE)
        makeUrlBuilder()
        xmlParsing()

        return v
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(recipeName: String, recipeDescription: String, recipeTime: String, recipeDifficulty: String) {
        recipeRecommendAdapter = RecipeRecommendRecyclerAdapter(mainActivity)
        recipeRecommendRv.adapter = recipeRecommendAdapter
        datas.apply { add(RecipeRecommendItem(recipeName,recipeDescription,recipeTime,recipeDifficulty)) }
        recipeRecommendRv.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        recipeRecommendRv.setHasFixedSize(true)
        recipeRecommendAdapter.datas = datas
        recipeRecommendAdapter.notifyDataSetChanged()

        recipeRecommendAdapter.setOnRecipeClickListener(object: RecipeRecommendRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(v: View?) {
                val recipeClickName = v!!.findViewById<TextView>(R.id.rvItem_recommendRecipeNameTv)
                Log.e("@@@@@Click@@@@@",   recipeClickName.toString())
            }

            override fun onLongClick(v: View?) {
                val recipeClickName = v!!.findViewById<TextView>(R.id.rvItem_recommendRecipeNameTv)
                Log.e("@@@@@LongClick@@@@@",   recipeClickName.toString())
            }

        })
    }

    private fun getFirstAndLastNumber(): Pair<Int, Int> {
        val start = Random.nextInt(1, 518)
        return Pair(start, start+19)
    }

    private fun makeUrlBuilder() {
        val (first, last) = getFirstAndLastNumber()
        try {
            urlBuilder.append(BuildConfig.API_RECIPE_KEY)
                .append("/xml")
                .append("/$basic")
                .append("/$first") // 요청 시작 위치
                .append("/$last") // 요청 종료 위치
            Log.d("parsingLog", "url: $urlBuilder")

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun xmlParsing() {
        try {
            val url = URL(urlBuilder.toString())

            val task = XMLTask1()
            task.execute(url)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class XMLTask1 : AsyncTask<URL?, Void?, Unit?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: URL?) {
            val myUrl = urls[0]

            try {
                val `is` = myUrl?.openStream()
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()

                parser.setInput(`is`, "UTF8")
                var eventType = parser.eventType

                var tagName: String

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            tagName = parser.name
                            when (tagName) {
                                "RECIPE_ID" -> {
                                    parser.next()
                                    recipeId = parser.text ?: ""
                                }
                                "RECIPE_NM_KO" -> {
                                    parser.next()
                                    recipeName = parser.text ?: ""
                                }
                                "SUMRY" -> {
                                    parser.next()
                                    recipeInfo = parser.text ?: ""
                                }
                                "NATION_NM" -> {
                                    parser.next()
                                    recipeCookType = parser.text ?: ""
                                }
                                "TY_NM" -> {
                                    parser.next()
                                    recipeCookCategory = parser.text ?: ""
                                }
                                "COOKING_TIME" -> {
                                    parser.next()
                                    recipeTime = parser.text ?: ""
                                }
                                "CALORIE" -> {
                                    parser.next()
                                    recipeKcal = parser.text ?: ""
                                }
                                "QNT" -> {
                                    parser.next()
                                    recipePerson = parser.text ?: ""
                                }
                                "LEVEL_NM" -> {
                                    parser.next()
                                    recipeLevel = parser.text ?: ""
                                }
                                "IRDNT_CODE" -> {
                                    parser.next()
                                    recipeIngredientsCategory = parser.text ?: ""
                                }
                                "PC_NM" -> {
                                    parser.next()
                                    recipePrice = parser.text ?: ""
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            tagName = parser.name
                            if (tagName == "row") {
                                recipeRecommendItems.add(RecipeRecommendItem(recipeName,recipeInfo,recipeTime,recipeLevel))
                            }
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        @SuppressLint("DiscouragedApi")
        @Deprecated("Deprecated in Java", ReplaceWith("super.onPostExecute(result)", "android.os.AsyncTask"))
        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            if(recipeRecommendItems.isNotEmpty()) {
                for(i in 0 until recipeRecommendItems.size) {
                    if(i != 0) {
                        initRecycler(recipeRecommendItems[i].recommendRecipeName,recipeRecommendItems[i].recommendRecipeDescription,recipeRecommendItems[i].recommendRecipeTime,recipeRecommendItems[i].recommendRecipeDifficulty)
                    }
                }
            }

            else {
                recipeRecommendItems.clear()
            }
        }
    }
}