package com.test.cleanarchitecture.domain.use_case

import com.test.cleanarchitecture.common.Resource
import com.test.cleanarchitecture.data.remote.dto.toDbContacts
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import com.test.cleanarchitecture.domain.repository.ContactsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetContactsFromApiUseCase @Inject constructor(
    private val repository: ContactsRepository,
    private val daoRepository: ContactsDaoRepository
) {
    suspend operator fun invoke(): Resource<List<DbContact>> {
        return try {
            daoRepository.deleteAll()
            val data = repository.getContacts(30)
            Resource.Success(data.toDbContacts())
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "An unexpected error occured")
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Couldn't reach server")
        }
    }
}