package dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight

enum class WeightMap(val weight: Double, val speed: Double, val cancelsJump: Boolean = false) {
    HEAVY(3000.0, 0.00256, true),
    HIGH(2000.0, 0.0125, true),
    MEDIUM(1500.0, 0.025),
    LOW(1000.0, 0.05),
    NORMAL(500.0, 0.1)
}