package dev.slne.surf.roleplay.server

import dev.slne.surf.cloud.api.server.exposed.migration.generateSimpleExposedMigration
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun main() {
    generateSimpleExposedMigration(
        scriptName = "V1__create_initial_tables",
    )
}