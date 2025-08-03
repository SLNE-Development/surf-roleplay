package dev.slne.surf.roleplay.api.mechanic.cash.utils

enum class AddCashToInventoryResult {
    /**
     * Indicates that the cash was successfully added to the player's inventory.
     */
    SUCCESS,

    /**
     * Indicates that the player is not online, and therefore the cash cannot be added.
     */
    PLAYER_NOT_ONLINE,

    /**
     * Indicates that the cash amount is not calculable, possibly due to an invalid or unsupported operation.
     */
    CASH_NOT_CALCULABLE,

    /**
     * Indicates that there is not enough space in the player's inventory to add the cash.
     */
    NOT_ENOUGH_SPACE;
}