package com.test.cleanarchitecture.domain.use_case


import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import javax.inject.Inject

class SaveContactsUseCase @Inject constructor(
    private val repository: ContactsDaoRepository
) {
    suspend operator fun invoke(contactList: List<DbContact>): Resource<Any> {
        return try {
            repository.addAll(contactList)
            Resource.Success()
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        }
    }
}