package com.example.ocr_digital.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.models.UserInformation
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(usersViewModel: UsersViewModel) {
    val state = usersViewModel.state
    val refreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = usersViewModel::refresh)
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Users", fontWeight = FontWeight.SemiBold) })
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .pullRefresh(refreshState)
        ){
            item {
                SearchBar(
                    query = state.query,
                    onQueryChange = usersViewModel::onQueryChange,
                    onSearch = { usersViewModel.searchUser() },
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    placeholder = { Text(text = "Search User") },
                    trailingIcon = {
                        Icon(Icons.Default.Search, "Search")
                    }
                ) {}
            }
            itemsIndexed(state.users) {index: Int,  item: UserInformation ->
                ListItem(
                    leadingContent = { Text(text = "${index+1}") },
                    headlineContent = { Text(text = item.firstName + " " + item.lastName, fontSize = 15.sp, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(text = item.profileId, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSecondaryContainer) },
                    trailingContent = {
                        var showDropDown by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { showDropDown = !showDropDown }) {
                                Icon(Icons.Default.MoreVert, "More")
                            }
                            DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
                                DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { /*TODO*/ })
                                DropdownMenuItem(text = { Text(text = "Change Password") }, onClick = { /*TODO*/ })
                            }
                        }
                    }
                )
            }
        }
    }
}