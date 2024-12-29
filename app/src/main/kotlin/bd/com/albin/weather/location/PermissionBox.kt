package bd.com.albin.weather.location

import android.content.Context
import android.location.LocationManager
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(
    permissions: List<String>,
    requiredPermissions: List<String> = permissions,
    onGranted: @Composable (List<String>) -> Unit,
) {
    val context = LocalContext.current
    val locationManager = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    val isGpsEnabled =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

    var errorText by remember {
        mutableStateOf("")
    }

    val permissionState = rememberMultiplePermissionsState(permissions = permissions) { map ->
        val rejectedPermissions = map.filterValues { !it }.keys
        errorText = if (rejectedPermissions.none { it in requiredPermissions }) {
            ""
        } else {
            "Location permission required for the Weather App"
        }
    }
    val allRequiredPermissionsGranted =
        permissionState.revokedPermissions.none { it.permission in requiredPermissions }


    if (allRequiredPermissionsGranted && isGpsEnabled) {
        onGranted(
            permissionState.permissions.filter { it.status.isGranted }.map { it.permission },
        )

    } else {
        PermissionScreen(
            allRequiredPermissionsGranted,
            permissionState,
            errorText,
        )
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionScreen(
    allPermission: Boolean,
    state: MultiplePermissionsState,
    errorText: String,
) {
    var showRationale by remember(state) {
        mutableStateOf(false)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(

            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .animateContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!allPermission) {
                Text(
                    text = "The Weather app requires location permission",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                )
                Button(
                    onClick = {
                        if (state.shouldShowRationale) {
                            showRationale = true
                        } else {
                            state.launchMultiplePermissionRequest()
                        }
                    },
                ) {
                    Text(text = "Grant permissions")
                }
            } else Text(
                "Please enable device location/GPS and restart the app.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(16.dp),
            )

            if (errorText.isNotBlank()) {
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
    if (showRationale) {
        AlertDialog(
            onDismissRequest = {
                showRationale = false
            },
            title = {
                Text(text = "Location Permissions required by the Weather App")
            },
            text = {
                Text(text = "The Weather App requires the location permissions to work")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationale = false
                        state.launchMultiplePermissionRequest()
                    },
                ) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationale = false
                    },
                ) {
                    Text("No Thanks")
                }
            },
        )
    }
}