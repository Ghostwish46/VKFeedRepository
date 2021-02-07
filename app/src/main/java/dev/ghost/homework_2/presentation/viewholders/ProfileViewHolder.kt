package dev.ghost.homework_2.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Profile
import kotlinx.android.synthetic.main.item_profile.view.*

open class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        profile: Profile
    ) = with(itemView) {

        profileName.text = checkData(profile.getFullName())
        profileFollowers.text = "${profile.followersCount} followers"
        profileAbout.text = checkData(profile.about)
        profileCareer.text = checkData(profile.career)
        profileBirthday.text = checkData(profile.bdate)
        profileEducation.text = checkData(profile.education)
        profileHome.text = checkData(profile.getHomeFullName())
        profileLastSeen.text = "last seen " + profile.getLastSeenText()
        profileStatus.text = checkData(profile.status)

        Glide.with(profileAvatar)
            .load(profile.photo50)
            .error(R.drawable.placeholder_avatar)
            .transform(CenterCrop(), RoundedCorners(100))
            .into(profileAvatar)
    }

    private fun checkData(data: String?): String {
        return if (data.isNullOrEmpty())
            "Not specified"
        else
            data
    }
}