package com.project.damda

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        urlBuilder = StringBuilder(BuildConfig.API_RECIPE)
        makeUrlBuilder()
        xmlParsing()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun makeUrlBuilder() {
        try {
            urlBuilder.append(URLEncoder.encode("", "UTF-8")+ BuildConfig.API_RECIPE_KEY)
            urlBuilder.append("/" + URLEncoder.encode("", "UTF-8") + URLEncoder.encode("xml", "UTF-8"))
            urlBuilder.append("/" + URLEncoder.encode("", "UTF-8")+ URLEncoder.encode(basic, "UTF-8"))
            urlBuilder.append("/" + URLEncoder.encode("", "UTF-8")+ URLEncoder.encode("1", "UTF-8")) // 요청 시작 위치
            urlBuilder.append("/" + URLEncoder.encode("", "UTF-8")+ URLEncoder.encode("5", "UTF-8")) // 요청 종료 위치
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
                                    Log.e("recipeId", recipeId.ifEmpty { "NULL" }) // Null 값 방지
                                }
                                "RECIPE_NM_KO" -> {
                                    parser.next()
                                    recipeName = parser.text ?: ""
                                    Log.e("recipeName", recipeName.ifEmpty { "NULL" })
                                }
                                "SUMRY" -> {
                                    parser.next()
                                    recipeInfo = parser.text ?: ""
                                    Log.e("recipeInfo", recipeInfo.ifEmpty { "NULL" })
                                }
                                "NATION_NM" -> {
                                    parser.next()
                                    recipeCookType = parser.text ?: ""
                                    Log.e("recipeCookType", recipeCookType.ifEmpty { "NULL" })
                                }
                                "TY_NM" -> {
                                    parser.next()
                                    recipeCookCategory = parser.text ?: ""
                                    Log.e("recipeCookCategory", recipeCookCategory.ifEmpty { "NULL" })
                                }
                                "COOKING_TIME" -> {
                                    parser.next()
                                    recipeTime = parser.text ?: ""
                                    Log.e("recipeTime", recipeTime.ifEmpty { "NULL" })
                                }
                                "CALORIE" -> {
                                    parser.next()
                                    recipeKcal = parser.text ?: ""
                                    Log.e("recipeKcal", recipeKcal.ifEmpty { "NULL" })
                                }
                                "QNT" -> {
                                    parser.next()
                                    recipePerson = parser.text ?: ""
                                    Log.e("recipePerson", recipePerson.ifEmpty { "NULL" })
                                }
                                "LEVEL_NM" -> {
                                    parser.next()
                                    recipeLevel = parser.text ?: ""
                                    Log.e("recipeLevel", recipeLevel.ifEmpty { "NULL" })
                                }
                                "IRDNT_CODE" -> {
                                    parser.next()
                                    recipeIngredientsCategory = parser.text ?: ""
                                    Log.e("recipeIngredientsCategory", recipeIngredientsCategory.ifEmpty { "NULL" })
                                }
                                "PC_NM" -> {
                                    parser.next()
                                    recipePrice = parser.text ?: ""
                                    Log.e("recipePrice", recipePrice.ifEmpty { "NULL" })
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            //Log.d("RESULT", "$recipeId/$recipeName/$recipeInfo/$recipeCookType/$recipeCookCategory/$recipeTime/$recipeKcal/$recipePerson/$recipeLevel/$recipeIngredientsCategory/$recipePrice" )
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
            /*lunItems.forEach { item ->
                val itemAge = "%.0f".format(item.lunAge.toDouble())
                val itemYear = item.solYear.toInt()
                val itemMonth = item.solMonth.toInt() - 1
                val itemDay = item.solDay.toInt()
                val drawableId = resources.getIdentifier("moon_shape$itemAge", "drawable", packageName)
                val drawable: Drawable? = ContextCompat.getDrawable(this@MainActivity, drawableId)
                calendar.set(itemYear, itemMonth, itemDay)
                events.add(EventDay(calendar.clone() as Calendar, drawable!!))
                calendar.add(Calendar.DATE, 1)
            }*/
        }
    }
}