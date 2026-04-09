package example.traktbot

import jp.xhw.trakt.bot.trakt

suspend fun main() {
    val config = BotConfig.fromEnvironment()

    val client =
        trakt(
            token = config.token,
            botId = config.botId,
        ) {
            registerDefaultHandlers()
        }

    try {
        client.start()
    } finally {
        client.stop()
    }
}
