package dev.ghost.homework_2.model.repositories

import dev.ghost.homework_2.model.database.GroupDao
import dev.ghost.homework_2.model.entities.Group

class GroupRepository(
    private val groupDao: GroupDao
) {
    private val groups = groupDao.getAllGroups()

    fun getGroups() = groups

    fun add(group: Group) {
        groupDao.add(group)
    }

    fun deleteGroup(group: Group) {
        groupDao.delete(group)
    }

    fun updateReceivedGroups(groups: List<dev.ghost.homework_2.model.models.Group>) {
        groups.forEach {
            add(
                Group(
                    id = -it.id,
                    name = it.name,
                    photo50 = it.photo50
                )
            )
        }
    }
}