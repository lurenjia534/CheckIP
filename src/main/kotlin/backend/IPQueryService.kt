package backend

import com.google.gson.GsonBuilder
import io.ipinfo.api.IPinfo
import io.ipinfo.api.cache.SimpleCache
import io.ipinfo.api.errors.RateLimitedException
import io.ipinfo.api.model.IPResponse
import java.time.Duration

class IPQueryService(private val token: String) {

    fun query(ip: String): String {
        val ipinfo = IPinfo.Builder()
            .setToken(token)
            .setCache(SimpleCache(Duration.ofHours(1)))
            .build()

        val gson = GsonBuilder().setPrettyPrinting().create()

        return try {
            val ipResponse: IPResponse = ipinfo.lookupIP(ip)
            // 直接将ipResponse对象转换为JSON字符串返回
            gson.toJson(ipResponse)
        } catch (e: RateLimitedException) {
            // 将异常信息转换为JSON格式返回
            gson.toJson(mapOf("error" to e.toString()))
        }
    }
}
