package com.test.cleanarchitecture.data.repository

import com.test.cleanarchitecture.db.contacts.ContactsDao
import com.test.cleanarchitecture.db.contacts.DbContact
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import javax.inject.Inject

class ContactsDaoRepositoryImpl @Inject constructor(private val dao: ContactsDao): ContactsDaoRepository {

    override suspend fun getContacts(): List<DbContact> {
      return dao.getContacts()
    }

    override suspend fun update(contact: DbContact) {
        dao.update(contact)
    }

    override suspend fun addAll(contact: List<DbContact>) {
        dao.addAll(contact)
    }

    override suspend fun deleteById(contactId: Int) {
        dao.deleteById(contactId)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun getContactById(id: Int): DbContact {
        return dao.getContactById(id)
    }
}