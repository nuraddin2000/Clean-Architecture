package com.test.cleanarchitecture.ui.contactslist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.cleanarchitecture.databinding.ItemContactListBinding
import com.test.cleanarchitecture.db.contacts.DbContact

class ContactAdapter (
    private val context: Activity,
    private val onItemClicked: ItemClick
) : RecyclerView.Adapter<ViewHolder>() {

    var items = mutableListOf(DbContact())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemContactListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            Picasso.get().load(item.photo).into(userImage)
            firstName.text = items[position].firstName
            lastName.text = items[position].lastName
            email.text = items[position].email
            root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder (val binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root)

typealias ItemClick = (DbContact) -> Unit