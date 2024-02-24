package com.fobgochod.settings

import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableMutableProperty

/**
 * Password Persistence
 *
 * See [Persisting Sensitive Data](https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html)
 */
class PasswordProperty : ObservableMutableProperty<String> {

    override fun set(value: String) {
        val credentials = Credentials("password", value)
        PasswordSafe.instance.set(ZKSettings.instance.credentialAttributes(), credentials)
    }

    override fun afterChange(listener: (String) -> Unit) {
    }

    override fun afterChange(listener: (String) -> Unit, parentDisposable: Disposable) {
    }

    override fun get(): String {
        val pwd = PasswordSafe.instance.getPassword(ZKSettings.instance.credentialAttributes())
        return pwd ?: ""
    }
}