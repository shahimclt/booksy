package me.shahim.booksy.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import me.shahim.booksy.R
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.databinding.ListBookItemBinding

class BookListQuickAdapter(data: MutableList<Book>?): BaseQuickAdapter<Book, BaseDataBindingHolder<ListBookItemBinding>>(
    R.layout.list_book_item,data) {

    override fun convert(holder: BaseDataBindingHolder<ListBookItemBinding>, item: Book) {
        holder.dataBinding?.apply {
            book = item
            executePendingBindings()
        }
        Glide.with(context)
            .load(item.coverImage)
            .placeholder(R.drawable.im_book_cover_placeholder)
            .error(R.drawable.im_book_cover_placeholder)
            .into(holder.getView(R.id.book_cover))
        holder.setText(R.id.extraDetails,context.getString(R.string.book_list_extra_detail,item.pages,item.year))
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