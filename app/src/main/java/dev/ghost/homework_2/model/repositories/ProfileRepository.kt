package dev.ghost.homework_2.model.repositories

import dev.ghost.homework_2.model.database.ProfileDao
import dev.ghost.homework_2.model.entities.Profile
import dev.ghost.homework_2.model.models.UserResponseContent
import dev.ghost.homework_2.model.network.ApiService

class ProfileRepository(
    private val profileDao: ProfileDao,
    private val apiService: ApiService
) {
    fun getUserProfile(userId: Int) = profileDao.getUserProfile(userId)

    fun refresh() = apiService.getUserInfo()

    fun add(profile: Profile, isProfile: Boolean = true) {
        if (isProfile)
            profileDao.addProfile(profile)
        else
            profileDao.addUser(profile)
    }

    fun delete(profile: Profile) {
        profileDao.delete(profile)
    }


    fun addUser(userResponse: UserResponseContent) {
        add(
            Profile(
                id = userResponse.id,
                firstName = userResponse.firstName,
                lastName = userResponse.lastName,
                photo50 = userResponse.photo50,
                domain = userResponse.domain,
                status = userResponse.status,
                photoMax = userResponse.photoMax,
                about = userResponse.about,
                bdate = userResponse.bdate,
                city = userResponse.city.title,
                country = userResponse.country.title,
                career = userResponse.career.firstOrNull()?.company
                    ?: ("" + userResponse.career.firstOrNull()?.groupId),
                education = userResponse.universityName,
                followersCount = userResponse.followersCount,
                lastSeen = userResponse.lastSeen.time
            ),
            isProfile = false
        )
    }

    fun updateReceivedProfiles(profiles: List<UserResponseContent>) {
        profiles.forEach {
            add(
                Profile(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    photo50 = it.photo50
                ),
                isProfile = true
            )
        }
    }
}