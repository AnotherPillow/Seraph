package club.maxstats.seraph.util

import club.maxstats.seraph.render.FontRenderer
import club.maxstats.seraph.render.GLFont

private val splitChars = charArrayOf(' ', '-', '\t')
fun String.wrap(
    width: Int,
    fontRenderer: FontRenderer,
    font: GLFont
): String {
    val words = this.explode(splitChars)
    val builder = StringBuilder()
    var lineLength = 0f

    words.forEach { word ->
        val wordLength = fontRenderer.getWidth(word, font)

        if (lineLength + wordLength > width) {
            if (lineLength > 0) builder.appendLine()

            var remaining = word
            while (fontRenderer.getWidth(remaining, font) > width) {
                val slice = remaining.substring(0, width - 1) + '-'
                builder.appendLine(slice)
                remaining = remaining.substring(width - 1)
            }
            builder.append(remaining.trim())
            lineLength = fontRenderer.getWidth(remaining, font)
        } else {
            builder.append(word.trim())
            lineLength += wordLength
        }
    }
    return builder.toString()
}

private fun String.explode(
    splitChars: CharArray
): Array<String> {
    val parts = mutableListOf<String>()
    var startIndex = 0

    while (true) {
        val index = this.indexOfAny(splitChars, startIndex)

        if (index == -1) {
            parts.add(this.substring(startIndex))
            return parts.toTypedArray()
        }

        val word = this.substring(startIndex, index)
        val nextChar = this.substring(index, index + 1)[0]

        if (Character.isWhitespace(nextChar)) {
            parts.add(word)
            parts.add(nextChar.toString())
        } else
            parts.add(word + nextChar)

        startIndex = index + 1
    }
}

private fun String.indexOfAny(
    searchChars: CharArray,
    startingIndex: Int
): Int {
    val sub = this.substring(startingIndex)
    for ((index, char) in sub.withIndex()) {
        if (searchChars.contains(char))
            return index + startingIndex
    }

    return -1
}