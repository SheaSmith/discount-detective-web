import androidx.compose.runtime.Composable

expect fun AlertDialog2(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    text: @Composable () -> Unit,
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit
)