package com.test.cleanarchitecture.domain.use_case

import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetContactUseCase @Inject constructor(
    private val repository: ContactsDaoRepository
) {
    operator fun invoke(contactId:Int): Flow<Resource<DbContact>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.getContactById(contactId)
            emit(Resource.Success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}