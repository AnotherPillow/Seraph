package club.maxstats.seraph.render

import club.maxstats.seraph.render.shader.RoundedRectProgram
import org.lwjgl.opengl.GL11.*

val roundedRectProgram = RoundedRectProgram()
fun drawRoundedRect(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    topLeftRadius: Float = 4f,
    topRightRadius: Float = 4f,
    bottomLeftRadius: Float = 4f,
    bottomRightRadius: Float = 4f,
    color: Color = Color.white
) {
    roundedRectProgram.render(x, y, width, height, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, color)
}
fun drawRoundedRect(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: Float = 4f,
    color: Color = Color.white
) {
    drawRoundedRect(x, y, width, height, radius, radius, radius, radius, color)
}

fun drawQuad(
    x: Float,
    y: Float,
    x1: Float,
    y1: Float
) {
    glBegin(GL_QUADS)
    glVertex2f(x, y)
    glVertex2f(x, y1)
    glVertex2f(x1, y1)
    glVertex2f(x1, y)
    glEnd()
}