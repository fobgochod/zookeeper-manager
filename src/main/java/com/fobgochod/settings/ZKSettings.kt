package com.fobgochod.settings

import com.fobgochod.constant.ZKConstant
import com.fobgochod.util.StringUtil
import com.fobgochod.util.ZKBundle
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
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

    var name: String
        get() = state.name ?: ZKConstant.EMPTY
        set(value) {
            state.name = value
        }

    var charset: String
        get() = state.charset ?: StandardCharsets.UTF_8.name()
        set(value) {
            state.charset = StringUtil.charset(value).name()
        }

    var adminServer: String
        get() = state.adminServer ?: ZKConstant.EMPTY
        set(value) {
            state.adminServer = value
        }

    var host: String
        get() = state.host ?: ZKConstant.EMPTY
        set(value) {
            state.host = value
        }

    var port: Int
        get() = state.port
        set(value) {
            state.port = value
        }

    var path: String
        get() = state.path ?: ZKConstant.EMPTY
        set(value) {
            state.path = value
        }

    var sessionTimeout: Int
        get() = state.sessionTimeout
        set(value) {
            state.sessionTimeout = value
        }

    var enableSasl: Boolean
        get() = state.enableSasl
        set(value) {
            state.enableSasl = value
        }

    var username: String
        get() = state.username ?: ZKConstant.EMPTY
        set(value) {
            state.username = value
        }

    /**
     *  [Persisting Sensitive Data](https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html)
     */
    var password: String
        get() {
            val pwd = PasswordSafe.instance.getPassword(credentialAttributes())
            return pwd ?: ZKConstant.EMPTY
        }
        set(value) {
            val credentials = Credentials("password", value)
            PasswordSafe.instance.set(credentialAttributes(), credentials)
        }

    private fun credentialAttributes(): CredentialAttributes {
        return CredentialAttributes(ZKBundle.message("plugin.name"), "password")
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
        val address = if (host.contains(ZKConstant.COLON)) {
            host
        } else if (host.contains(ZKConstant.COMMA)) {
            host.split(ZKConstant.COMMA).joinToString(",") { "$it:$port" }
        } else {
            "$host:$port"
        }
        return address + StringUtil.getPath(path)
    }

    /**
     * 根据连接字符串获取一个name
     *
     * input:  192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181/hello
     * output: 192.168.10.10:2181/hello
     */
    fun getTitle(): String {
        if (StringUtil.isNotEmpty(name)) {
            return name
        }
        val connectString = connectString()
        if (connectString.contains(ZKConstant.COMMA)) {
            return connectString.split(ZKConstant.COMMA)[0] + StringUtil.getPath(path)
        }
        return connectString
    }

    fun adminServerUrl(): String {
        val singleHost = connectString().split(ZKConstant.COLON)[0]
        return adminServer.replace(ZKConstant.LOCALHOST, singleHost)
    }

    fun getVersion(): String {
        try {
            val client: HttpClient = HttpClient.newBuilder()
                .connectTimeout(ZKConstant.ADMIN_SERVER_TIMEOUT)
                .build()
            val request: HttpRequest = HttpRequest.newBuilder()
                .uri(URI.create(adminServerUrl() + ZKConstant.COMMAND_ENVIRONMENT))
                .timeout(ZKConstant.ADMIN_SERVER_TIMEOUT)
                .GET()
                .build()
            val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
            val json = JsonParser.parseString(response.body())
            if (json is JsonObject) {
                val version = json.get(ZKConstant.ZOOKEEPER_VERSION_KEY).asString.split(ZKConstant.HYPHEN)[0]
                return "(v$version)"
            }
        } catch (_: Exception) {
        }
        return ZKConstant.EMPTY
    }

    companion object {

        const val NAME = "ZookeeperManager"
        const val STORAGES = "zookeeper.manager.xml"

        @JvmStatic
        val instance: ZKSettings
            get() = service()
    }
}
