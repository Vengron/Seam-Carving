package seamcarving

import seamcarving.Helper.rotateBy
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt


fun main(args: Array<String>) {
    val inFile = args[args.indexOf("-in") + 1]
    val outFile = args[args.indexOf("-out") + 1]
    val widthReduced = args[args.indexOf("-width") + 1].toInt()
    val heightReduced = args[args.indexOf("-height") + 1].toInt()

    var bufferedImage = ImageIO.read(File(inFile))
    var imageEnergy = ImageEnergy(bufferedImage)

    val all = (widthReduced + heightReduced).toDouble()
    var done = 0

    repeat(widthReduced) {
        val seam = Seam(imageEnergy.energies)
        bufferedImage = seam.removeSeam(bufferedImage)
        imageEnergy.recalculateEnergy(seam)
        print("\b\b\b\b${(++done / all * 100.0).roundToInt()}%")
    }

    bufferedImage = rotateBy(bufferedImage, Math.PI / 2)
    imageEnergy = ImageEnergy(bufferedImage)
    repeat(heightReduced) {
        val seam = Seam(imageEnergy.energies)
        bufferedImage = seam.removeSeam(bufferedImage)
        imageEnergy.recalculateEnergy(seam)
        print("\b\b\b\b${(++done / all * 100.0).roundToInt()}%")
    }
    bufferedImage = rotateBy(bufferedImage, -Math.PI / 2)
    
    ImageIO.write(bufferedImage , "png", File(outFile))
}
