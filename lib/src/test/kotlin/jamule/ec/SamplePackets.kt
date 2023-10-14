package jamule.ec

@OptIn(ExperimentalStdlibApi::class)
class SamplePackets {

    companion object {
        val statusRequest = "00000022000000060a0108020100".hexToByteArray()
        val statusResponse =
            "000000220000008c0c10d08003021664d082020100d484020100d48603021664d488020100d48a020100d084020100d086020100d090020100d08c020100d092040400017cbbd09402010ad096040402e2740fd09803020438d0b60201000b023f03e0a881081f01e0a8820612416b74656f6e20536572766572204e6f3200b07de76247b50c04041d4e48541404041d4e485419".hexToByteArray()

    }
}