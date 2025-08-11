package dev.slne.surf.roleplay.core.common.mechanics.idcard

import dev.slne.surf.roleplay.api.common.mechanic.idcard.IdCardMechanic
import dev.slne.surf.roleplay.core.common.mechanics.CommonMechanic

abstract class IdCardCommonMechanic : CommonMechanic(
    name = "IdCardMechanic",
), IdCardMechanic