package ru.studiq.mcashier.interfaces

import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.ProviderDataHeader
import ru.studiq.mcashier.model.classes.network.ProviderResponse

interface IProviderClientListener {
    fun onSuccess(response: ProviderResponse, header: ProviderDataHeader, data: ProviderDataBody)
    fun onError(response: ProviderResponse, header: ProviderDataHeader)
}