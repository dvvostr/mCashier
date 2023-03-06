package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import com.google.gson.Gson
import ru.studiq.mcashier.R
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.interfaces.IProviderClientListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.network.*

open class CustomProviderData: java.io.Serializable {
    companion object {
        open fun  load(sender: Context?, request: ProviderRequest, param: String, listener: ICustomListActivityListener) {
            CustomProviderData.Companion.load(sender, request, listOf(), listOf(param), listener)
        }
        open fun  load(sender: Context?, request: ProviderRequest, constrictor: List<String>, params: List<String>, listener: ICustomListActivityListener) {
            val provider = ProviderClient()
            request.header.id = java.util.UUID.randomUUID().toString()
            var url = Settings.Application.Network.connectionURL
            params.forEach(){
                request.body.methodParams = request.body.methodParams.plus(ProviderRequestMethodParam(it))
            }
            constrictor.forEach() {
                request.body.constructorParams = request.body.constructorParams.plus(ProviderRequestMethodParam(it))
            }
            try {
                provider.fetchJSON(url, Gson().toJson(request), object : IProviderClientListener {
                        override fun onSuccess(response: ProviderResponse, header: ProviderDataHeader, data: ProviderDataBody) {
                            if (header.id == request.header.id && header.code >= 0)
                                if (data.type == ru.studiq.mcashier.model.classes.network.ProviderDataBodyType.normal.ordinal) {
                                    (data.data as? List<Any?>)?.let {
                                        if (it.size > 0)
                                            listener.onSuccess(sender, header.code, header.msg, data)
                                        else
                                            listener.onError(sender, -1, sender?.getString(R.string.err_data_empty) ?: "")
                                    } ?: run {
                                        listener.onError(sender, -1, sender?.getString(R.string.err_data_empty) ?: ""
                                        )
                                    }
                                } else if (data.type == ProviderDataBodyType.axapta.ordinal) {
                                    listener.onSuccess(sender, header.code, header.msg, data)
                                } else {
                                    listener.onError(sender, -1, sender?.getString(R.string.error_unassigned) ?: "")
                                }
                            else {
                                listener.onError(sender, header.code, header.msg)
                            }
                        }
                        override fun onError(response: ProviderResponse, header: ProviderDataHeader) {
                            listener.onError(sender, header.code, header.msg)
                        }
                    }
                )
            } catch (ex: Exception) {
                listener.onError(sender, -1, ex.localizedMessage ?: sender?.getString(R.string.error_unassigned) ?: "")
            }
        }
    }
}
