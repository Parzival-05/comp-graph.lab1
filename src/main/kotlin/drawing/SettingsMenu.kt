package drawing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import radar.scene.SceneConfig

@Composable
fun SettingsMenu(config: SceneConfig, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Settings") },
        text = {
            Column {
                Text(text = "Max Particle Speed: ${config.maxParticleSpeed}")
                Slider(
                    value = config.maxParticleSpeed.toFloat(),
                    onValueChange = { config.maxParticleSpeed = it.toInt() },
                    valueRange = 1f..100f,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(text = "Fight Distance: ${config.fightDist}")
                Slider(
                    value = config.fightDist.toFloat(),
                    onValueChange = { config.fightDist = it.toInt() },
                    valueRange = 1f..50f,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(text = "Hiss Distance: ${config.hissDist}")
                Slider(
                    value = config.hissDist.toFloat(),
                    onValueChange = { config.hissDist = it.toInt() },
                    valueRange = config.fightDist.toFloat()..100f,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                Text("Metric Type: ${config.metric}")
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { expanded = !expanded }) {
                        Text("Select Metric Type")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
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
        },
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        }
    )
}
