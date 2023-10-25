package jamule.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import jamule.ec.packet.PacketParser
import jamule.ec.packet.PacketParserTest
import jamule.ec.tag.TagParser
import jamule.ec.tag.special.PartFileTag
import jamule.ec.tag.special.SharedFileTag
import jamule.model.FileStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream

@OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
class DownloadQueueResponseTest : FunSpec({
    val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)
    val parser = PacketParser(TagParser(logger), logger)
    val response = ("000000210000033a789cad54dd4b5441149f3b" +
            "7b77eeac6cb6545888e8f4a168eaeef" +
            "dd84f09fce86a0896e2173e55ab7b85" +
            "25dd95552163a98788021f94e8a9374" +
            "3109fcaa71014050d6a5f7aedcd9088" +
            "de5ae80fb0f9baeb6ef5189799df997" +
            "3ce9cf39b73674e035090e201005581" +
            "10461000a0008c255649f44b8412558" +
            "9d548600d5f9fc1e7382af882c45a89" +
            "970422c8fc7bc652ceec12692703e9c" +
            "ce24332ea6473e92471c8502eb9909e" +
            "4a5243ff683769b674723b997b9425a" +
            "66eea2d647289dc1aee1f190be69239" +
            "806ef868a08072f7d9e3d8d6ab15531" +
            "f9fbdf8fad7371c60f17dba6e04e792" +
            "b905809097ae31cb6da70e5fa03a66d" +
            "e7252e6838e50283f9d9e71f29c4ba3" +
            "a9b7d3c1f950148ca8e0d021594917c" +
            "a8cce941b9d39bb46c68ef2a338c9e2" +
            "9438e623e1b019368d78229ad78d486" +
            "f3c12ebb5623d46221cefb34c4b8f44" +
            "a3b6114b74f759bde17c283f9f5dcc4" +
            "d39f36d66d0b41241c3308231ab231c" +
            "8d9af910c075b2ccf5a2ec2820cb199" +
            "46b516e9f06e4da2db728bf17d5483d" +
            "5629aa20330490ff54ac1622badc8fc" +
            "4fff32337d1156e72f48e9788b8e226" +
            "d2b9b51675d06b0320d451977437258" +
            "6254625f6486c532802a44a7ab6585e" +
            "a3a07cc79ae6c1d8a3618d4b950361a" +
            "1f7320f4de5323c1df4be722db5bb16" +
            "af8c432deee091ce7309721dd7703f6" +
            "a471a8fed95764df397e5f088888287" +
            "d845dd594ee92dfd901ce5dc8454e62" +
            "9351a162711ccd8c9b1aae10a1fcec8" +
            "2319bad590fe90ebc439a44d2d3110a" +
            "7832ca276ca1896aae09e0996d54070" +
            "123bbd329a0757d600ba192b2ac1bc7" +
            "d256ea8ccdf5391b11a6ba889fdf330" +
            "3891df7390fdd139b432b1b27a08767" +
            "7e1477ded0d694c81e565f8a9502c42" +
            "dba6574c53587faa47cd6cebd3d2d67" +
            "f7ef743fac9ea3d36cf75769d0d1c81" +
            "cf430d81a3af6fdf3d213f8b7b579b9" +
            "a60f3dafa3adcd8809bbbfbfbf0e000" +
            "7e6869d5adf7dbdb706707ee5991088" +
            "cdbb65a2ca2e56564dbb05080c7c7a8" +
            "50501585de7fbfa2d0ab3b8d5a19932" +
            "f00a256769317a8709dbdd0d440face" +
            "d8c4df0d6d2a9b2163938b99854562d" +
            "0b7ad93943343bb9a9120e369bb7790" +
            "dc1c1c1e192469daf4da89e88d15bde" +
            "f8fd617e7af475544f26137f9ffe8aa" +
            "32344489df55c8e803").hexToByteArray()

    test("parses response") {
        val packet = parser.parse(ByteArrayInputStream(response))
        val queue = DownloadQueueResponse.DownloadQueueResponseDeserializer.deserialize(packet)

        queue shouldBe DownloadQueueResponse(
            listOf(
                PartFileTag(
                    partMetID = 1,
                    sizeXfer = 7229440,
                    sizeDone = 7229440,
                    fileStatus = FileStatus.EMPTY,
                    stopped = false,
                    sourceCount = 9,
                    sourceNotCurrCount = 1,
                    sourceXferCount = 5,
                    sourceCountA4AF = 0,
                    speed = 401737,
                    downPrio = 12,
                    fileCat = 0,
                    lastSeenComplete = 1697659538,
                    lastDateChanged = 1697659561,
                    downloadActiveTime = 26,
                    availablePartCount = 560,
                    a4AFAuto = false,
                    hashingProgress = false,
                    getLostDueToCorruption = 0,
                    getGainDueToCompression = 0,
                    totalPacketsSavedDueToICH = 0,
                    sharedFileTag = SharedFileTag(
                        fileHashHexString = "015e857e37b1948f3230566d179af3e4",
                        fileName = "Udemy - Linux Teoria e Pratica - ITA (30 Marzo 2020) by GRISU.rar",
                        filePath = "001.part",
                        sizeFull = 5442421896,
                        fileEd2kLink = "ed2k://|file|Udemy%20-%20Linux%20Teoria%20e%20Pratica%20-%20ITA%20(30%20Marzo%202020)%20by%20GRISU.rar|5442421896|015E857E37B1948F3230566D179AF3E4|/|sources,2.239.111.73:4662|/",
                        upPrio = 13,
                        getRequests = 0,
                        getAllRequests = 0,
                        getAccepts = 0,
                        getAllAccepts = 0,
                        getXferred = 0,
                        getAllXferred = 0,
                        getCompleteSourcesLow = 1,
                        getCompleteSourcesHigh = 1,
                        getCompleteSources = 1,
                        getOnQueue = 1,
                        getComment = "",
                        getRating = 0,
                    )
                )
            )
        )
    }

})
