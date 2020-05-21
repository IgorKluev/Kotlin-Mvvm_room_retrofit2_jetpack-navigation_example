package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.models.data.GenreModel
import com.example.movies.utils.utilities.listen
import kotlinx.android.synthetic.main.item_genre.view.*

class GenrePickerAdapter(var genreList: List<GenreModel>, var adapterOnClick: (List<Long>) -> Unit) : RecyclerView.Adapter<GenrePickerAdapter.GenrePickerViewHolder>()
{
    var selectedHash = HashMap<Int,Long>()
    var selectedGenres : List<Long> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenrePickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre_picker, parent, false)
        return GenrePickerViewHolder(view).listen { position, type ->

            checkSelectedItem(position)
        }
    }


    override fun getItemCount(): Int = genreList.size


    override fun onBindViewHolder(holder: GenrePickerViewHolder, position: Int)
    {
        holder.bind(genreList[position])
    }

    private fun checkSelectedItem(position : Int)
    {
        //is item new and hasnt been clicked
        if(isItemNotSelected(position))//checks if item is unClicke
        {
            onItemSelected(position)
        }
        else{//checked item has been pressed!
            onClickedItemPressed(position)
        }

        selectedGenres = selectedHash.values.toList()
        adapterOnClick(selectedGenres)
    }

    private fun isItemNotSelected(position : Int) : Boolean =  genreList?.get(position) != null && selectedHash.get(position) == null

    fun checkIfSelectedItemIsAll(position : Int) : Boolean = genreList?.get(position)?.gId == 0L && !genreList?.get(position)?.isClicked!!

    private fun setGenrePositonClick(position: Int,isClicked : Boolean){
        genreList?.get(position)?.isClicked = isClicked
    }

    private fun insertSelectedHash(position: Int)
    {
        selectedHash[position] = genreList?.get(position)?.gId!!
    }



    private fun cleanHashMap()
    {
        selectedHash = HashMap()
    }

    private fun checkAllItemIsClicked() : Boolean = isAllItem(0) && genreList?.get(0)?.isClicked!!

    private fun isAllItem(position: Int) : Boolean = genreList?.get(position)?.gId == 0L

    private fun unSelectAllKeys()
    {
        for(itemPos in selectedHash.keys)
        {
            setGenrePositonClick(itemPos,false)
            notifyItemChanged(itemPos);
        }

        cleanHashMap()
    }

    private fun removeItemClicked(position: Int){
        selectedHash.remove(position)
        setGenrePositonClick(position,false)
    }

    fun unSelectItemsAndInsertAllItem()
    {
        unSelectAllKeys()
        setGenrePositonClick(0,true)
        insertItemAndNotify(0)

    }

    private fun onItemSelected(position : Int)
    {
        if(checkIfSelectedItemIsAll(position))//determine what is checked
        {
            unSelectItemsAndInsertAllItem()
        }
        else
        {
            if(checkAllItemIsClicked())//checks if when unClicked item is All Clicked if is clicked will uperform unclick All item
            {
                //clears the first item from hashed and genrseList sets to false
                removeItemAndNotify(0)
            }

            //set clicked item to be clicked!
            insertItemAndNotify(position)
        }
    }

    private fun onClickedItemPressed(position : Int)
    {
        //check clicked item is All and ignore
        if(!isAllItem(position))
        {
            removeItemAndNotify(position)

            if(selectedHash.isEmpty())
            {
                insertItemAndNotify(0)
            }
        }
    }



    private fun removeItemAndNotify(position: Int)
    {
        removeItemClicked(position)
        notifyItemChanged(position)
    }

    private fun insertItemAndNotify(position: Int)
    {
        insertSelectedHash(position)
        setGenrePositonClick(position,true)
        notifyItemChanged(position);
    }

    inner class GenrePickerViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private val genreName = view.genreName

        fun bind(genre: GenreModel?) {
            if (genre != null) {
                genreName.text = genre.genreName
            } else {
                view.visibility = View.GONE
            }

            checkClicked(genre)
        }

        fun checkClicked(genre: GenreModel?) {
            if (genre?.isClicked!!) {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.blockClickedr))
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.textColor))
            }
        }
    }

}