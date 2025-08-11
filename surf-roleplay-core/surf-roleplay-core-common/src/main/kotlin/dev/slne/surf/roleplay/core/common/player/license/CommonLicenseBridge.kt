package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.roleplay.api.common.player.license.InternalLicenseBridge
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import net.kyori.adventure.key.Key
import org.springframework.context.ApplicationContext

@OptIn(InternalRoleplayApi::class)
abstract class CommonLicenseBridge(
    private val context: ApplicationContext
) : InternalLicenseBridge {

    override val licenses
        get() = context.getBeansOfType(License::class.java)
            .values
            .toObjectSet()

    override fun <T : License> getLicense(license: Class<out T>) =
        context.getBean(license)

    override fun getLicenseByKey(key: Key) =
        licenses.find { it.key == key }

    override fun getLicenseByKeyOrThrow(key: Key) =
        getLicenseByKey(key) ?: error("License with key $key not found in the context.")
}