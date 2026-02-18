package com.pragament.buttonmapper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pragament.buttonmapper.data.model.ActionType
import com.pragament.buttonmapper.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionPickerScreen(
    pressType: String,
    onBack: () -> Unit,
    onActionSelected: (ActionType) -> Unit,
    onAppPickerRequested: () -> Unit
) {
    val categories = ActionType.getCategories()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Select Action",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${pressType.replaceFirstChar { it.uppercase() }} Press",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Clear/Reset option
            item {
                ActionItem(
                    actionType = if (pressType == "single") ActionType.DEFAULT else ActionType.NONE,
                    icon = Icons.Default.Clear,
                    onClick = {
                        onActionSelected(if (pressType == "single") ActionType.DEFAULT else ActionType.NONE)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            categories.forEach { category ->
                val actions = ActionType.getByCategory(category)
                if (actions.isNotEmpty()) {
                    item {
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
                        )
                    }

                    items(actions) { action ->
                        ActionItem(
                            actionType = action,
                            icon = getActionIcon(action),
                            onClick = {
                                if (action == ActionType.LAUNCH_APP || action == ActionType.LAUNCH_SHORTCUT) {
                                    onAppPickerRequested()
                                } else {
                                    onActionSelected(action)
                                }
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun ActionItem(
    actionType: ActionType,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = actionType.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun getActionIcon(action: ActionType): ImageVector {
    return when (action) {
        ActionType.DEFAULT -> Icons.Default.RadioButtonUnchecked
        ActionType.NONE -> Icons.Default.Block
        ActionType.DISABLED -> Icons.Default.DoNotDisturb
        ActionType.LAUNCH_APP -> Icons.Default.Apps
        ActionType.LAUNCH_SHORTCUT -> Icons.Default.Shortcut
        ActionType.HOME -> Icons.Default.Home
        ActionType.BACK -> Icons.Default.ArrowBack
        ActionType.RECENTS -> Icons.Default.ViewCarousel
        ActionType.SCREEN_OFF -> Icons.Default.ScreenLockPortrait
        ActionType.POWER_DIALOG -> Icons.Default.PowerSettingsNew
        ActionType.SCREENSHOT -> Icons.Default.Screenshot
        ActionType.NOTIFICATIONS -> Icons.Default.Notifications
        ActionType.QUICK_SETTINGS -> Icons.Default.Settings
        ActionType.SPLIT_SCREEN -> Icons.Default.Splitscreen
        ActionType.CLEAR_NOTIFICATIONS -> Icons.Default.NotificationsOff
        ActionType.LAST_APP -> Icons.Default.SwapHoriz
        ActionType.MENU -> Icons.Default.Menu
        ActionType.MEDIA_PLAY_PAUSE -> Icons.Default.PlayArrow
        ActionType.MEDIA_NEXT -> Icons.Default.SkipNext
        ActionType.MEDIA_PREVIOUS -> Icons.Default.SkipPrevious
        ActionType.VOLUME_UP -> Icons.Default.VolumeUp
        ActionType.VOLUME_DOWN -> Icons.Default.VolumeDown
        ActionType.MUTE -> Icons.Default.VolumeOff
        ActionType.FLASHLIGHT -> Icons.Default.FlashlightOn
        ActionType.WIFI -> Icons.Default.Wifi
        ActionType.BLUETOOTH -> Icons.Default.Bluetooth
        ActionType.ROTATION -> Icons.Default.ScreenRotation
        ActionType.DO_NOT_DISTURB -> Icons.Default.DoNotDisturbOn
        ActionType.BRIGHTNESS_UP -> Icons.Default.BrightnessHigh
        ActionType.BRIGHTNESS_DOWN -> Icons.Default.BrightnessLow
        ActionType.CAMERA_SHUTTER -> Icons.Default.Camera
    }
}
