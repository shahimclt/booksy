package me.shahim.booksy.ui.bookshelf

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.GridBookItemBinding
import me.shahim.booksy.databinding.ListBookItemBinding

class BookGridQuickAdapter(data: MutableList<Book>?): BaseQuickAdapter<Book, BaseDataBindingHolder<GridBookItemBinding>>(
    R.layout.grid_book_item,data) {

    override fun convert(holder: BaseDataBindingHolder<GridBookItemBinding>, item: Book) {
        holder.dataBinding?.apply {
            book = item
            executePendingBindings()
        }
        Glide.with(context)
            .load(item.coverImage)
            .placeholder(R.drawable.im_book_cover_placeholder)
            .error(R.drawable.im_book_cover_placeholder)
            .into(holder.getView(R.id.book_cover))
    }

    class DiffCallback: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return true
        }
    }

}