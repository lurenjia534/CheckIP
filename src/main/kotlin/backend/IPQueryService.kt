package backend

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ipinfo.api.IPinfo
import io.ipinfo.api.cache.SimpleCache
import io.ipinfo.api.errors.RateLimitedException
import io.ipinfo.api.model.IPResponse
import java.time.Duration

class IPQueryService {

    fun query(ip: String): String {
        val ipinfo = IPinfo.Builder()
            .setToken("")
            .setCache(SimpleCache(Duration.ofHours(1)))
            .build()

        val gson = GsonBuilder().setPrettyPrinting().create()

        return try {
            val ipResponse: IPResponse = ipinfo.lookupIP(ip)
            val ipResponseString = gson.toJson(ipResponse)
            gson.toJson(ipResponseString)
        } catch (e: RateLimitedException) {
            gson.toJson(e)
        }
    }
}