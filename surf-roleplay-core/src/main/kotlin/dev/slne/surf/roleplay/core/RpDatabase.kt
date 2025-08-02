package dev.slne.surf.roleplay.core

import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.nio.file.Path
import kotlin.io.path.div

class RpDatabase(val configPath: Path, val storagePath: Path = configPath / "storage") {

    private lateinit var dbManager: DatabaseManager

    suspend fun onLoad() {
        dbManager = DatabaseManager(configPath, storagePath)
        dbManager.databaseProvider.connect()

        newSuspendedTransaction {
            SchemaUtils.create(
                RpPlayerTable
            )
        }
    }

    fun onDisable() {
        dbManager.databaseProvider.disconnect()
    }

}