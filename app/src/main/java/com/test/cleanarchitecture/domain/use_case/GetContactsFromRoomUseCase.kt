package com.test.cleanarchitecture.domain.use_case

import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import retrofit2.HttpException
import javax.inject.Inject

class GetContactsFromRoomUseCase @Inject constructor(
    private val repository: ContactsDaoRepository
) {
    suspend operator fun invoke(): Resource<List<DbContact>> {
       return try {
            val data = repository.getContacts()
            Resource.Success(data)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }
}