package com.project.damda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeRecommendRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<RecipeRecommendRecyclerAdapter.ViewHolder>() {
    private var recipeClickListener: OnItemClickListener? = null
    var datas = mutableListOf<RecipeRecommendItem>()

    interface OnItemClickListener {
        fun onItemClick(v: View?)
        fun onLongClick(v: View?)
    }

    fun setOnRecipeClickListener(listener: OnItemClickListener) {
        this.recipeClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommend_recipe_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(datas[position])

    override fun getItemCount(): Int = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var recommendRecipeNameTv: TextView = itemView.findViewById(R.id.rvItem_recommendRecipeNameTv)
        private var recommendRecipeDescriptionTv: TextView = itemView.findViewById(R.id.rvItem_recommendRecipeDescriptionTv)
        private var recommendRecipeTimeTv: TextView = itemView.findViewById(R.id.rvItem_recommendRecipeTimeTv)
        private var recommendRecipeDifficultyTv: TextView = itemView.findViewById(R.id.rvItem_recommendRecipeDifficultyTv)

        fun bind(item: RecipeRecommendItem) {
            recommendRecipeNameTv.text = item.recommendRecipeName
            recommendRecipeDescriptionTv.text = item.recommendRecipeDescription
            recommendRecipeTimeTv.text = item.recommendRecipeTime
            recommendRecipeDifficultyTv.text = item.recommendRecipeDifficulty

            itemView.setOnClickListener { view ->
                recipeClickListener?.onItemClick(view)
            }

            itemView.setOnLongClickListener { view ->
                recipeClickListener?.onLongClick(view)
                true
            }
        }
    }
}