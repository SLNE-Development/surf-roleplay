package dev.slne.surf.job.api.job

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike

enum class JobCategory(val sorting: Int) : ComponentLike {

    /**
     * Neutral jobs that are not related to any specific gang or faction
     */
    NEUTRAL(1) {
        override fun asComponent() = buildText {
            primary("Neutral")
        }

        override fun asDescriptionComponent() = buildText {
            spacer("Neutrale Jobs sind wichtig für die Wirtschaft und das tägliche Leben.")
        }
    },

    /**
     * Jobs that are related to the state, such as police or government jobs
     */
    STATE(2) {
        override fun asComponent() = buildText {
            primary("Staat")
        }

        override fun asDescriptionComponent() = buildText {
            spacer("Staatliche Jobs sind für die Aufrechterhaltung der Ordnung und Sicherheit zuständig. Sie arbeiten oft eng mit der Polizei und anderen staatlichen Institutionen zusammen.")
        }
    },

    /**
     * Jobs that are related to the seller, such as selling items or services
     */
    SELLER(3) {
        override fun asComponent() = buildText {
            primary("Verkäufer")
        }

        override fun asDescriptionComponent() = buildText {
            spacer("Verkäufer sind für den Verkauf von Waren und Dienstleistungen zuständig. Sie arbeiten oft in Geschäften oder auf Märkten.")
        }
    },

    /**
     * Jobs that are related to a specific gang or faction
     */
    GANG(4) {
        override fun asComponent() = buildText {
            primary("Gang")
        }

        override fun asDescriptionComponent() = buildText {
            spacer("Die Gangs versetzen den Staat in Angst und Schrecken. Sie sind für ihre kriminellen Machenschaften bekannt und haben oft ihre eigenen Regeln und Gesetze.")
        }
    };

    abstract fun asDescriptionComponent(): Component

}