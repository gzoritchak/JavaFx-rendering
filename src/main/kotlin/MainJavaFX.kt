import io.data2viz.color.Colors
import io.data2viz.geom.Size
import io.data2viz.viz.newVizContainer
import javafx.stage.Stage
import tornadofx.*


fun main() {
    launch<MyApp>()
}

const val appWidth = 1280.0
const val appHeight = 720.0

enum class RenderingWith {
    Square, Circle, Diamond
}

val particlesCount = listOf(100, 300, 1000, 3000, 10_000, 30_000, 100_000)

val particles = Array(particlesCount.last()) { RandomParticle() }


class MyApp : App(MyView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = appWidth
        stage.height = appHeight
    }
}


class MyView : View() {

    var partCount = particlesCount.first()
    var rendering = RenderingWith.Square
    var fpsCount = 0
    var fps = 0
    var lastUpdate = 0.0

    override val root = vbox {
        hbox(spacing = 10.0) {
            RenderingWith.values().forEach { renderingWith ->
                button(renderingWith.name) {
                    action {
                        rendering = renderingWith
                    }
                }
            }
        }
        hbox(spacing = 10.0) {
            particlesCount.forEach { count ->
                button(count.toString()) {
                    action {
                        partCount = count
                    }
                }
            }
        }
        borderpane {

            val pane = pane {
                useMaxSize = true
            }
            center = pane

            val vc = pane.newVizContainer()
            vc.size = Size(appWidth, appHeight)
            val viz = vc.newViz {

                animation { time ->
                    fpsCount++
                    if (fpsCount == 10) {
                        fps = (10000 / (time - lastUpdate)).toInt()
                        lastUpdate = time
                        fpsCount = 0
                    }

                    clear()
                    rect {
                        width = appWidth
                        height = appHeight
                        fill = Colors.rgb(180,0,0)

                    }

                    text {
                        y = 20.0
                        textContent = "Rendering $partCount ${rendering.name}s at $fps FPS"
                        fontSize = 12.0
                        textColor = Colors.Web.white
                    }

                    (0 until partCount).forEach { id ->
                        particles[id].updatePositionAndSpeed()
                    }


                    val xScale = io.data2viz.scale.Scales.Continuous.linear {
                        domain = listOf(.0, 5.0)
                        range = listOf(appWidth / 2.0, appWidth)
                    }
                    val yScale = io.data2viz.scale.Scales.Continuous.linear {
                        domain = listOf(.0, 5.0)
                        range = listOf(appHeight / 2.0, appHeight)
                    }

                    (0 until partCount).forEach { id ->
                        val particle = particles[id]
                        val posX = xScale(particle.x)
                        val posY = yScale(particle.y)
                        when (rendering) {
                            RenderingWith.Square -> {
                                rect {
                                    fill = Colors.Web.white
                                    x = posX
                                    y = posY
                                    width = 5.0
                                    height = 5.0
                                }
                            }
                            RenderingWith.Circle -> {
                                circle {
                                    fill = Colors.Web.white
                                    x = posX
                                    y = posY
                                    radius = 2.5
                                }
                            }
                            RenderingWith.Diamond -> {
                                path {
                                    fill = Colors.Web.white
                                    moveTo(posX, posY + 2.5)
                                    lineTo(posX + 2.5, posY)
                                    lineTo(posX, posY - 2.5)
                                    lineTo(posX - 2.5, posY)
                                    closePath()
                                }
                            }
                        }
                    }

                }
            }
            viz.startAnimations()
        }

    }
}
