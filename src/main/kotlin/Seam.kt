package seamcarving

import seamcarving.Helper.getMinUpper
import seamcarving.Helper.getRowWithIndex
import java.awt.image.BufferedImage

class Seam(private val energies: Array<Array<Double>>) {
    var coordinates: Array<Pair<Int, Int>>

    // Create a 2D array of weights by dynamic programming approach
    // https://en.wikipedia.org/wiki/Seam_carving#Dynamic_programming
    init {
        val weights = Array(energies.size) { Array(energies.first().size) { -1.0 } }
        for (x in energies.indices) {
            weights[x][0]= energies[x][0]
        }
        for (y in 1 until energies.first().size) {
            for (x in energies.indices) {
                val minUpper = getMinUpper(weights, x, y)
                weights[x][y] = weights[minUpper.first][minUpper.second] + energies[x][y]
            }
        }

        coordinates = constructSeam(weights)
    }

    // Constructs a minimum seam from given weights by greedy alg from the bottom minimum to top
    private fun constructSeam(weights: Array<Array<Double>>): Array<Pair<Int, Int>> {
        val size = weights.first().size
        val seam = Array(size) { Pair(-1, -1) }
        var indexOfMin = getRowWithIndex(weights, size - 1)
            .withIndex().minByOrNull { (_, w) -> w }!!.index
        seam[size - 1] = Pair(indexOfMin, size - 1)

        for (y in size - 1 downTo 1) {
            indexOfMin = getMinUpper(weights, indexOfMin, y).first
            seam[y - 1] = Pair(indexOfMin, y - 1)
        }
        return seam
    }

    // Removes the seam by relocating content of the image
    fun removeSeam(image: BufferedImage): BufferedImage {
        var index = coordinates[0].first
        return if (index <= image.width / 2) {
            for (y in 0 until image.height) {
                index = coordinates[y].first
                for (x in index downTo 1) {
                    image.setRGB(x, y, image.getRGB(x - 1, y))
                }
            }
            image.getSubimage(1, 0, image.width - 1, image.height)
        } else {
            for (y in 0 until image.height) {
                index = coordinates[y].first
                for (x in index until image.width - 1) {
                    image.setRGB(x, y, image.getRGB(x + 1, y))
                }
            }
            image.getSubimage(0, 0, image.width - 1, image.height)
        }
    }
}