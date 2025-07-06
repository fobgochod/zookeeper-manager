package com.fobgochod.settings

import com.fobgochod.constant.ZKConstant
import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.OptionTag
import java.nio.charset.StandardCharsets

class ZKSettingsState : BaseState() {

    @get:OptionTag("NAME")
    var name by string()

    @get:OptionTag("CHARSET")
    var charset by string(StandardCharsets.UTF_8.name())

    @get:OptionTag("ADMIN_SERVER")
    var adminServer by string(ZKConstant.COMMAND_URL)

    @get:OptionTag("HOST")
    var host by string()

    @get:OptionTag("PORT")
    var port by property(ZKConstant.DEFAULT_CLIENT_PORT)

    @get:OptionTag("PATH")
    var path by string()

    @get:OptionTag("SESSION_TIMEOUT")
    var sessionTimeout by property(ZKConstant.DEFAULT_SESSION_TIMEOUT)

    @get:OptionTag("ENABLE_SASL")
    var enableSasl by property(false)

    @get:OptionTag("USERNAME")
    var username by string()
}

