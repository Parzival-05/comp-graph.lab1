package drawing.menu

import CatSimulation.Companion.MAX_CAT_RADIUS
import CatSimulation.Companion.MAX_CAT_SPEED
import CatSimulation.Companion.MAX_FIGHT_DIST
import CatSimulation.Companion.MAX_HISS_DIST
import CatSimulation.Companion.MIN_CAT_RADIUS
import CatSimulation.Companion.MIN_CAT_SPEED
import CatSimulation.Companion.MIN_FIGHT_DIST
import CatSimulation.Companion.MIN_HISS_DIST
import CatSimulation.Companion.MIN_TAU
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import radar.scene.SceneConfig

var showTauErrorAlert by mutableStateOf(false)

@Composable
private fun showTauErrorAlert(onClose: () -> Unit) {
    if (showTauErrorAlert) AlertDialog(onDismissRequest = onClose,
        title = { Text(text = "Недопустимое значение аттритбута Tau") },
        text = {
            Column {
                Text(text = "Введите целое положительное число большее, чем $MIN_TAU")
            }
        },
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        })
}

@Composable
fun sceneSettingsMenu(config: SceneConfig, onClose: () -> Unit) {
    var tauTextState by remember { mutableStateOf(config.tau.toString()) }
    showTauErrorAlert {
        showTauErrorAlert = false
    }
    val minusMarginModifier = Modifier.padding(5.dp) // margin
    val plusMarginModifier = Modifier.padding(5.dp) // margin
    AlertDialog(onDismissRequest = onClose, title = { Text(text = "Settings") }, text = {
        Column {
            Text(text = "Cat size: ${config.catRadius}")
            Row {
                Button(onClick = {
                    if (config.catRadius > MIN_CAT_RADIUS) {
                        config.catRadius--
                    }
                }, modifier = minusMarginModifier) {
                    Text("-")
                }
                Button(onClick = {
                    if (config.catRadius < MAX_CAT_RADIUS) {
                        config.catRadius++
                    }
                }, modifier = plusMarginModifier) {
                    Text("+")
                }
                Slider(
                    value = config.catRadius.toFloat(),
                    onValueChange = { config.catRadius = it.toInt() },
                    valueRange = MIN_CAT_RADIUS.toFloat()..MAX_CAT_RADIUS.toFloat(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Text(text = "Tau (ms)")
            OutlinedTextField(
                value = tauTextState,
                onValueChange = {
                    tauTextState = it
                },
                modifier = Modifier.padding(vertical = 8.dp).onPreviewKeyEvent {
                    if (it.key == Key.Enter) {
                        val newValue = tauTextState.toLongOrNull()
                        if (newValue != null && newValue >= MIN_TAU) {
                            config.tau = newValue
                            showTauErrorAlert = false // Reset error state
                        } else {
                            showTauErrorAlert = true // Show error alert if the value is invalid.
                        }
                        true // Indicate that the key event has been handled
                    } else {
                        false // Allow other key events to be processed normally
                    }
                },
                singleLine = true,
            )
            Slider(
                value = config.tau.toFloat(),
                onValueChange = { config.tau = it.toLong() },
                valueRange = 500f..3600000f,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(text = "Max Particle Speed: ${config.maxParticleSpeed}")
            Row {
                Button(onClick = {
                    if (config.maxParticleSpeed > MIN_CAT_SPEED) {
                        config.maxParticleSpeed--
                    }
                }, modifier = minusMarginModifier) {
                    Text("-")
                }
                Button(onClick = {
                    if (config.maxParticleSpeed < MAX_CAT_SPEED) {
                        config.maxParticleSpeed++
                    }
                }, modifier = plusMarginModifier) {
                    Text("+")
                }
                Slider(
                    value = config.maxParticleSpeed.toFloat(),
                    onValueChange = { config.maxParticleSpeed = it.toDouble() },
                    valueRange = 1f..100f,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Text(text = "Fight Distance: ${config.fightDist}")
            Row {
                Button(onClick = {
                    if (config.fightDist > MIN_FIGHT_DIST) {
                        config.fightDist--
                    }
                }, modifier = minusMarginModifier) {
                    Text("-")
                }
                Button(onClick = {
                    if (config.fightDist < min(MAX_FIGHT_DIST, config.hissDist)) {
                        config.fightDist++
                    }
                }, modifier = plusMarginModifier) {
                    Text("+")
                }
                Slider(
                    value = config.fightDist.toFloat(),
                    onValueChange = { config.fightDist = it.toInt() },
                    valueRange = MIN_HISS_DIST.toFloat()..(config.hissDist.toFloat() - 1),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Text(text = "Hiss Distance: ${config.hissDist}")
            Row {
                Button(onClick = {
                    if (config.hissDist > MIN_HISS_DIST && config.hissDist > config.fightDist) {
                        config.hissDist--
                    }
                }, modifier = minusMarginModifier) {
                    Text("-")
                }
                Button(onClick = {
                    if (config.hissDist < MAX_HISS_DIST) {
                        config.hissDist++
                    }
                }, modifier = plusMarginModifier) {
                    Text("+")
                }
                Slider(
                    value = config.hissDist.toFloat(),
                    onValueChange = { config.hissDist = it.toInt() },
                    valueRange = config.fightDist.toFloat()..MAX_HISS_DIST.toFloat(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            var expanded by remember { mutableStateOf(false) }
            Text("Metric Type: ${config.metric}")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = !expanded }) {
                    Text("Select Metric Type")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    SceneConfig.Companion.MetricType.entries.forEach { metricType ->
                        DropdownMenuItem(onClick = {
                            config.metric = metricType
                            expanded = false
                        }) {
                            Text(text = metricType.name)
                        }
                    }
                }
            }
        }
    }, confirmButton = {
        Button(onClick = onClose) {
            Text("Close")
        }
    })
}