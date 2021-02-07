package dev.ghost.homework_2.di

import dagger.Module
import dagger.Provides
import dev.ghost.homework_2.model.database.*
import dev.ghost.homework_2.model.network.ApiService
import dev.ghost.homework_2.model.repositories.*
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    @Singleton
    fun providePhotoRepository(photoDao: PhotoDao) =
        PhotoRepository(photoDao)

    @Provides
    @Singleton
    fun provideGroupRepository(groupDao: GroupDao) =
        GroupRepository(groupDao)

    @Provides
    @Singleton
    fun providePostsRepository(postDao: PostDao, apiService: ApiService) =
        PostsRepository(postDao, apiService)

    @Provides
    @Singleton
    fun provideProfileRepository(profileDao: ProfileDao, apiService: ApiService) =
        ProfileRepository(profileDao, apiService)

    @Provides
    @Singleton
    fun provideCommentRepository(commentDao: CommentDao, apiService: ApiService) =
        CommentRepository(commentDao, apiService)
}
