package com.example.cosc345project.ui.components.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.jetbrains.skiko.SkikoKey

/**
 * A modified version of the top app bar that contains a search form.
 *
 * @param search The search query.
 * @param loading Boolean variable for whether or not the screens finished loading.
 * @param onValueChange The function called whenever the search query has changed.
 * @param onFocusChanged The function called whenever the search field has its focus changed.
 * @param onSearch The function called when the user selects a suggestion, or clicks the search button.
 * @param onClear The function called when the user clears the search.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    search: String,
    loading: Boolean,
    onValueChange: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onSearch: (Any?) -> Unit,
    onClear: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
        shape = CircleShape,
        tonalElevation = 16.dp
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(start = 16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                TextField(
                    value = search,
                    placeholder = { Text("Search Products") },
                    onValueChange = onValueChange,
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .onFocusChanged(onFocusChanged)
                        .onKeyEvent {
                            if (it.nativeKeyEvent.key == SkikoKey.KEY_ENTER) {
                                onSearch(null)
                                true
                            } else {
                                false
                            }
                        }
                    ,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = onSearch
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                )
                AnimatedVisibility(visible = search.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            Icons.Rounded.Close,
                            "Clear Search"
                        )
                    }
                }
            }

            if (loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                )
            }
        }
    }
}