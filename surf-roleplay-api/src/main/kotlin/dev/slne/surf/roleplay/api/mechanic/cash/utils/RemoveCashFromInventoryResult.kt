package dev.slne.surf.roleplay.api.mechanic.cash.utils

enum class RemoveCashFromInventoryResult {
    /**
     * Indicates that the cash was successfully added to the player's inventory.
     */
    SUCCESS,

    /**
     * Indicates that the player is not online, and therefore the cash cannot be added.
     */
    PLAYER_NOT_ONLINE,

    /**
     * Indicates that the player does not have enough cash in their inventory to remove the specified amount.
     */
    NOT_ENOUGH_CASH,

    /**
     * Indicates that the operation failed for an unknown reason.
     */
    UNKNOWN
}