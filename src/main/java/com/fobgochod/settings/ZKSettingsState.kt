package com.fobgochod.settings

import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection
import java.nio.charset.StandardCharsets

class ZKSettingsState : BaseState() {

    @get:Tag("HOST_ROWS")
    @get:XCollection(style = XCollection.Style.v2)
    var hostRows by list<String>()

    @get:OptionTag("NAME")
    var name by string("")

    @get:OptionTag("CHARSET")
    var charset by string(StandardCharsets.UTF_8.name())

    @get:OptionTag("HOST")
    var host by string("127.0.0.1")

    @get:OptionTag("PORT")
    var port by property(2181)

    @get:OptionTag("PATH")
    var path by string("")

    @get:OptionTag("BLOCK_UNTIL_CONNECTED")
    var blockUntilConnected by property(6 * 1000)

    @get:OptionTag("SASL_CLIENT_ENABLED")
    var saslClientEnabled by property(false)

    @get:OptionTag("USERNAME")
    var username by string("")
}

