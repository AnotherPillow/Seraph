package club.maxstats.seraph.util

import ApiKeyThrottleException
import HypixelAPIException
import club.maxstats.seraph.Main
import getPlayerFromUUID
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import player.Player
import java.io.InputStream
import java.time.Duration
import java.util.*
import kotlin.math.abs

private val playerCache = SimpleCache<UUID, Player>(500, Duration.ofMinutes(5))
private val safeJson = Json { ignoreUnknownKeys = true }
fun String.asResource(): InputStream = Main::class.java.classLoader.getResourceAsStream(this)
    ?: throw IllegalStateException("Failed to fetch resource $this")
fun now() = System.currentTimeMillis()
fun InputStream.readToString() = this.readBytes().toString(Charsets.UTF_8)
fun String.deserializeLocraw() {
    locrawInfo = safeJson.decodeFromString<LocrawInfo>(this)
}
infix fun Float.isCloseTo(other: Float) = abs(this - other) < 0.0001f
val mc = Minecraft.getMinecraft()
fun getScaledResolution(): ScaledResolution {
    return ScaledResolution(mc)
}
val isOnHypixel: Boolean
        get() =
            if (mc.theWorld != null && !mc.isSingleplayer)
                mc.currentServerData.serverIP.lowercase(Locale.getDefault()).contains("hypixel")
            else false
fun calculateScaleFactor(mc: Minecraft): Int {
    val displayWidth = mc.displayWidth
    val displayHeight = mc.displayHeight

    var scaleFactor = 1
    val isUnicode = mc.isUnicode

    val guiScale = mc.gameSettings.guiScale
    if (guiScale == 0) scaleFactor = 1000

    while (scaleFactor < guiScale && displayWidth / (scaleFactor + 1) >= 320 && displayHeight / (scaleFactor + 1) >= 240) ++scaleFactor
    if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) --scaleFactor

    return scaleFactor
}
private var keyThrottleStart: Long = now()
suspend fun UUID.getPlayerData() : Player? {
    return playerCache.get(this)
        ?: try {
            if (now() - keyThrottleStart >= 70 * 1000)
                getPlayerFromUUID(this.toString(), hypixelApiKey).also { playerCache.put(this, it) }
            else
                return null
        } catch (ex: HypixelAPIException) {
            if (ex is ApiKeyThrottleException)
                keyThrottleStart = now()
            return null
        }
}
var hypixelApiKey : String = ""