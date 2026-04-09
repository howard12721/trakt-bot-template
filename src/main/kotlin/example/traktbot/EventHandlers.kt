package example.traktbot

import jp.xhw.trakt.bot.TraktClient
import jp.xhw.trakt.bot.model.MessageCreated
import jp.xhw.trakt.bot.scope.reply

fun TraktClient.registerDefaultHandlers() {
    on<MessageCreated> { event ->
        if (event.message.content.trim() == "ping") {
            event.message.reply("pong")
        }
    }
}
