package seamcarving

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object Helper {

    fun getRowWithIndex(array2D: Array<Array<Double>>, index: Int): Array<Double> {
        return Array(array2D.size) { x -> array2D[x][index] }
    }

    // Return index of cell with the minimum value from upper three cells
    fun getMinUpper(weights: Array<Array<Double>>, x: Int, y: Int): Pair<Int, Int> {
        val uppers = when (x) {
            0 -> arrayOf(Pair(x, y - 1), Pair(x + 1, y - 1))
            weights.size - 1 -> arrayOf(Pair(x - 1, y - 1), Pair(x, y - 1))
            else -> arrayOf(Pair(x - 1, y - 1), Pair(x, y - 1), Pair(x + 1, y - 1))
        }
        var index = Pair(-1, -1)
        var min = Double.MAX_VALUE
        for (pair in uppers) {
            if (weights[pair.first][pair.second] < min) {
                min = weights[pair.first][pair.second]
                index = pair
            }
        }
        return index
    }

    // Credit: https://blog.idrsolutions.com/2019/05/image-rotation-in-java/
    fun rotateBy(image: BufferedImage, theta: Double): BufferedImage {
        val sin = abs(sin(theta))
        val cos = abs(cos(theta))
        val width = (image.width * cos + image.height * sin).toInt()
        val height = (image.height * cos + image.width * sin).toInt()
        val rotatedImage = BufferedImage(width, height, image.type)
        val at = AffineTransform()
        at.translate((width / 2).toDouble(), (height / 2).toDouble())
        at.rotate(theta, 0.0, 0.0)
        at.translate((-image.width / 2).toDouble(), (-image.height / 2).toDouble())
        val rotateOp = AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR)

        return rotateOp.filter(image, rotatedImage)
    }
}