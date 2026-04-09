package example.traktbot

import kotlin.uuid.Uuid

data class BotConfig(
    val token: String,
    val botId: Uuid?,
) {
    companion object {
        fun fromEnvironment(environment: Map<String, String> = System.getenv()): BotConfig {
            val token = environment.required("TRAQ_BOT_TOKEN")
            val botId =
                environment.optional("TRAQ_BOT_ID")?.let { rawBotId ->
                    runCatching { Uuid.parse(rawBotId) }.getOrElse { cause ->
                        throw IllegalArgumentException("TRAQ_BOT_ID must be a valid UUID.", cause)
                    }
                }

            return BotConfig(token = token, botId = botId)
        }

        private fun Map<String, String>.required(key: String): String =
            this[key]?.takeIf(String::isNotBlank) ?: throw IllegalStateException("$key is required.")

        private fun Map<String, String>.optional(key: String): String? = this[key]?.takeIf(String::isNotBlank)
    }
}
