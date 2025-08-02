package dev.slne.surf.roleplay.mechanic.mechanics.license

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object LicenseService {

    suspend fun findAllPlayerLicenses() = newSuspendedTransaction(Dispatchers.IO) {
        
    }

}