package com.fobgochod.settings

import com.fobgochod.constant.ZKConstant
import com.fobgochod.util.StringUtil
import com.fobgochod.util.ZKBundle
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import java.nio.charset.StandardCharsets

/**
 * SSL: Secure Sockets Layer 安全套接字协议
 * SASL: Simple Authentication and Security Layer
 * <p>
 * zookeeper configuration persistence
 *
 * @author fobgochod
 * @since 1.0
 */
@State(name = ZKSettings.NAME, storages = [Storage(ZKSettings.STORAGES)], category = SettingsCategory.TOOLS)
class ZKSettings : PersistentStateComponent<ZKSettingsState> {

    private val state = ZKSettingsState()

    override fun getState() = state

    override fun loadState(state: ZKSettingsState) = XmlSerializerUtil.copyBean(state, this.state)

    var hostRows: MutableList<String>
        get() = state.hostRows
        set(value) {
            state.hostRows = value
        }

    var name: String
        get() = state.name ?: ""
        set(value) {
            state.name = value
        }

    var charset: String
        get() = state.charset ?: StandardCharsets.UTF_8.name()
        set(value) {
            state.charset = StringUtil.charset(value).name()
        }

    var host: String
        get() = state.host ?: ""
        set(value) {
            state.host = value
        }

    var port: Int
        get() = state.port
        set(value) {
            state.port = value
        }

    var path: String
        get() = state.path ?: ""
        set(value) {
            state.path = value
        }

    var blockUntilConnected: Int
        get() = state.blockUntilConnected
        set(value) {
            state.blockUntilConnected = value
        }

    var saslClientEnabled: Boolean
        get() = state.saslClientEnabled
        set(value) {
            state.saslClientEnabled = value
        }

    var username: String
        get() = state.username ?: ""
        set(value) {
            state.username = value
        }

    /**
     *  [Persisting Sensitive Data](https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html)
     */
    var password: String
        get() {
            val pwd = PasswordSafe.instance.getPassword(credentialAttributes())
            return pwd ?: ""
        }
        set(value) {
            val credentials = Credentials("password", value)
            PasswordSafe.instance.set(credentialAttributes(), credentials)
        }

    fun credentialAttributes(): CredentialAttributes {
        return CredentialAttributes(generateServiceName(ZKBundle.message("plugin.name"), "password"))
    }

    /**
     * example1:
     * input:  host=192.168.10.10:2181  port=
     * output: 192.168.10.10:2181
     *
     *
     * example2:
     * input:  host=192.168.10.10,192.168.10.11,192.168.10.10  port=2181
     * output: 192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181
     *
     *
     * example3:
     * input:  host=192.168.10.10  port=2181
     * output: 192.168.10.10:2181
     *
     * @return connectString
     */
    fun connectString(): String {
        return connectString(host, port, path)
    }

    fun connectString(host: String, port: Int, path: String?): String {
        val address = if (host.contains(":")) {
            host
        } else if (host.contains(",")) {
            host.replace("[\\s,]+".toRegex(), ":$port,")
        } else {
            "$host:$port"
        }
        return address + StringUtil.getPath(path)
    }

    /**
     * 根据连接字符串获取一个name
     *
     *
     * input:  192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181/hello
     * output: 192.168.10.10:2181/hello
     */
    fun getTitle(): String {
        if (StringUtil.isNotEmpty(name)) {
            return name
        }
        val connectString = connectString()
        if (connectString.contains(",")) {
            return connectString.substring(
                0, connectString.indexOf(",")
            ) + connectString.substring(connectString.indexOf(ZKConstant.SLASH))
        }
        return connectString
    }

    companion object {

        const val NAME = "ZookeeperManager"
        const val STORAGES = "zookeeper.manager.xml"

        @JvmStatic
        val instance: ZKSettings
            get() = service()
    }
}
