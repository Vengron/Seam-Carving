package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class ImageEnergy(private val image: BufferedImage) {
    var energies: Array<Array<Double>> = Array(image.width) { Array(image.height) { 0.0 } }

    init {
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                energies[x][y] = computePixelEnergy(x, y, image)
            }
        }
    }

    // Recalculate the pixel energy at place of the former seam and left to it
    fun recalculateEnergy(seam: Seam) {
        for (co in seam.coordinates) {
            energies[co.first][co.second] = computePixelEnergy(co.first, co.second, image)
            val xLeft = (co.first - 1).coerceAtLeast(0)
            energies[xLeft][co.second] = computePixelEnergy(xLeft, co.second, image)
        }
        energies = energies.copyOfRange(0, energies.size - 1)
    }

    // Compute energy of pixel at (x, y) via dual-gradient energy function
    private fun computePixelEnergy(x: Int, y: Int, image: BufferedImage): Double {
        val cx = when (x) {
            0 -> 1
            image.width - 1 -> image.width - 2
            else -> x
        }
        val cy = when (y) {
            0 -> 1
            image.height - 1 -> image.height - 2
            else -> y
        }
        val leftPixel = Color(image.getRGB(cx - 1, y))
        val rightPixel = Color(image.getRGB(cx + 1, y))
        val upperPixel = Color(image.getRGB(x, cy - 1))
        val bottomPixel = Color(image.getRGB(x, cy + 1))
        val xGrad = (leftPixel.red - rightPixel.red.toDouble()).pow(2) +
                (leftPixel.green - rightPixel.green.toDouble()).pow(2) +
                (leftPixel.blue - rightPixel.blue.toDouble()).pow(2)
        val yGrad = (upperPixel.red - bottomPixel.red.toDouble()).pow(2) +
                (upperPixel.green - bottomPixel.green.toDouble()).pow(2) +
                (upperPixel.blue - bottomPixel.blue.toDouble()).pow(2)

        return sqrt(xGrad + yGrad)
    }
}