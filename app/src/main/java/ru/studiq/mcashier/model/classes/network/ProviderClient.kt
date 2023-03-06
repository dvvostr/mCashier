package ru.studiq.mcashier.model.classes.network


import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import ru.studiq.mcashier.interfaces.IProviderClientListener
import java.util.concurrent.TimeUnit


class ProviderClient {
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    /*
    public val client: OkHttpClient

    private var listener: IProviderClientListener? = null

    init {
        this.client = this.getUnsafeOkHttpClient()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })
        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()
    }
    public fun addProviderListener(listener: IProviderClientListener) {
        this.listener = listener
    }

 */
    public fun fetchJSON(url: String, json: String, listener: IProviderClientListener) {
        println("******* FETCH JSON *******\r\n $json")
//        var client = OkHttpClient()
        var client = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val body: RequestBody = json.toRequestBody(JSON) // new
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string()
                    println("******* JSON Response *******\r\n $body")
                    val gson = GsonBuilder().create()
                    val data = gson.fromJson(body, ProviderData::class.java)
                    val providerResponse = ProviderResponse(response.code, response.header("x-result")?.toInt(), response.header("x-message"))
                    if (response.code == 200) {
                        listener.onSuccess(providerResponse, data.header, data.body)
                    } else {
                        listener.onError(providerResponse, data.header)
                    }
                } catch (e: Exception) {
                    listener.onError(ProviderResponse(-1, -1, e.message), ProviderDataHeader("", -1, 0, "", e.message ?: "", ""))
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                listener.onError(ProviderResponse(-1, -1, e.message), ProviderDataHeader("", -1, 0, "", e.message ?: "", ""))
                println("******* JSON Failure *******")
            }
        })
    }
}