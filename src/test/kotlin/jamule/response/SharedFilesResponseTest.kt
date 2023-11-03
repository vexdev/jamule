package jamule.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import jamule.ec.packet.PacketParser
import jamule.ec.packet.PacketParserTest
import jamule.ec.tag.TagParser
import jamule.ec.tag.special.SharedFileTag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream

@OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
class SharedFilesResponseTest : FunSpec({
    val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)
    val parser = PacketParser(TagParser(logger), logger)
    val response =
        ("0000002100000784789cad976b8c1b5715c7c7bb1b6bb20414c1aa201ed534a228ab786767ee9d67280af3f4da1e3b7eaebd5b" +
                "95746ccf6e47ebf538b67793ac864428424248a81fdaa62280548916f12512417c40854f55c5bb088947be0404888a2" +
                "221211e155f287066e67a1f29a9d86d258fcff1cc78e67feeefdc73cf3d45d1746a8aa252af5273741a1c2a55a1e969" +
                "b053a93c3d4b4e9c989c989a0167e6e3e8718a9e39062e1d5dffc4572f53f47bd2e03e54add4507319572db1b5526e9" +
                "4aa2547d4ac65b3905b4182b8b42a52f403f1234fd0ef8d2d45cf11fb41623f10db547a2a7a9edee87a9b579805c6f1" +
                "fb5b9799ba170c7d97f198f2d01dfb1d172ee4ea1a731a734cd11dee040ce21037cfb4af30d96aaed66087ee904a3f7" +
                "21c1e7432f5a9cf5e956f3ffd24e29637df7ff3ef7fa04f46cf9f5d5cf3fbfee809af4ba5d3bbf198dd973f97fe7074" +
                "fdb6d7451b671717c335bfe785b1988711b700472c086c22091c0f0e228bdc02d2e01bc4c1772cefe1481e0804db8e9" +
                "eb32b321405010988575429e478d15244d9c2b2ceab826263843951924c5e56351b5b42b8188e82ad61c71b65108bb0" +
                "caf23ccfcaf8ac2049285ca4e858378ce383c978a63f9a020b43f09ff8835209edd77669cfd0746c4f11d833f409f23" +
                "b61fdc69dc68b7bac8f65cfbc7e97b0ce36b3ba5469998d222eac66570a3857cb098db2602fc939bd525dfa7f595309" +
                "ebf34861b37e30ecfbac190c02f634e23834cfea66d51fb03ca7700316469435eb35d62a6559cdedb0b5adf688359e7" +
                "00723f6d17a7b7561c9cc8dddde95c7d8cd8ded09f8f4bf33fffafcef7ff46b8afef3ddfcc93f7defadc0bf71edc6cd" +
                "04fcd70e807f6795858a28c8aa2488321772926d21c336140d8c669a1cc72996611a82c673966a4847a27d62421b3ec" +
                "3a904f84bbbc029029c22c029029ca2a7889d2136c17cded29c7cbe5645d2796c1a859ad434ca4db3a4d99556c311ed" +
                "f221311b05b7e793f95cf2c69782e10653ebb8fdbedf5f678c20d868c3c19c2ebb9d8d710626342f64185e56b0aa205" +
                "e10e6d941776d8276fd9b3f79a6bff1cb6befda78e8933f7eb7f0d4ff401ba730e5dc7921c1fac201ac91927d5399a8" +
                "016fa207dc89a26826279ae2590caac0dea32b14392caa021f4ab28e0c4315254bd24d51b6395dd2e12eced039130b2" +
                "a3e12d5143513a39c12df8432750fcad41eca6400e0ff846669b566554a0dd3142cd4124da76e6939b3e23896b3aa39" +
                "a5bc7e489a171fd5b6ba7ed0f3dbc380e979e371c0745dc66699328c60002002a8d15d7fdb1f058cdf673aeec01fc3d" +
                "d8f3179b6c032d5e0522fc2bec0f0cc923b1c5e61cac178ec0da1c2f75c66e07be3a1cb00aa6014acb93d8fddf10713" +
                "f6e3d9efeebcf2cfebe6e3afbe76eb6ce9ea77eecb1e424fd8fff100fb7dba0164ac1c6c37aadd361b95f2897e52cc9" +
                "318c0f1fbf0b51b07f8512460482ce4761e8e38a2e851714c6489e8456f482203e7606ca1248a580a6581d37443d60c" +
                "d386fc120489b36049d04cac09966ce9fa11b3e758923d9fdecd9e14c99e39923d29923d73a4f29ff945e94bfb56f9c" +
                "dc1d52f9324cae7eb2b2b5a4928140b42a36aae940cad5228e8b5fc524bc24ba673c8247a521b0dfca1db1f7bccb6d7" +
                "0db603062a2c8f212d8aee68e46f06ccb2d7f7bde13cd309fa8cedb6fd80313dc6d9f2d7fd0c988eeff65d2829e3b1b" +
                "fb31391cc305acf1b8ddc7e17b252f7465e7f0c6991616c78090c5b074e82d7853346cfdf818ec286e2ee8ee015acbb" +
                "ed4f52ec0b1f9b63af4fd5eefef0c2cbfca59fe78edf2fc59ad79e4b522c451f48b1dda8007412575444a2c8489290e" +
                "8c023f145991544f915c708d68cfe9bc4193b71a471c9da8b157eee450b3ff6e28d1e348938ba90c41c15b538eae8f2" +
                "feb8435e906455e11524848a89245e409682392442d98762661b02e665392a609c7aa41c8426f2d974b21c7de3c8cb5" +
                "1b3b0aa3b5251b38c72c530b1deace156dd925b4d39572ed65b872d60e71c972dfa2e2cd8031805eb325b75d7dd9d1d" +
                "9785355e62fdbae6f85a89754cd65c366b46956d6dfbe682b66c951ad6fe5cf9d653375ffac785efa317cf5732ea23a" +
                "73e74bf5c3973eb07a41c3d732057de9e8a50c648e154c44ba10ecb8baa19c81600189cd635c116796460151b08f147" +
                "2b1e00ce21db84dfec825b23db84e90709b935b24d98fe082920995b37feb65740e8577eb5f13e0211d70a86a515f3d" +
                "5e572416c14b442b18891d82adbad5c59d32de1901073451f6652d067739b8300bc36d4d35c2e978cdec2eef84173e6" +
                "f8b52556333069de4ec7dddb3c7b1949c2fea6f1dbafcfa9b717cfb56fc85fffccf3eb177efa964de3ec579e4d903e7" +
                "700e93ba5296a1739a42a922087ba023005acea3cb2b12d2155c2b2a628bc063317785b6fbb5dbc783cc1fcfc9be6e7" +
                "f43df373fade1e63e3b777085dcbc8af88d9a6d5cc97b2a5e56641ac36ebce6a49ae57609b63cbd543d2e59366b10cb" +
                "5bcc4428ec3ba60fa1d6fb33df4a20d1f62b25ebfeffaf1ee0fefef0ffff2c0f2faf58be7b25f9cc76bcaeff2cafd26" +
                "658afdd95f13824f1f9c94a4312cc775357e37a9dc93f7273b3a14ede5120dc9895845c8ab8a00eb391f5a362fc1160" +
                "e167399c736f4fe2a52319624ac58bca061e5a873727af6bfdf586003").hexToByteArray()

    test("parses response") {
        val packet = parser.parse(ByteArrayInputStream(response))
        val queue = SharedFilesResponse.SharedFilesResponseDeserializer.deserialize(packet)

        queue shouldBe SharedFilesResponse(
            listOf(
                SharedFileTag(
                    fileHashHexString = "015e857e37b1948f3230566d179af3e4",
                    fileName = "Udemy - Linux Teoria e Pratica - ITA (30 Marzo 2020) by GRISU.rar",
                    filePath = "/finished",
                    sizeFull = 5442421896,
                    fileEd2kLink = "ed2k://|file|Udemy%20-%20Linux%20Teoria%20e%20Pratica%20-%20ITA%20(30%20Marzo%202020)%20by%20GRISU.rar|5442421896|015E857E37B1948F3230566D179AF3E4|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 81,
                    getAllRequests = 330,
                    getAccepts = 81,
                    getAllAccepts = 330,
                    getXferred = 993157120,
                    getAllXferred = 5329090560,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 1,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "06fe2cfc8ae2cadd0008ecdc4a10e9c6",
                    fileName = "28.Giorni.Dopo.(2002).BDRip.1080p.ITA.DTS.ENG.Aac.Subs.Chaps.[TbZ-HDItaly].mkv",
                    filePath = "/finished",
                    sizeFull = 8547964570,
                    fileEd2kLink = "ed2k://|file|28.Giorni.Dopo.(2002).BDRip.1080p.ITA.DTS.ENG.Aac.Subs.Chaps.[TbZ-HDItaly].mkv|8547964570|06FE2CFC8AE2CADD0008ECDC4A10E9C6|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 4,
                    getAllRequests = 34,
                    getAccepts = 4,
                    getAllAccepts = 34,
                    getXferred = 4258747834,
                    getAllXferred = 22668899804,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "67b2cc956e6bd57f0b6b2140cb0d3493",
                    fileName = "Kali Linux Network Scanning Cookbook (Packt, 2014, 1783982144).pdf",
                    filePath = "/finished",
                    sizeFull = 5035941,
                    fileEd2kLink = "ed2k://|file|Kali%20Linux%20Network%20Scanning%20Cookbook%20(Packt,%202014,%201783982144).pdf|5035941|67B2CC956E6BD57F0B6B2140CB0D3493|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 0,
                    getAllRequests = 0,
                    getAccepts = 0,
                    getAllAccepts = 0,
                    getXferred = 0,
                    getAllXferred = 0,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "740abc7acdf7834460e5e8ad3a4e7ebb",
                    fileName = "[Audiolibro letto da F. Pannofino - diviso in capitoli] J.K. Rowling - 1 Harry Potter e la pietra filosofale.zip",
                    filePath = "/finished",
                    sizeFull = 65536,
                    fileEd2kLink = "ed2k://|file|[Audiolibro%20letto%20da%20F.%20Pannofino%20-%20diviso%20in%20capitoli]%20J.K.%20Rowling%20-%201%20Harry%20Potter%20e%20la%20pietra%20filosofale.zip|65536|740ABC7ACDF7834460E5E8AD3A4E7EBB|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 0,
                    getAllRequests = 1,
                    getAccepts = 0,
                    getAllAccepts = 1,
                    getXferred = 0,
                    getAllXferred = 65536,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "8d26142e830253dcc95fc43177d34909",
                    fileName = "Aspirante vedovo (2013 - Massimo Venier) con Fabio De Luigi, Luciana Littizzetto, Alessandro Besentini, Francesco Brandi, Clizia Fornasier.avi",
                    filePath = "/finished",
                    sizeFull = 1467981824,
                    fileEd2kLink = "ed2k://|file|Aspirante%20vedovo%20(2013%20-%20Massimo%20Venier)%20con%20Fabio%20De%20Luigi,%20Luciana%20Littizzetto,%20Alessandro%20Besentini,%20Francesco%20Brandi,%20Clizia%20Fornasier.avi|1467981824|8D26142E830253DCC95FC43177D34909|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 1,
                    getAllRequests = 20,
                    getAccepts = 1,
                    getAllAccepts = 20,
                    getXferred = 735334043,
                    getAllXferred = 6131056284,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "b4939ac2f45fc732ba4f512c393c221b",
                    fileName = "La.Mia.Super.Ex.Ragazza.2006.iTALiAN.LD.DVDSCR.XviD-AVENUE.avi",
                    filePath = "/finished",
                    sizeFull = 732809216,
                    fileEd2kLink = "ed2k://|file|La.Mia.Super.Ex.Ragazza.2006.iTALiAN.LD.DVDSCR.XviD-AVENUE.avi|732809216|B4939AC2F45FC732BA4F512C393C221B|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 0,
                    getAllRequests = 0,
                    getAccepts = 0,
                    getAllAccepts = 0,
                    getXferred = 0,
                    getAllXferred = 0,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "b8f51439b12f3f629637a881a4675fce",
                    fileName = "Mission.Impossible.III.2006.-.iTALiAN.ENGLiSH.AC3.BDRip.(1080p).x264.mkv",
                    filePath = "/finished",
                    sizeFull = 8540298647,
                    fileEd2kLink = "ed2k://|file|Mission.Impossible.III.2006.-.iTALiAN.ENGLiSH.AC3.BDRip.(1080p).x264.mkv|8540298647|B8F51439B12F3F629637A881A4675FCE|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 102,
                    getAllRequests = 798,
                    getAccepts = 102,
                    getAllAccepts = 797,
                    getXferred = 749573874,
                    getAllXferred = 37813119763,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                ),
                SharedFileTag(
                    fileHashHexString = "ef16566783713f479929336638e14a38",
                    fileName = "Linux Pro N.216 - Dicembre 2022 Gennaio 2023.pdf",
                    filePath = "/finished",
                    sizeFull = 19845361,
                    fileEd2kLink = "ed2k://|file|Linux%20Pro%20N.216%20-%20Dicembre%202022%20Gennaio%202023.pdf|19845361|EF16566783713F479929336638E14A38|/|sources,2.239.111.73:4662|/",
                    upPrio = 12,
                    getRequests = 0,
                    getAllRequests = 3,
                    getAccepts = 0,
                    getAllAccepts = 3,
                    getXferred = 0,
                    getAllXferred = 7069911,
                    getCompleteSourcesLow = 0,
                    getCompleteSourcesHigh = 0,
                    getCompleteSources = 0,
                    getOnQueue = 0,
                    getComment = "",
                    getRating = 0
                )
            )
        )
    }

})
