package com.example.safetycheck.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.safetycheck.databinding.CustomItemBinding
import com.example.safetycheck.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ContactAdapter(private var context: Context, private var list: ArrayList<Item>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth

    class ViewHolder(binding: CustomItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contactName = binding.tvNameItem
        val relation = binding.tvRelationItem
        val number = binding.tvNumberItem
        val card = binding.card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseAuth = FirebaseAuth.getInstance()

        val contact = list[position]
        holder.contactName.text = contact.name
        holder.relation.text = contact.relation
        holder.number.text = contact.number
        holder.card.setOnLongClickListener {
            val alert = AlertDialog.Builder(context)
            alert.setMessage("You want to remove the contact from emergency contacts?")
            alert.setTitle("Remove Contact!!")
                .setPositiveButton("Yes, Delete!!") { _, _ ->
                    //delete the contact.
                    FirebaseDatabase.getInstance().reference.child("UsersSafety")
                        .child(firebaseAuth.currentUser!!.uid).child(contact.number!!).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                Toast.makeText(context, "Contact Removed.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .setNegativeButton("No") { _, _ -> }
                .create()
                .show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}