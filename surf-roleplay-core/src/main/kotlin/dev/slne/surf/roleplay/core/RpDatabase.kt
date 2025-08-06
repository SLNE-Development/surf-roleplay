package dev.slne.surf.roleplay.core

import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.civilian.RpPlayerCivilianIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.police.RpPlayerPoliceIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityTable
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.nio.file.Path
import kotlin.io.path.div

class RpDatabase(val configPath: Path, val storagePath: Path = configPath / "storage") {

    private lateinit var dbManager: DatabaseManager
    private val mechanicTables = mutableObjectSetOf<Table>()

    fun registerMechanicTable(table: Table) = mechanicTables.add(table)

    suspend fun onLoad() {
        dbManager = DatabaseManager(configPath, storagePath)
        dbManager.databaseProvider.connect()

        newSuspendedTransaction {
            SchemaUtils.create(
                RpPlayerTable,
                RpPlayerCivilianIdentityTable,
                RpPlayerRescueServiceIdentityTable,
                RpPlayerPoliceIdentityTable,
                *mechanicTables.toTypedArray()
            )
        }
    }

    fun onDisable() {
        dbManager.databaseProvider.disconnect()
    }

}