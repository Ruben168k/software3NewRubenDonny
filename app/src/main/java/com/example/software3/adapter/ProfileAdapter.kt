package com.example.software3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.software3.R
import com.example.software3.model.Profile

class ProfileAdapter(private val profiles: List<Profile>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Verbind de Views hier, bijv. TextViews voor voornaam, achternaam, etc.
        val firstName: TextView = itemView.findViewById(R.id.firstNameTextView)
        val lastName: TextView = itemView.findViewById(R.id.lastNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_item, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentProfile = profiles[position]

        // Controleer de visability voordat je gegevens toont
        if (currentProfile.visability) {
            holder.itemView.visibility = View.VISIBLE
            holder.firstName.visibility = View.VISIBLE
            holder.lastName.visibility = View.VISIBLE
            holder.firstName.text = currentProfile.firstname
            holder.lastName.text = currentProfile.lastname
        } else {
            // Verberg de View als de visability niet waar is
            holder.itemView.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return profiles.size
    }
}
