package com.example.mobiory.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventItem(eventListViewModel: EventListViewModel, event: Event, alwaysExpanded: Boolean) {

    var expanded by remember { mutableStateOf(false) }
    var onFavoriteClick by remember { mutableStateOf(false) }
    val (showTagDialog, setShowTagDialog) = remember { mutableStateOf(false) }
    var (updateTagClick, setUpdateTagClick) = remember { mutableStateOf(false) }
    val (newTag, setNewTag) = remember { mutableStateOf(event.tag) }


    LaunchedEffect(onFavoriteClick) {
        if (onFavoriteClick) {
            eventListViewModel.toggleFavorite(event.id)
            onFavoriteClick = false
        }
    }

    LaunchedEffect(updateTagClick, newTag) {
        if (updateTagClick) {
            eventListViewModel.updateTag(event.id, newTag)
            updateTagClick = false
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                expanded = !expanded
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = event.label?.labelEN ?: event.label?.labelFR ?: "no label",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
                Row() {
                    IconButton(
                        onClick = {
                            onFavoriteClick = true
                        }) {
                        Icon(
                            imageVector = if (event.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                    if (!alwaysExpanded)
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Drop-Down Arrow"
                            )
                        }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "URL"
                )
                if ((event.startDate != null) and (event.endDate != null))
                    Text(
                        text = getDate(event.startDate) + " to " + getDate(event.endDate),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                else
                    Text(
                        text = getDate(event.pointInTime),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(8.dp)
                    )
            }

            event.popularity?.let { popularity ->
                val progress = 1 - (popularity.popularityEN.toFloat() / 7000000f)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "URL"
                    )
                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

            }
            //set expected as parameter //TODO

            if (expanded or alwaysExpanded) {
                val eventDescription =
                    event.description?.descriptionEN ?: event.description?.descriptionEN
                eventDescription?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "URL"
                        )
                        Text(
                            text = event.description?.descriptionEN
                                ?: event.description?.descriptionEN
                                ?: "",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                event.country?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Country"
                        )
                        Text(
                            text = event.country,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                ) {
                    if (event.tag != "") {

                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = "Tag"
                        )
                        Tag(tag = event.tag)
                    }
                    if (showTagDialog)
                        TagDialog(
                            setExpandedDialog = setShowTagDialog,
                            setUpdateTagClick = setUpdateTagClick,
                            setNewTag = setNewTag,
                            tag = event.tag
                        )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                ) {
                    OutlinedButton(onClick = {
                        setShowTagDialog(true)
                    }) {
                        Text(if (event.tag == "") "Add Tag" else "Update Tag")
                    }
                    Button(onClick = {
                        //TODO: Go to event details
                    }) {
                        Text("See more")
                    }
                }
            }
        }
    }
}

fun getDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("EN"))
    return if (date != null) dateFormat.format(date).toString()
    else ""
}